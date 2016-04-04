/*
 * (c) 2012 holder contact. All rights reserved. This material is solely and exclusively owned by holder and
 * may not be reproduced elsewhere without prior written approval.
 */

package ch.stvkirchberg.models.components;

import info.magnolia.module.templatingkit.functions.STKTemplatingFunctions;
import info.magnolia.rendering.model.RenderingModel;
import info.magnolia.rendering.template.TemplateDefinition;
import info.magnolia.templating.functions.TemplatingFunctions;

import javax.jcr.Node;

import ch.stvkirchberg.utils.NodeUtils;

public class GoogleCalendar<RD extends TemplateDefinition> extends Component<TemplateDefinition> {
    private static final String PROP_CALENDAR_FEED = "calendarFeed";

    private String calendarFeed;
    
    public GoogleCalendar(Node content, TemplateDefinition definition, RenderingModel<?> parent, STKTemplatingFunctions stkFunctions, TemplatingFunctions templatingFunctions) {
        super(content, definition, parent, stkFunctions, templatingFunctions);
        init();
    }

    private void init() {
        this.setCalendarFeed(NodeUtils.getStringProperty(content, PROP_CALENDAR_FEED));
    }

    public String getCalendarFeed() {
        return calendarFeed;
    }

    public void setCalendarFeed(String calendarFeed) {
        this.calendarFeed = calendarFeed;
    }
}
