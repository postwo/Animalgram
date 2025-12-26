package com.example.Animalgram.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SubscribeErrorCode implements ErrorCodeIfs{

    SUBSCRIBE_ALREADY_EXISTS(409, 1501, "이미 구독을 하였습니다.");

    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String description;
}
