
package ch.stvkirchberg.models.components;

import info.magnolia.cms.security.Group;
import info.magnolia.cms.security.SecuritySupport;
import info.magnolia.module.templatingkit.functions.STKTemplatingFunctions;
import info.magnolia.rendering.model.RenderingModel;
import info.magnolia.rendering.template.TemplateDefinition;
import info.magnolia.repository.RepositoryConstants;
import info.magnolia.templating.functions.TemplatingFunctions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Session;

import org.apache.commons.lang.StringUtils;
import org.apache.jackrabbit.JcrConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.stvkirchberg.models.common.Member;
import ch.stvkirchberg.models.common.MemberSearchFilter;
import ch.stvkirchberg.utils.JcrUtil;
import ch.stvkirchberg.utils.MemberSearchUtil;
import ch.stvkirchberg.utils.NodeUtils;

public class MemberSearch<RD extends TemplateDefinition> extends Component<TemplateDefinition> {

    @SuppressWarnings("unused")
    private static Logger LOG = LoggerFactory.getLogger(MemberSearch.class);

    private static final String PROP_TITLE = "title";

    private String title;

    private Map<String, String> squadMap = new HashMap<String, String>();

    private Map<String, String> roleMap = new HashMap<String, String>();

    private List<Member> members = new ArrayList<Member>();

    private MemberSearchFilter memberSearchFilter;

    public MemberSearch(Node content, TemplateDefinition definition, RenderingModel<?> parent, STKTemplatingFunctions stkFunctions, TemplatingFunctions templatingFunctions) {
        super(content, definition, parent, stkFunctions, templatingFunctions);
        init();
    }

    private void init() {
        this.setTitle(NodeUtils.getStringProperty(content, PROP_TITLE));
        this.squadMap = fillSquadMap();
        this.roleMap = fillRoleMap();
        this.memberSearchFilter = new MemberSearchFilter(getRequest());
        if (memberSearchFilter.isSearchExecuted()) {
            this.members = MemberSearchUtil.searchMembers(memberSearchFilter);
        }
    }

    private Map<String, String> fillSquadMap() {
        Map<String, String> squadMap = new HashMap<String, String>();
        List<Group> allGroups = (List<Group>) SecuritySupport.Factory.getInstance().getGroupManager().getAllGroups();
        Session adminSession = JcrUtil.getAdminSession(RepositoryConstants.USER_GROUPS);
        for (Group group : allGroups) {
            Node groupNode = NodeUtils.getNodeById(adminSession, RepositoryConstants.USER_GROUPS, group.getId());
            if (groupNode != null) {
                squadMap.put(NodeUtils.getStringProperty(groupNode, PROP_TITLE), group.getId());
            }
        }
        adminSession.logout();
        return sortMapByKey(squadMap);
    }

    private Map<String, String> fillRoleMap() {
        Map<String, String> roleMap = new HashMap<String, String>();

        Session adminSession = JcrUtil.getAdminSession(RepositoryConstants.USER_ROLES);
        NodeIterator result = JcrUtil.executeQuery(adminSession, RepositoryConstants.USER_ROLES, "select * from mgnl:role");
        if (result != null) {
            while (result.hasNext()) {
                Node roleNode = result.nextNode();
                String id = NodeUtils.getStringProperty(roleNode, JcrConstants.JCR_UUID);
                String name = NodeUtils.getStringProperty(roleNode, PROP_TITLE);
                if (StringUtils.isNotEmpty(name) && !this.squadMap.containsKey(name)) {
                    roleMap.put(name, id);
                }
            }
        }
        adminSession.logout();
        return sortMapByKey(roleMap);
    }

    private Map<String, String> sortMapByKey(Map<String, String> map) {
        Map<String, String> sortedMap = new LinkedHashMap<String, String>();
        List<String> sortedKeys = new LinkedList<String>();
        for (String key : map.keySet()) {
            sortedKeys.add(key);
        }
        Collections.sort(sortedKeys);
        for (String key : sortedKeys) {
            sortedMap.put(key, map.get(key));
        }
        return sortedMap;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Map<String, String> getSquadMap() {
        return squadMap;
    }

    public void setSquadMap(Map<String, String> squadMap) {
        this.squadMap = squadMap;
    }

    public Map<String, String> getRoleMap() {
        return roleMap;
    }

    public void setRoleMap(Map<String, String> roleMap) {
        this.roleMap = roleMap;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }

    public MemberSearchFilter getMemberSearchFilter() {
        return memberSearchFilter;
    }

    public void setMemberSearchFilter(MemberSearchFilter memberSearchFilter) {
        this.memberSearchFilter = memberSearchFilter;
    }

}
