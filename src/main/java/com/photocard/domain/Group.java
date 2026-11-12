package com.photocard.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Group {

    private final String name;
    private final List<Member> members = new ArrayList<>();

    public Group(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Group name must not be blank.");
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addMember(Member member) {
        if (member == null || member.getGroup() != this) {
            throw new IllegalArgumentException("Member does not belong to this group.");
        }
        members.add(member);
    }

    public List<Member> getMembers() {
        return Collections.unmodifiableList(members);
    }
}
