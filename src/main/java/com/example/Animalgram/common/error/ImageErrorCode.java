package com.example.Animalgram.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ImageErrorCode implements ErrorCodeIfs {
    FILE_UPLOAD_FAILED(500, 560, "프로필 이미지 업로드 실패"),
    IMAGE_NOT_ATTACHED(400, 561, "이미지가 첨부되지 않았습니다");

    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String description;
}
