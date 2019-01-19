/*
 * Copyright 2019 W.I.S.V. 'Christiaan Huygens'
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

import java.io.Serializable;
import java.net.URI;
import java.util.Objects;

/**
 * Entity model
 */
public class Entity implements Serializable {
    private Integer id;
    private URI url;

    private String streetAddress;
    private String formattedAddress;
    private String streetName;
    private String postcode;
    private String city;
    private String country;
    private String email;

    private String revisionComment;

    public Integer getId() {
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

    public String getPostcode() {
        return postcode;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getEmail() {
        return email;
    }

    public String getRevisionComment() {
        return revisionComment;
    }

    public void setRevisionComment(String revisionComment) {
        this.revisionComment = revisionComment;
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
                Objects.equals(url, entity.url) &&
                Objects.equals(streetAddress, entity.streetAddress) &&
                Objects.equals(formattedAddress, entity.formattedAddress) &&
                Objects.equals(streetName, entity.streetName) &&
                Objects.equals(postcode, entity.postcode) &&
                Objects.equals(city, entity.city) &&
                Objects.equals(country, entity.country) &&
                Objects.equals(email, entity.email) &&
                Objects.equals(revisionComment, entity.revisionComment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, url, streetAddress, formattedAddress, streetName, postcode, city, country, email, revisionComment);
    }
}
