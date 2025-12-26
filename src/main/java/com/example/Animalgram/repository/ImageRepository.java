package com.example.Animalgram.repository;

import com.example.Animalgram.domain.image.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image,Integer> {

    @Query(value ="select i.* from image i inner join (select imageId, count(imageId) likeCount from likes group by imageId) c on i.id = c.imageid order by likecount desc ",nativeQuery = true)
    List<Image> mPopular();
}
