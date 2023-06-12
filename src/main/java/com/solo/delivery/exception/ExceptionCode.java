package com.solo.delivery.exception;

import lombok.Getter;

public enum ExceptionCode {
    MEMBER_NOT_FOUND(404, "Member not found"),
    MEMBER_EXISTS(409, "Member exists"),
    STORE_NOT_FOUND(404, "Store not found"),
    STORE_CATEGORY_NOT_FOUND(404, "StoreCategory not found"),
    ITEM_NOT_FOUND(404, "Item not found"),
    OUT_OF_STOCK(400, "Out of stock"),
    REVIEW_NOT_FOUND(404, "Review not found"),
    REVIEW_CANNOT_CHANGE(409, "Review can not change"),
    ORDER_NOT_FOUND(404, "Order not found"),
    ORDER_CANNOT_CHANGE(409, "Order can not change");

    @Getter
    private int status;

    @Getter
    private String message;

    ExceptionCode(int code, String message){
        this.status = code;
        this.message = message;
    }
}
