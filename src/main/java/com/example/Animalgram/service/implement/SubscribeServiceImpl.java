package com.example.Animalgram.service.implement;

import com.example.Animalgram.common.error.SubscribeErrorCode;
import com.example.Animalgram.common.exception.ApiException;
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

    @Override
    @Transactional
    public void subscribe(int fromUserid, int toUserId) {
        try {
            subscribeRepository.mSubscribe(fromUserid,toUserId);
        }catch (Exception e){
            throw new ApiException(SubscribeErrorCode.SUBSCRIBE_ALREADY_EXISTS,e);
        }
    }

    @Override
    @Transactional
    public void cancleSubscribe(int fromUserid, int toUserId) {
        subscribeRepository.mUnSubscribe(fromUserid, toUserId);
    }
}
