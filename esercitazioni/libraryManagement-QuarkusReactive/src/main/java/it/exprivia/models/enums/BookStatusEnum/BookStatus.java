package it.exprivia.models.enums.BookStatusEnum;

public enum BookStatus {
    AVAILABLE("Available", "The book is available for borrowing"),
    RESERVED("Reserved", "The book is reserved by a user"),
    DAMAGED("Damaged", "The book is damaged and cannot be borrowed"),
    OUT_OF_STOCK("Out of Stock", "The book is currently out of stock");

    private final String displayName;
    private final String description;

    BookStatus(String displayName, String description){
        this.displayName = displayName;
        this.description = description;
    }

    // GETTER, ENUM IMMUTABILI
    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}
