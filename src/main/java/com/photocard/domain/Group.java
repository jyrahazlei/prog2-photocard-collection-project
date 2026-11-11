package com.photocard.domain;

public class Group {

    private final String name;

    public Group(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Group name must not be blank.");
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
