package com.example.Animalgram.repository;

import com.example.Animalgram.domain.subscribe.Subscribe;
import com.example.Animalgram.repository.querydsl.SubscribeRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SubscribeRepository extends JpaRepository<Subscribe, Integer>, SubscribeRepositoryCustom {

    //타입이 int 여서 Subscribe객체를 못만든다 그이유는 타입이 User오브젝트이기 떄문이다 그러므로 네이티브 쿼리 사용하기
    // ,createDate 를 넣은 이유는 Jpa에 있는 save등이 아니고 네이티브쿼리이기 때문이다. = createDate 직접적어줘야한다
    //nativeQuery = true 네이티브 쿼리는 이걸 꼭 넣어줘야 한다.
    @Modifying //insert,delete, updat 로 데이터베이스에 변경을 줄려면 네이티브 쿼리에는 Modifying이 필요하다
    @Query(value = "INSERT INTO subscribe(fromUserid, toUserid, createDate) VALUES(:fromUserid, :toUserid, now())", nativeQuery = true)
    void mSubscribe(int fromUserid, int toUserid); //구독하기 //1(변경된 행(인서트된 횟수)의 개수가 리턴된다),-1

    // (:) 을 붙인 이유는 파라미터를 바인딩하기 위해서
    @Modifying
    @Query(value = "DELETE FROM subscribe WHERE fromUserid = :fromUserid AND toUserid = :toUserid", nativeQuery = true)
    void mUnSubscribe(int fromUserid, int toUserid); //구독 취소하기 //1,-1

    // 구독상태
    // 이거는 select만 하는거여서 @Modifying 이 필요없다
    @Query(value = "select count(*) from subscribe where fromUserid= :principalid and touserid = :pageuserid", nativeQuery = true)
    int mSubscribeState(int principalid,int pageuserid);// 로그인한 사용자 아이디 , 그페이지 아이디

    // 구독수
    @Query(value = "select count(*) from subscribe where fromUserid = :pageUserid", nativeQuery = true)
    int mSubscribeCount(int pageUserid);
}
