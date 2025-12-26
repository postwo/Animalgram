package com.example.Animalgram.dto.member;


import com.example.Animalgram.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyUpdateResponse {

    private String memberName; //필수

    private String password; //필수

    private String website;
    private String bio;

    public static MyUpdateResponse myProfileUpdate(Member member){
        return MyUpdateResponse.builder()
                .memberName(member.getMemberName())
                .password(member.getPassword())
                .website(member.getWebsite())
                .bio(member.getBio())
                .build();
    }
}
