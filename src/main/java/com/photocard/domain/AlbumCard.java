package com.photocard.domain;

public class AlbumCard extends Card {

    private static final double TRADE_MULTIPLIER = 1.0;

    private final String albumName;

    public AlbumCard(String id, String name, Member member, double price, CardStatus status, String albumName) {
        super(id, name, member, price, status);
        if (albumName == null || albumName.isBlank()) {
            throw new IllegalArgumentException("Album name must not be blank.");
        }
        this.albumName = albumName;
    }

    public String getAlbumName() {
        return albumName;
    }

    @Override
    public double getTradeValue() {
        return getPrice() * TRADE_MULTIPLIER;
    }
}
