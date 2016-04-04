
package ch.stvkirchberg.models.components;

import info.magnolia.context.MgnlContext;
import info.magnolia.context.WebContext;
import info.magnolia.module.templatingkit.functions.STKTemplatingFunctions;
import info.magnolia.module.templatingkit.templates.AbstractSTKTemplateModel;
import info.magnolia.rendering.model.RenderingModel;
import info.magnolia.rendering.template.TemplateDefinition;
import info.magnolia.templating.functions.TemplatingFunctions;

import javax.jcr.Node;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Component<RD extends TemplateDefinition> extends AbstractSTKTemplateModel<TemplateDefinition> {

    private static Logger LOG = LoggerFactory.getLogger(Component.class);

    private WebContext webContext;

    private HttpServletRequest request;

    public Component(Node content, TemplateDefinition definition, RenderingModel<?> parent, STKTemplatingFunctions stkFunctions, TemplatingFunctions templatingFunctions) {
        super(content, definition, parent, stkFunctions, templatingFunctions);
        init();
    }

    private void init() {
        this.webContext = (WebContext) MgnlContext.getInstance();
        this.request = webContext.getRequest();
    }

    public WebContext getWebContext() {
        return webContext;
    }

    public void setWebContext(WebContext webContext) {
        this.webContext = webContext;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

}
