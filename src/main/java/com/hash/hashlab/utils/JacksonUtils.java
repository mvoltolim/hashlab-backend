package com.hash.hashlab.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import org.springframework.util.ResourceUtils;

import java.io.IOException;

public final class JacksonUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

    private JacksonUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static <T> T readFromFile(final String filename, Class<T> type) throws IOException {
        return MAPPER.readValue(ResourceUtils.getURL("classpath:" + filename), type);
    }

    public static <T> T clone(T object) {
        return MAPPER.convertValue(object, (Class<T>) object.getClass());
    }

}
