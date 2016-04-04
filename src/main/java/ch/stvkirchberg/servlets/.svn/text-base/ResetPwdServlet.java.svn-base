/*
 * (c) 2012 holder contact. All rights reserved. This material is solely and exclusively owned by holder and
 * may not be reproduced elsewhere without prior written approval.
 */

package ch.stvkirchberg.servlets;

import info.magnolia.cms.security.Digester;
import info.magnolia.cms.security.SecuritySupport;
import info.magnolia.cms.security.User;
import info.magnolia.cms.security.UserManager;
import info.magnolia.link.LinkUtil;
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
import javax.jcr.RepositoryException;
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
import ch.stvkirchberg.models.components.ChangePwd;
import ch.stvkirchberg.utils.JcrUtil;
import ch.stvkirchberg.utils.NodeUtils;
import ch.stvkirchberg.utils.NotificationUtil;
import ch.stvkirchberg.utils.STVKConstants;

public class ResetPwdServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static Logger LOG = LoggerFactory.getLogger(ResetPwdServlet.class);

    public static final String PENDING_RESETS = "pendingResets";

    @Override
    protected void service(HttpServletRequest req,
                           HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        if (StringUtils.isEmpty(username)) {
            NotificationUtil.redirectToRefererWithMsg(req, resp, NotificationUtil.ATTR_FORM_ERROR_MSG, "Bitte geben sie einen Benutzernamen an.");
            return;
        }
        UserManager userManager = SecuritySupport.Factory.getInstance().getUserManager();
        User user = userManager.getUser(username);
        if (user == null) {
            NotificationUtil.redirectToRefererWithMsg(req, resp, NotificationUtil.ATTR_FORM_ERROR_MSG, "Es wurde kein Benutzer mit diesem Benutzername gefunden");
            return;
        }

        String mail = user.getProperty(Member.PROP_MAIL);
        if (StringUtils.isEmpty(mail)) {
            NotificationUtil.redirectToRefererWithMsg(req, resp, NotificationUtil.ATTR_FORM_ERROR_MSG, "Passwort konnte nicht zurückgesetzt werdern, da der Benutzer keine Mail Adresse eingetragen hat. Melden sie sich beim Adressverwalter");
            return;
        }

        String token = Digester.getMD5Hex(System.nanoTime() + "");

        boolean infoSaved = saveResetInformation(username, token);
        if (!infoSaved) {
            NotificationUtil.redirectToRefererWithMsg(req, resp, NotificationUtil.ATTR_FORM_ERROR_MSG, "Ein unerwarteter Fehler ist aufgetreten. Bitte melden Sie sich beim Administrator.");
            return;
        }

        boolean sentSuccessful = sendNotificationMail(mail, token);
        if (sentSuccessful) {
            NotificationUtil.redirectToRefererWithMsg(req, resp, NotificationUtil.ATTR_FORM_MSG, "Sie sollten in kürze eine Mail mit einem Link erhalten.");
        } else {
            NotificationUtil.redirectToRefererWithMsg(req, resp, NotificationUtil.ATTR_FORM_ERROR_MSG, "Ein technischer Fehler ist aufgetreten. Es konnte keine Mail an ihre Adresse verschickt werden.");
        }
    }

    @SuppressWarnings("deprecation")
    private boolean sendNotificationMail(String mail,
                                         String token) {

        String subject = "STV Kirchberg: Passwort Reset";
        String message;
        try {
            message = getMailBody(token);
        } catch (RepositoryException e) {
            LOG.error("Unable to create absolute link to change password page", e);
            return false;
        }

        Map<String, Object> params = getParameterMap("noreply@stvkirchberg.ch", mail, subject, null, message);

        MgnlMailFactory mailFactory = MailModule.getInstance().getFactory();
        MgnlEmail mgnlEmail = mailFactory.getEmail(params, null);
        MgnlMailHandler emailHandler = mailFactory.getEmailHandler();

        try {
            emailHandler.prepareAndSendMail(mgnlEmail);
            return true;
        } catch (Exception e) {
            LOG.error("Unable to send reset password notification mail to [" + mail + "]", e);
            return false;
        }
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

    private String getMailBody(String token) throws RepositoryException {
        String changePwdPage = WebsiteConfig.getInstance().getChangePwdPage();
        String changePwdLink = LinkUtil.createAbsoluteLink(RepositoryConstants.WEBSITE, changePwdPage);
        StringBuilder body = new StringBuilder();
        body.append("Hallo,\n\n");
        body.append("Sie erhalten diese Nachricht, weil sie ihr Passwort vergessen haben und dieses gerne neu setzen würden.\n");
        body.append("Sollten sie diese Aktion nicht ausgeführt haben, so ignorieren sie diese Nachricht einfach.\n\n");
        body.append("Um ein neues Passwort zu definieren, klicken sie bitte auf den folgenden Link:\n");
        body.append(STVKConstants.STVK_WEBSITE_DOMAIN).append(changePwdLink).append("?").append(ChangePwd.ATTR_TOKEN).append("=").append(token).append("\n\n");
        body.append("Freundliche Grüsse\n");
        body.append("STV Kirchberg");
        return body.toString();

    }

    private boolean saveResetInformation(String username,
                                         String token) throws IOException {
        Session adminSession = JcrUtil.getAdminSession(RepositoryConstants.USERS);
        Node pendingResetRootNode = NodeUtils.getOrCreateByPath("/" + PENDING_RESETS, JcrConstants.NT_UNSTRUCTURED, adminSession);
        if (pendingResetRootNode == null) {
            adminSession.logout();
            return false;
        }

        Node pendingResetNode = NodeUtils.createChildNode(pendingResetRootNode, token);
        NodeUtils.setStringProperty(pendingResetNode, "token", token);
        NodeUtils.setStringProperty(pendingResetNode, "username", username);
        NodeUtils.saveChangesInSession(adminSession);
        adminSession.logout();
        return true;
    }
}
