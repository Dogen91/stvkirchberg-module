
package ch.stvkirchberg.models.components;

import info.magnolia.module.templatingkit.dam.Asset;
import info.magnolia.module.templatingkit.functions.STKTemplatingFunctions;
import info.magnolia.module.templatingkit.style.CssSelectorBuilder;
import info.magnolia.module.templatingkit.templates.components.ImageModel;
import info.magnolia.rendering.model.RenderingModel;
import info.magnolia.rendering.template.TemplateDefinition;
import info.magnolia.templating.functions.TemplatingFunctions;

import javax.jcr.Node;

import ch.stvkirchberg.utils.STVKConstants;

public class Sponsor extends ImageModel<TemplateDefinition> {

    public Sponsor(Node content, TemplateDefinition definition, RenderingModel<?> parent, STKTemplatingFunctions stkFunctions, CssSelectorBuilder cssSelectorBuilder, TemplatingFunctions templatingFunctions) {
        super(content, definition, parent, stkFunctions, cssSelectorBuilder, templatingFunctions);
    }

    @Override
    public Asset getImage() {
        Asset asset = stkFunctions.getAsset(content, getImageName());
        return stkFunctions.getAssetVariation(asset, STVKConstants.IMAGE_VARIATION_SPONSOR);
    }
}
