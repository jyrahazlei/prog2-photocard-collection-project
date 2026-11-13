package com.photocard.persistence;

import com.photocard.domain.Card;
import com.photocard.domain.DuplicateCardException;
import com.photocard.service.CardFactory;
import com.photocard.service.Collection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CardStorage {

    public static void save(Collection collection, Path path) throws IOException {
        Path parent = path.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (Card card : collection.getCards()) {
                writer.write(card.toCsvLine());
                writer.newLine();
            }
        }
    }

    public static Collection load(Path path) throws IOException {
        Collection collection = new Collection();
        if (!Files.exists(path)) {
            return collection;
        }
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.isBlank()) {
                    continue;
                }
                try {
                    collection.add(CardFactory.fromCsvLine(line));
                } catch (IllegalArgumentException | DuplicateCardException e) {
                    throw new IOException("Line " + lineNumber + ": " + e.getMessage(), e);
                }
            }
        }
        return collection;
    }
}