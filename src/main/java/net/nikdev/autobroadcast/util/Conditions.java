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
