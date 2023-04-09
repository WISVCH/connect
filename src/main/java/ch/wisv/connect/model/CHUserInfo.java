/*
 * Copyright 2019 W.I.S.V. 'Christiaan Huygens'
 * Copyright 2018 The MITRE Corporation
 *    and the MIT Internet Trust Consortium
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ch.wisv.connect.model;

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
    private String study;
    private String ldapUsername;
    private Set<String> ldapGroups;
    private String googleUsername;
    private Set<String> googleGroups;

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

    public String getStudy() {
        return study;
    }

    public void setStudy(String study) {
        this.study = study;
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

    public String getGoogleUsername() {
        return googleUsername;
    }

    public Set<String> getGoogleGroups() {
        return googleGroups;
    }

    public void setGoogleUsername(String googleUsername) {
        this.googleUsername = googleUsername;
    }

    public void setLdapGroups(Set<String> ldapGroups) {
        this.ldapGroups = ldapGroups;
    }

    public void setGoogleGroups(Set<String> googleGroups) {
        this.googleGroups = googleGroups;
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
            obj.addProperty("study", this.getStudy());
            obj.addProperty("ldap_username", this.getLdapUsername());
            obj.add("ldap_groups", gson.toJsonTree(ldapGroups));
            obj.addProperty("google_username", this.getGoogleUsername());
            obj.add("google_groups", gson.toJsonTree(googleGroups));

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
        ui.setStudy(nullSafeGetString(obj, "study"));
        ui.setLdapUsername(nullSafeGetString(obj, "ldap_username"));
        ui.setLdapGroups(gson.fromJson(obj.get("ldap_groups"), stringSetType));
        ui.setGoogleUsername(nullSafeGetString(obj, "google_username"));
        ui.setGoogleGroups(gson.fromJson(obj.get("google_groups"), stringSetType));

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
                Objects.equals(study, that.study) &&
                Objects.equals(ldapUsername, that.ldapUsername) &&
                Objects.equals(ldapGroups, that.ldapGroups) &&
                Objects.equals(googleUsername, that.googleUsername) &&
                Objects.equals(googleGroups, that.googleGroups);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), netid, studentNumber, study, ldapUsername, ldapGroups, googleUsername, googleGroups);
    }
}
