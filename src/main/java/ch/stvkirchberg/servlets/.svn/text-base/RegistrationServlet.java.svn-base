/*
 * (c) 2012 holder contact. All rights reserved. This material is solely and exclusively owned by holder and
 * may not be reproduced elsewhere without prior written approval.
 */

package ch.stvkirchberg.servlets;

import info.magnolia.cms.security.Realm;
import info.magnolia.cms.security.SecuritySupport;
import info.magnolia.cms.security.User;
import info.magnolia.cms.security.UserManager;
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
import org.apache.jackrabbit.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.stvkirchberg.config.WebsiteConfig;
import ch.stvkirchberg.models.common.Member;
import ch.stvkirchberg.utils.JcrUtil;
import ch.stvkirchberg.utils.NodeUtils;
import ch.stvkirchberg.utils.NotificationUtil;

public class RegistrationServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static Logger LOG = LoggerFactory.getLogger(RegistrationServlet.class);

    private static final char[] SPECIAL_CHARS = "äüö./ +@*%&()=?`^!$#".toCharArray();

    @Override
    protected void service(HttpServletRequest req,
                           HttpServletResponse resp) throws ServletException, IOException {

        String username = req.getParameter("username");
        String prename = req.getParameter("prename");
        String lastname = req.getParameter("lastname");
        String mail = req.getParameter("mail");
        String password = req.getParameter("password");
        String passwordConfirmation = req.getParameter("passwordConfirmation");

        if (StringUtils.isNotEmpty(username) && StringUtils.indexOfAny(username, SPECIAL_CHARS) >= 0) {
            NotificationUtil.redirectToRefererWithMsg(req, resp, NotificationUtil.ATTR_FORM_ERROR_MSG, "Im Benutzername sind keine Sonderzeichen erlaubt. Ebenso darf kein Leerzeichen verwendet werden.");
        } else {
            UserManager userManager = SecuritySupport.Factory.getInstance().getUserManager(Realm.REALM_ADMIN.getName());
            User user = userManager.getUser(username);

            boolean hasEmptyFields = StringUtils.isEmpty(username) || StringUtils.isEmpty(prename) || StringUtils.isEmpty(lastname) || StringUtils.isEmpty(mail) || StringUtils.isEmpty(password);
            boolean hasPasswordError = StringUtils.isEmpty(password) || !password.equals(passwordConfirmation);
            boolean userDoesNotExist = user == null;

            handleRegistration(req, resp, username, prename, lastname, mail, password, hasEmptyFields, hasPasswordError, userDoesNotExist);
        }
    }

    private void handleRegistration(HttpServletRequest req,
                                    HttpServletResponse resp,
                                    String username,
                                    String prename,
                                    String lastname,
                                    String mail,
                                    String password,
                                    boolean hasEmptyFields,
                                    boolean hasPasswordError,
                                    boolean userDoesNotExist) throws IOException {
        if (hasEmptyFields || hasPasswordError) {
            NotificationUtil.redirectToRefererWithMsg(req, resp, NotificationUtil.ATTR_FORM_ERROR_MSG, "Bitte füllen sie alle Felder Sinngemäss aus.");
        } else if (userDoesNotExist) {
            processRegistration(req, resp, username, prename, lastname, mail, password);
        } else {
            NotificationUtil.redirectToRefererWithMsg(req, resp, NotificationUtil.ATTR_FORM_ERROR_MSG, "Ein Benutzer mit dem Benutzername " + username + " existiert bereits.");
        }
    }

    private void processRegistration(HttpServletRequest req,
                                     HttpServletResponse resp,
                                     String username,
                                     String prename,
                                     String lastname,
                                     String mail,
                                     String password) throws IOException {
        Session adminSession = JcrUtil.getAdminSession(RepositoryConstants.USERS);
        Node pendingRegistrationsRoot = NodeUtils.getOrCreateByPath("/pendingRegistrations", JcrConstants.NT_UNSTRUCTURED, adminSession);
        if (pendingRegistrationsRoot == null) {
            adminSession.logout();
            NotificationUtil.redirectToRefererWithMsg(req, resp, NotificationUtil.ATTR_FORM_ERROR_MSG, "Ein unerwarteter Fehler ist aufgetreten. Bitte melden Sie sich beim Administrator.");
        } else if (NodeUtils.childNodeExists(pendingRegistrationsRoot, username)) {
            adminSession.logout();
            NotificationUtil.redirectToRefererWithMsg(req, resp, NotificationUtil.ATTR_FORM_ERROR_MSG, "Ein Benutzer mit dem Benutzername " + username + " existiert bereits.");
        } else {
            boolean infosSaved = savePendingRegistrationInfos(pendingRegistrationsRoot, username, prename, lastname, mail, password) && NodeUtils.saveChangesInSession(adminSession);
            if (infosSaved) {
                boolean sentSuccessful = sendNotificationToAddressAdmin(username, prename, lastname, mail);
                if (sentSuccessful) {
                    adminSession.logout();
                    NotificationUtil.redirectToRefererWithMsg(req, resp, NotificationUtil.ATTR_FORM_MSG, "Ihre Registrationsanfrage wurde abgeschickt. Sobald der Account aktiviert wurde, werden Sie via E-Mail benachrichtigt.");
                } else {
                    adminSession.logout();
                    NotificationUtil.redirectToRefererWithMsg(req, resp, NotificationUtil.ATTR_FORM_ERROR_MSG, "Ein unerwarteter Fehler ist aufgetreten. Bitte melden Sie sich beim Administrator.");
                }
            } else {
                adminSession.logout();
                NotificationUtil.redirectToRefererWithMsg(req, resp, NotificationUtil.ATTR_FORM_ERROR_MSG, "Ein unerwarteter Fehler ist aufgetreten. Bitte melden Sie sich beim Administrator.");
            }
        }
    }

    @SuppressWarnings("deprecation")
    private boolean sendNotificationToAddressAdmin(String username,
                                                   String prename,
                                                   String lastname,
                                                   String mail) {
        WebsiteConfig websiteConfig = WebsiteConfig.getInstance();
        String receivers = websiteConfig.getMailOfAddressMaintainer();
        String subject = "STV Kirchberg: Registrierung";
        String message = getNotificationMailBody(username, prename, lastname, mail);

        Map<String, Object> params = getParameterMap("noreply@stvkirchberg.ch", receivers, subject, null, message);

        MgnlMailFactory mailFactory = MailModule.getInstance().getFactory();
        MgnlEmail mgnlEmail = mailFactory.getEmail(params, null);
        MgnlMailHandler emailHandler = mailFactory.getEmailHandler();

        try {
            emailHandler.prepareAndSendMail(mgnlEmail);
            return true;
        } catch (Exception e) {
            LOG.error("Unable to send registration info to admin.", e);
            return false;
        }
    }

    private String getNotificationMailBody(String username,
                                           String prename,
                                           String lastname,
                                           String mail) {
        StringBuilder body = new StringBuilder();
        body.append("Der Benutzer [").append(username).append("] hat sich soeben mit folgenden Daten registriert:");
        body.append("\n").append("\n");
        body.append("Username: ").append(username).append("\n");
        body.append("Vorname: ").append(prename).append("\n");
        body.append("Nachname: ").append(lastname).append("\n");
        body.append("E-Mail: ").append(mail).append("\n");
        body.append("\n").append("\n");
        body.append("Um den Benutzer zu aktivieren müssen folgende Schritte ausgeführt werden:\n");
        body.append("1. Benutzer auf Authoreninstanz suchen\n");
        body.append("2. Den Benutzername auf den Usernamen setzen\n");
        body.append("3. Den Benutzer bearbeiten und das Flag Enable aktivieren\n");
        body.append("4. Den Benutzer auf die Publish Instanz aktivieren");

        return body.toString();
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

    private boolean savePendingRegistrationInfos(Node pendingRegistrationsRoot,
                                                 String username,
                                                 String prename,
                                                 String lastname,
                                                 String mail,
                                                 String password) {
        Node pendingRegistration = NodeUtils.createChildNode(pendingRegistrationsRoot, username);
        if (pendingRegistration == null) {
            return false;
        }

        password = Base64.encode(password);
        NodeUtils.setStringProperty(pendingRegistration, Member.PROP_USERNAME, username);
        NodeUtils.setStringProperty(pendingRegistration, Member.PROP_PRENAME, prename);
        NodeUtils.setStringProperty(pendingRegistration, Member.PROP_LASTNAME, lastname);
        NodeUtils.setStringProperty(pendingRegistration, Member.PROP_MAIL, mail);
        NodeUtils.setStringProperty(pendingRegistration, "password", password);
        return true;
    }

}
