/*
 * (c) 2012 holder contact. All rights reserved. This material is solely and exclusively owned by holder and
 * may not be reproduced elsewhere without prior written approval.
 */

package ch.stvkirchberg.models.components;

import info.magnolia.module.templatingkit.functions.STKTemplatingFunctions;
import info.magnolia.rendering.model.RenderingModel;
import info.magnolia.rendering.template.TemplateDefinition;
import info.magnolia.templating.functions.TemplatingFunctions;

import javax.jcr.Node;

import ch.stvkirchberg.models.common.Notification;
import ch.stvkirchberg.utils.NodeUtils;

public class ChangePwd<RD extends TemplateDefinition> extends Component<TemplateDefinition> {
    private static final String PROP_TITLE = "title";

    public static final String ATTR_TOKEN = "token";

    private String title;

    private Notification notification;

    private String token;

    public ChangePwd(Node content, TemplateDefinition definition, RenderingModel<?> parent, STKTemplatingFunctions stkFunctions, TemplatingFunctions templatingFunctions) {
        super(content, definition, parent, stkFunctions, templatingFunctions);
        init();
    }

    private void init() {
        this.setTitle(NodeUtils.getStringProperty(content, PROP_TITLE));
        this.setNotification(new Notification(getRequest()));
        this.token = getRequest().getParameter("token");
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
