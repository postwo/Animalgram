package com.example.Animalgram.service;

import com.example.Animalgram.domain.member.Member;
import com.example.Animalgram.dto.subscribe.SubscribeResponse;

import java.util.List;

public interface SubscribeService {
    List<SubscribeResponse> subscribeList(Member member, int pageUserId);

    void subscribe(int id, int toUserId);

    void cancleSubscribe(int id, int toUserId);
}
