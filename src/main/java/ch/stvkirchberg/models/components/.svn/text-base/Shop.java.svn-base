
package ch.stvkirchberg.models.components;

import info.magnolia.module.templatingkit.functions.STKTemplatingFunctions;
import info.magnolia.rendering.model.RenderingModel;
import info.magnolia.rendering.template.TemplateDefinition;
import info.magnolia.templating.functions.TemplatingFunctions;

import javax.jcr.Node;

import ch.stvkirchberg.models.common.Notification;
import ch.stvkirchberg.utils.NodeUtils;

public class Shop extends Component<TemplateDefinition> {

    public Shop(Node content, TemplateDefinition definition, RenderingModel<?> parent, STKTemplatingFunctions stkFunctions, TemplatingFunctions templatingFunctions) {
        super(content, definition, parent, stkFunctions, templatingFunctions);

        this.setNotification(new Notification(getRequest()));
        this.setReceiverMail(NodeUtils.getStringProperty(content, "receiverMail"));
    }

    private String title;

    private String receiverMail;

    private Notification notification;

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

    public String getReceiverMail() {
        return receiverMail;
    }

    public void setReceiverMail(String receiverMail) {
        this.receiverMail = receiverMail;
    }
}
