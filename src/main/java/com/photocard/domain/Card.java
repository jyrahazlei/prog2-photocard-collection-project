package com.photocard.domain;

public abstract class Card {

    private final String id;
    private final String name;
    private final Member member;
    private final double price;
    private CardStatus status;

protected Card(String id, String name, Member member, double price, CardStatus status) {
    if (id == null || id.isBlank()) {
        throw new IllegalArgumentException("Card id must not be blank.");
    }
    if (name == null || name.isBlank()) {
        throw new IllegalArgumentException("Card name must not be blank.");
    }
    if (member == null) {
        throw new IllegalArgumentException("Member must not be null.");
    }
    if (price < 0) {
        throw new IllegalArgumentException("Price must not be negative.");
    }
    if (status == null) {
        throw new IllegalArgumentException("Status must not be null.");
    }
    this.id = id;
    this.name = name;
    this.member = member;
    this.price = price;
    this.status = status;
}
    public String getId () {
        return id;
    }
    public String getName () {
        return name;
    }
    public Member getMember () {
        return member;
    }
    public double getPrice () {
        return price;
    }
    public CardStatus getStatus () {
        return status;
    }

    public void setStatus (CardStatus status){
        if (status == null) {
            throw new IllegalArgumentException("Status must not be null.");
        }
        this.status = status;
    }

    public abstract double getTradeValue ();
}


