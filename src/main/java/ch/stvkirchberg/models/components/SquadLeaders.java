
package ch.stvkirchberg.models.components;

import info.magnolia.module.templatingkit.functions.STKTemplatingFunctions;
import info.magnolia.rendering.model.RenderingModel;
import info.magnolia.rendering.template.TemplateDefinition;
import info.magnolia.repository.RepositoryConstants;
import info.magnolia.templating.functions.TemplatingFunctions;

import java.util.LinkedList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.stvkirchberg.models.common.Member;
import ch.stvkirchberg.utils.JcrUtil;
import ch.stvkirchberg.utils.NodeUtils;

public class SquadLeaders<RD extends TemplateDefinition> extends Component<TemplateDefinition> {

    private static Logger LOG = LoggerFactory.getLogger(Registration.class);

    private static final String PROP_MAIN_LEADER = "mainLeader";

    private static final String PROP_LEADERS = "leaders";

    private static final String PROP_SQUAD = "squad";

    private String squad;

    private List<Member> mainLeader = new LinkedList<Member>();

    private List<Member> leaders = new LinkedList<Member>();

    public SquadLeaders(Node content, TemplateDefinition definition, RenderingModel<?> parent, STKTemplatingFunctions stkFunctions, TemplatingFunctions templatingFunctions) {
        super(content, definition, parent, stkFunctions, templatingFunctions);
        init();
    }

    private void init() {
        Session adminSession = JcrUtil.getAdminSession(RepositoryConstants.USER_GROUPS);
        String squadUUID = NodeUtils.getStringProperty(content, PROP_SQUAD);
        Node squadNode = NodeUtils.getNodeById(adminSession, RepositoryConstants.USER_GROUPS, squadUUID);
        this.squad = NodeUtils.getStringProperty(squadNode, "title");
        adminSession.logout();

        this.mainLeader = getMembersFromProperty(PROP_MAIN_LEADER);
        this.leaders = getMembersFromProperty(PROP_LEADERS);
    }

    public List<Member> getMembersFromProperty(String property) {
        List<Member> members = new LinkedList<Member>();
        try {
            Node leadersNode = content.getNode(property);
            if (leadersNode != null) {
                PropertyIterator properties = leadersNode.getProperties();
                Session adminSession = JcrUtil.getAdminSession(RepositoryConstants.USERS);
                while (properties.hasNext()) {
                    Property prop = properties.nextProperty();
                    if (PropertyType.STRING == prop.getType()) {
                        String memberPath = prop.getString();
                        Node memberNode = NodeUtils.getNodeByAbsPath(memberPath, adminSession);
                        if (memberNode != null) {
                            members.add(new Member(memberNode));
                        }
                    }
                }
                adminSession.logout();
            }
        } catch (PathNotFoundException e) {
            LOG.warn("No leaders are set");
        } catch (RepositoryException e) {
            LOG.warn("Unable to access leaders node");
        }
        return members;
    }

    public String getSquad() {
        return squad;
    }

    public void setSquad(String squad) {
        this.squad = squad;
    }

    public List<Member> getMainLeader() {
        return mainLeader;
    }

    public void setMainLeader(List<Member> mainLeader) {
        this.mainLeader = mainLeader;
    }

    public List<Member> getLeaders() {
        return leaders;
    }

    public void setLeaders(List<Member> leaders) {
        this.leaders = leaders;
    }

}