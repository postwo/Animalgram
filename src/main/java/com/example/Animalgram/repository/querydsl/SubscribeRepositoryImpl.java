package com.example.Animalgram.repository.querydsl;

import com.example.Animalgram.domain.member.QMember;
import com.example.Animalgram.domain.subscribe.QSubscribe;
import com.example.Animalgram.dto.subscribe.SubscribeResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class SubscribeRepositoryImpl implements SubscribeRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    public List<SubscribeResponse> findSubscribeList(int principalId, int pageMemberId) {
        QMember member = QMember.member;
        QSubscribe subscribe = QSubscribe.subscribe;
        QSubscribe subQuery = new QSubscribe("subQuery");

        return queryFactory
                .select(Projections.constructor(SubscribeResponse.class,
                        member.id,
                        member.memberName,
                        member.profileImageUrl,
                        // subscribeState: 로그인한 유저가 해당 유저를 구독하는지
                        JPAExpressions
                                .selectOne()
                                .from(subQuery)
                                .where(
                                        subQuery.fromMember.id.eq(principalId)
                                                .and(subQuery.toMember.id.eq(member.id))
                                )
                                .exists(),
                        // equalUserState: 로그인한 유저와 같은 유저인지
                        member.id.eq(principalId)
                ))
                .from(member)
                .innerJoin(subscribe).on(member.id.eq(subscribe.toMember.id))
                .where(subscribe.fromMember.id.eq(pageMemberId))
                .fetch();
    }
}
