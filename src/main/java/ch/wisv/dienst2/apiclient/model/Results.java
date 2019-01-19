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
import java.util.List;
import java.util.Objects;

/**
 * Result wrapper: metadata and multiple result objects
 */
public class Results<T> implements Serializable {

    private int count;
    private URI next;
    private URI previous;

    private List<T> results;

    public int getCount() {
        return count;
    }

    public URI getNext() {
        return next;
    }

    public URI getPrevious() {
        return previous;
    }

    public List<T> getResults() {
        return results;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Results<?> results1 = (Results<?>) o;
        return count == results1.count &&
                Objects.equals(next, results1.next) &&
                Objects.equals(previous, results1.previous) &&
                Objects.equals(results, results1.results);
    }

    @Override
    public int hashCode() {
        return Objects.hash(count, next, previous, results);
    }
}
