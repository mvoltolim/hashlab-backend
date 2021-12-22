package com.hash.hashlab.repositories;

import com.hash.hashlab.models.Entity;
import com.hash.hashlab.utils.JacksonUtils;

import java.util.HashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Table<V extends Entity> extends HashMap<Integer, V> {

    Table(V[] values) {
        super(Stream.of(values).collect(Collectors.toMap(Entity::getId, Function.identity())));
    }

    @Override
    public V get(Object key) {
        return JacksonUtils.clone(super.get(key));
    }

}
