package com.photocard.service;

import com.photocard.domain.AlbumCard;
import com.photocard.domain.Card;
import com.photocard.domain.CardStatus;
import com.photocard.domain.Group;
import com.photocard.domain.LuckyDrawCard;
import com.photocard.domain.Member;

public class CardFactory {

    private static final int FIELD_COUNT = 8;

    public static Card fromCsvLine(String line) {
        if (line == null || line.isBlank()) {
            throw new IllegalArgumentException("The line is empty.");
        }
        String[] parts = line.split(",", -1);
        if (parts.length != FIELD_COUNT) {
            throw new IllegalArgumentException(
                    "Expected " + FIELD_COUNT + " fields but found " + parts.length + ": " + line);
        }

        String type = parts[0].trim();
        String id = parts[1].trim();
        String name = parts[2].trim();
        String memberName = parts[3].trim();
        String groupName = parts[4].trim();
        double price = parsePrice(parts[5].trim());
        CardStatus status = parseStatus(parts[6].trim());
        String extra = parts[7].trim();

        Member member = groupName.isEmpty()
                ? new Member(memberName)
                : new Member(memberName, new Group(groupName));

        return switch (type) {
            case "ALBUM" -> new AlbumCard(id, name, member, price, status, extra);
            case "LUCKY" -> new LuckyDrawCard(id, name, member, price, status, extra);
            default -> throw new IllegalArgumentException("Unknown card type: " + type);
        };
    }

    private static double parsePrice(String text) {
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Price is not a number: " + text);
        }
    }

    private static CardStatus parseStatus(String text) {
        try {
            return CardStatus.valueOf(text);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown status: " + text);
        }
    }
}