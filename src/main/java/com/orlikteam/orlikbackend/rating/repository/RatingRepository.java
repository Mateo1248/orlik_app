package com.orlikteam.orlikbackend.rating.repository;

import com.orlikteam.orlikbackend.pitch.Pitch;
import com.orlikteam.orlikbackend.rating.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Integer> {

    List<Rating> findAllByPitchId(Pitch pitchId);
}
