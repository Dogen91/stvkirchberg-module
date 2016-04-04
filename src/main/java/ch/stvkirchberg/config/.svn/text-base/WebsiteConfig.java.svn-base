/*
 * (c) 2013 holder contact. All rights reserved. This material is solely and exclusively owned by holder and
 * may not be reproduced elsewhere without prior written approval.
 */

package ch.stvkirchberg.config;

import info.magnolia.cms.core.MgnlNodeType;

import java.util.List;

import javax.jcr.Node;

import ch.stvkirchberg.utils.NodeUtils;
import ch.stvkirchberg.utils.STVKConstants;
import ch.stvkirchberg.utils.TmplSearch;

public class WebsiteConfig {

    private static WebsiteConfig instance;

    private String mailOfAddressMaintainer;

    private String changePwdPage;

    public static WebsiteConfig getInstance() {
        List<Node> homePages = TmplSearch.execute("/", STVKConstants.TMPL_NAME_HOME, MgnlNodeType.NT_CONTENT, 1L);
        if (homePages == null || homePages.size() == 0) {
            return null;
        }
        Node configPageNode = homePages.get(0);
        instance = new WebsiteConfig(configPageNode);
        return instance;
    }

    private WebsiteConfig(Node configNode) {
        this.mailOfAddressMaintainer = NodeUtils.getStringProperty(configNode, "mailOfAddressMaintainer");
        this.changePwdPage = NodeUtils.getStringProperty(configNode, "changePwdPage");
    }

    public String getMailOfAddressMaintainer() {
        return mailOfAddressMaintainer;
    }

    public void setMailOfAddressMaintainer(String mailOfAddressMaintainer) {
        this.mailOfAddressMaintainer = mailOfAddressMaintainer;
    }

    public String getChangePwdPage() {
        return changePwdPage;
    }

    public void setChangePwdPage(String changePwdPage) {
        this.changePwdPage = changePwdPage;
    }
}
