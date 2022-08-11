package com.natour.natour.model.dto.util;

import java.util.stream.Collectors;

import com.natour.natour.model.dto.DurationDto;
import com.natour.natour.model.dto.PositionDto;
import com.natour.natour.model.dto.TrailResponseDto;
import com.natour.natour.model.dto.UserDetailsDto;
import com.natour.natour.model.dto.TrailPhotoResponseDto;
import com.natour.natour.model.dto.TrailReviewResponseDto;
import com.natour.natour.model.dto.RoutePointDto;
import com.natour.natour.model.entity.Position;
import com.natour.natour.model.entity.RoutePoint;
import com.natour.natour.model.entity.Trail;
import com.natour.natour.model.entity.TrailPhoto;
import com.natour.natour.model.entity.TrailReview;
import com.natour.natour.model.entity.TrailDuration;
import com.natour.natour.util.BlobUtils;

import java.util.List;

public abstract class TrailDtoUtils {
    
    public static TrailResponseDto convertTrailEntityToTrailResponseDto(Trail trail) {
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

    public static DurationDto convertTrailDurationToDurationDto(TrailDuration trailDuration) {
        return new DurationDto(
            trailDuration.getMouths(), 
            trailDuration.getDays(), 
            trailDuration.getHours(), 
            trailDuration.getMinutes()
        );
    }

    public static List<RoutePointDto> 
        convertListOfRoutePointEntityToListOfRoutePointDto(
            List<RoutePoint> points
        ) {
            return points.stream()
                .map(TrailDtoUtils::convertRoutePointEntityToRoutePointDto)
                .collect(Collectors.toList());
    }

    public static RoutePointDto convertRoutePointEntityToRoutePointDto(
        RoutePoint point
    ) {
        return new RoutePointDto(
            point.getLatitude(),
            point.getLongitude()
        );
    }

    public static List<TrailPhotoResponseDto> 
        convertListOfTrailPhotoEntityToListOfTrailPhotoResponseDto(
            List<TrailPhoto> photos
        ) {
            return photos.stream()
                .map(TrailDtoUtils::convertTrailPhotoEntityToTrailPhotoResponseDto)
                .collect(Collectors.toList());
    }

    public static TrailPhotoResponseDto 
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

    public static PositionDto convertPositionEntityToPositionDto(
        Position position
    ) {
        return new PositionDto(
            position.getLatitude(), 
            position.getLongitude()
        );
    }

    public static List<TrailReviewResponseDto> 
        convertListOfTrailReviewEntityToListOfTrailReviewResponseDto(
            List<TrailReview> reviews
        ) {
            return reviews.stream()
                .map(TrailDtoUtils::convertTrailReviewEntityToTrailReviewResponseDto)
                .collect(Collectors.toList());
    }

    public static TrailReviewResponseDto
        convertTrailReviewEntityToTrailReviewResponseDto(
            TrailReview review
        ) {
            UserDetailsDto owner = new UserDetailsDto(review.getOwner());
            return new TrailReviewResponseDto(
                owner,
                review.getStars(),
                review.getDescription()
            );
    }
}
