/*
 * (c) 2012 holder contact. All rights reserved. This material is solely and exclusively owned by holder and
 * may not be reproduced elsewhere without prior written approval.
 */

package ch.stvkirchberg.servlets;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Session;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.jackrabbit.JcrConstants;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import ch.stvkirchberg.models.common.Member;
import ch.stvkirchberg.models.common.MemberSearchFilter;
import ch.stvkirchberg.utils.JcrUtil;
import ch.stvkirchberg.utils.MemberSearchUtil;
import ch.stvkirchberg.utils.NodeUtils;
import info.magnolia.cms.security.Group;
import info.magnolia.cms.security.SecuritySupport;
import info.magnolia.repository.RepositoryConstants;

public class MemberListToXslServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void service(HttpServletRequest req,
                           HttpServletResponse resp)
        throws ServletException, IOException {
        MemberSearchFilter memberSearchFilter = new MemberSearchFilter(req);
        List<Member> members = MemberSearchUtil.searchMembers(memberSearchFilter);
        Collections.sort(members);

        Map<String, String> roleMap = getRoleMap();
        Map<String, String> squadMap = getSquadMap();

        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("Mitgliederliste");

        if (members != null) {
            createDataRows(members, sheet, roleMap, squadMap);
        }
        createHeaderRow(wb, sheet, roleMap, squadMap);

        resp.setContentType("application/vnd.ms-excel");
        resp.setHeader("Content-Disposition", "attachment; filename=Mitgliederliste.xls");
        wb.write(resp.getOutputStream());

    }

    private void createDataRows(List<Member> members,
                                Sheet sheet,
                                Map<String, String> roleMap,
                                Map<String, String> squadMap) {
        for (int i = 0; i < members.size(); i++) {
            Row row = sheet.createRow(i + 1);
            Member member = members.get(i);

            int col = 0;
            createDataCell(member.getSalutation(), col++, row);
            createDataCell(member.getLastname(), col++, row);
            createDataCell(member.getPrename(), col++, row);
            createDataCell(member.getAddress(), col++, row);
            createDataCell(member.getPostalCode(), col++, row);
            createDataCell(member.getCity(), col++, row);
            createDataCell(member.getMail(), col++, row);
            createDataCell(member.getPhoneNumber(), col++, row);
            createDataCell(member.getMobilePhoneNumber(), col++, row);
            createDataCell(member.getBirthdate(), col++, row);
            createDataCell(member.getStvNumber(), col++, row);
            createDataCell(member.getEntryDate(), col++, row);
            createDataCell(getStringOfBoolean(member.isActive()), col++, row);
            createDataCell(member.getHonoraryMemberSince(), col++, row);
            createDataCell(getStringOfBoolean(member.isHonoraryMember()), col++, row);
            createDataCell(getStringOfBoolean(member.isBenefactor()), col++, row);
            createDataCell(getStringOfBoolean(member.isPassivMember()), col++, row);
            createDataCell(getStringOfBoolean(member.isClubMember()), col++, row);
            createDataCell(getStringOfBoolean(member.isImgForbidden()), col++, row);
            createDataCell(member.getRemark(), col++, row);

            for (String roleId : roleMap.values()) {
                if (member.getRoles().contains(roleId)) {
                    createDataCell("X", col++, row);
                } else {
                    createDataCell("", col++, row);
                }
            }

            for (String squadId : squadMap.values()) {
                if (member.getSquads().contains(squadId)) {
                    createDataCell("X", col++, row);
                } else {
                    createDataCell("", col++, row);
                }
            }

        }
    }

    private void createDataCell(String cellValue,
                                int i,
                                Row row) {
        Cell cell = row.createCell(i);
        cell.setCellValue(cellValue);
    }

    private void createHeaderRow(Workbook wb,
                                 Sheet sheet,
                                 Map<String, String> roleMap,
                                 Map<String, String> squadMap) {
        Row headerRow = sheet.createRow(0);

        CellStyle headerCellStyle = wb.createCellStyle();
        Font headerFont = wb.createFont();
        headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        headerCellStyle.setFont(headerFont);

        int col = 0;
        createHeaderCell("Anrede", col++, headerRow, headerCellStyle);
        createHeaderCell("Nachname", col++, headerRow, headerCellStyle);
        createHeaderCell("Vorname", col++, headerRow, headerCellStyle);
        createHeaderCell("Adresse", col++, headerRow, headerCellStyle);
        createHeaderCell("PLZ", col++, headerRow, headerCellStyle);
        createHeaderCell("Ort", col++, headerRow, headerCellStyle);
        createHeaderCell("E-Mail", col++, headerRow, headerCellStyle);
        createHeaderCell("Telefon", col++, headerRow, headerCellStyle);
        createHeaderCell("Mobile", col++, headerRow, headerCellStyle);
        createHeaderCell("Geburtsdatum", col++, headerRow, headerCellStyle);
        createHeaderCell("STV Nummer", col++, headerRow, headerCellStyle);
        createHeaderCell("Eintrittsdatum", col++, headerRow, headerCellStyle);
        createHeaderCell("Aktiv", col++, headerRow, headerCellStyle);
        createHeaderCell("Ehrenmitglied Seit", col++, headerRow, headerCellStyle);
        createHeaderCell("Ehrenmitglied", col++, headerRow, headerCellStyle);
        createHeaderCell("G�nner", col++, headerRow, headerCellStyle);
        createHeaderCell("Passivmitglied", col++, headerRow, headerCellStyle);
        createHeaderCell("Vereinsmitglied", col++, headerRow, headerCellStyle);
        createHeaderCell("Bilderverbot", col++, headerRow, headerCellStyle);
        createHeaderCell("Bemerkung", col++, headerRow, headerCellStyle);

        for (String role : roleMap.keySet()) {
            createHeaderCell(role, col++, headerRow, headerCellStyle);
        }

        for (String squad : squadMap.keySet()) {
            createHeaderCell(squad, col++, headerRow, headerCellStyle);
        }

        for (int i = 0; i < col; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private void createHeaderCell(String cellName,
                                  int cellIndex,
                                  Row headerRow,
                                  CellStyle headerCellStyle) {
        Cell headerCell = headerRow.createCell(cellIndex);
        headerCell.setCellStyle(headerCellStyle);
        headerCell.setCellValue(cellName);
    }

    private String getStringOfBoolean(boolean bool) {
        if (bool) {
            return "Ja";
        }
        return "Nein";
    }

    private Map<String, String> getSquadMap() {
        Map<String, String> squadMap = new HashMap<String, String>();
        List<Group> allGroups = (List<Group>) SecuritySupport.Factory.getInstance().getGroupManager().getAllGroups();
        Session adminSession = JcrUtil.getAdminSession(RepositoryConstants.USER_GROUPS);
        for (Group group : allGroups) {
            Node groupNode = NodeUtils.getNodeById(adminSession, RepositoryConstants.USER_GROUPS, group.getId());
            if (groupNode != null) {
                squadMap.put(NodeUtils.getStringProperty(groupNode, "title"), group.getId());
            }
        }
        adminSession.logout();
        return sortMapByKey(squadMap);
    }

    private Map<String, String> getRoleMap() {
        Map<String, String> roleMap = new HashMap<String, String>();

        Map<String, String> squadMap = getSquadMap();

        Session adminSession = JcrUtil.getAdminSession(RepositoryConstants.USER_ROLES);
        NodeIterator result = JcrUtil.executeQuery(adminSession, RepositoryConstants.USER_ROLES, "select * from mgnl:role");
        if (result != null) {
            while (result.hasNext()) {
                Node roleNode = result.nextNode();
                String id = NodeUtils.getStringProperty(roleNode, JcrConstants.JCR_UUID);
                String name = NodeUtils.getStringProperty(roleNode, "title");
                if (StringUtils.isNotEmpty(name) && !squadMap.containsKey(name)) {
                    roleMap.put(name, id);
                }
            }
        }
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
}
