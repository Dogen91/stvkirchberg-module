/* (c) 2013 holder contact. All rights reserved. 
 * This material is solely and exclusively owned by holder 
 * and may not be reproduced elsewhere without prior written approval.
 */
 
package ch.stvkirchberg.models.components;

import info.magnolia.cms.security.auth.login.LoginResult;
import info.magnolia.context.MgnlContext;
import info.magnolia.context.WebContext;
import info.magnolia.jcr.util.PropertyUtil;
import info.magnolia.module.publicuserregistration.frontend.action.BasePublicUserParagraphAction;
import info.magnolia.rendering.model.EarlyExecutionAware;
import info.magnolia.rendering.model.RenderingModel;
import info.magnolia.rendering.template.RenderableDefinition;

import javax.jcr.Node;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
 
public class Login extends BasePublicUserParagraphAction implements EarlyExecutionAware {

    public Login(Node content, RenderableDefinition definition, RenderingModel<?> parent) {
        super(content, definition, parent);
    }

    public String executeEarly() {
        if (isAuthenticated()) {

            LoginResult loginResult = LoginResult.getCurrentLoginResult();
            if (loginResult.getStatus() == LoginResult.STATUS_SUCCEEDED) {
                String redirectTarget = getRedirectTarget();
                if (StringUtils.isNotEmpty(redirectTarget))
                    return "redirect:" + redirectTarget;
            }
        }

        return super.execute();
    }

    private String getRedirectTarget() {
        HttpServletRequest request = ((WebContext) MgnlContext.getInstance()).getRequest();
        String redirectTarget = request.getParameter("ref");
        if(StringUtils.isEmpty(redirectTarget)){
            redirectTarget = PropertyUtil.getString(content, "targetPage");
        }
        return redirectTarget;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void setParent(RenderingModel parentModel) {
        this.parentModel = parentModel;        
    }}
