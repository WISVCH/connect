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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * MembershipStatus enum
 */
public enum MembershipStatus {
    NONE(0),
    DONATING(10),
    ALUMNUS(20),
    REGULAR(30),
    ASSOCIATE(40),
    MERIT(50),
    HONORARY(60);

    private final int value;

    MembershipStatus(int value) {
        this.value = value;
    }

    @JsonCreator
    public static MembershipStatus forValue(int value) {
        for (MembershipStatus membershipStatus : MembershipStatus.values()) {
            if (membershipStatus.getValue() == value) {
                return membershipStatus;
            }
        }
        throw new IllegalArgumentException("Invalid MembershipStatus value: " + value);
    }

    @JsonValue
    public int getValue() {
        return value;
    }
}
