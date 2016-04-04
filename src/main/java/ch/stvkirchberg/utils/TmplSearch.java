/*
 * (c) 2012 holder contact. All rights reserved. This material is solely and exclusively owned by holder and
 * may not be reproduced elsewhere without prior written approval.
 */

package ch.stvkirchberg.utils;

import info.magnolia.cms.core.Content;
import info.magnolia.cms.core.search.Query;
import info.magnolia.cms.util.QueryUtil;
import info.magnolia.jcr.wrapper.I18nNodeWrapper;
import info.magnolia.repository.RepositoryConstants;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TmplSearch {

    private static Logger LOG = LoggerFactory.getLogger(TmplSearch.class);

    private static String TMPL_QUERY = "select * from nt:base where jcr:path like '%s' AND (mgnl:template = '%s') %s";

    public static List<Node> execute(Node searchRoot,
                                     String templateName,
                                     String orderBy,
                                     String nodeType,
                                     long numOfNews) {
        String path = "";
        try {
            path = searchRoot.getPath();
        } catch (RepositoryException e) {
            LOG.error("Unable to get path of search root node.", e);
        }
        return execute(path, templateName, orderBy, nodeType, numOfNews);
    }

    public static List<Node> execute(String searchPath,
                                     String templateName,
                                     String nodeType,
                                     long numOfNews) {
        return execute(searchPath, templateName, "", nodeType, numOfNews);
    }

    @SuppressWarnings("deprecation")
    public static List<Node> execute(String searchPath,
                                     String templateName,
                                     String orderBy,
                                     String nodeType,
                                     long numOfNews) {
        if (StringUtils.endsWith(searchPath, "/")) {
            searchPath = searchPath + "%";
        } else {
            searchPath = searchPath + "/%";
        }
        String query = String.format(TMPL_QUERY, searchPath, templateName, orderBy);
        List<Content> itemsListFromQuery = (List<Content>) QueryUtil.query(RepositoryConstants.WEBSITE, query, Query.SQL, nodeType, numOfNews);
        List<Node> itemsList = new ArrayList<Node>(itemsListFromQuery.size());

        // wrap all
        for (int i = 0; i < itemsListFromQuery.size(); i++) {
            itemsList.add(i, new I18nNodeWrapper(itemsListFromQuery.get(i).getJCRNode()));
        }
        return itemsList;
    }
}
