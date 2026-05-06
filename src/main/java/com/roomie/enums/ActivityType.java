package com.roomie.enums;

public enum ActivityType {
    CHORE_COMPLETED(1),
    CHORE_CREATED(2),
    // GROCERY_ITEM_ADDED(3),
    // GROCERY_ITEM_PURCHASED(4),
    // GROCERY_LIST_CREATED(5),
    EXPENSE_CREATED(3),
    BILL_PAID(4),
    MEMBER_JOINED(5),
    MEMBER_LEFT(6);

    private final int value;

    ActivityType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
