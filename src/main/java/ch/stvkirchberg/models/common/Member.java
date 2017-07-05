/*
 * (c) 2012 holder contact. All rights reserved. This material is solely and exclusively owned by holder and
 * may not be reproduced elsewhere without prior written approval.
 */

package ch.stvkirchberg.models.common;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.RepositoryException;

import org.apache.commons.lang.StringUtils;
import org.apache.jackrabbit.JcrConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.stvkirchberg.utils.NodeUtils;
import info.magnolia.cms.security.User;

public class Member implements Comparable<Member> {

    private static Logger LOG = LoggerFactory.getLogger(Member.class);

    public static final String PROP_USERNAME = "username";

    public static final String PROP_SALUTATION = "salutation";

    public static final String PROP_PRENAME = "prename";

    public static final String PROP_LASTNAME = "lastname";

    public static final String PROP_ENTRY_DATE = "entryDate";

    public static final String PROP_BIRTHDATE = "birthdate";

    public static final String PROP_ADDRESS = "address";

    public static final String PROP_POSTAL_CODE = "postalCode";

    public static final String PROP_CITY = "city";

    public static final String PROP_PHONE_NUMBER = "phoneNumber";

    public static final String PROP_MOBILE_PHONE_NUMBER = "mobilePhoneNumber";

    public static final String PROP_MAIL = "email";

    public static final String PROP_ACTIVE = "activ";

    public static final String PROP_HONORARY_MEMBER_SINCE = "honoraryMemberSince";

    public static final String PROP_HONORARY_MEMBER = "honoraryMember";

    public static final String PROP_BENEFACTOR = "benefactor";

    public static final String PROP_PASSIV_MEMBER = "passivMember";

    public static final String PROP_CLUB_MEMBER = "clubMember";

    public static final String PROP_STV_NUMBER = "stvNumber";

    public static final String PROP_IMG_FORBIDDEN = "imgForbidden";

    public static final String PROP_REMARK = "remark";

    private String uuid;

    private String username;

    private String salutation;

    private String prename;

    private String lastname;

    private String entryDate;

    private String honoraryMemberSince;

    private String birthdate;

    private String address;

    private String postalCode;

    private String city;

    private String phoneNumber;

    private String mobilePhoneNumber;

    private String mail;

    private boolean active;

    private boolean honoraryMember;

    private boolean benefactor;

    private boolean passivMember;

    private boolean clubMember;

    private List<String> roles = new ArrayList<String>();

    private List<String> squads = new ArrayList<String>();

    private String stvNumber;

    private boolean imgForbidden;

    private String remark;

    public Member() {

    }

    public Member(Node userNode) {
        this.setUuid(NodeUtils.getStringProperty(userNode, JcrConstants.JCR_UUID));
        try {
            this.username = userNode.getName();
        } catch (RepositoryException e1) {
            LOG.warn("Unable to read node name while Member construction", e1);
        }
        this.salutation = NodeUtils.getStringProperty(userNode, PROP_SALUTATION);
        this.prename = NodeUtils.getStringProperty(userNode, PROP_PRENAME);
        this.lastname = NodeUtils.getStringProperty(userNode, PROP_LASTNAME);
        this.birthdate = getFormattedDate(NodeUtils.getDateProperty(userNode, PROP_BIRTHDATE));
        this.entryDate = getFormattedDate(NodeUtils.getDateProperty(userNode, PROP_ENTRY_DATE));
        this.address = NodeUtils.getStringProperty(userNode, PROP_ADDRESS);
        this.postalCode = NodeUtils.getStringProperty(userNode, PROP_POSTAL_CODE);
        this.city = NodeUtils.getStringProperty(userNode, PROP_CITY);
        this.phoneNumber = NodeUtils.getStringProperty(userNode, PROP_PHONE_NUMBER);
        this.mobilePhoneNumber = NodeUtils.getStringProperty(userNode, PROP_MOBILE_PHONE_NUMBER);
        this.mail = NodeUtils.getStringProperty(userNode, PROP_MAIL);
        this.active = getBooleanValueOfString(NodeUtils.getStringProperty(userNode, PROP_ACTIVE));
        this.honoraryMemberSince = getFormattedDate(NodeUtils.getDateProperty(userNode, PROP_HONORARY_MEMBER_SINCE));
        this.honoraryMember = getBooleanValueOfString(NodeUtils.getStringProperty(userNode, PROP_HONORARY_MEMBER));
        this.benefactor = getBooleanValueOfString(NodeUtils.getStringProperty(userNode, PROP_BENEFACTOR));
        this.passivMember = getBooleanValueOfString(NodeUtils.getStringProperty(userNode, PROP_PASSIV_MEMBER));
        this.clubMember = getBooleanValueOfString(NodeUtils.getStringProperty(userNode, PROP_CLUB_MEMBER));
        this.setRoles(initRoleList(userNode));
        this.setSquads(initSquadList(userNode));
        this.stvNumber = NodeUtils.getStringProperty(userNode, PROP_STV_NUMBER);
        this.imgForbidden = getBooleanValueOfString(NodeUtils.getStringProperty(userNode, PROP_IMG_FORBIDDEN));
        this.remark = NodeUtils.getStringProperty(userNode, PROP_REMARK);
    }

