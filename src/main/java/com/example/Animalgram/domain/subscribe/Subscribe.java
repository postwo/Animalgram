package com.example.Animalgram.domain.subscribe;

import com.example.Animalgram.domain.member.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(
        name="subscribe",
        uniqueConstraints={
                @UniqueConstraint(
                        name = "subscribe_uk",
                        columnNames={"fromUserId","toUserId"}
                )
        }
)
public class Subscribe { //many

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    //@Column( unique = true) 이거는 유니크 제약조건을 하나만 걸때만 이렇게 사용
    @JoinColumn(name = "fromUserId") //카멜표기법으로 변경 , 이렇게 컬럼명을 만들어
    @ManyToOne
    private Member fromMember;  // ~~로부터  (1)  구독하는 유저

    @JoinColumn(name = "toUserId")
    @ManyToOne
    private Member toMember; // ~~를  (3) 구독 받는 유저



}
