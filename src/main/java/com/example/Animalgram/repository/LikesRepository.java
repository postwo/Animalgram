package com.example.Animalgram.repository;

import com.example.Animalgram.domain.likes.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<Likes, Integer> {
}
