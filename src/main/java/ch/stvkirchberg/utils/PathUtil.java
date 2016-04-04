/*
 * (c) 2012 holder contact. All rights reserved. This material is solely and exclusively owned by holder and
 * may not be reproduced elsewhere without prior written approval.
 */

package ch.stvkirchberg.utils;

import org.apache.commons.lang.StringUtils;

public class PathUtil {

    private static final String DOT_HTML = ".html";

    public static String smartAddDotHtml(String link) {
        if (StringUtils.isEmpty(link)) {
            return StringUtils.EMPTY;
        }
        if (StringUtils.endsWith(link, DOT_HTML)) {
            return link;
        }
        if (StringUtils.endsWith(link, "/")) {
            link = StringUtils.removeEnd(link, "/");
        }
        return link + DOT_HTML;
    }
}
