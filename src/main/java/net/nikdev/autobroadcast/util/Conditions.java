/*
Copyright 2017 NickTheDev <http://nikdev.net/>

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package net.nikdev.autobroadcast.util;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Stream;

public final class Conditions {

    private Conditions() {}

    public static <T> T orElse(T value, T other) {
        return notNull(value) ? value : other;
    }

    public static <T> boolean isPresentCollection(Collection<T> collection) {
       return notNull(collection) && collection.stream().allMatch(Objects::nonNull);
    }

    @SafeVarargs
    public static <T> boolean notNull(T... values) {
        return Stream.of(values).allMatch(Objects::nonNull);
    }

}
