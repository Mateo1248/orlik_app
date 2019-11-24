package com.orlikteam.orlikbackend.rating.entity;

import com.orlikteam.orlikbackend.pitch.Pitch;
import com.orlikteam.orlikbackend.user.User;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Entity
public class Rating {
    
    @Id
    @GeneratedValue
    private Integer ratingId;
    
    @ManyToOne
    @JoinColumn(name = "pitch_id", referencedColumnName = "pitch_id")
    private Pitch pitchId;
    
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_login")
    private User userId;
    
    @Min(1)
    @Max(5)
    private Integer value;
}
