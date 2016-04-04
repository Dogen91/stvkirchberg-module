/*
 * (c) 2012 holder contact. All rights reserved. This material is solely and exclusively owned by holder and
 * may not be reproduced elsewhere without prior written approval.
 */

package ch.stvkirchberg.models.common;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

public class MemberSearchFilter {
    private String name;

    private String squad;

    private String role;

    private String stvNumber;

    private boolean searchExecuted = false;

    public MemberSearchFilter() {

    }

    public MemberSearchFilter(HttpServletRequest request) {
        if (request != null) {
            this.name = request.getParameter("name");
            this.squad = request.getParameter("squad");
            this.role = request.getParameter("role");
            this.stvNumber = request.getParameter("stvNumber");
            this.searchExecuted = request.getParameter("name") != null;
        }
    }

    public boolean isEmpty() {
        return StringUtils.isEmpty(name) && StringUtils.isEmpty(squad) && StringUtils.isEmpty(role) && StringUtils.isEmpty(stvNumber);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSquad() {
        return squad;
    }

    public void setSquad(String squad) {
        this.squad = squad;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStvNumber() {
        return stvNumber;
    }

    public void setStvNumber(String stvNumber) {
        this.stvNumber = stvNumber;
    }

    public boolean isSearchExecuted() {
        return searchExecuted;
    }

    public void setSearchExecuted(boolean searchExecuted) {
        this.searchExecuted = searchExecuted;
    }
}
