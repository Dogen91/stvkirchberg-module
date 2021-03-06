/*
 * (c) 2012 holder contact. All rights reserved. This material is solely and exclusively owned by holder and
 * may not be reproduced elsewhere without prior written approval.
 */

package ch.stvkirchberg.servlets;

import info.magnolia.cms.security.Realm;
import info.magnolia.cms.security.SecuritySupport;
import info.magnolia.cms.security.User;
import info.magnolia.cms.security.UserManager;
import info.magnolia.cms.util.NodeDataUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import javax.jcr.Value;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.stvkirchberg.models.common.Member;

public class MigrateMembersFromXML extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static Logger LOG = LoggerFactory.getLogger(MigrateMembersFromXML.class);

    private Map<String, String> fieldMapping = new HashMap<String, String>();

    private Map<String, String> groupdMapping = new HashMap<String, String>();

    private Map<String, Set<String>> groupUserMapping = new HashMap<String, Set<String>>();

    private Map<String, String> stvNumberMapping = new HashMap<String, String>();

    private Map<String, String> jsNumberMapping = new HashMap<String, String>();

    private List<String> errors = new ArrayList<String>();

    private int undefinedCounter = 0;

    @SuppressWarnings("unchecked")
    @Override
    protected void service(HttpServletRequest req,
                           HttpServletResponse resp) throws ServletException, IOException {

        UserManager userManager = SecuritySupport.Factory.getInstance().getUserManager(Realm.REALM_ADMIN.getName());
        initFieldMapping();
        initGroupMapping();
        initStvNumberMapping();
        initJSNumberMapping();

        try {
            SAXBuilder builder = new SAXBuilder();
            Document doc = builder.build(getInputXml());
            Element rootElement = doc.getRootElement();

            initGroupUserMapping(rootElement);

            List<Element> addresses = rootElement.getChildren("Adressen");

            for (Element address : addresses) {
                migrateUser(userManager, address);
            }

        } catch (JDOMException e) {
            LOG.warn("Error while migrating members", e);
        }
        printErrors(resp);
    }

    private void printErrors(HttpServletResponse resp) {
        StringBuilder errorMsg = new StringBuilder();
        int i = 1;
        for (String error : errors) {
            errorMsg.append(i).append(": ").append(error).append("\n");
            i++;
        }
        LOG.warn(errorMsg.toString());
    }

    private void initJSNumberMapping() {
        InputStream jsNumberXlsStream = getJSNumberXlsAsStream();
        HSSFWorkbook workbook;
        try {
            workbook = new HSSFWorkbook(jsNumberXlsStream);
            HSSFSheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                HSSFRow row = sheet.getRow(i);

                HSSFCell jsNumberCell = row.getCell(0);
                HSSFCell lastnameCell = row.getCell(2);
                HSSFCell prenameCell = row.getCell(3);

                if (jsNumberCell != null && prenameCell != null && lastnameCell != null) {
                    int jsNumberInt = (int) jsNumberCell.getNumericCellValue();
                    String jsNumber = String.valueOf(jsNumberInt);
                    String prename = prenameCell.getStringCellValue();
                    String lastname = lastnameCell.getStringCellValue();

                    jsNumber = StringUtils.trim(jsNumber);
                    prename = StringUtils.trimToEmpty(prename);
                    lastname = StringUtils.trimToEmpty(lastname);

                    if (StringUtils.isNotEmpty(jsNumber)) {
                        this.jsNumberMapping.put(prename + lastname, jsNumber);
                    }
                }
            }
        } catch (IOException e) {
            LOG.error("Unable to parse J+S Liste ", e);
        }
    }

    private void initStvNumberMapping() {
        InputStream stvNumberXlsStream = getStvNumberXlsAsStream();
        HSSFWorkbook workbook;
        try {
            workbook = new HSSFWorkbook(stvNumberXlsStream);
            HSSFSheet sheet = workbook.getSheetAt(0);
            for (int i = 7; i <= sheet.getLastRowNum(); i = i + 7) {
                HSSFRow row = sheet.getRow(i);

                HSSFCell stvNumberCell = row.getCell(0);
                HSSFCell lastnameCell = row.getCell(1);
                HSSFCell prenameCell = row.getCell(5);

                if (stvNumberCell != null && prenameCell != null && lastnameCell != null) {
                    String stvNumber = stvNumberCell.getStringCellValue();
                    String prename = prenameCell.getStringCellValue();
                    String lastname = lastnameCell.getStringCellValue();

                    stvNumber = StringUtils.trim(stvNumber);
                    prename = StringUtils.trimToEmpty(prename);
                    lastname = StringUtils.trimToEmpty(lastname);

                    if (StringUtils.isNotEmpty(stvNumber)) {
                        this.stvNumberMapping.put(prename + lastname, stvNumber);
                    }
                }
            }
        } catch (IOException e) {
            LOG.error("Unable to parse Verbansliste ", e);
        }
    }

    private void initGroupMapping() {
        this.groupdMapping.put("1", "motherChild");
        this.groupdMapping.put("2", "childGym");
        this.groupdMapping.put("3", "youthMixed");
        this.groupdMapping.put("4", "girls1");
        this.groupdMapping.put("5", "girls2");
        this.groupdMapping.put("6", "boys1");
        this.groupdMapping.put("7", "boys2");
        this.groupdMapping.put("8", "youthAndSport");
        this.groupdMapping.put("9", "ladiesSquad");
        this.groupdMapping.put("29", "teamAerobic");
        this.groupdMapping.put("10", "gymnasts");
        this.groupdMapping.put("31", "apparatusGym1");
        this.groupdMapping.put("32", "apparatusGym2");
        this.groupdMapping.put("33", "aerobicFit");
        this.groupdMapping.put("11", "activSportFit");
        this.groupdMapping.put("12", "womenSquad");
        this.groupdMapping.put("13", "menSquad");
        this.groupdMapping.put("34", "seniors");
    }

    @SuppressWarnings("unchecked")
    private void initGroupUserMapping(Element rootElement) {
        List<Element> groupDetails = rootElement.getChildren("Gruppendetails");
        for (Element groupDetail : groupDetails) {
            String userId = readElementText("AdressGD", groupDetail);
            String groupId = readElementText("GruppeID", groupDetail);
            Set<String> groups = groupUserMapping.get(userId);
            if (groups == null) {
                groups = new HashSet<String>();
            }
            groups.add(groupId);
            groupUserMapping.put(userId, groups);
        }
    }

    private void migrateUser(UserManager userManager,
                             Element address) {
        try {

            LOG.info("------------------------------------------------------------");
            LOG.info("Start creating User");
            if (isValidUser(address)) {
                User user = createUser(userManager, address);
                if (user != null) {
                    String prename = "";
                    String lastname = "";
                    for (String addressField : fieldMapping.keySet()) {
                        String fieldText = readElementText(addressField, address);
                        if ("GeburtsDatum".equals(addressField)) {
                            userManager.setProperty(user, fieldMapping.get(addressField), createDateValue(fieldText));
                        } else {
                            userManager.setProperty(user, fieldMapping.get(addressField), createValue(fieldText));
                        }

                        if ("Vorname".equals(addressField)) {
                            prename = fieldText;
                        }
                        if ("Nachname".equals(addressField)) {
                            lastname = fieldText;
                        }
                    }
                    setSpecialValues(userManager, user, address, prename, lastname);
                    LOG.info("Created User [" + user.getName() + "]");
                }
            }
            LOG.info("------------------------------------------------------------");
        } catch (RuntimeException e) {
            errors.add(readElementText("EMail", address));
        }
    }

    private boolean isValidUser(Element elem) {
        String birthDay = readElementText("GeburtsDatum", elem);
        String[] birthDayArr = birthDay.split("-");
        if (StringUtils.isNotEmpty(birthDayArr[0]) && Integer.parseInt(birthDayArr[0]) >= 1997) {
            String userId = readElementText("AdressGD", elem);
            Set<String> groups = groupUserMapping.get(userId);
            if (groups == null) {
                return false;
            }
        }
        String active = readElementText("Aktiv", elem);
        if (StringUtils.isEmpty(active) || !Boolean.parseBoolean(active)) {
            return false;
        }
        return true;
    }

    private void setSpecialValues(UserManager userManager,
                                  User user,
                                  Element elem,
                                  String prename,
                                  String lastname) {
        setTitle(userManager, user, elem);
        userManager.setProperty(user, "enabled", createValue("false"));
        addGroups(userManager, user, elem);
        addStvNumber(userManager, user, prename, lastname);
    }

    private void addStvNumber(UserManager userManager,
                              User user,
                              String prename,
                              String lastname) {
        String stvNumber = this.stvNumberMapping.get(prename + lastname);
        if (StringUtils.isNotEmpty(stvNumber)) {
            userManager.setProperty(user, Member.PROP_STV_NUMBER, createValue(stvNumber));
        }
    }

    private void addGroups(UserManager userManager,
                           User user,
                           Element elem) {
        String userId = readElementText("AdressGD", elem);
        Set<String> groups = groupUserMapping.get(userId);
        if (groups == null) {
            userManager.addGroup(user, "stvkirchberg");
        } else {
            for (String group : groups) {
                userManager.addGroup(user, groupdMapping.get(group));
            }
        }
    }

    private void setTitle(UserManager userManager,
                          User user,
                          Element elem) {
        String title = readElementText("Vorname", elem) + " " + readElementText("Nachname", elem);
        userManager.setProperty(user, "title", createValue(title));
    }

    private void initFieldMapping() {
        this.fieldMapping.put("Anrede", Member.PROP_SALUTATION);
        this.fieldMapping.put("Vorname", Member.PROP_PRENAME);
        this.fieldMapping.put("Nachname", Member.PROP_LASTNAME);
        this.fieldMapping.put("GeburtsDatum", Member.PROP_BIRTHDATE);
        this.fieldMapping.put("Adresse", Member.PROP_ADDRESS);
        this.fieldMapping.put("PLZ", Member.PROP_POSTAL_CODE);
        this.fieldMapping.put("Ort", Member.PROP_CITY);
        this.fieldMapping.put("Telefon", Member.PROP_PHONE_NUMBER);
        this.fieldMapping.put("Mobile", Member.PROP_MOBILE_PHONE_NUMBER);
        this.fieldMapping.put("EMail", Member.PROP_MAIL);
        this.fieldMapping.put("Aktiv", Member.PROP_ACTIVE);
        this.fieldMapping.put("Ehrenmitglied", Member.PROP_HONORARY_MEMBER);
        this.fieldMapping.put("G�nner", Member.PROP_BENEFACTOR);
    }

    private Value createValue(String val) {
        Value value = null;
        try {
            value = NodeDataUtil.createValue(val, PropertyType.STRING);
        } catch (RepositoryException e) {
            LOG.warn("Unable to create value for value " + val, e);
        }
        return value;
    }

    private Value createDateValue(String val) {
        Value value = null;
        try {
            value = NodeDataUtil.createValue(StringUtils.substringBefore(val, "+"), PropertyType.DATE);
        } catch (RepositoryException e) {
            LOG.warn("Unable to create date value for value " + val, e);
        }
        return value;
    }

    private User createUser(UserManager userManager,
                            Element address) {
        String email = readElementText("EMail", address);
        String userName = "";
        if (StringUtils.isEmpty(email)) {
            userName = "undefined" + undefinedCounter;
            undefinedCounter++;
            LOG.info("User has no email address. generated username [" + userName + "]");
        } else {
            userName = StringUtils.replace(email, "@", "-");
            boolean userAlreadyExists = userManager.getUser(userName) != null;
            if (userAlreadyExists) {
                LOG.info("A user with the name [" + userName + "] already exists. ");
                userName = "undefined" + undefinedCounter;
                undefinedCounter++;
                LOG.info("generated username [" + userName + "]");
            }
        }
        LOG.info("Choosen username is: " + userName);
        User user = userManager.createUser(userName, StringUtils.EMPTY);
        if (user == null) {
            LOG.info("Unable to create user [" + userName + "]");
        }
        return user;
    }

    private String readElementText(String childElement,
                                   Element address) {
        Element elem = address.getChild(childElement);
        if (elem == null) {
            return StringUtils.EMPTY;
        } else {
            return elem.getText();
        }
    }

    private InputStream getInputXml() {
        return this.getClass().getResourceAsStream("/mgnl-resources/stvkirchberg-module/migration/users.xml");
    }

    private InputStream getStvNumberXlsAsStream() {
        return this.getClass().getResourceAsStream("/mgnl-resources/stvkirchberg-module/migration/VerbandslisteMai13.xls");
    }

    private InputStream getJSNumberXlsAsStream() {
        return this.getClass().getResourceAsStream("/mgnl-resources/stvkirchberg-module/migration/JS_Personennummer_alle Mitglieder.xls");
    }
}
