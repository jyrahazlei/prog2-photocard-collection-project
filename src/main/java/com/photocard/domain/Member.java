package com.photocard.domain;

public class Member {
    private final String name;
    private final Group group; // null -> solo artist

    public Member(String name, Group group) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Member name must not be blank.");
        }
        this.name = name;
        this.group = group;
    }

    public Member(String name) {
        this(name, null);
    }

    public String getName() { return name; }
    public Group getGroup() { return group; }
}
