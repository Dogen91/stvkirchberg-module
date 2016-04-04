/*
 * (c) 2012 holder contact. All rights reserved. This material is solely and exclusively owned by holder and
 * may not be reproduced elsewhere without prior written approval.
 */

package ch.stvkirchberg.servlets;

import info.magnolia.cms.exchange.ExchangeException;
import info.magnolia.cms.security.Realm;
import info.magnolia.cms.security.SecuritySupport;
import info.magnolia.cms.security.User;
import info.magnolia.cms.security.UserManager;
import info.magnolia.cms.util.Rule;
import info.magnolia.context.MgnlContext;
import info.magnolia.module.admininterface.commands.ActivationCommand;
import info.magnolia.module.exchangesimple.ResourceCollector;
import info.magnolia.module.exchangesimple.SimpleSyndicator;
import info.magnolia.module.mail.MailModule;
import info.magnolia.module.mail.MailTemplate;
import info.magnolia.module.mail.MgnlMailFactory;
import info.magnolia.module.mail.handlers.MgnlMailHandler;
import info.magnolia.module.mail.templates.MailAttachment;
import info.magnolia.module.mail.templates.MgnlEmail;
import info.magnolia.repository.RepositoryConstants;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.Session;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.jackrabbit.JcrConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.stvkirchberg.config.WebsiteConfig;
import ch.stvkirchberg.models.common.Member;
import ch.stvkirchberg.utils.JcrUtil;
import ch.stvkirchberg.utils.NodeUtils;
import ch.stvkirchberg.utils.NotificationUtil;

public class UpdateProfileServlet extends HttpServlet {

    private static final String PENDING_UPDATES_ROOT = "/pendingUpdates";

    private static final long serialVersionUID = 1L;

    private static final String LINK_PREFIX = "http://www.stvkirchberg.ch/.stvkirchberg/updateProfile?id=";

    private static Logger LOG = LoggerFactory.getLogger(UpdateProfileServlet.class);

    @Override
    protected void service(HttpServletRequest req,
                           HttpServletResponse resp) throws ServletException, IOException {
        String uuid = req.getParameter("id");
        if (StringUtils.isEmpty(uuid)) {
            handleFormSubmit(req, resp);
        } else {
            handleUpdateProfile(req, resp, uuid);
        }

    }

    private void handleUpdateProfile(HttpServletRequest req,
                                     HttpServletResponse resp,
                                     String uuid) {
        Boolean accept = Boolean.parseBoolean(req.getParameter("accept"));
        Session adminSession = JcrUtil.getAdminSession(RepositoryConstants.USERS);
        Node userNode = NodeUtils.getNodeById(adminSession, RepositoryConstants.USERS, uuid);
        if (userNode == null) {
            NotificationUtil.printOutMessage(resp, "Technischer Fehler: Profildaten konnten nicht bearbeitet werden, da uuid nicht existiert.");
            return;
        }

        String username = NodeUtils.getNodeName(userNode);
        UserManager userManager = SecuritySupport.Factory.getInstance().getUserManager(Realm.REALM_ADMIN.getName());
        User user = userManager.getUser(username);
        if (user == null) {
            NotificationUtil.printOutMessage(resp, "Technischer Fehler: Profildaten konnten nicht bearbeitet werden, da der Benutzer nicht existiert.");
            return;
        }

        Node pendingUpdateInfos = NodeUtils.getNodeByAbsPath(PENDING_UPDATES_ROOT + "/" + username, adminSession);
        if (pendingUpdateInfos == null) {
            NotificationUtil.printOutMessage(resp, "Technischer Fehler: Profildaten konnten nicht bearbeitet werden, da keine pendente Änderungen vorhanden sind.");
            return;
        }

        boolean accepted = accept != null && accept;
        String mail = NodeUtils.getStringProperty(pendingUpdateInfos, Member.PROP_MAIL);
        if (accepted) {
            String phoneNumber = NodeUtils.getStringProperty(pendingUpdateInfos, Member.PROP_PHONE_NUMBER);
            String mobilePhoneNumber = NodeUtils.getStringProperty(pendingUpdateInfos, Member.PROP_MOBILE_PHONE_NUMBER);
            String address = NodeUtils.getStringProperty(pendingUpdateInfos, Member.PROP_ADDRESS);
            String postalCode = NodeUtils.getStringProperty(pendingUpdateInfos, Member.PROP_POSTAL_CODE);
            String city = NodeUtils.getStringProperty(pendingUpdateInfos, Member.PROP_CITY);

            userManager.setProperty(user, Member.PROP_MAIL, JcrUtil.createValue(mail));
            userManager.setProperty(user, Member.PROP_PHONE_NUMBER, JcrUtil.createValue(phoneNumber));
            userManager.setProperty(user, Member.PROP_MOBILE_PHONE_NUMBER, JcrUtil.createValue(mobilePhoneNumber));
            userManager.setProperty(user, Member.PROP_ADDRESS, JcrUtil.createValue(address));
            userManager.setProperty(user, Member.PROP_POSTAL_CODE, JcrUtil.createValue(postalCode));
            userManager.setProperty(user, Member.PROP_CITY, JcrUtil.createValue(city));

        }
        NodeUtils.removeNode(pendingUpdateInfos);
        NodeUtils.saveChangesInSession(adminSession);

        boolean replicationSuccessful = replicateUserToAuthor("/admin/" + username, uuid);
        if (replicationSuccessful) {
            boolean sentSuccessfull = sendConfirmationMail(mail, accepted);
            if (sentSuccessfull) {
                NotificationUtil.printOutMessage(resp, "Der Benutzer wurde erfolgreich verarbeitet.");
            } else {
                NotificationUtil.printOutMessage(resp, "Der Benutzer wurde erfolgreich verarbeitet. Es konnte aber keine Bestätigung an den Benutzer geschickt werden.");
            }
        } else {
            NotificationUtil.printOutMessage(resp, "Der Benutzer konnte nicht bearbeitet werden.");
        }
    }

