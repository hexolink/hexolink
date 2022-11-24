package com.auth.entity;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Random;
import java.util.Set;

public enum Color {
    PURPLE,
    GREEN,
    BLUE,
    ORANGE;

    private static final Set<Color> VALUES = Collections.synchronizedSet(EnumSet.allOf(Color.class));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static String randomColor() {
        return VALUES.toArray()[RANDOM.nextInt(SIZE)].toString();
    }
}
