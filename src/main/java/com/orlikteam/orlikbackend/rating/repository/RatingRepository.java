package com.orlikteam.orlikbackend.rating.repository;

import com.orlikteam.orlikbackend.rating.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Integer> {
}
