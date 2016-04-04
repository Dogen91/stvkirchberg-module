/*
 * (c) 2012 holder contact. All rights reserved. This material is solely and exclusively owned by holder and
 * may not be reproduced elsewhere without prior written approval.
 */

package ch.stvkirchberg.utils;

import info.magnolia.cms.core.search.Query;
import info.magnolia.cms.util.NodeDataUtil;
import info.magnolia.objectfactory.Components;
import info.magnolia.repository.RepositoryManager;

import javax.jcr.NodeIterator;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JcrUtil {

    private static Logger LOG = LoggerFactory.getLogger(MemberSearchUtil.class);

    public static NodeIterator executeQuery(Session adminSession,
                                            String repositoryId,
                                            String queryString) {
        if (adminSession == null) {
            LOG.error("Unable to execute Query. Because there is no admin session.");
            return null;
        }
        NodeIterator resultNodes = null;
        QueryManager queryManager;
        try {
            queryManager = adminSession.getWorkspace().getQueryManager();
            javax.jcr.query.Query query = queryManager.createQuery(queryString, Query.SQL);
            QueryResult result = query.execute();
            resultNodes = result.getNodes();
        } catch (RepositoryException e) {
            LOG.error("Unable to execute Query.", e);
        }

        return resultNodes;
    }

    public static Session getAdminSession(String repositoryId) {
        RepositoryManager component = Components.getComponent(RepositoryManager.class);
        component.init();
        Session adminSession = null;
        try {
            adminSession = component.getSystemSession(repositoryId);
        } catch (RepositoryException e) {
            LOG.error("Unable to get system session. Couldn't search for members.", e);
        }
        return adminSession;
    }

    public static Value createValue(String val) {
        Value value = null;
        try {
            value = NodeDataUtil.createValue(val, PropertyType.STRING);
        } catch (RepositoryException e) {
            LOG.warn("Unable to create value for value " + val, e);
        }
        return value;
    }
}
