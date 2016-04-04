/*
 * (c) 2013 holder contact. All rights reserved. This material is solely and exclusively owned by holder and
 * may not be reproduced elsewhere without prior written approval.
 */

package ch.stvkirchberg.models.common;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import ch.stvkirchberg.utils.NotificationUtil;

public class Notification {

    private String formErrorMsg;

    private String formMsg;

    public Notification(HttpServletRequest request) {
        this.formErrorMsg = (String) request.getSession().getAttribute(NotificationUtil.ATTR_FORM_ERROR_MSG);
        this.formMsg = (String) request.getSession().getAttribute(NotificationUtil.ATTR_FORM_MSG);
        if (StringUtils.isNotEmpty(this.formErrorMsg)) {
            request.getSession().removeAttribute(NotificationUtil.ATTR_FORM_ERROR_MSG);
        }
        if (StringUtils.isNotEmpty(this.formMsg)) {
            request.getSession().removeAttribute(NotificationUtil.ATTR_FORM_MSG);
        }
    }

    public Notification(String errorMsg, String formMsg) {
        this.formErrorMsg = errorMsg;
        this.formMsg = formMsg;
    }

    public String getFormErrorMsg() {
        return formErrorMsg;
    }

    public void setFormErrorMsg(String formErrorMsg) {
        this.formErrorMsg = formErrorMsg;
    }

    public String getFormMsg() {
        return formMsg;
    }

    public void setFormMsg(String formMsg) {
        this.formMsg = formMsg;
    }

}
