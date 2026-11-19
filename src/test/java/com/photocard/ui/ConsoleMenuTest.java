package com.photocard.ui;

import com.photocard.persistence.CardStorage;
import com.photocard.service.Collection;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConsoleMenuTest {

    private String run(Collection collection, Path file, String input) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        new ConsoleMenu(collection, file, new ByteArrayInputStream(input.getBytes()),
                new PrintStream(bytes)).run();
        return bytes.toString();
    }

    @Test
    void addingACardShowsItAndSavesTheFile(@TempDir Path dir) throws Exception {
        Path file = dir.resolve("collection.csv");
        Collection collection = new Collection();

        String output = run(collection, file,
                "1\nA\nA1\nJL Card\nJL\nAHOF\n5.0\nOWNED\nAlbum One\n3\n1\n6\n");

        assertEquals(1, collection.getCards().size());
        assertTrue(output.contains("A1 | JL Card | JL (AHOF)"));
        assertEquals(1, CardStorage.load(file).getCards().size());
    }

    @Test
    void badInputGivesAMessageAndTheMenuKeepsRunning(@TempDir Path dir) {
        Collection collection = new Collection();

        String output = run(collection, dir.resolve("collection.csv"),
                "9\n1\nA\nA1\nCard\nMina\n\nabc\n1\nA\nA2\nCard\nMina\n\n-5\nOWNED\nX\n6\n");

        assertTrue(output.contains("Please type a number from 1 to 6."));
        assertTrue(output.contains("Price is not a number: abc"));
        assertTrue(output.contains("Price must not be negative."));
        assertEquals(0, collection.getCards().size());
    }

    @Test
    void duplicateIdIsReportedAndNotAdded(@TempDir Path dir) {
        Collection collection = new Collection();
        String oneCard = "1\nL\nL1\nSolo Card\nMina\n\n10\nWANTED\nFansign\n";

        String output = run(collection, dir.resolve("collection.csv"), oneCard + oneCard + "6\n");

        assertEquals(1, collection.getCards().size());
        assertTrue(output.contains("A card with id L1 already exists."));
    }
}
