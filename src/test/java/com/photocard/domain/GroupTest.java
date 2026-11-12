package com.photocard.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GroupTest {

    @Test
    void memberofAnotherGroupIsRejected() {
        Group groupA = new Group("Group A");
        Group groupB = new Group("Group B");
        Member member = new Member("Member One", groupB);

    assertThrows(IllegalArgumentException.class, () -> groupA.addMember(member));
    }

    @Test
    void soloMemberHasNoGroup() {
        Member solo = new Member("Solo Artist");

        assertNull(solo.getGroup());
    }
}
