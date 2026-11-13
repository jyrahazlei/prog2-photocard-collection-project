package com.photocard.service;

import com.photocard.domain.AlbumCard;
import com.photocard.domain.Card;
import com.photocard.domain.LuckyDrawCard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CardFactoryTest {

    @Test
    void albumLineBuildsAnAlbumCardWithItsGroup() {
        Card card = CardFactory.fromCsvLine("ALBUM,A1,Hanbin Card,Hanbin,AHOF,5.0,OWNED,Album One");
        assertTrue(card instanceof AlbumCard);
        assertEquals("A1", card.getId());
        assertEquals("Hanbin", card.getMember().getName());
        assertEquals(5.0, card.getTradeValue());
    }

    @Test
    void luckyLineBuildsALuckyDrawCardForASoloMember() {
        Card card = CardFactory.fromCsvLine("LUCKY,L1,Solo Card,Mina,,10.0,WANTED,Fansign");
        assertTrue(card instanceof LuckyDrawCard);
        assertNull(card.getMember().getGroup());
        assertEquals(15.0, card.getTradeValue());
    }

    @Test
    void unknownTypeIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> CardFactory.fromCsvLine("POB,P1,Card,Hanbin,AHOF,5.0,OWNED,Shop"));
    }

    @Test
    void malformedLineIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> CardFactory.fromCsvLine("ALBUM,A1,Card"));
        assertThrows(IllegalArgumentException.class,
                () -> CardFactory.fromCsvLine("ALBUM,A1,Card,Hanbin,AHOF,abc,OWNED,Album One"));
    }
}