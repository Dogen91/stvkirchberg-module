
package ch.stvkirchberg.models.common;

import info.magnolia.rendering.template.TemplateAvailability;
import info.magnolia.rendering.template.TemplateDefinition;

import javax.jcr.Node;

public class CustomTemplateAvailability implements TemplateAvailability {

    public boolean isAvailable(Node content,
                               TemplateDefinition templateDefinition) {
        return true;
    }

}
