package com.photocard.service;

import com.photocard.domain.Card;
import com.photocard.domain.DuplicateCardException;
import com.photocard.domain.CardStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Collection {

    private final List<Card> cards = new ArrayList<>();

    public void add(Card card) throws DuplicateCardException {
        if (card == null) {
            throw new IllegalArgumentException("Card must not be null.");
        }
        if (findById(card.getId()).isPresent()) {
            throw new DuplicateCardException(card.getId());
        }
        cards.add(card);
    }

    public Optional<Card> findById(String id) {
        for (Card card : cards) {
            if (card.getId().equals(id)) {
                return Optional.of(card);
            }
        }
        return Optional.empty();
    }

    public boolean remove(String id) {
        return cards.removeIf(card -> card.getId().equals(id));
    }

    public double getTotalValue() {
        double total = 0;
        for (Card card : cards) {
            total += card.getTradeValue();
        }
        return total;
    }

    public List<Card> getByStatus(CardStatus status) {
        List<Card> result = new ArrayList<>();
        for (Card card : cards) {
            if (card.getStatus() == status) {
                result.add(card);
            }
        }
        return result;
    }

    public List<Card> getWishlist() {
        return getByStatus(CardStatus.WANTED);
    }

    public List<Card> getCards() {
        return Collections.unmodifiableList(cards);
    }
}
