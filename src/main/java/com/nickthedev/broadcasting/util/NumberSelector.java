/*
Copyright 2020 NickTheDev

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
package com.nickthedev.broadcasting.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

/**
 * Random number generator that allows for the user to switch between allowing repeats or not.
 *
 * @author NickTheDev
 * @since 3.0
 */
public class NumberSelector {

    private final Random random = new Random();
    private Supplier<Integer> supplier;

    /**
     * Creates a new number selector with the specs.
     *
     * @param repeat If the generator allows repeats.
     * @param bounds Bounds to choose from.
     */
    public NumberSelector(boolean repeat, int bounds) {
        supplier = repeat ? repeatable(bounds) : unique(bounds);
    }

    /**
     * Creates a behavior that generates numbers that are unique till all numbers in the bound
     * are used and resets.
     *
     * @param bounds Bounds to apply.
     * @return New number supplier.
     */
    private Supplier<Integer> unique(int bounds) {
        List<Integer> possible = new ArrayList<>();

        for(int i = 0; i < bounds; i++) {
            possible.add(i);
        }

        List<Integer> current = new ArrayList<>(possible);

        return () -> {
            int next = current.remove(random.nextInt(current.size()));

            if(current.isEmpty()) {
                current.addAll(possible);
            }

            return next;
        };

    }

    /**
     * Creates a behavior that generates random numbers allowing repeats.
     *
     * @param bounds Bounds to apply.
     * @return New number supplier.
     */
    private Supplier<Integer> repeatable(int bounds) {
        return () -> random.nextInt(bounds);
    }

    /**
     * Generates the next integer from the bounds.
     *
     * @return Next pseudorandom number.
     */
    public int next() {
        return supplier.get();
    }

}
