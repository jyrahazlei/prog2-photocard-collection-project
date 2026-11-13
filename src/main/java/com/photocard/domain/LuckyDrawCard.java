package com.photocard.domain;

public class LuckyDrawCard extends Card {

    private static final double TRADE_MULTIPLIER = 1.5;

    private final String eventName;

    public LuckyDrawCard(String id, String name, Member member, double price, CardStatus status, String eventName) {
        super(id, name, member, price, status);
        if (eventName == null || eventName.isBlank()) {
            throw new IllegalArgumentException("Event name must not be blank.");
        }
        this.eventName = eventName;
    }

    public String getEventName() {
        return eventName;
    }

    @Override
    public double getTradeValue() {
        return getPrice() * TRADE_MULTIPLIER;
    }

    @Override
    public String toCsvLine() {
        return buildCsvLine("LUCKY", eventName);
    }
}