
package ch.stvkirchberg.models.components;

import info.magnolia.module.templatingkit.dam.Asset;
import info.magnolia.module.templatingkit.functions.STKTemplatingFunctions;
import info.magnolia.module.templatingkit.style.CssSelectorBuilder;
import info.magnolia.module.templatingkit.templates.components.ImageModel;
import info.magnolia.rendering.model.RenderingModel;
import info.magnolia.rendering.template.TemplateDefinition;
import info.magnolia.templating.functions.TemplatingFunctions;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.RepositoryException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.stvkirchberg.utils.NodeUtils;
import ch.stvkirchberg.utils.STVKConstants;

public class ShopItem extends ImageModel<TemplateDefinition> {

    private static Logger LOG = LoggerFactory.getLogger(ShopItem.class);

    private List<String> sizesLady;

    private List<String> sizesMan;

    private String id;

    public ShopItem(Node content, TemplateDefinition definition, RenderingModel<?> parent, STKTemplatingFunctions stkFunctions, CssSelectorBuilder cssSelectorBuilder, TemplatingFunctions templatingFunctions) {
        super(content, definition, parent, stkFunctions, cssSelectorBuilder, templatingFunctions);

        this.sizesLady = readSizes("sizesLady");
        this.sizesMan = readSizes("sizesMan");
        this.id = String.valueOf(System.nanoTime());
    }

    private List<String> readSizes(String nodeName) {
        Node sizesParent = NodeUtils.getNode(content, nodeName);
        List<String> sizes = new ArrayList<String>();
        if (sizesParent == null) {
            return sizes;
        }
        try {
            PropertyIterator properties = sizesParent.getProperties();
            while (properties.hasNext()) {
                Property sizeProperty = properties.nextProperty();
                String name = sizeProperty.getName();
                if (name != null && name.indexOf("jcr") < 0) {
                    sizes.add(sizeProperty.getString());
                }
            }
        } catch (RepositoryException e) {
            LOG.warn("Unable to fetch child nodes. No sizes defined.", e);
        }
        return sizes;
    }

    @Override
    public Asset getImage() {
        Asset asset = stkFunctions.getAsset(content, getImageName());
        return stkFunctions.getAssetVariation(asset, STVKConstants.IMAGE_VARIATION_SHOP_ITEM);
    }

    public Asset getBigImage() {
        Asset asset = stkFunctions.getAsset(content, getImageName());
        return stkFunctions.getAssetVariation(asset, "zoom");
    }

    public List<String> getSizesLady() {
        return sizesLady;
    }

    public void setSizesLady(List<String> sizesLady) {
        this.sizesLady = sizesLady;
    }

    public List<String> getSizesMan() {
        return sizesMan;
    }

    public void setSizesMan(List<String> sizesMan) {
        this.sizesMan = sizesMan;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
