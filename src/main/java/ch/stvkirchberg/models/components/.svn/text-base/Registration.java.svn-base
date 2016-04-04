
package ch.stvkirchberg.models.components;

import info.magnolia.module.templatingkit.functions.STKTemplatingFunctions;
import info.magnolia.rendering.model.RenderingModel;
import info.magnolia.rendering.template.TemplateDefinition;
import info.magnolia.templating.functions.TemplatingFunctions;

import javax.jcr.Node;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.stvkirchberg.models.common.Notification;
import ch.stvkirchberg.utils.NodeUtils;

public class Registration<RD extends TemplateDefinition> extends Component<TemplateDefinition> {

    @SuppressWarnings("unused")
    private static Logger LOG = LoggerFactory.getLogger(Registration.class);

    private static final String PROP_TITLE = "title";

    private String title;

    private Notification notification;

    public Registration(Node content, TemplateDefinition definition, RenderingModel<?> parent, STKTemplatingFunctions stkFunctions, TemplatingFunctions templatingFunctions) {
        super(content, definition, parent, stkFunctions, templatingFunctions);
        init();
    }

    private void init() {
        this.title = NodeUtils.getStringProperty(content, PROP_TITLE);
        this.notification = new Notification(getRequest());
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
