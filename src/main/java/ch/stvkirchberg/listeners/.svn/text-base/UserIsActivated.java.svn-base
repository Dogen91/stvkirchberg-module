/*
 * (c) 2012 holder contact. All rights reserved. This material is solely and exclusively owned by holder and
 * may not be reproduced elsewhere without prior written approval.
 */

package ch.stvkirchberg.listeners;

import info.magnolia.cms.exchange.ExchangeException;
import info.magnolia.cms.security.Digester;
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
import info.magnolia.module.observation.commands.RestrictToNodeTypeEventListener;
import info.magnolia.repository.RepositoryConstants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.observation.Event;
import javax.jcr.observation.EventIterator;

import org.apache.commons.lang.StringUtils;
import org.apache.jackrabbit.JcrConstants;
import org.apache.jackrabbit.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.stvkirchberg.config.WebsiteConfig;
import ch.stvkirchberg.models.common.Member;
import ch.stvkirchberg.utils.JcrUtil;
import ch.stvkirchberg.utils.NodeUtils;

public class UserIsActivated extends RestrictToNodeTypeEventListener {

    private static Logger LOG = LoggerFactory.getLogger(UserIsActivated.class);

    private static final String ADMINPATH = "/admin/";

    @Override
    public void onEvent(EventIterator events) {
        // TODO pforster: check if public instance
        Session adminSession = JcrUtil.getAdminSession(RepositoryConstants.USERS);
        Event event = events.nextEvent();
        String username = getUsername(event);
        Node pendingRegistrationsRoot = NodeUtils.getOrCreateByPath("/pendingRegistrations", JcrConstants.NT_UNSTRUCTURED, adminSession);
        if (pendingRegistrationsRoot != null) {
            Node pendingRegistration = getPendingRegistrationNode(pendingRegistrationsRoot, username);
            if (pendingRegistration != null) {
                // Activated User is in registration process
                UserManager userManager = SecuritySupport.Factory.getInstance().getUserManager(Realm.REALM_ADMIN.getName());
                User user = userManager.getUser(username);

                if (user == null) {
                    LOG.error("Could not get user with username [" + username + "], but this user is in registration process");
                } else {
                    setPasswordToUser(pendingRegistration, userManager, user);

                    Node userNode = searchUserNode(adminSession, username);

                    boolean replicationSuccessful = replicateUserToAuthor(ADMINPATH + username, NodeUtils.getNodeIdentifier(userNode));
                    if (replicationSuccessful) {
                        String mail = NodeUtils.getStringProperty(pendingRegistration, Member.PROP_MAIL);
                        NodeUtils.removeNode(pendingRegistration);
                        NodeUtils.saveChangesInSession(adminSession);
                        boolean sentSuccessful = sendNotificationToRegisteredUser(mail, username);
                        if (!sentSuccessful) {
                            LOG.error("User [" + username + "] has been activated successfully. But the notification mail wasn't sent due to an error.");
                        }
                    } else {
                        LOG.error("Unable to replicate activated user: " + username);
                    }
                }
            }
        }

        adminSession.logout();
    }

    @SuppressWarnings("deprecation")
    private boolean sendNotificationToRegisteredUser(String receiver,
                                                     String username) {
        WebsiteConfig websiteConfig = WebsiteConfig.getInstance();
        String sender = websiteConfig.getMailOfAddressMaintainer();
        String subject = "STV Kirchberg: Registrierung abgeschlossen";
        String message = getNotificationMailBody(username);

        Map<String, Object> params = getParameterMap(sender, receiver, subject, null, message);

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

    private String getNotificationMailBody(String username) {
        StringBuilder body = new StringBuilder();
        body.append("Hallo,\n");
        body.append("Dein Benutzer [").append(username).append("] wurde soeben freigegeben. \n");
        body.append("Du kannst dich nun mit deinem Benutzername und dem von Dir gewählten Passwort anmelden. \n");
        body.append("Freundliche Grüsse\n");
        body.append("STV Kirchberg");

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

    private Node searchUserNode(Session adminSession,
                                String username) {
        NodeIterator nodeIterator = JcrUtil.executeQuery(adminSession, RepositoryConstants.USERS, "select * from mgnl:user where jcr:path like '/admin/" + username + "'");
        Node userNode = nodeIterator.nextNode();
        return userNode;
    }

    private void setPasswordToUser(Node pendingRegistration,
                                   UserManager userManager,
                                   User user) {
        String base64EncodedPwd = NodeUtils.getStringProperty(pendingRegistration, "password");
        String password = Base64.decode(base64EncodedPwd);

        String hashedPwd = Digester.getBCrypt(password);
        userManager.setProperty(user, "pswd", JcrUtil.createValue(hashedPwd));
    }

    private Node getPendingRegistrationNode(Node pendingRegistrationsRoot,
                                            String username) {
        Node node = null;
        try {
            node = pendingRegistrationsRoot.getNode(username);
        } catch (PathNotFoundException e) {
            LOG.debug("No pending registration found with nodename [" + username + "]. Note that this can be a valid case.");
        } catch (RepositoryException e) {
            LOG.error("Unable to check if user [" + username + "] is in registration process.", e);
        }
        return node;
    }

    private String getUsername(Event event) {
        String path = null;
        try {
            path = getPath(event);
        } catch (RepositoryException e) {
            LOG.error("Unable to get path of event node", e);
        }
        if (StringUtils.isEmpty(path) || StringUtils.indexOf(path, ADMINPATH) < 0) {
            return StringUtils.EMPTY;
        }
        return StringUtils.replace(path, ADMINPATH, StringUtils.EMPTY);
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
