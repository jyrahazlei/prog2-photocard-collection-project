package com.photocard.ui;

import com.photocard.domain.AlbumCard;
import com.photocard.domain.Card;
import com.photocard.domain.CardStatus;
import com.photocard.domain.DuplicateCardException;
import com.photocard.domain.Group;
import com.photocard.domain.LuckyDrawCard;
import com.photocard.domain.Member;
import com.photocard.persistence.CardStorage;
import com.photocard.service.Collection;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Text menu for the photocard collection. It only talks to the user:
 * the rules live in Collection, Card and CardFactory, and the file work in CardStorage.
 */
public class ConsoleMenu {

    private final Collection collection;
    private final Path file;
    private final Scanner in;
    private final PrintStream out;

    public ConsoleMenu(Collection collection, Path file, InputStream input, PrintStream out) {
        this.collection = collection;
        this.file = file;
        this.in = new Scanner(input);
        this.out = out;
    }

    public void run() {
        try {
            boolean running = true;
            while (running) {
                printMenu();
                String choice = ask("Choose 1-6: ");
                switch (choice) {
                    case "1" -> addCard();
                    case "2" -> removeCard();
                    case "3" -> listCards();
                    case "4" -> showWishlist();
                    case "5" -> showTotalValue();
                    case "6" -> running = false;
                    default -> out.println("Please type a number from 1 to 6.");
                }
            }
            out.println("Goodbye!");
        } catch (NoSuchElementException e) {
            // input ended (for example Ctrl+D): leave quietly
            out.println();
            out.println("Input ended. Goodbye!");
        }
    }

    private void printMenu() {
        out.println();
        out.println("=== Photocard Collection ===");
        out.println("1. Add a card");
        out.println("2. Remove a card");
        out.println("3. List cards (sorted)");
        out.println("4. Show wishlist");
        out.println("5. Show total trade value");
        out.println("6. Exit");
    }

    // ---- menu actions ----

    private void addCard() {
        String type = ask("Card type (A = album, L = lucky draw): ").toUpperCase(Locale.ROOT);
        if (!type.equals("A") && !type.equals("L")) {
            out.println("Unknown card type. Use A or L.");
            return;
        }
        try {
            String id = askText("Card id: ");
            String name = askText("Card name: ");
            String memberName = askText("Member name: ");
            String groupName = ask("Group name (leave empty for a solo artist): ");
            if (groupName.contains(",")) {
                out.println("Commas are not allowed in names.");
                return;
            }
            double price = askPrice();
            CardStatus status = askStatus();
            String extra = askText(type.equals("A") ? "Album name: " : "Event name: ");

            Member member = groupName.isBlank()
                    ? new Member(memberName)
                    : new Member(memberName, new Group(groupName));
            Card card = type.equals("A")
                    ? new AlbumCard(id, name, member, price, status, extra)
                    : new LuckyDrawCard(id, name, member, price, status, extra);

            collection.add(card);
            out.println("Added: " + describe(card));
            save();
        } catch (InvalidInput e) {
            out.println(e.getMessage());
        } catch (DuplicateCardException e) {
            out.println(e.getMessage());
        } catch (IllegalArgumentException e) {
            out.println("Could not add the card: " + e.getMessage());
        }
    }

    private void removeCard() {
        String id = ask("Id of the card to remove: ");
        if (collection.remove(id)) {
            out.println("Removed card " + id + ".");
            save();
        } else {
            out.println("No card with id " + id + ".");
        }
    }

    private void listCards() {
        out.println("Sort by: 1 = member, 2 = name, 3 = trade value (highest first)");
        Comparator<Card> comparator;
        switch (ask("Choose 1-3: ")) {
            case "1" -> comparator = Collection.BY_MEMBER;
            case "2" -> comparator = Collection.BY_NAME;
            case "3" -> comparator = Collection.BY_TRADE_VALUE;
            default -> {
                out.println("Please type 1, 2 or 3.");
                return;
            }
        }
        printCards(collection.getSortedBy(comparator));
    }

    private void showWishlist() {
        printCards(collection.getWishlist());
    }

    private void showTotalValue() {
        out.println(String.format(Locale.ROOT, "Total trade value: %.2f", collection.getTotalValue()));
    }

    // ---- helpers ----

    private void save() {
        try {
            CardStorage.save(collection, file);
        } catch (IOException e) {
            out.println("Warning: could not save to " + file + ": " + e.getMessage());
        }
    }

    private void printCards(List<Card> cards) {
        if (cards.isEmpty()) {
            out.println("(no cards)");
            return;
        }
        for (Card card : cards) {
            out.println(describe(card));
        }
    }

    private String describe(Card card) {
        String group = card.getMember().getGroup() == null ? "solo" : card.getMember().getGroup().getName();
        return String.format(Locale.ROOT, "%s | %s | %s (%s) | %s | price %.2f | trade value %.2f",
                card.getId(), card.getName(), card.getMember().getName(), group,
                card.getStatus(), card.getPrice(), card.getTradeValue());
    }

    private String ask(String prompt) {
        out.print(prompt);
        out.flush();
        return in.nextLine().trim();
    }

    private String askText(String prompt) {
        String text = ask(prompt);
        if (text.isBlank()) {
            throw new InvalidInput("This field must not be empty.");
        }
        if (text.contains(",")) {
            throw new InvalidInput("Commas are not allowed in names.");
        }
        return text;
    }

    private double askPrice() {
        String text = ask("Price: ");
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            throw new InvalidInput("Price is not a number: " + text);
        }
    }

    private CardStatus askStatus() {
        String text = ask("Status (OWNED or WANTED): ").toUpperCase(Locale.ROOT);
        try {
            return CardStatus.valueOf(text);
        } catch (IllegalArgumentException e) {
            throw new InvalidInput("Status must be OWNED or WANTED.");
        }
    }

    /** Thrown for bad typed input; caught in addCard, which prints the message and returns to the menu. */
    private static class InvalidInput extends RuntimeException {
        InvalidInput(String message) {
            super(message);
        }
    }
}
