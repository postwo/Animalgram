package com.example.Animalgram.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TokenErrorCode implements ErrorCodeIfs{

    // --- 기존 오류 코드 ---
    INVALID_TOKEN(400 , 2000 , "유효하지 않은 토큰"),
    EXPIRED_TOKEN(400 , 2001 , "만료된 토큰"),
    TOKEN_EXCEPTION(400 , 2002 , "토큰 알수 없는 에러"),
    AUTHORIZATION_TOKEN_NOT_FOUND(400, 2003, "인증 헤더 토큰 없음"),
    TOKEN_REMOVE_FAIL(500, 2004, "블랙리스트 제거 실패"),
    TOKEN_ADD_FAIL(500, 2005, "블랙리스트 추가 실패"),

    // --- 새로 추가된 리프레시 토큰 및 인증 관련 오류 코드 ---
    REFRESH_TOKEN_NOT_FOUND(400, 2006, "리프레시 토큰이 없습니다."),
    INVALID_REFRESH_TOKEN(400, 2007, "유효하지 않은 리프레시 토큰입니다."),
    EXPIRED_REFRESH_TOKEN(400, 2008, "리프레시 토큰이 만료되었습니다. 다시 로그인해주세요."),
    AUTHORIZATION_HEADER_NOT_FOUND(400, 2009, "인증 헤더가 없습니다."),

    // --- JWT 예외 처리를 위한 새로운 오류 코드 (2010번대) ---
    MALFORMED_JWT_TOKEN(400, 2010, "형식이 잘못된 토큰"), // MalformedJwtException
    SIGNATURE_INVALID(400, 2011, "토큰 서명이 유효하지 않음"), // SignatureException 등을 포괄
    INVALID_CLAIM(400, 2012, "클레임 정보가 유효하지 않음"), // InvalidClaimException

    // JWTException과 그 외 일반 예외는 기존 코드를 활용하거나 새로운 코드를 사용할 수 있습니다.
    GENERAL_JWT_ERROR(400, 2013, "알 수 없는 JWT 처리 에러"), // JwtException
    GENERAL_SERVER_ERROR(500, 5000, "서버 내부 오류 발생"), // Exception e

    REFRESH_TOKEN_NOT_MATCH(401, 2014, "서버에 저장된 리프레시 토큰과 다릅니다."),
    ;

    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String description;
}