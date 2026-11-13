package com.photocard.domain;

public class DuplicateCardException extends Exception {

    public DuplicateCardException(String id) {
        super("A card with id " + " already exists.");
    }
}
