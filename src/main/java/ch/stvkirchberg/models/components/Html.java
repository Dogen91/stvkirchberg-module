
package ch.stvkirchberg.models.components;

import info.magnolia.module.templatingkit.functions.STKTemplatingFunctions;
import info.magnolia.rendering.model.RenderingModel;
import info.magnolia.rendering.template.TemplateDefinition;
import info.magnolia.templating.functions.TemplatingFunctions;

import javax.jcr.Node;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.stvkirchberg.utils.NodeUtils;

public class Html<RD extends TemplateDefinition> extends Component<TemplateDefinition> {

    @SuppressWarnings("unused")
    private static Logger LOG = LoggerFactory.getLogger(Html.class);

    private static final String PROP_TITLE = "title";

    private static final String PROP_HTML = "html";

    private String title;

    private String html;

    public Html(Node content, TemplateDefinition definition, RenderingModel<?> parent, STKTemplatingFunctions stkFunctions, TemplatingFunctions templatingFunctions) {
        super(content, definition, parent, stkFunctions, templatingFunctions);
        init();
    }

    private void init() {
        this.title = NodeUtils.getStringProperty(content, PROP_TITLE);
        this.html = NodeUtils.getStringProperty(content, PROP_HTML);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

}
