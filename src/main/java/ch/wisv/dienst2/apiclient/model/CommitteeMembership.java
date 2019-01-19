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

import java.io.Serializable;
import java.util.Objects;

/**
 * Committee membership model
 */
public class CommitteeMembership implements Serializable {
    private int id;
    private int person;

    private String committee;
    private int board;
    private String position;

    public int getId() {
        return id;
    }

    public int getPerson() {
        return person;
    }

    public String getCommittee() {
        return committee;
    }

    public int getBoard() {
        return board;
    }

    public String getPosition() {
        return position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CommitteeMembership that = (CommitteeMembership) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(person, that.person) &&
                Objects.equals(board, that.board) &&
                Objects.equals(committee, that.committee) &&
                Objects.equals(position, that.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, person, committee, board, position);
    }
}
