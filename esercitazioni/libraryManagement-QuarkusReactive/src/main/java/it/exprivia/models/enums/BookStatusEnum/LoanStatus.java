package it.exprivia.models.enums.BookStatusEnum;

public enum LoanStatus {
    ACTIVE("Active", "The loan is currently active and the book is borrowed."),
    RETURNED("Returned", "The loan has been completed and the book has been returned."),
    OVERDUE("Overdue", "The loan is overdue and the book has not been returned within the expected time frame."),
    PENDING("Pending", "The loan is pending and has not been finalized yet.");

    private final String displayName;
    private final String description;

    LoanStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}
