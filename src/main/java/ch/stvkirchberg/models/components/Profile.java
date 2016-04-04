/*
 * (c) 2012 holder contact. All rights reserved. This material is solely and exclusively owned by holder and
 * may not be reproduced elsewhere without prior written approval.
 */

package ch.stvkirchberg.models.components;

import info.magnolia.cms.security.User;
import info.magnolia.module.templatingkit.functions.STKTemplatingFunctions;
import info.magnolia.rendering.model.RenderingModel;
import info.magnolia.rendering.template.TemplateDefinition;
import info.magnolia.repository.RepositoryConstants;
import info.magnolia.templating.functions.TemplatingFunctions;

import javax.jcr.Node;
import javax.jcr.Session;

import ch.stvkirchberg.models.common.Member;
import ch.stvkirchberg.models.common.Notification;
import ch.stvkirchberg.utils.JcrUtil;
import ch.stvkirchberg.utils.MemberSearchUtil;
import ch.stvkirchberg.utils.NodeUtils;

public class Profile<RD extends TemplateDefinition> extends Component<TemplateDefinition> {
    private static final String PROP_TITLE = "title";

    public static final String ATTR_FORM_ERROR_MSG = "formErrorMsg";

    public static final String ATTR_FORM_MSG = "formMsg";

    private String title;

    private Notification notification;

    private Member member;

    public Profile(Node content, TemplateDefinition definition, RenderingModel<?> parent, STKTemplatingFunctions stkFunctions, TemplatingFunctions templatingFunctions) {
        super(content, definition, parent, stkFunctions, templatingFunctions);
        init();
    }

    private void init() {
        this.setTitle(NodeUtils.getStringProperty(content, PROP_TITLE));
        this.setNotification(new Notification(getRequest()));
        User user = getWebContext().getUser();
        if (user != null) {
            Session adminSession = JcrUtil.getAdminSession(RepositoryConstants.USERS);
            Node userNode = MemberSearchUtil.searchUserNode(adminSession, user.getName());
            if (userNode != null) {
                this.setMember(new Member(userNode));
            }
            adminSession.logout();
        }
    }

    public Member getMember() {
        return member == null ? new Member() : member;
    }

    public void setMember(Member member) {
        this.member = member;
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

}
