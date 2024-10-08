/**
 * Copyright (C) 2024, Sartaj Hussain.
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 * Project: quick-cache
 * @author sartajhussain
 */
package com.sartaj.cache;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import lombok.*;

@Builder(builderClassName = "Builder", toBuilder = true)
@Getter
public class RandomGenerator<T extends Number> {

    /** Initial value */
    private T max;

    private T origin;
    private T bound;
    @lombok.Builder.Default private Random with = new Random();

    public IntStream ints() {
        origin = Objects.isNull(origin) ? (T) Integer.valueOf(Integer.MAX_VALUE) : origin;
        bound = Objects.isNull(bound) ? (T) Integer.valueOf(0) : bound;

        return with.ints(max.intValue(), origin.intValue(), bound.intValue());
    }

    public LongStream longs() {
        origin = Objects.isNull(origin) ? (T) Long.valueOf(Long.MAX_VALUE) : origin;
        bound = Objects.isNull(bound) ? (T) Long.valueOf(0) : bound;

        return with.longs(max.longValue(), origin.longValue(), bound.longValue());
    }

    public DoubleStream doubles() {
        origin = Objects.isNull(origin) ? (T) Double.valueOf(Integer.MAX_VALUE) : origin;
        bound = Objects.isNull(bound) ? (T) Double.valueOf(0) : bound;

        return with.doubles(max.longValue(), origin.doubleValue(), bound.doubleValue());
    }

    public List<T> toList() {
        if (max instanceof Integer) {
            return (List<T>) ints().boxed().toList();
        } else if (max instanceof Long) {
            return (List<T>) longs().boxed().toList();
        } else if (max instanceof DoubleStream) {
            return (List<T>) doubles().boxed().toList();
        } else {
            throw new RuntimeException("Provided generator type is not supported " + max.getClass());
        }
    }

    public List<T> toPositiveList() {
        if (max instanceof Integer) {
            return (List<T>) ints().map(t -> t < 0 ? t * -1 : t).boxed().toList();
        } else if (max instanceof Long) {
            return (List<T>) longs().map(t -> t < 0 ? t * -1 : t).boxed().toList();
        } else if (max instanceof DoubleStream) {
            return (List<T>) doubles().map(t -> t < 0 ? t * -1 : t).boxed().toList();
        } else {
            throw new RuntimeException("Provided generator type is not supported " + max.getClass());
        }
    }

    public T toPositive() {
        if (max instanceof Integer) {
            return (T) Integer.valueOf(with.nextInt((int) bound));
        } else if (max instanceof Long) {
            return (T) Long.valueOf(with.nextLong((int) bound));
        } else if (max instanceof DoubleStream) {
            return (T) Integer.valueOf(with.nextInt((int) bound));
        } else {
            throw new RuntimeException("Provided generator type is not supported " + max.getClass());
        }
    }
}
