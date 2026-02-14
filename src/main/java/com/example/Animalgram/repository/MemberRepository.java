package com.example.Animalgram.repository;

import com.example.Animalgram.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