    @SuppressWarnings("deprecation")
    private boolean sendConfirmationMail(String receiver,
                                         boolean accepted) {
        if (StringUtils.isEmpty(receiver)) {
            return false;
        } else {
            WebsiteConfig websiteConfig = WebsiteConfig.getInstance();
            String sender = websiteConfig.getMailOfAddressMaintainer();
            String subject = "STV Kirchberg: Update Profile";
            String message = getConfirmationMailBody(accepted);

            Map<String, Object> params = getParameterMap(sender, receiver, subject, null, message);

            MgnlMailFactory mailFactory = MailModule.getInstance().getFactory();
            MgnlEmail mgnlEmail = mailFactory.getEmail(params, null);
            MgnlMailHandler emailHandler = mailFactory.getEmailHandler();

            try {
                emailHandler.prepareAndSendMail(mgnlEmail);
                return true;
            } catch (Exception e) {
                LOG.error("Unable to send update profile mail to admin.", e);
                return false;
            }
        }
    }

    private String getConfirmationMailBody(boolean accepted) {
        StringBuilder body = new StringBuilder();
        body.append("Hallo, \n");
        if (accepted) {
            body.append("Dein Profil wurde erfolgreich verarbeitet. \n");
        } else {
            body.append("Dein Antrag zur Profilaktualisierung wurde vom Adressverantwortlichen abgelehnt. \n");
        }
        body.append("Freundliche Grüsse\n");
        body.append("STV Kirchberg");
        return body.toString();
    }

