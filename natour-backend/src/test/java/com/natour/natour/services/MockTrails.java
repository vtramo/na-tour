package com.natour.natour.services;

import java.util.List;

import com.natour.natour.model.Stars;
import com.natour.natour.model.entity.Trail;

public class MockTrails {
    public static List<Trail> getMockTrails() {
        return List.of(
            new Trail(0, Stars.FOUR),
            new Trail(1, Stars.ONE),
            new Trail(2, Stars.FIVE),
            new Trail(3, Stars.FOUR),
            new Trail(4, Stars.TWO),
            new Trail(5, Stars.ZERO),
            new Trail(6, Stars.FOUR),
            new Trail(7, Stars.FIVE),
            new Trail(8, Stars.ZERO),
            new Trail(9, Stars.ONE),
            new Trail(10, Stars.FOUR),
            new Trail(11, Stars.THREE),
            new Trail(12, Stars.FIVE),
            new Trail(13, Stars.TWO),
            new Trail(14, Stars.ONE),
            new Trail(15, Stars.ZERO),
            new Trail(16, Stars.THREE)
        );
    }
}
