/*
 * (c) 2013 holder contact. All rights reserved. This material is solely and exclusively owned by holder and
 * may not be reproduced elsewhere without prior written approval.
 */

package ch.stvkirchberg.utils;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotificationUtil {

    private static Logger LOG = LoggerFactory.getLogger(NotificationUtil.class);

    public static final String ATTR_FORM_ERROR_MSG = "formErrorMsg";

    public static final String ATTR_FORM_MSG = "formMsg";

    public static void redirectToRefererWithMsg(HttpServletRequest req,
                                                HttpServletResponse resp,
                                                String msgType,
                                                String msg) throws IOException {
        req.getSession().setAttribute(msgType, msg);
        String referer = req.getHeader("referer");
        referer = addParameterToUrl(referer, "cc", "1");
        resp.sendRedirect(referer);
    }

    public static void printOutMessage(HttpServletResponse resp,
                                       String msg) {
        try {
            PrintWriter writer = resp.getWriter();
            writer.append(msg);
            writer.flush();
        } catch (IOException e) {
            LOG.error("Unable to get writer from response", e);
        }

    }

    private static String addParameterToUrl(String url,
                                            String param,
                                            String value) {
        StringBuilder stringBuilder = new StringBuilder(url);
        if (url.indexOf(param) > 0) {
            return url;
        }

        if (url.indexOf("?") > 0) {
            stringBuilder.append("&");
            stringBuilder.append(param);
            stringBuilder.append("=");
            stringBuilder.append(value);
        } else {
            stringBuilder.append("?");
            stringBuilder.append(param);
            stringBuilder.append("=");
            stringBuilder.append(value);
        }
        return stringBuilder.toString();
    }
}
