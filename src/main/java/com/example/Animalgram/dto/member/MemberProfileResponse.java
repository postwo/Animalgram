package com.example.Animalgram.dto.member;

import com.example.Animalgram.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberProfileResponse {
    private boolean pageOwnerState; // 마이페이지에 주인인지 아닌지 여부를 가르킨다 //jsp 에서 변수명 앞에다가 is를 붙이면 파싱이 안되다
    private int imageCount; // 이미지 수
    private boolean subscribeState; // 구독을 한 상태인지
    private int subscribeCount;
    private Member member;
}