    public Member(User user) {
        this.username = user.getName();
        this.salutation = user.getProperty(PROP_SALUTATION);
        this.prename = user.getProperty(PROP_PRENAME);
        this.lastname = user.getProperty(PROP_LASTNAME);
        this.birthdate = getFormattedDate(user.getProperty(PROP_BIRTHDATE));
        this.entryDate = getFormattedDate(user.getProperty(PROP_ENTRY_DATE));
        this.honoraryMemberSince = getFormattedDate(user.getProperty(PROP_HONORARY_MEMBER_SINCE));
        this.address = user.getProperty(PROP_ADDRESS);
        this.postalCode = user.getProperty(PROP_POSTAL_CODE);
        this.city = user.getProperty(PROP_CITY);
        this.phoneNumber = user.getProperty(PROP_PHONE_NUMBER);
        this.mobilePhoneNumber = user.getProperty(PROP_MOBILE_PHONE_NUMBER);
        this.mail = user.getProperty(PROP_MAIL);
        this.active = getBooleanValueOfString(user.getProperty(PROP_ACTIVE));
        this.honoraryMember = getBooleanValueOfString(user.getProperty(PROP_HONORARY_MEMBER));
        this.benefactor = getBooleanValueOfString(user.getProperty(PROP_BENEFACTOR));
        this.passivMember = getBooleanValueOfString(user.getProperty(PROP_PASSIV_MEMBER));
        this.clubMember = getBooleanValueOfString(user.getProperty(PROP_CLUB_MEMBER));
        // TODO pforster: adapt functionality if used in future
        // this.setRoles(initRoleList(this.username));
        // this.setSquads(initSquadList(user));
        this.stvNumber = user.getProperty(PROP_STV_NUMBER);
        this.imgForbidden = getBooleanValueOfString(user.getProperty(PROP_IMG_FORBIDDEN));
        this.remark = user.getProperty(PROP_REMARK);
    }

    private List<String> initSquadList(Node userNode) {
        return getPropertyList(userNode, "groups");
    }

    private List<String> initRoleList(Node userNode) {
        return getPropertyList(userNode, "roles");
    }

    private List<String> getPropertyList(Node userNode,
                                         String subNodeName) {
        List<String> props = new ArrayList<String>();

        Node subNode = NodeUtils.getNode(userNode, subNodeName);
        if (subNode == null) {
            return props;
        }
        try {
            PropertyIterator properties = subNode.getProperties();
            while (properties.hasNext()) {
                Property prop = properties.nextProperty();
                if (!prop.getName().startsWith("jcr:")) {
                    props.add(prop.getValue().getString());
                }
            }
        } catch (RepositoryException e) {
            LOG.warn("Unable to find Property");
        }
        return props;
    }

    private String getFormattedDate(String date) {
        if (StringUtils.isEmpty(date)) {
            return StringUtils.EMPTY;
        }
        String[] dateArr = date.split("-");
        if (dateArr.length != 3) {
            return StringUtils.EMPTY;
        }
        return dateArr[2].substring(0, 2) + "." + dateArr[1] + "." + dateArr[0];
    }

    private String getFormattedDate(Calendar cal) {
        if (cal == null) {
            return StringUtils.EMPTY;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        Date birthdate = cal.getTime();
        return sdf.format(birthdate);
    }

    private boolean getBooleanValueOfString(String string) {
        if (StringUtils.isEmpty(string)) {
            return false;
        }
        return Boolean.parseBoolean(string);
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public String getHonoraryMemberSince() {
        return honoraryMemberSince;
    }

    public void setHonoraryMemberSince(String honoraryMemberSince) {
        this.honoraryMemberSince = honoraryMemberSince;
    }

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    public String getPrename() {
        return prename;
    }

    public void setPrename(String prename) {
        this.prename = prename;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMobilePhoneNumber() {
        return mobilePhoneNumber;
    }

    public void setMobilePhoneNumber(String mobilePhoneNumber) {
        this.mobilePhoneNumber = mobilePhoneNumber;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isHonoraryMember() {
        return honoraryMember;
    }

    public void setHonoraryMember(boolean honoraryMember) {
        this.honoraryMember = honoraryMember;
    }

    public boolean isBenefactor() {
        return benefactor;
    }

    public void setBenefactor(boolean benefactor) {
        this.benefactor = benefactor;
    }

    public boolean isPassivMember() {
        return passivMember;
    }

    public void setPassivMember(boolean passivMember) {
        this.passivMember = passivMember;
    }

    public boolean isClubMember() {
        return clubMember;
    }

    public void setClubMember(boolean clubMember) {
        this.clubMember = clubMember;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<String> getSquads() {
        return squads;
    }

    public void setSquads(List<String> squads) {
        this.squads = squads;
    }

    public String getStvNumber() {
        return stvNumber;
    }

    public void setStvNumber(String stvNumber) {
        this.stvNumber = stvNumber;
    }

    public boolean isImgForbidden() {
        return imgForbidden;
    }

    public void setImgForbidden(boolean imgForbidden) {
        this.imgForbidden = imgForbidden;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int compareTo(Member other) {
        if (other == null) {
            return -1;
        }
        if (this.lastname == null) {
            return 1;
        }
        int lastnameCompareResult = this.lastname.compareToIgnoreCase(other.lastname);
        if (lastnameCompareResult == 0) {
            if (this.prename == null) {
                return 1;
            }
            return this.prename.compareTo(other.prename);
        }
        return lastnameCompareResult;
    }

}
