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

package ch.wisv.dienst2.apiclient.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.net.URI;
import java.util.Objects;

/**
 * Entity model
 */
public class Entity implements Serializable {
    private int id;
    private URI url;

    private String streetAddress;
    private String formattedAddress;
    private String streetName;
    private String houseNumber;
    @JsonProperty("address_2")
    private String address2;
    @JsonProperty("address_3")
    private String address3;
    private String postcode;
    private String city;
    private String country;
    private String countryFull;
    private String email;

    private String phoneFixed;

    private boolean machazine;
    private boolean constitutionCard;
    private boolean christmasCard;
    private boolean yearbook;

    public int getId() {
        return id;
    }

    public URI getUrl() {
        return url;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public String getStreetName() {
        return streetName;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public String getAddress2() {
        return address2;
    }

    public String getAddress3() {
        return address3;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getCountryFull() {
        return countryFull;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneFixed() {
        return phoneFixed;
    }

    public boolean isMachazine() {
        return machazine;
    }

    public boolean isConstitutionCard() {
        return constitutionCard;
    }

    public boolean isChristmasCard() {
        return christmasCard;
    }

    public boolean isYearbook() {
        return yearbook;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Entity entity = (Entity) o;
        return Objects.equals(id, entity.id) &&
                Objects.equals(machazine, entity.machazine) &&
                Objects.equals(constitutionCard, entity.constitutionCard) &&
                Objects.equals(christmasCard, entity.christmasCard) &&
                Objects.equals(yearbook, entity.yearbook) &&
                Objects.equals(url, entity.url) &&
                Objects.equals(streetName, entity.streetName) &&
                Objects.equals(houseNumber, entity.houseNumber) &&
                Objects.equals(address2, entity.address2) &&
                Objects.equals(address3, entity.address3) &&
                Objects.equals(postcode, entity.postcode) &&
                Objects.equals(city, entity.city) &&
                Objects.equals(country, entity.country) &&
                Objects.equals(email, entity.email) &&
                Objects.equals(phoneFixed, entity.phoneFixed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, url, streetName, houseNumber, address2, address3, postcode, city, country, email,
                phoneFixed, machazine, constitutionCard, christmasCard, yearbook);
    }
}
