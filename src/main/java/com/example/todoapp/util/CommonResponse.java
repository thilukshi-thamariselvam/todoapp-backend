package com.example.todoapp.util;

import lombok.Data;

@Data
public class CommonResponse<T> {

    private boolean success;
    private String message;
    private T data;

    public CommonResponse() {}

    public CommonResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

}
