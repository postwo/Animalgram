package com.example.Animalgram.repository.querydsl;

import com.example.Animalgram.dto.subscribe.SubscribeResponse;

import java.util.List;

public interface SubscribeRepositoryCustom {
    List<SubscribeResponse> findSubscribeList(int principalId, int pageUserId);
}
