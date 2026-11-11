package com.photocard.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CardTest {

    @Test
    void tradeValueDependsOnCardType() {
        Card album = new AlbumCard("A1", "Card One", "Member One", 10.0, CardStatus.OWNED, "Album X");
        Card lucky = new LuckyDrawCard("L1", "Card Two", "Member One", 10.0, CardStatus.OWNED, "Event Y");

        assertEquals(10.0, album.getTradeValue(), 0.001);
        assertEquals(15.0, lucky.getTradeValue(), 0.001);
    }

    @Test
    void negativePriceIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new AlbumCard("A2", "Card Three", "Member One", -5.0, CardStatus.OWNED, "Album X"));
    }
}
