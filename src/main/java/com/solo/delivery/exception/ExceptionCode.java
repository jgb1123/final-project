package com.solo.delivery.exception;

import lombok.Getter;

public enum ExceptionCode {
    MEMBER_NOT_FOUND(404, "Member not found"),
    MEMBER_EXISTS(409, "Member exists"),
    ROLE_CANNOT_CHANGE(409, "Role can not change"),
    STORE_NOT_FOUND(404, "Store not found"),
    STORE_CATEGORY_NOT_FOUND(404, "StoreCategory not found"),
    ITEM_NOT_FOUND(404, "Item not found"),
    ITEM_CANNOT_CHANGE(409, "Item can not change"),
    OUT_OF_STOCK(400, "Out of stock"),
    REVIEW_NOT_FOUND(404, "Review not found"),
    REVIEW_CANNOT_CHANGE(409, "Review can not change"),
    ORDER_NOT_FOUND(404, "Order not found"),
    ORDER_CANNOT_CHANGE(409, "Order can not change"),
    ONLY_ITEMS_FROM_SAME_STORE(400, "Only items from the same store can be ordered"),
    CART_NOT_FOUND(404, "Cart not found");

    @Getter
    private int status;

    @Getter
    private String message;

    ExceptionCode(int code, String message){
        this.status = code;
        this.message = message;
    }
}
