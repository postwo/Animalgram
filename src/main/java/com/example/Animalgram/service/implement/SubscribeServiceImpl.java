package com.example.Animalgram.service.implement;

import com.example.Animalgram.domain.member.Member;
import com.example.Animalgram.dto.subscribe.SubscribeResponse;
import com.example.Animalgram.repository.SubscribeRepository;
import com.example.Animalgram.service.SubscribeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscribeServiceImpl implements SubscribeService {

    private final SubscribeRepository subscribeRepository;

    @Override
    @Transactional(readOnly = true)
    public List<SubscribeResponse> subscribeList(Member member, int pageUserId) {
        int principalId = member.getId();
        return subscribeRepository.findSubscribeList(principalId, pageUserId);
    }
}
