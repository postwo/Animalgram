package com.example.Animalgram.dto.subscribe;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubscribeResponse {
    private int id; //누군가의 정보 구독할 취소 할 id값
    private String username;
    private String profileImageUrl; //모달창 구독한 유저 사진

    //MySQL에서 COUNT 같은 연산 결과를 반환 할떄는 BigInteger를 활용해야 한다
    private BigInteger subscribeState; //구독상태
    private BigInteger equalUserState; //로그인한 사용자의 동일인 여부
}
