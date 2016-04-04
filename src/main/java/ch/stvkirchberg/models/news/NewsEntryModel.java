/*
 * (c) 2012 holder contact. All rights reserved. This material is solely and exclusively owned by holder and
 * may not be reproduced elsewhere without prior written approval.
 */

package ch.stvkirchberg.models.news;

import info.magnolia.cms.core.MgnlNodeType;
import info.magnolia.module.templatingkit.dam.Asset;
import info.magnolia.module.templatingkit.dam.DAMException;
import info.magnolia.module.templatingkit.dam.NoSuchVariationException;
import info.magnolia.module.templatingkit.functions.STKTemplatingFunctions;

import java.util.List;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.stvkirchberg.utils.NodeUtils;
import ch.stvkirchberg.utils.PathUtil;
import ch.stvkirchberg.utils.STVKConstants;
import ch.stvkirchberg.utils.TmplSearch;

public class NewsEntryModel {
    private static Logger LOG = LoggerFactory.getLogger(NewsEntryModel.class);

    private static final String PROP_LEAD_TEXT = "leadText";

    private static final String PROP_TEXT = "text";

    private static final String PROP_NAME = "name";

    private static final String PROP_TITLE = "title";

    private STKTemplatingFunctions stkFunctions;

    private String title;

    private String leadText;

    private Asset image;

    private String link;

    public NewsEntryModel(Node newsNode, STKTemplatingFunctions stkFunctions) {
        this.stkFunctions = stkFunctions;
        String title = NodeUtils.getStringProperty(newsNode, PROP_TITLE);
        if (StringUtils.isEmpty(title)) {
            title = NodeUtils.getStringProperty(newsNode, PROP_NAME);
        }
        this.title = title;
        this.leadText = getLeadText(newsNode);
        this.image = getLeadImage(newsNode);
        try {
            this.link = PathUtil.smartAddDotHtml(newsNode.getPath());
        } catch (RepositoryException e) {
            LOG.warn("Unable to get path of news node", e);
        }
    }

    private Asset getLeadImage(Node node) {
        Asset image = stkFunctions.getAsset(node, "leadImage");
        if (image == null) {
            Node textImageNode = getTextImageNode(node);
            if (textImageNode == null) {
                return null;
            }
            image = stkFunctions.getAsset(textImageNode, "image");
        }

        try {
            if (image != null) {
                image = image.getVariation("content-small");
            }
        } catch (NoSuchVariationException e) {
            LOG.error("Content-small is not a variation of the image.", e);
        } catch (DAMException e) {
            LOG.error("Unable to the the variation of the image", e);
        }

        return image;
    }

    private String getLeadText(Node node) {
        String leadText = NodeUtils.getStringProperty(node, PROP_LEAD_TEXT);

        if (StringUtils.isEmpty(leadText)) {
            Node textImageNode = getTextImageNode(node);
            if (textImageNode == null) {
                return StringUtils.EMPTY;
            }
            String text = NodeUtils.getStringProperty(textImageNode, PROP_TEXT);
            StringBuilder leadTextBuilder = new StringBuilder();
            if (text.length() > 850) {
                leadTextBuilder.append(StringUtils.substring(text, 0, 850));
                leadTextBuilder.append("...</p>");
            } else {
                leadTextBuilder.append(text);
            }
            return leadTextBuilder.toString();
        } else {
            return "<p>" + leadText + "</p>";
        }
    }

    private Node getTextImageNode(Node node) {
        List<Node> textImageComps = TmplSearch.execute(node, STVKConstants.TMPL_NAME_TEXTIMAGE, "", MgnlNodeType.NT_CONTENTNODE, 1);
        if (textImageComps == null || textImageComps.size() <= 0) {
            return null;
        }
        Node textImageNode = textImageComps.get(0);
        return textImageNode;
    }

    public STKTemplatingFunctions getStkFunctions() {
        return stkFunctions;
    }

    public void setStkFunctions(STKTemplatingFunctions stkFunctions) {
        this.stkFunctions = stkFunctions;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLeadText() {
        return leadText;
    }

    public void setLeadText(String leadText) {
        this.leadText = leadText;
    }

    public Asset getImage() {
        return image;
    }

    public void setImage(Asset image) {
        this.image = image;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
