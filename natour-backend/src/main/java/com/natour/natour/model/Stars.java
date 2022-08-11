package com.natour.natour.model;

public enum Stars {
    ZERO, ONE, TWO, THREE, FOUR, FIVE;

    public static Stars getStarsFromAvgReviews(double avg) {
        if (avg < 0.5) return ZERO;
        if (avg < 1.5) return ONE;
        if (avg < 2.5) return TWO;
        if (avg < 3.5) return THREE;
        if (avg < 4.5) return FOUR;
        if (avg <= 5) return FIVE;
        else throw new IllegalArgumentException("Illegal avg reviews");
    }
}
