package com.example.foldervocuc.Base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse {
    boolean error;
    String message;
    Object data;

    public BaseResponse(boolean error, String message) {
        this.error = error;
        this.message = message;
    }

    public BaseResponse(String message) {
        this.message = message;
    }
}
