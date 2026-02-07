package com.example.Animalgram.domain.subscribe;

import com.example.Animalgram.domain.member.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(of = "id", callSuper = false)
@Entity
@Table(
        name="subscribe",
        uniqueConstraints={
                @UniqueConstraint(
                        name = "subscribe_uk",
                        columnNames={"fromMemberId","toMemberId"}
                )
        }
)
public class Subscribe { //many

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@Column( unique = true) 이거는 유니크 제약조건을 하나만 걸때만 이렇게 사용
    @JoinColumn(name = "fromMemberId") //카멜표기법으로 변경 , 이렇게 컬럼명을 만들어
    @ManyToOne
    private Member fromMember;  // ~~로부터  (1)  구독하는 유저

    @JoinColumn(name = "toMemberId")
    @ManyToOne
    private Member toMember; // ~~를  (3) 구독 받는 유저

    private LocalDateTime createDate;

    @PrePersist //데이터베이스 인서트되기 직전에 실행 //이거는 jpa save등을 사용할때 적용 네이티브쿼리를 사용하면 적용이 안되다.
    public void prePersist() {
        this.createDate = LocalDateTime.now();
    }


}
