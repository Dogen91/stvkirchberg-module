
package ch.stvkirchberg.models.components;

import info.magnolia.cms.core.MgnlNodeType;
import info.magnolia.link.LinkException;
import info.magnolia.link.LinkUtil;
import info.magnolia.module.templatingkit.functions.STKTemplatingFunctions;
import info.magnolia.rendering.model.RenderingModel;
import info.magnolia.rendering.template.TemplateDefinition;
import info.magnolia.templating.functions.TemplatingFunctions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.stvkirchberg.models.news.NewsEntryModel;
import ch.stvkirchberg.utils.NodeUtils;
import ch.stvkirchberg.utils.STVKConstants;
import ch.stvkirchberg.utils.TmplSearch;

public class NewsTeaser<RD extends TemplateDefinition> extends Component<TemplateDefinition> {

    private static Logger LOG = LoggerFactory.getLogger(NewsTeaser.class);

    private static final String ORDER_BY_MGNL_CREATIONDATE_DESC = "ORDER BY mgnl:creationdate DESC";

    private static final String PROP_NUM_OF_NEWS = "numOfNews";

    private static final String PROP_SEARCH_ROOT_PAGE = "rootPage";

    private static final String PROP_NEWS_SINCE = "newsSince";

    private static final String PROP_NEWS_TILL = "newsTill";

    private List<NewsEntryModel> newsEntries;

    private long numOfNews;

    private Calendar newsSince;

    private Calendar newsTill;

    public NewsTeaser(Node content, TemplateDefinition definition, RenderingModel<?> parent, STKTemplatingFunctions stkFunctions, TemplatingFunctions templatingFunctions) {
        super(content, definition, parent, stkFunctions, templatingFunctions);
        init();
    }

    private void init() {
        this.numOfNews = NodeUtils.getLongProperty(content, PROP_NUM_OF_NEWS);

        String searchRootUUID = NodeUtils.getStringProperty(content, PROP_SEARCH_ROOT_PAGE);
        String searchRootPath = "";
        if (StringUtils.isNotEmpty(searchRootUUID)) {
            try {
                searchRootPath = LinkUtil.convertUUIDtoHandle(searchRootUUID, "website");
            } catch (LinkException e) {
                LOG.warn("Error while retrieving information about news root page", e);
            }
        }
        this.setNewsSince(NodeUtils.getDateProperty(content, PROP_NEWS_SINCE));
        this.setNewsTill(NodeUtils.getDateProperty(content, PROP_NEWS_TILL));
        this.newsEntries = searchNewsPages(searchRootPath);
    }

    private List<NewsEntryModel> searchNewsPages(String searchRootPath) {
        List<Node> newsPageNodes = new ArrayList<Node>();
        Node searchRoot = null;
        if (StringUtils.isEmpty(searchRootPath)) {
            searchRoot = getSiteRoot();
        } else {
            try {
                searchRoot = getSiteRoot().getNode(StringUtils.replaceOnce(searchRootPath, "/", ""));
            } catch (RepositoryException e) {
                LOG.warn("Unable to get root node for news teaser search", e);
            }
        }

        // ORDER BY mgnl:creationdate DESC
        newsPageNodes = TmplSearch.execute(searchRoot, STVKConstants.TMPL_NAME_NEWS, ORDER_BY_MGNL_CREATIONDATE_DESC, MgnlNodeType.NT_CONTENT, this.numOfNews);
        if (newsPageNodes == null || newsPageNodes.size() == 0) {
            return new ArrayList<NewsEntryModel>();
        }

        List<NewsEntryModel> newsEntries = new ArrayList<NewsEntryModel>();
        for (Node newsNode : getFilteredNodes(newsPageNodes)) {
            newsEntries.add(new NewsEntryModel(newsNode, stkFunctions));
        }

        return newsEntries;
    }

    private List<Node> getFilteredNodes(List<Node> newsPageNodes) {
        List<Node> filteredNodes = new LinkedList<Node>();
        if (newsSince == null) {
            newsSince = Calendar.getInstance();
            newsSince.set(1990, 1, 1);
        }
        if (newsTill == null) {
            newsTill = Calendar.getInstance();
        }

        for (Node node : newsPageNodes) {
            Calendar newsDate = NodeUtils.getDateProperty(node, "MetaData/mgnl:creationdate");
            if (newsSince.compareTo(newsDate) <= 0 && newsTill.compareTo(newsDate) >= 0) {
                filteredNodes.add(node);
            }
        }
        return filteredNodes;
    }

    public List<NewsEntryModel> getNewsEntries() {
        return newsEntries;
    }

    public void setNewsEntries(List<NewsEntryModel> newsEntries) {
        this.newsEntries = newsEntries;
    }

    public long getNumOfNews() {
        return numOfNews;
    }

    public void setNumOfNews(long numOfNews) {
        this.numOfNews = numOfNews;
    }

    public Calendar getNewsSince() {
        return newsSince;
    }

    public void setNewsSince(Calendar newsSince) {
        this.newsSince = newsSince;
    }

    public Calendar getNewsTill() {
        return newsTill;
    }

    public void setNewsTill(Calendar newsTill) {
        this.newsTill = newsTill;
    }

}
