/*
 * (c) 2012 holder contact. All rights reserved. This material is solely and exclusively owned by holder and
 * may not be reproduced elsewhere without prior written approval.
 */

package ch.stvkirchberg.utils;

import info.magnolia.cms.security.User;
import info.magnolia.repository.RepositoryConstants;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.stvkirchberg.models.common.Member;
import ch.stvkirchberg.models.common.MemberSearchFilter;

public class MemberSearchUtil {

    private static Logger LOG = LoggerFactory.getLogger(MemberSearchUtil.class);

    private static final String ROLES_NODE_NAME = "roles";

    private static final String GROUPS_NODE_NAME = "groups";

    private static final String QUERY_SELECT_ALL = "select * from mgnl:user where jcr:path like '/admin/%'";

    public static List<Member> searchMembers(MemberSearchFilter searchFilter) {
        List<Member> memberList = new ArrayList<Member>();

        Session adminSession = JcrUtil.getAdminSession(RepositoryConstants.USERS);
        NodeIterator nodes = JcrUtil.executeQuery(adminSession, RepositoryConstants.USERS, getMemberQuery(searchFilter));
        while (nodes.hasNext()) {
            Node userNode = nodes.nextNode();
            userNode = filterUserBySquadAndRole(searchFilter, userNode);
            if (userNode != null) {
                memberList.add(new Member(userNode));
            }
        }
        adminSession.logout();
        return memberList;
    }

    public static Member getMemberByUser(User user,
                                         String mail) {
        MemberSearchFilter searchFilter = new MemberSearchFilter();
        searchFilter.setName(user.getProperty(Member.PROP_PRENAME) + " " + user.getProperty(Member.PROP_LASTNAME));
        List<Member> searchMembers = MemberSearchUtil.searchMembers(searchFilter);
        if (searchMembers == null) {
            return null;
        }
        for (Member member : searchMembers) {
            if (member.getMail().equals(mail)) {
                return member;
            }
        }
        return null;
    }

    private static String getMemberQuery(MemberSearchFilter searchFilter) {
        if (searchFilter.isEmpty()) {
            return QUERY_SELECT_ALL;
        }
        StringBuilder memberQuery = new StringBuilder(QUERY_SELECT_ALL);
        if (StringUtils.isNotEmpty(searchFilter.getName())) {
            memberQuery.append("and UPPER(title) like '%" + searchFilter.getName().toUpperCase() + "%'");
        } else if (StringUtils.isNotEmpty(searchFilter.getStvNumber())) {
            memberQuery.append("and stvNumber like '%" + searchFilter.getStvNumber() + "%'");
        }
        return memberQuery.toString();
    }

    private static Node filterUserBySquadAndRole(MemberSearchFilter searchFilter,
                                                 Node userNode) {
        if (searchFilter.isEmpty() || userNode == null) {
            return userNode;
        }
        Node returnUserNode = null;
        returnUserNode = filterUserNode(GROUPS_NODE_NAME, searchFilter.getSquad(), userNode);
        returnUserNode = filterUserNode(ROLES_NODE_NAME, searchFilter.getRole(), returnUserNode);

        return returnUserNode;
    }

    private static Node filterUserNode(String filterNodeName,
                                       String filterValue,
                                       Node userNode) {
        if (StringUtils.isEmpty(filterValue) || userNode == null) {
            return userNode;
        }

        Node groupsNode = NodeUtils.getNode(userNode, filterNodeName);
        if (groupsNode != null) {
            try {
                PropertyIterator propIter = groupsNode.getProperties();
                while (propIter.hasNext()) {
                    Property prop = (Property) propIter.next();
                    if (!prop.isMultiple()) {
                        String value = prop.getValue().getString();
                        if (filterValue.equalsIgnoreCase(value)) {
                            return userNode;
                        }
                    }
                }
            } catch (RepositoryException e) {
                LOG.error("Unable to get " + filterNodeName + " properties", e);
            }
        }
        return null;
    }

    public static Node searchUserNode(Session adminSession,
                                      String username) {
        NodeIterator nodeIterator = JcrUtil.executeQuery(adminSession, RepositoryConstants.USERS, "select * from mgnl:user where jcr:path like '/admin/" + username + "'");
        if (nodeIterator.hasNext()) {
            return nodeIterator.nextNode();
        }
        return null;
    }
}
