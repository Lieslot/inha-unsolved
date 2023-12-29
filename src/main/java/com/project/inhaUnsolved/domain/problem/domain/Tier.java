package com.project.inhaUnsolved.domain.domain;

import jakarta.persistence.Entity;
import lombok.Getter;

@Getter
public enum Tier {
    UNRATED(0),
    BRONZE_V(1),
    BRONZE_IV(2),
    BRONZE_III(3),
    BRONZE_II(4),
    BRONZE_I(5),
    SILVER_V(6),
    SILVER_IV(7),
    SILVER_III(8),
    SILVER_II(9),
    SILVER_I(10),
    GOLD_V(11),
    GOLD_IV(12),
    GOLD_III(13),
    GOLD_II(14),
    GOLD_I(15),
    PLATINUM_V(16),
    PLATINUM_IV(17),
    PLATINUM_III(18),
    PLATINUM_II(19),
    PLATINUM_I(20),
    DIAMOND_V(21),
    DIAMOND_IV(22),
    DIAMOND_III(23),
    DIAMOND_II(24),
    DIAMOND_I(25),
    RUBY_V(26),
    RUBY_IV(27),
    RUBY_III(28),
    RUBY_II(29),
    RUBY_I(30);

    private final int value;

    Tier(int value) {
        this.value = value;
    }


}