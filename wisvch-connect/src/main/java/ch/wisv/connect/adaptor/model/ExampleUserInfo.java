package ch.wisv.connect.adaptor.model;
import javax.persistence.Basic;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.google.gson.JsonObject;
import org.mitre.openid.connect.model.UserInfo;
import org.mitre.openid.connect.model.Address;

public class ExampleUserInfo implements UserInfo {

	/**
	 * @return the userId
	 */
	@Id
	public String getUserId(){
		return "USERID";
        }

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId){
	}

	@Override
	public String getSub() {
		return null;
	}

	@Override
	public void setSub(String sub) {

	}

	/**
	 * @return the preferred username
	 */
	@Basic
	public String getPreferredUsername(){
		return "PREFERRED_USERNAME";
	}
	
	/**
	 * @param preferredUsername the preferredUsername to set
	 */
	public void setPreferredUsername(String preferredUsername){
	}

	/**
	 * @return the name
	 */
	@Basic
	public String getName(){
		return "NAME";
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name){}

	/**
	 * @return the givenName
	 */
	@Basic
	public String getGivenName(){
		return "foo";
	}

	/**
	 * @param givenName the givenName to set
	 */
	public void setGivenName(String givenName){}

	/**
	 * @return the familyName
	 */
	@Basic
	public String getFamilyName(){
		return "foo";
	}

	/**
	 * @param familyName the familyName to set
	 */
	public void setFamilyName(String familyName){}

	/**
	 * @return the middleName
	 */
	@Basic
	public String getMiddleName(){
		return "foo";
	}

	/**
	 * @param middleName the middleName to set
	 */
	public void setMiddleName(String middleName){}

	/**
	 * @return the nickname
	 */
	@Basic
	public String getNickname(){
		return "foo";
	}

	/**
	 * @param nickname the nickname to set
	 */
	public void setNickname(String nickname){}

	/**
	 * @return the profile
	 */
	@Basic
	public String getProfile(){
		return "foo";
	}

	/**
	 * @param profile the profile to set
	 */
	public void setProfile(String profile){}

	/**
	 * @return the picture
	 */
	@Basic
	public String getPicture(){
		return "foo";
	}

	/**
	 * @param picture the picture to set
	 */
	public void setPicture(String picture){}

	/**
	 * @return the website
	 */
	@Basic
	public String getWebsite(){
		return "foo";
	}

	/**
	 * @param website the website to set
	 */
	public void setWebsite(String website){}

	/**
	 * @return the email
	 */
	@Basic
	public String getEmail(){
		return "foo";
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email){}

	/**
	 * @return the verified
	 */
	@Basic
	public Boolean getEmailVerified(){
		return new Boolean(false);
	}

	/**
	 * @param verified the verified to set
	 */
	public void setEmailVerified(Boolean emailVerified){}

	/**
	 * @return the gender
	 */
	@Basic
	public String getGender(){
		return "foo";
	}

	/**
	 * @param gender the gender to set
	 */
	public void setGender(String gender){}

	/**
	 * @return the zoneinfo
	 */
	@Basic
	public String getZoneinfo(){
		return "foo";
	}

	/**
	 * @param zoneinfo the zoneinfo to set
	 */
	public void setZoneinfo(String zoneinfo){}

	/**
	 * @return the locale
	 */
	@Basic
	public String getLocale(){
		return "foo";
	}

	/**
	 * @param locale the locale to set
	 */
	public void setLocale(String locale){}

	/**
	 * @return the phoneNumber
	 */
	@Basic
	public String getPhoneNumber(){
		return "foo";
	}

	/**
	 * @param phoneNumber the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber){}

	@Override
	public Boolean getPhoneNumberVerified() {
		return null;
	}

	@Override
	public void setPhoneNumberVerified(Boolean phoneNumberVerified) {

	}

	/**
	 * @return the address
	 */
	@OneToOne
	public Address getAddress(){
		return null;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(Address address){}

	/**
	 * @return the updatedTime
	 */
	@Basic
	public String getUpdatedTime(){
		return "foo";
	}

	/**
	 * @param updatedTime the updatedTime to set
	 */
	public void setUpdatedTime(String updatedTime){}

	@Override
	public String getBirthdate() {
		return null;
	}

	@Override
	public void setBirthdate(String birthdate) {

	}

	@Override
	public JsonObject toJson() {
		return null;
	}

	@Override
	public JsonObject getSource() {
		return null;
	}


}
