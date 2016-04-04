/*
 * (c) 2012 holder contact. All rights reserved. This material is solely and exclusively owned by holder and
 * may not be reproduced elsewhere without prior written approval.
 */

package ch.stvkirchberg.servlets;

import info.magnolia.cms.security.User;
import info.magnolia.cms.security.UserManager;
import info.magnolia.context.MgnlContext;
import info.magnolia.module.mail.MailModule;
import info.magnolia.module.mail.MailTemplate;
import info.magnolia.module.mail.MgnlMailFactory;
import info.magnolia.module.mail.handlers.MgnlMailHandler;
import info.magnolia.module.mail.templates.MailAttachment;
import info.magnolia.module.mail.templates.MgnlEmail;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
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
import ch.stvkirchberg.models.common.ShopItemModel;
import ch.stvkirchberg.utils.NotificationUtil;

public class ShopOrderServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static Logger LOG = LoggerFactory.getLogger(ShopOrderServlet.class);

    private static final String PREFIX_ARTICLE = "article";

    private static final String PREFIX_CUT = "cut";

    private static final String PREFIX_SIZES_MAN = "sizesMan";

    private static final String PREFIX_SIZES_LADY = "sizesLady";

    private static final String PREFIX_AMOUNT = "amount";

    @Override
    protected void service(HttpServletRequest req,
                           HttpServletResponse resp) throws ServletException, IOException {

        Map<String, ShopItemModel> idToItemMap = getIdToItemMap(req);

        completeItems(idToItemMap);

        if (idToItemMap.size() <= 0) {
            NotificationUtil.redirectToRefererWithMsg(req, resp, NotificationUtil.ATTR_FORM_ERROR_MSG, "Es wurden keine Artikel ausgewählt. Die Bestellung konnte daher nicht abgeschickt werden.");
        } else {
            Member member = getMember(req, resp);
            if (member == null) {
                NotificationUtil.redirectToRefererWithMsg(req, resp, NotificationUtil.ATTR_FORM_ERROR_MSG, "Ihre Bestellung konnte nicht abgeschickt werden, da Sie nicht angemeldet sind.");
            } else {
                try {
                    sendMailToShopMaintainer("noreply@stvkirchberg.ch", req.getParameter("receiver"), "Bestellung STV Kirchberg", getMailBody(idToItemMap, member));
                    NotificationUtil.redirectToRefererWithMsg(req, resp, NotificationUtil.ATTR_FORM_MSG, "Ihre Bestellung wurde erfolgreich verschickt und wird in den nächsten Tagen bearbeitet.");
                } catch (Exception e) {
                    LOG.error("Unable to send mail to shop maintainer", e);
                    NotificationUtil.redirectToRefererWithMsg(req, resp, NotificationUtil.ATTR_FORM_ERROR_MSG, "Ihre Bestellung konnte aufgrund eines technischen Problems nicht verschickt werden. Bitte wenden Sie sich an den Administrator.");
                }
            }
        }

    }

    private Member getMember(HttpServletRequest req,
                             HttpServletResponse resp) throws IOException {
        User user = MgnlContext.getUser();

        if (user == null || UserManager.ANONYMOUS_USER.equals(user.getName())) {
            return null;
        }
        Member member = new Member(user);
        return member;
    }

    private void completeItems(Map<String, ShopItemModel> idToItemMap) {
        List<String> emptyItems = new LinkedList<String>();
        for (Map.Entry<String, ShopItemModel> entry : idToItemMap.entrySet()) {
            ShopItemModel item = entry.getValue();
            if (item.getAmount() <= 0) {
                emptyItems.add(entry.getKey());
            } else {
                if ("man".equals(item.getCut())) {
                    item.setSize(item.getSizeMan());
                } else {
                    item.setSize(item.getSizeLady());
                }
            }
        }
        for (String key : emptyItems) {
            idToItemMap.remove(key);
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, ShopItemModel> getIdToItemMap(HttpServletRequest req) {
        Map<String, ShopItemModel> idToItemMap = new HashMap<String, ShopItemModel>();
        Enumeration<String> params = req.getParameterNames();
        while (params.hasMoreElements()) {
            String param = params.nextElement();
            if (param.startsWith(PREFIX_ARTICLE)) {
                ShopItemModel item = getItemFromMap(idToItemMap, param);
                item.setArticle(req.getParameter(param));
            } else if (param.startsWith(PREFIX_AMOUNT)) {
                ShopItemModel item = getItemFromMap(idToItemMap, param);
                item.setAmount(Integer.parseInt(req.getParameter(param)));
            } else if (param.startsWith(PREFIX_CUT)) {
                ShopItemModel item = getItemFromMap(idToItemMap, param);
                item.setCut(req.getParameter(param));
            } else if (param.startsWith(PREFIX_SIZES_LADY)) {
                ShopItemModel item = getItemFromMap(idToItemMap, param);
                item.setSizeLady(req.getParameter(param));
            } else if (param.startsWith(PREFIX_SIZES_MAN)) {
                ShopItemModel item = getItemFromMap(idToItemMap, param);
                item.setSizeMan(req.getParameter(param));
            }
        }
        return idToItemMap;
    }

    private ShopItemModel getItemFromMap(Map<String, ShopItemModel> idToItemMap,
                                         String param) {
        int idx = param.indexOf("_");
        String id = param.substring(idx);
        ShopItemModel item = idToItemMap.get(id);
        if (item == null) {
            item = new ShopItemModel();
            idToItemMap.put(id, item);
        }
        return item;
    }

    @SuppressWarnings("deprecation")
    private void sendMailToShopMaintainer(String from,
                                          String receiver,
                                          String subject,
                                          String message) throws Exception {
        if (StringUtils.isNotEmpty(receiver)) {
            Map<String, Object> params = getParameterMap(from, receiver, subject, null, message);

            MgnlMailFactory mailFactory = MailModule.getInstance().getFactory();

            MgnlEmail mgnlEmail = mailFactory.getEmail(params, null);
            MgnlMailHandler emailHandler = mailFactory.getEmailHandler();

            emailHandler.prepareAndSendMail(mgnlEmail);
        }
    }

    private String getMailBody(Map<String, ShopItemModel> idToItemMap,
                               Member member) {
        StringBuilder sb = new StringBuilder();
        sb.append("Der Benutzer [" + member.getUsername() + "] hat soeben eine Bestellung über die Webseite aufgegeben.");
        sb.append("\n");
        sb.append("Dabei hat er folgende Auswahl getroffen:");
        sb.append("\n").append("\n");
        for (ShopItemModel item : idToItemMap.values()) {
            if (item.getAmount() > 0) {
                sb.append("Artikel: ").append(item.getArticle()).append("\n");
                sb.append("Schnitt: ").append(item.getCut()).append("\n");
                sb.append("Grösse: ").append(item.getSize()).append("\n");
                sb.append("Anzahl: ").append(item.getAmount());
                sb.append("\n").append("\n");
            }
        }
        sb.append("Benutzerdaten: ").append("\n");
        sb.append("Vorname: ").append(member.getPrename()).append("\n");
        sb.append("Nachname: ").append(member.getLastname()).append("\n");
        sb.append("Adresse: ").append(member.getAddress()).append("\n");
        sb.append("Ort: ").append(member.getPostalCode()).append(" ").append(member.getCity()).append("\n");
        sb.append("Telefon: ").append(member.getPhoneNumber()).append("\n");
        sb.append("Mobile: ").append(member.getMobilePhoneNumber()).append("\n");
        sb.append("Mail: ").append(member.getMail()).append("\n");
        sb.append("STV Nummer: ").append(member.getStvNumber()).append("\n");
        sb.append("\n");
        sb.append("Bitte bestätige dem Benutzer den erhalt dieser Bestellung.").append("\n");
        sb.append("\n");
        sb.append("Freundliche Grüsse").append("\n");
        sb.append("STV Kirchberg");
        return sb.toString();
    }

    private Map<String, Object> getParameterMap(String mailFrom,
                                                String receiver,
                                                String subject,
                                                List<MailAttachment> attachments,
                                                String message) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(MailTemplate.MAIL_FROM, mailFrom);
        params.put(MailTemplate.MAIL_TO, receiver);
        params.put(MailTemplate.MAIL_SUBJECT, subject);
        params.put(MailTemplate.MAIL_ATTACHMENTS, attachments);
        params.put(MailTemplate.MAIL_BODY, message);
        params.put(MailTemplate.MAIL_TYPE, "text");
        params.put(MailTemplate.MAIL_CONTENT_TYPE, "text/plain");
        return params;
    }

}
