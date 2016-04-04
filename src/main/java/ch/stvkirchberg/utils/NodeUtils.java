/*
 * (c) 2012 holder contact. All rights reserved. This material is solely and exclusively owned by holder and
 * may not be reproduced elsewhere without prior written approval.
 */

package ch.stvkirchberg.utils;

import info.magnolia.cms.util.NodeDataUtil;

import java.util.Calendar;

import javax.jcr.AccessDeniedException;
import javax.jcr.InvalidItemStateException;
import javax.jcr.ItemExistsException;
import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.PropertyType;
import javax.jcr.ReferentialIntegrityException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.jcr.ValueFormatException;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.nodetype.NoSuchNodeTypeException;
import javax.jcr.version.VersionException;

import org.apache.commons.lang.StringUtils;
import org.apache.jackrabbit.commons.JcrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NodeUtils {
    private static Logger LOG = LoggerFactory.getLogger(NodeUtils.class);

    public static Property getProperty(Node node,
                                       String propName) {
        Property prop = null;
        try {
            if (node.hasProperty(propName)) {
                prop = node.getProperty(propName);
            }
        } catch (PathNotFoundException e) {
            LOG.warn("Unable to find property with name [" + propName + "]");
        } catch (RepositoryException e) {
            LOG.warn("Unable to get property with name [" + propName + "]");
        }
        return prop;
    }

    public static String getStringProperty(Node node,
                                           String propName) {
        Property prop = getProperty(node, propName);
        if (prop == null) {
            return StringUtils.EMPTY;
        }
        try {
            return prop.getString();
        } catch (ValueFormatException e) {
            LOG.warn("Unable to Format property [" + propName + "]", e);
        } catch (RepositoryException e) {
            LOG.warn("Unable to get property [" + propName + "]", e);
        }
        return StringUtils.EMPTY;
    }

    public static Calendar getDateProperty(Node node,
                                           String propName) {
        Property prop = getProperty(node, propName);
        if (prop == null) {
            return null;
        }
        try {
            return prop.getDate();
        } catch (ValueFormatException e) {
            LOG.warn("Unable to Format property [" + propName + "]", e);
        } catch (RepositoryException e) {
            LOG.warn("Unable to get property [" + propName + "]", e);
        }
        return null;
    }

    public static long getLongProperty(Node node,
                                       String propName) {
        Property prop = getProperty(node, propName);
        if (prop == null) {
            return 0;
        }
        try {
            return prop.getLong();
        } catch (ValueFormatException e) {
            LOG.warn("Unable to Format property [" + propName + "]", e);
        } catch (RepositoryException e) {
            LOG.warn("Unable to get property [" + propName + "]", e);
        }
        return 0;
    }

    public static void setStringProperty(Node node,
                                         String propertyName,
                                         String property) {
        if (node == null || StringUtils.isEmpty(propertyName) || StringUtils.isEmpty(property)) {
            LOG.error("Invalid parameters. Unable to set property to node");
        } else {
            try {
                node.setProperty(propertyName, createValue(property));
            } catch (ValueFormatException e) {
                LOG.error("Unable to set property [" + propertyName + "]", e);
            } catch (VersionException e) {
                LOG.error("Unable to set property [" + propertyName + "]", e);
            } catch (LockException e) {
                LOG.error("Unable to set property [" + propertyName + "]", e);
            } catch (ConstraintViolationException e) {
                LOG.error("Unable to set property [" + propertyName + "]", e);
            } catch (RepositoryException e) {
                LOG.error("Unable to set property [" + propertyName + "]", e);
            }
        }
    }

    public static Node getNode(Node parentNode,
                               String relPath) {
        Node childNode = null;
        if (parentNode == null) {
            return childNode;
        }
        try {
            if (parentNode.hasNode(relPath)) {
                childNode = parentNode.getNode(relPath);
            }
        } catch (PathNotFoundException e) {
            LOG.error("Unable to get childNode with path " + relPath, e);
        } catch (RepositoryException e) {
            LOG.error("Unable to get childNode with path " + relPath, e);
        }
        return childNode;
    }

    public static Node getNodeById(Session adminSession,
                                   String workspace,
                                   String id) {
        Node node = null;
        if (adminSession == null || StringUtils.isEmpty(workspace) || StringUtils.isEmpty(id)) {
            return null;
        }

        if (adminSession != null) {
            try {
                node = adminSession.getNodeByIdentifier(id);
            } catch (ItemNotFoundException e) {
                LOG.error("No node found with id [" + id + "]", e);
            } catch (RepositoryException e) {
                LOG.error("Unable to get node with id [" + id + "]", e);
            }
        }

        return node;
    }

    public static boolean removeNode(Node node) {
        if (node == null) {
            LOG.error("Unable to remove node because the node is null");
            return false;
        }
        try {
            node.remove();
            return true;
        } catch (AccessDeniedException e) {
            LOG.error("Unable to remove node.", e);
        } catch (VersionException e) {
            LOG.error("Unable to remove node.", e);
        } catch (LockException e) {
            LOG.error("Unable to remove node.", e);
        } catch (ConstraintViolationException e) {
            LOG.error("Unable to remove node.", e);
        } catch (RepositoryException e) {
            LOG.error("Unable to remove node.", e);
        }
        return false;
    }

    public static String getNodePath(Node node) {
        try {
            return node.getPath();
        } catch (RepositoryException e) {
            LOG.error("Unable to get path of the given Node", e);
        }
        return StringUtils.EMPTY;
    }

    public static Node getOrCreateByPath(String absPath,
                                         String nodeType,
                                         Session adminSession) {
        if (StringUtils.isEmpty(absPath) || StringUtils.isEmpty(nodeType) || adminSession == null) {
            LOG.error("Invalid parameters. One or more parameter is empty. Unable to get or create a node with path [" + absPath + "]");
            return null;
        }
        try {
            return JcrUtils.getOrCreateByPath(absPath, nodeType, nodeType, adminSession, true);
        } catch (RepositoryException e) {
            LOG.error("Unable to get or create a node with path [" + absPath + "]", e);
            return null;
        }
    }

    public static Node createChildNode(Node parentNode,
                                       String relPath) {
        Node childNode = null;
        try {
            childNode = parentNode.addNode(relPath);
        } catch (ItemExistsException e) {
            LOG.error("A node with this nodename [" + relPath + "] already exists", e);
        } catch (PathNotFoundException e) {
            LOG.error("Unable to create a child node with this path [" + relPath + "]", e);
        } catch (VersionException e) {
            LOG.error("Version exception while creating child node with path [" + relPath + "]");
        } catch (ConstraintViolationException e) {
            LOG.error("Unable to create child node with path [" + relPath + "]", e);
        } catch (LockException e) {
            LOG.error("Node with path [" + relPath + "] is locked.", e);
        } catch (RepositoryException e) {
            LOG.error("Unable to create child node with path [" + relPath + "]", e);
        }
        return childNode;
    }

    public static boolean saveChangesInSession(Session session) {
        try {
            session.save();
            return true;
        } catch (AccessDeniedException e) {
            LOG.error("Unable to save changes in session.", e);
        } catch (ItemExistsException e) {
            LOG.error("Unable to save changes in session.", e);
        } catch (ReferentialIntegrityException e) {
            LOG.error("Unable to save changes in session.", e);
        } catch (ConstraintViolationException e) {
            LOG.error("Unable to save changes in session.", e);
        } catch (InvalidItemStateException e) {
            LOG.error("Unable to save changes in session.", e);
        } catch (VersionException e) {
            LOG.error("Unable to save changes in session.", e);
        } catch (LockException e) {
            LOG.error("Unable to save changes in session.", e);
        } catch (NoSuchNodeTypeException e) {
            LOG.error("Unable to save changes in session.", e);
        } catch (RepositoryException e) {
            LOG.error("Unable to save changes in session.", e);
        }
        return false;
    }

    private static Value createValue(String val) {
        Value value = null;
        try {
            value = NodeDataUtil.createValue(val, PropertyType.STRING);
        } catch (RepositoryException e) {
            LOG.warn("Unable to create value for value " + val, e);
        }
        return value;
    }

    public static String getNodeIdentifier(Node node) {
        if (node == null) {
            LOG.error("Can't get identifier because node is null");
            return StringUtils.EMPTY;
        }
        try {
            return node.getIdentifier();
        } catch (RepositoryException e) {
            LOG.error("Can't get identifier of node", e);
        }
        return StringUtils.EMPTY;
    }

    public static String getNodeName(Node node) {
        if (node == null) {
            LOG.error("Can't read node name because node is null");
            return StringUtils.EMPTY;
        }
        try {
            return node.getName();
        } catch (RepositoryException e) {
            LOG.error("Can't get node name.", e);
        }
        return StringUtils.EMPTY;
    }

    public static Node getNodeByAbsPath(String absPath,
                                        Session adminSession) {
        try {
            return adminSession.getNode(absPath);
        } catch (PathNotFoundException e) {
            LOG.error("Path was not found [" + absPath + "]", e);
        } catch (RepositoryException e) {
            LOG.error("Unable to get node with path [" + absPath + "]", e);
        }
        return null;
    }

    public static boolean childNodeExists(Node rootNode,
                                          String childNodeName) {
        Node node = null;
        try {
            node = rootNode.getNode(childNodeName);
        } catch (PathNotFoundException e) {
            // User isn't in registration process
            return false;
        } catch (RepositoryException e) {
            LOG.error("Unable to check if child node [" + childNodeName + "] exists", e);
        }
        return node != null;
    }
}
