package ch.wisv.connect.common.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.mitre.openid.connect.model.DefaultUserInfo;
import org.mitre.openid.connect.model.UserInfo;

import java.lang.reflect.Type;
import java.util.Objects;
import java.util.Set;

/**
 * CH Connect UserInfo.
 */
public class CHUserInfo extends DefaultUserInfo {
    private String netid;
    private String studentNumber;
    private String ldapUsername;
    private Set<String> ldapGroups;

    private transient JsonObject src;

    private static Gson gson = new Gson();
    private final static Type stringSetType = new TypeToken<Set<String>>() {
    }.getType();


    public String getNetid() {
        return netid;
    }

    public void setNetid(String netid) {
        this.netid = netid;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getLdapUsername() {
        return ldapUsername;
    }

    public void setLdapUsername(String ldapUsername) {
        this.ldapUsername = ldapUsername;
    }

    public Set<String> getLdapGroups() {
        return ldapGroups;
    }

    public void setLdapGroups(Set<String> ldapGroups) {
        this.ldapGroups = ldapGroups;
    }

    @Override
    public JsonObject getSource() {
        return src;
    }

    @Override
    public void setSource(JsonObject src) {
        this.src = src;
    }

    @Override
    public JsonObject toJson() {
        if (src == null) {
            JsonObject obj = super.toJson();

            obj.addProperty("netid", this.getNetid());
            obj.addProperty("student_number", this.getStudentNumber());
            obj.addProperty("ldap_username", this.getLdapUsername());
            obj.add("ldap_groups", gson.toJsonTree(ldapGroups));

            return obj;
        } else {
            return src;
        }
    }

    public static UserInfo fromJson(JsonObject obj) {
        CHUserInfo ui = new CHUserInfo();

        ui.setSource(obj);

        UserInfo dui = DefaultUserInfo.fromJson(obj);
        ui.setSub(dui.getSub());
        ui.setName(dui.getName());
        ui.setPreferredUsername(dui.getPreferredUsername());
        ui.setGivenName(dui.getGivenName());
        ui.setFamilyName(dui.getFamilyName());
        ui.setMiddleName(dui.getMiddleName());
        ui.setNickname(dui.getNickname());
        ui.setProfile(dui.getProfile());
        ui.setPicture(dui.getPicture());
        ui.setWebsite(dui.getWebsite());
        ui.setGender(dui.getGender());
        ui.setZoneinfo(dui.getZoneinfo());
        ui.setLocale(dui.getLocale());
        ui.setUpdatedTime(dui.getUpdatedTime());
        ui.setBirthdate(dui.getBirthdate());
        ui.setEmail(dui.getEmail());
        ui.setEmailVerified(dui.getEmailVerified());
        ui.setAddress(dui.getAddress());

        ui.setNetid(nullSafeGetString(obj, "netid"));
        ui.setStudentNumber(nullSafeGetString(obj, "student_number"));
        ui.setLdapUsername(nullSafeGetString(obj, "ldap_username"));
        ui.setLdapGroups(gson.<Set<String>>fromJson(obj.get("ldap_groups"), stringSetType));

        return ui;
    }

    private static String nullSafeGetString(JsonObject obj, String field) {
        return obj.has(field) && obj.get(field).isJsonPrimitive() ? obj.get(field).getAsString() : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CHUserInfo)) return false;
        if (!super.equals(o)) return false;
        CHUserInfo that = (CHUserInfo) o;
        return Objects.equals(netid, that.netid) &&
                Objects.equals(studentNumber, that.studentNumber) &&
                Objects.equals(ldapUsername, that.ldapUsername) &&
                Objects.equals(ldapGroups, that.ldapGroups);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), netid, studentNumber, ldapUsername, ldapGroups);
    }
}
