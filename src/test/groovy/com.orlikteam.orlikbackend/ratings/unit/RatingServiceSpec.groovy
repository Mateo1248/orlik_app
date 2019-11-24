package com.orlikteam.orlikbackend.ratings.unit

import com.orlikteam.orlikbackend.pitch.Pitch
import com.orlikteam.orlikbackend.pitch.PitchRepository
import com.orlikteam.orlikbackend.pitch.exception.PitchNotFoundException
import com.orlikteam.orlikbackend.rating.dto.RatingRequestDto
import com.orlikteam.orlikbackend.rating.entity.Rating
import com.orlikteam.orlikbackend.rating.repository.RatingRepository
import com.orlikteam.orlikbackend.rating.service.RatingService
import com.orlikteam.orlikbackend.user.User
import com.orlikteam.orlikbackend.user.UserRepository
import com.orlikteam.orlikbackend.user.exception.UserNotFoundException
import spock.lang.Specification
import spock.lang.Subject

class RatingServiceSpec extends Specification {

    private static final String TEST_EMAIL = "test@email.com"
    private static final String NON_TEST_EMAIL = "nontest@email.com"
    private static final int PITCH_ID = 1
    private static final int RATING_ID = 1
    private static final int NON_EXISTENT_PITCH_ID = -1

    @Subject
    private RatingService ratingService

    private UserRepository userRepository
    private PitchRepository pitchRepository
    private RatingRepository ratingRepository

    def setup() {
        pitchRepository = Mock(PitchRepository) {
            findById(PITCH_ID) >> Optional.of(buildPitch())
            findById(NON_EXISTENT_PITCH_ID) >> Optional.empty()
        }

        userRepository = Mock(UserRepository) {
            findById(TEST_EMAIL) >> Optional.of(buildUser())
            findById(NON_TEST_EMAIL) >> Optional.empty()
        }

        ratingRepository = Mock(RatingRepository) {
            save(buildRating()) >> buildRating(RATING_ID)
        }

        ratingService = new RatingService(pitchRepository, userRepository, ratingRepository)
    }

    def "should add rating"() {
        when:
        def result = ratingService.addPitchRating(buildRatingDto())

        then:
        with(result) {
            ratingId
            userId == TEST_EMAIL
            pitchId == PITCH_ID
            value == 5
        }
    }

    def "should throw exception when adding rate by non existent user"() {
        when:
        ratingService.addPitchRating(buildRatingDto(NON_TEST_EMAIL))

        then:
        thrown(UserNotFoundException)
    }

    def "should throw exception when adding rate of non existent pitch"() {
        when:
        ratingService.addPitchRating(buildRatingDto(TEST_EMAIL, NON_EXISTENT_PITCH_ID))

        then:
        thrown(PitchNotFoundException)
    }

    def "should return average of ratings"() {
        given:
        ratingRepository.findAllByPitchId(buildPitch()) >> buildListOfRatings()

        when:
        def result = ratingService.getAverageRating(PITCH_ID)

        then:
        with (result) {
            pitchId == PITCH_ID
            averageRating == 3.5
        }
    }

    def "should throw exception when getting average rating of non existent pitch"() {
        when:
        ratingService.getAverageRating(NON_EXISTENT_PITCH_ID)

        then:
        thrown(PitchNotFoundException)
    }

    def buildListOfRatings() {
        return List.of(
                buildRating(1, 3),
                buildRating(2, 4)
        )
    }

    def buildRating(Integer id = null, Integer value = 5) {
        return Rating.builder()
                .ratingId(id)
                .userId(buildUser())
                .pitchId(buildPitch())
                .value(value)
                .build()
    }

    def buildRatingDto(String email = TEST_EMAIL, Integer pitchId = PITCH_ID) {
        return RatingRequestDto
                .builder()
                .value(5)
                .userId(email)
                .pitchId(pitchId)
                .build()
    }

    def buildUser() {
        return User
                .builder()
                .userLogin(TEST_EMAIL)
                .userPassword("test")
                .build()
    }

    def buildPitch() {
        return Pitch.builder()
                .pitchId(1)
                .pitchName("Testname")
                .latitude(50)
                .longitude(50)
                .build()
    }
}