    private void handleFormSubmit(HttpServletRequest req,
                                  HttpServletResponse resp) throws IOException {
        String uuid = req.getParameter("uuid");
        String username = req.getParameter("username");
        String prename = req.getParameter("prename");
        String lastname = req.getParameter("lastname");
        String mail = req.getParameter("mail");
        String phoneNumber = req.getParameter("phoneNumber");
        String mobilePhoneNumber = req.getParameter("mobilePhoneNumber");
        String address = req.getParameter("address");
        String postalCode = req.getParameter("postalCode");
        String city = req.getParameter("city");

        Session adminSession = JcrUtil.getAdminSession(RepositoryConstants.USERS);
        Node pendingUpdateRoot = NodeUtils.getOrCreateByPath(PENDING_UPDATES_ROOT, JcrConstants.NT_UNSTRUCTURED, adminSession);
        if (NodeUtils.childNodeExists(pendingUpdateRoot, username)) {
            adminSession.logout();
            NotificationUtil.redirectToRefererWithMsg(req, resp, NotificationUtil.ATTR_FORM_ERROR_MSG, "Dein Profil befindet sich bereits in einem Aktualisierungsprozess. Sobald dieser Prozess beendet ist, wirst Du via Mail darüber benachrichtigt.");
        } else {
            boolean saveSuccessful = savePendingUpdateInfos(pendingUpdateRoot, username, prename, lastname, mail, phoneNumber, mobilePhoneNumber, address, postalCode, city);
            if (saveSuccessful) {
                String acceptLink = LINK_PREFIX + uuid + "&accept=true";
                String declineLink = LINK_PREFIX + uuid + "&accept=false";
                if (sendNotificationToAddressAdmin(req, acceptLink, declineLink)) {
                    NodeUtils.saveChangesInSession(adminSession);
                    adminSession.logout();
                    NotificationUtil.redirectToRefererWithMsg(req, resp, NotificationUtil.ATTR_FORM_MSG, "Dein Profil wird aktualisiert, sobald die Daten von der Adressverwaltung bestätigt wurden. Du wirst via Mail darüber benachrichtigt.");
                } else {
                    adminSession.logout();
                    NotificationUtil.redirectToRefererWithMsg(req, resp, NotificationUtil.ATTR_FORM_ERROR_MSG, "Auf Grund eines technischen Fehlers konnte dein Profil nicht aktualisiert werden.");
                }
            } else {
                adminSession.logout();
                NotificationUtil.redirectToRefererWithMsg(req, resp, NotificationUtil.ATTR_FORM_ERROR_MSG, "Es gab einen Fehler beim aktualisieren der Daten. Bitte versuch es nocheinmal oder melde dich beim Administrator.");
            }
        }
    }

    @SuppressWarnings("deprecation")
    private boolean sendNotificationToAddressAdmin(HttpServletRequest req,
                                                   String acceptLink,
                                                   String declineLink) {
        WebsiteConfig websiteConfig = WebsiteConfig.getInstance();
        String receivers = websiteConfig.getMailOfAddressMaintainer();
        String subject = "STV Kirchberg: Update Profile";
        String message = getNotificationMailBody(req, acceptLink, declineLink);

        Map<String, Object> params = getParameterMap("info@stvkirchberg.ch", receivers, subject, null, message);

        MgnlMailFactory mailFactory = MailModule.getInstance().getFactory();
        MgnlEmail mgnlEmail = mailFactory.getEmail(params, null);
        MgnlMailHandler emailHandler = mailFactory.getEmailHandler();

        try {
            emailHandler.prepareAndSendMail(mgnlEmail);
            return true;
        } catch (Exception e) {
            LOG.error("Unable to send update profile mail to admin.", e);
            return false;
        }
    }

    private String getNotificationMailBody(HttpServletRequest req,
                                           String acceptLink,
                                           String declineLink) {
        String username = req.getParameter("username");
        String newMail = req.getParameter("mail");
        String newPhoneNumber = req.getParameter("phoneNumber");
        String newMobilePhoneNumber = req.getParameter("mobilePhoneNumber");
        String newAddress = req.getParameter("address");
        String newPostalCode = req.getParameter("postalCode");
        String newCity = req.getParameter("city");
        
        UserManager userManager = SecuritySupport.Factory.getInstance().getUserManager(Realm.REALM_ADMIN.getName());
        User user = userManager.getUser(username);
        
        String mail = user.getProperty(Member.PROP_MAIL);
        String phoneNumber = user.getProperty(Member.PROP_PHONE_NUMBER);
        String mobilePhoneNumber = user.getProperty(Member.PROP_MOBILE_PHONE_NUMBER);
        String address = user.getProperty(Member.PROP_ADDRESS);
        String postalCode = user.getProperty(Member.PROP_POSTAL_CODE);
        String city = user.getProperty(Member.PROP_CITY);

        StringBuilder body = new StringBuilder();
        body.append("Der Benutzer [").append(username).append("] möchte seine Profildaten aktualisieren. Bitte überprüfen Sie diese.");
        body.append("\n").append("\n");
        
        body.append("Neue Daten:\n");
        body.append("E-Mail: ").append(newMail).append("\n");
        body.append("Telefonnummer: ").append(newPhoneNumber).append("\n");
        body.append("Mobiltelefonnummer: ").append(newMobilePhoneNumber).append("\n");
        body.append("Adresse: ").append(newAddress).append("\n");
        body.append("Postleitzahl: ").append(newPostalCode).append("\n");
        body.append("Ort: ").append(newCity).append("\n");
        body.append("\n").append("\n");
        
        body.append("Bisherige Daten:\n");
        body.append("E-Mail: ").append(mail).append("\n");
        body.append("Telefonnummer: ").append(phoneNumber).append("\n");
        body.append("Mobiltelefonnummer: ").append(mobilePhoneNumber).append("\n");
        body.append("Adresse: ").append(address).append("\n");
        body.append("Postleitzahl: ").append(postalCode).append("\n");
        body.append("Ort: ").append(city).append("\n");
        body.append("\n").append("\n");
        
        body.append("Änderungen bestätigen:\n");
        body.append(acceptLink).append("\n");
        body.append("Änderungen ablehnen:\n");
        body.append(declineLink);

        return body.toString().replaceAll("null", "");
    }

