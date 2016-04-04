/*
 * (c) 2012 holder contact. All rights reserved. This material is solely and exclusively owned by holder and
 * may not be reproduced elsewhere without prior written approval.
 */

package ch.stvkirchberg.servlets;

import info.magnolia.cms.security.User;
import info.magnolia.context.MgnlContext;
import info.magnolia.module.mail.MailModule;
import info.magnolia.module.mail.MailTemplate;
import info.magnolia.module.mail.MgnlMailFactory;
import info.magnolia.module.mail.handlers.MgnlMailHandler;
import info.magnolia.module.mail.templates.MailAttachment;
import info.magnolia.module.mail.templates.MgnlEmail;
import info.magnolia.module.mail.util.MailUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.stvkirchberg.models.common.Member;
import ch.stvkirchberg.utils.NotificationUtil;

public class MailServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static Logger LOG = LoggerFactory.getLogger(MailServlet.class);

    private static final long MAX_MAIL_SIZE = 10000000;

    @Override
    protected void service(HttpServletRequest req,
                           HttpServletResponse resp) throws ServletException, IOException {
        String from = getMailFromAdress();
        if (StringUtils.isEmpty(from)) {
            NotificationUtil.redirectToRefererWithMsg(req, resp, NotificationUtil.ATTR_FORM_ERROR_MSG, "Mail konnte nicht verschickt werden. Sie sind entweder nicht angemeldet oder ihrem Benutzer ist keine E-Mail Adresse zugewiesen.");
            return;
        }
        String receivers = req.getParameter("receivers");
        String subject = req.getParameter("subject");
        List<MailAttachment> attachments = setAttachmentType(MailUtil.createAttachmentList());
        String message = req.getParameter("message");

        if (!mailSizeIsOkey(attachments)) {
            NotificationUtil.redirectToRefererWithMsg(req, resp, NotificationUtil.ATTR_FORM_ERROR_MSG, "Die maximale Mailgrösse von 10 MB wurde überschritten. E-Mail konnte nicht verschickt werden.");
            return;
        }

        try {
            sendMailToReceivers(from, receivers, subject, attachments, message);
            NotificationUtil.redirectToRefererWithMsg(req, resp, NotificationUtil.ATTR_FORM_MSG, "Die Nachricht wurde erfolgreich verschickt.");
        } catch (Exception e) {
            LOG.error("Unable to send mail.", e);
            NotificationUtil.redirectToRefererWithMsg(req, resp, NotificationUtil.ATTR_FORM_ERROR_MSG, "Die Nachricht konnte nicht verschickt werden. Überprüfen sie alle Felder auf ihre Korrektheit oder wenden sie sich an den Administrator.");
        }

    }

    @SuppressWarnings("deprecation")
    private void sendMailToReceivers(String from,
                                     String receivers,
                                     String subject,
                                     List<MailAttachment> attachments,
                                     String message) throws Exception {
        for (String receiver : receivers.split(";")) {
            if (StringUtils.isNotEmpty(receiver)) {
                Map<String, Object> params = getParameterMap(from, receiver, subject, attachments, message);

                MgnlMailFactory mailFactory = MailModule.getInstance().getFactory();

                MgnlEmail mgnlEmail = mailFactory.getEmail(params, attachments);
                MgnlMailHandler emailHandler = mailFactory.getEmailHandler();

                emailHandler.prepareAndSendMail(mgnlEmail);
            }
        }
    }

    private boolean mailSizeIsOkey(List<MailAttachment> attachments) {
        if (attachments.isEmpty()) {
            return true;
        }
        long mailSize = 0;
        for (MailAttachment attachment : attachments) {
            mailSize = mailSize + attachment.getFile().length();
        }
        return mailSize <= MAX_MAIL_SIZE;
    }

    private List<MailAttachment> setAttachmentType(List<MailAttachment> attachments) {
        if (attachments.isEmpty()) {
            return attachments;
        }
        for (MailAttachment attachment : attachments) {
            attachment.setDisposition(MailAttachment.DISPOSITION_ATTACHMENT);
        }
        return attachments;
    }

    private String getMailFromAdress() {
        User user = MgnlContext.getUser();
        if (user == null) {
            return StringUtils.EMPTY;
        }
        return user.getProperty(Member.PROP_MAIL);
    }

    private String getFromName() {
        User user = MgnlContext.getUser();
        if (user == null) {
            return StringUtils.EMPTY;
        }
        return user.getProperty(Member.PROP_PRENAME) + " " + user.getProperty(Member.PROP_LASTNAME);
    }

    private Map<String, Object> getParameterMap(String mailFrom,
                                                String receiver,
                                                String subject,
                                                List<MailAttachment> attachments,
                                                String message) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("replyTo", mailFrom);
        params.put(MailTemplate.MAIL_FROM, getFromName() + " <info@stvkirchberg.ch>");
        params.put(MailTemplate.MAIL_TO, receiver);
        params.put(MailTemplate.MAIL_SUBJECT, subject);
        params.put(MailTemplate.MAIL_ATTACHMENTS, attachments);
        params.put(MailTemplate.MAIL_BODY, message);
        params.put(MailTemplate.MAIL_TYPE, "text");
        params.put(MailTemplate.MAIL_CONTENT_TYPE, "text/plain");
        return params;
    }
}
