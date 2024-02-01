package com.example.mokkoji.common;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

// 클라이언트한테 API 요청 처리상태를 알려줌
@Setter
@Getter
@AllArgsConstructor
public class ApiResponseHeader {
    private int code;
    private String message;
}