package com.photocard;

import com.photocard.persistence.CardStorage;
import com.photocard.service.Collection;
import com.photocard.ui.ConsoleMenu;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    private static final Path DATA_FILE = Paths.get("data", "collection.csv");

    public static void main(String[] args) {
        Collection collection;
        try {
            collection = CardStorage.load(DATA_FILE);
        } catch (IOException e) {
            // Stop here: starting empty would let the next save overwrite the broken file.
            System.out.println("Could not load " + DATA_FILE + ": " + e.getMessage());
            System.out.println("Fix or delete the file, then start again.");
            return;
        }
        new ConsoleMenu(collection, DATA_FILE, System.in, System.out).run();
    }
}
