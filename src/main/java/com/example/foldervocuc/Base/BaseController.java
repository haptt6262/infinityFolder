package com.example.foldervocuc.Base;

import org.springframework.http.ResponseEntity;

public class BaseController {
    protected ResponseEntity<BaseResponse> successApi(Object data, String message) {
        return ResponseEntity.ok(new BaseResponse(false, message, data));
    }

    protected ResponseEntity<BaseResponse> errorApi(String message) {
        return ResponseEntity.ok(new BaseResponse(true, message, null));
    }
}
