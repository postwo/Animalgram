package com.example.Animalgram.service;

import com.example.Animalgram.dto.response.post.PostLikeResponse;

public interface LikeService {
    PostLikeResponse toggleLike(Long postId, String username);

    PostLikeResponse getLikeStatus(Long postId, String username);
}
