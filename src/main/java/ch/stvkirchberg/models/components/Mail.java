
package ch.stvkirchberg.models.components;

import info.magnolia.module.templatingkit.functions.STKTemplatingFunctions;
import info.magnolia.rendering.model.RenderingModel;
import info.magnolia.rendering.template.TemplateDefinition;
import info.magnolia.templating.functions.TemplatingFunctions;

import javax.jcr.Node;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.stvkirchberg.models.common.Notification;

public class Mail<RD extends TemplateDefinition> extends MemberSearch<TemplateDefinition> {

    private static Logger LOG = LoggerFactory.getLogger(Mail.class);

    private String title;

    private Notification notification;

    public Mail(Node content, TemplateDefinition definition, RenderingModel<?> parent, STKTemplatingFunctions stkFunctions, TemplatingFunctions templatingFunctions) {
        super(content, definition, parent, stkFunctions, templatingFunctions);
        this.setNotification(new Notification(getRequest()));
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