    private Map<String, Object> getParameterMap(String mailFrom,
                                                String receivers,
                                                String subject,
                                                List<MailAttachment> attachments,
                                                String message) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(MailTemplate.MAIL_FROM, mailFrom);
        params.put(MailTemplate.MAIL_TO, receivers);
        params.put(MailTemplate.MAIL_SUBJECT, subject);
        params.put(MailTemplate.MAIL_ATTACHMENTS, attachments);
        params.put(MailTemplate.MAIL_BODY, message);
        params.put(MailTemplate.MAIL_TYPE, "text");
        params.put(MailTemplate.MAIL_CONTENT_TYPE, "text/plain");
        return params;
    }

    private boolean savePendingUpdateInfos(Node pendingUpdateRoot,
                                           String username,
                                           String prename,
                                           String lastname,
                                           String mail,
                                           String phoneNumber,
                                           String mobilePhoneNumber,
                                           String address,
                                           String postalCode,
                                           String city) {
        Node pendingRegistration = NodeUtils.createChildNode(pendingUpdateRoot, username);
        if (pendingRegistration == null) {
            return false;
        }

        NodeUtils.setStringProperty(pendingRegistration, Member.PROP_USERNAME, username);
        NodeUtils.setStringProperty(pendingRegistration, Member.PROP_PRENAME, prename);
        NodeUtils.setStringProperty(pendingRegistration, Member.PROP_LASTNAME, lastname);
        NodeUtils.setStringProperty(pendingRegistration, Member.PROP_MAIL, mail);
        NodeUtils.setStringProperty(pendingRegistration, Member.PROP_PHONE_NUMBER, phoneNumber);
        NodeUtils.setStringProperty(pendingRegistration, Member.PROP_MOBILE_PHONE_NUMBER, mobilePhoneNumber);
        NodeUtils.setStringProperty(pendingRegistration, Member.PROP_ADDRESS, address);
        NodeUtils.setStringProperty(pendingRegistration, Member.PROP_POSTAL_CODE, postalCode);
        NodeUtils.setStringProperty(pendingRegistration, Member.PROP_CITY, city);
        return true;
    }

    @SuppressWarnings("deprecation")
    private boolean replicateUserToAuthor(String path,
                                          String uuid) {
        UserManager userManager = SecuritySupport.Factory.getInstance().getUserManager(Realm.REALM_SYSTEM.getName());
        User systemUser = userManager.getSystemUser();

        SimpleSyndicator simpleSyndicator = new SimpleSyndicator();
        simpleSyndicator.init(systemUser, RepositoryConstants.USERS, "users", new Rule());
        try {
            simpleSyndicator.setResouceCollector(new ResourceCollector());
        } catch (ExchangeException e1) {
            LOG.error("Unable to set resource collector to syndicator.", e1);
        }

        ActivationCommand activation = new ActivationCommand();
        activation.setRepository(RepositoryConstants.USERS);
        activation.setEnabled(true);
        activation.setItemTypes("mgnl:contentNode");
        activation.setPath(path);
        activation.setRecursive(false);
        activation.setUuid(uuid);
        activation.setSyndicator(simpleSyndicator);
        MgnlContext.setInstance(MgnlContext.getSystemContext());
        return activation.execute(MgnlContext.getSystemContext());
    }
}
