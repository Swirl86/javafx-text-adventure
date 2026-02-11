package com.sus.questbound.util;

import com.sus.questbound.ui.OutputController;
import com.sus.questbound.model.MsgType;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public final class OutputFormatHelper {

    private OutputFormatHelper() {}

    public static <T> void printCollectionWithDetails(
            OutputController output,
            List<T> items,
            MsgType type,
            Supplier<String> emptyMsg,
            Function<T, String> singleMsg,
            Function<String, String> multiMsg,
            Function<T, String> nameMapper
    ) {
        if (items.isEmpty()) {
            output.println(emptyMsg.get(), type);
        } else if (items.size() == 1) {
            output.println(singleMsg.apply(items.get(0)), type);
        } else {
            String names = items.stream()
                    .map(nameMapper)
                    .collect(Collectors.joining(", "));
            output.println(multiMsg.apply(names), type);
        }
    }
}