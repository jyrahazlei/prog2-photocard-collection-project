package com.photocard.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.photocard.domain.AlbumCard;
import com.photocard.domain.CardStatus;
import com.photocard.domain.DuplicateCardException;
import com.photocard.domain.LuckyDrawCard;
import com.photocard.domain.Member;
import org.junit.jupiter.api.Test;

class CollectionTest {

    private final Member member = new Member("Hanbin");

    @Test
    void addedCardCanBeFound() throws Exception {
        Collection collection = new Collection();
        collection.add(new AlbumCard("A1", "Album card", member, 10, CardStatus.OWNED, "Album One"));
        assertTrue(collection.findById("A1").isPresent());
        assertFalse(collection.findById("nope").isPresent());
    }

    @Test
    void duplicateIdIsRejected() throws Exception {
        Collection collection = new Collection();
        collection.add(new AlbumCard("A1", "Album card", member, 10, CardStatus.OWNED, "Album One"));
        assertThrows(DuplicateCardException.class, () ->
                collection.add(new LuckyDrawCard("A1", "Other card", member, 20, CardStatus.OWNED, "Fansign")));
    }

    @Test
    void removeDeletesOnlyExistingCards() throws Exception {
        Collection collection = new Collection();
        collection.add(new AlbumCard("A1", "Album card", member, 10, CardStatus.OWNED, "Album One"));
        assertTrue(collection.remove("A1"));
        assertFalse(collection.remove("A1"));
    }

    @Test
    void totalValueUsesEachCardsOwnTradeValue() throws Exception {
        Collection collection = new Collection();
        collection.add(new AlbumCard("A1", "Album card", member, 10, CardStatus.OWNED, "Album One"));
        collection.add(new LuckyDrawCard("L1", "Lucky card", member, 20, CardStatus.OWNED, "Fansign"));
        assertEquals(40.0, collection.getTotalValue(), 0.001);
    }
}