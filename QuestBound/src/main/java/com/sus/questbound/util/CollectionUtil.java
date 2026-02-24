package com.sus.questbound.util;

import java.util.List;
import java.util.function.Function;

public final class CollectionUtil {

    private CollectionUtil() {}

    public static <T, R> List<R> mapToList(List<T> list, Function<T, R> mapper) {
        return list.stream()
                .map(mapper)
                .toList();
    }
}