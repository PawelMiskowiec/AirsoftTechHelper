package com.example.airsofttechhelper.part.domain;

import java.util.Arrays;
import java.util.Optional;

public enum PartCategory {
    PNEUMATICS, MECHANICAL, HOPUP, BARREL, MISC;

    public static Optional<PartCategory> parseString(String category){
        return Arrays
                .stream(PartCategory.values())
                .filter(partCategory -> partCategory.name().equalsIgnoreCase(category))
                .findFirst();
    }
}
