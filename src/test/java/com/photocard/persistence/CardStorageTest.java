package com.photocard.persistence;

import com.photocard.domain.AlbumCard;
import com.photocard.domain.Card;
import com.photocard.domain.CardStatus;
import com.photocard.domain.Group;
import com.photocard.domain.LuckyDrawCard;
import com.photocard.domain.Member;
import com.photocard.service.Collection;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CardStorageTest {

    @TempDir
    Path tempDir;

    @Test
    void saveThenLoadGivesTheSameCards() throws Exception {
        Collection original = new Collection();
        Member hanbin = new Member("Hanbin", new Group("AHOF"));
        Member mina = new Member("Mina");
        original.add(new AlbumCard("A1", "Hanbin Card", hanbin, 5.0, CardStatus.OWNED, "Album One"));
        original.add(new LuckyDrawCard("L1", "Solo Card", mina, 10.0, CardStatus.WANTED, "Fansign"));

        Path file = tempDir.resolve("collection.csv");
        CardStorage.save(original, file);
        Collection loaded = CardStorage.load(file);

        assertEquals(2, loaded.getCards().size());
        Card album = loaded.findById("A1").orElseThrow();
        assertTrue(album instanceof AlbumCard);
        assertEquals("AHOF", album.getMember().getGroup().getName());
        assertEquals(CardStatus.OWNED, album.getStatus());
        Card lucky = loaded.findById("L1").orElseThrow();
        assertTrue(lucky instanceof LuckyDrawCard);
        assertNull(lucky.getMember().getGroup());
        assertEquals(15.0, lucky.getTradeValue());
    }

    @Test
    void missingFileGivesAnEmptyCollection() throws Exception {
        Collection loaded = CardStorage.load(tempDir.resolve("does-not-exist.csv"));

        assertTrue(loaded.getCards().isEmpty());
    }

    @Test
    void brokenLineGivesAClearErrorWithTheLineNumber() throws Exception {
        Path file = tempDir.resolve("broken.csv");
        Files.writeString(file,
                "ALBUM,A1,Hanbin Card,Hanbin,AHOF,5.0,OWNED,Album One\n"
                        + "ALBUM,A2,only,three\n");

        IOException error = assertThrows(IOException.class, () -> CardStorage.load(file));

        assertTrue(error.getMessage().startsWith("Line 2"));
    }
}