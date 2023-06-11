package com.solo.delivery.exception;

import lombok.Getter;

public enum ExceptionCode {
    MEMBER_NOT_FOUND(404, "Member not found"),
    MEMBER_EXISTS(409, "Member exists"),
    STORE_NOT_FOUND(404, "Store not found"),
    STORE_CATEGORY_NOT_FOUND(404, "StoreCategory not found"),
    ITEM_NOT_FOUND(404, "Item not found"),
    REVIEW_NOT_FOUND(404, "Review not found"),
    REVIEW_CANNOT_CHANGE(403, "Review can not change");

    @Getter
    private int status;

    @Getter
    private String message;

    ExceptionCode(int code, String message){
        this.status = code;
        this.message = message;
    }
}
