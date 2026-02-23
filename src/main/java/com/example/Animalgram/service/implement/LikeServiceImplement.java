package com.example.Animalgram.service.implement;

import com.example.Animalgram.common.error.MemberErrorCode;
import com.example.Animalgram.common.error.PostErrorCode;
import com.example.Animalgram.common.exception.ApiException;
import com.example.Animalgram.domain.likes.Likes;
import com.example.Animalgram.dto.response.post.PostLikeResponse;
import com.example.Animalgram.repository.LikesRepository;
import com.example.Animalgram.repository.MemberRepository;
import com.example.Animalgram.repository.PostRepository;
import com.example.Animalgram.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeServiceImplement implements LikeService {

    private final LikesRepository likesRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    @Override
    @Transactional
    public PostLikeResponse toggleLike(Long postId, String username) {
        var member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(MemberErrorCode.MEMBER_NOT_FOUND, "Member not found."));
        var post = postRepository.findById(postId)
                .orElseThrow(() -> new ApiException(PostErrorCode.POST_NOT_FOUND));

        boolean liked;
        var existingLike = likesRepository.findByPost_IdAndMember_Id(postId, member.getId());
        if (existingLike.isPresent()) {
            likesRepository.delete(existingLike.get());
            liked = false;
        } else {
            likesRepository.save(Likes.builder()
                    .post(post)
                    .member(member)
                    .build());
            liked = true;
        }

        int likeCount = likesRepository.countByPost_Id(postId);
        return PostLikeResponse.of(postId, liked, likeCount);
    }

    @Override
    @Transactional(readOnly = true)
    public PostLikeResponse getLikeStatus(Long postId, String username) {
        var member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(MemberErrorCode.MEMBER_NOT_FOUND, "Member not found."));
        if (!postRepository.existsById(postId)) {
            throw new ApiException(PostErrorCode.POST_NOT_FOUND);
        }

        boolean liked = likesRepository.existsByPost_IdAndMember_Id(postId, member.getId());
        int likeCount = likesRepository.countByPost_Id(postId);

        return PostLikeResponse.of(postId, liked, likeCount);
    }
}
