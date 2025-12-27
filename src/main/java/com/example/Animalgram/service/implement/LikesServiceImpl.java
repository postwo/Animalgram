package com.example.Animalgram.service.implement;

import com.example.Animalgram.repository.LikesRepository;
import com.example.Animalgram.service.LikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikesServiceImpl implements LikesService {
    private final LikesRepository likesRepository;
}
