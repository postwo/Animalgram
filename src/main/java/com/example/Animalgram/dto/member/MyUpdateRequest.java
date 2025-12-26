package com.example.Animalgram.dto.member;

import com.example.Animalgram.domain.member.Member;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MyUpdateRequest {
    @NotBlank
    private String memberName; //필수
    @NotBlank
    private String password; //필수

    private String website;
    private String bio;


   public Member myUpdate(){
       return Member.builder()
               .memberName(memberName)// 이름을 기재 안했으면 공백으로 데이터베이스에 들어가게 된다, validation 체크를 해주면 된다.
               .password(password) //패스워드를 기재 안했으면 공백으로 데이터베이스에 들어가게 된다, validation 체크를 해주면 된다.
               .website(website)
               .bio(bio)
               .build();
   }
}
