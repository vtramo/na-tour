package com.natour.natour.model.dto.util;

import com.natour.natour.model.dto.TrailResponseDto;
import com.natour.natour.model.entity.Trail;
import com.natour.natour.util.BlobUtils;
import com.natour.natour.model.dto.*;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.natour.natour.model.entity.*;

@Service
public class TrailDtoConverterImpl implements TrailDtoConverter {

    @Transactional
    @Override
    public TrailResponseDto convertTrailEntityToTrailResponseDto(Trail trail) {
        UserDetailsDto owner = new UserDetailsDto(trail.getOwner());
        DurationDto duration = convertTrailDurationToDurationDto(trail.getDuration());
        byte[] bytesImage = BlobUtils.getBytesFromBlob(trail.getImage());

        List<RoutePointDto> routePoints = 
            convertListOfRoutePointEntityToListOfRoutePointDto(
                trail.getRoutePoints()
            );

        List<TrailPhotoResponseDto> photos = 
            convertListOfTrailPhotoEntityToListOfTrailPhotoResponseDto(
                trail.getTrailPhotos()
            );

        List<TrailReviewResponseDto> reviews =
            convertListOfTrailReviewEntityToListOfTrailReviewResponseDto(
                trail.getTrailReviews()
            );

        return new TrailResponseDto(
            trail.getId(), 
            owner, 
            trail.getName(), 
            trail.getDescription(), 
            trail.getDifficulty(), 
            duration, 
            bytesImage, 
            routePoints, 
            photos, 
            reviews, 
            trail.getStars()
        );
    }

    public DurationDto convertTrailDurationToDurationDto(TrailDuration trailDuration) {
        return new DurationDto(
            trailDuration.getMouths(), 
            trailDuration.getDays(), 
            trailDuration.getHours(), 
            trailDuration.getMinutes()
        );
    }

    public List<RoutePointDto> 
        convertListOfRoutePointEntityToListOfRoutePointDto(
            List<RoutePoint> points
        ) {
            return points.stream()
                .map(TrailDtoUtils::convertRoutePointEntityToRoutePointDto)
                .collect(Collectors.toList());
    }

    public RoutePointDto convertRoutePointEntityToRoutePointDto(
        RoutePoint point
    ) {
        return new RoutePointDto(
            point.getLatitude(),
            point.getLongitude()
        );
    }

    public List<TrailPhotoResponseDto> 
        convertListOfTrailPhotoEntityToListOfTrailPhotoResponseDto(
            List<TrailPhoto> photos
        ) {
            return photos.stream()
                .map(TrailDtoUtils::convertTrailPhotoEntityToTrailPhotoResponseDto)
                .collect(Collectors.toList());
    }

    public TrailPhotoResponseDto 
        convertTrailPhotoEntityToTrailPhotoResponseDto(
            TrailPhoto photo
        ) {
            UserDetailsDto owner = new UserDetailsDto(photo.getOwner());
            byte[] bytesImage = BlobUtils.getBytesFromBlob(photo.getImage());
            return new TrailPhotoResponseDto(
                owner,
                bytesImage,
                convertPositionEntityToPositionDto(photo.getPosition())
            );
    }

    public PositionDto convertPositionEntityToPositionDto(
        Position position
    ) {
        return new PositionDto(
            position.getLatitude(), 
            position.getLongitude()
        );
    }

    public List<TrailReviewResponseDto> 
        convertListOfTrailReviewEntityToListOfTrailReviewResponseDto(
            List<TrailReview> reviews
        ) {
            return reviews.stream()
                .map(TrailDtoUtils::convertTrailReviewEntityToTrailReviewResponseDto)
                .collect(Collectors.toList());
    }

    public TrailReviewResponseDto
        convertTrailReviewEntityToTrailReviewResponseDto(
            TrailReview review
        ) {
            UserDetailsDto owner = new UserDetailsDto(review.getOwner());
            return new TrailReviewResponseDto(
                owner,
                review.getStars(),
                review.getDescription(),
                review.getDate()
            );
    }
}
