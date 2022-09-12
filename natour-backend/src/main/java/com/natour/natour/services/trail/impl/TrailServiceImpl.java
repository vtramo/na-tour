package com.natour.natour.services.trail.impl;

import java.sql.Blob;
import java.util.LinkedList;
import java.util.List;

import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.natour.natour.model.dto.TrailRequestDto;
import com.natour.natour.model.dto.TrailPhotoRequestDto;
import com.natour.natour.model.dto.TrailReviewRequestDto;
import com.natour.natour.model.dto.TrailResponseDto;
import com.natour.natour.model.dto.PositionDto;
import com.natour.natour.model.entity.ApplicationUser;
import com.natour.natour.model.entity.Position;
import com.natour.natour.model.entity.RoutePoint;
import com.natour.natour.model.entity.Trail;
import com.natour.natour.model.entity.TrailDuration;
import com.natour.natour.model.entity.TrailPhoto;
import com.natour.natour.model.entity.TrailReview;
import com.natour.natour.repositories.TrailRepository;
import com.natour.natour.repositories.ApplicationUserRepository;
import com.natour.natour.services.trail.TrailService;
import com.natour.natour.util.EntityUtils;
import com.natour.natour.model.dto.util.TrailDtoConverter;

import lombok.extern.java.Log;

@Service
@Log
public class TrailServiceImpl implements TrailService {

    @Autowired
    private TrailRepository trailRepository;
    @Autowired
    private ApplicationUserRepository applicationUserRepository;
    @Autowired
    private TrailDtoConverter trailDtoConverter;

    @Override
    public boolean saveTrail(final TrailRequestDto trailDto) {
        final ApplicationUser trailOwner = EntityUtils.findEntityById(
            applicationUserRepository, 
            trailDto.getIdOwner(), 
            "Invalid user ID (trailOwner)"
        );
        final Blob trailBlobImage = BlobProxy.generateProxy(trailDto.getBytesImage());

        final Trail trail = createTrail(trailDto, trailBlobImage, trailOwner);
        trailOwner.addTrail(trail);

        trailRepository.save(trail);
        applicationUserRepository.save(trailOwner);

        log.info("A trail has been successfully created: " + trail.getName());
        return true;
    }

    private Trail createTrail(
        final TrailRequestDto trailDto, 
        final Blob trailBlobImage, 
        final ApplicationUser trailOwner
    ) {
        final Trail trail = new Trail(
            null,
            trailDto.getName(),
            trailBlobImage,
            trailDto.getDescription(),
            trailDto.getDifficulty(),
            new LinkedList<RoutePoint>(),
            new TrailDuration(trailDto.getTrailDuration()),
            trailOwner
        );
        addRoutePointsToTrail(trail, trailDto);
        return trail;
    }

    private void addRoutePointsToTrail(
        final Trail trail, 
        final TrailRequestDto trailDto
    ) {
        final List<RoutePoint> routePoints = trail.getRoutePoints();
        trailDto.getRoutePoints().stream().forEach(
            point -> {
                routePoints.add(
                    new RoutePoint(
                        null, 
                        trail, 
                        point.getLatitude(), 
                        point.getLongitude())
                );
            }
        );
    }

    @Override
    public boolean addReview(final TrailReviewRequestDto trailReviewDto) {
        final ApplicationUser owner = EntityUtils.findEntityById(
            applicationUserRepository,
            trailReviewDto.getIdOwner(), 
            "Invalid user ID (reviewOwner)"
        );
        final Trail trail = EntityUtils.findEntityById(
            trailRepository,
            trailReviewDto.getIdTrail(),
            "Invalid trail ID (review)"
        );

        final TrailReview trailReview = new TrailReview();
        trailReview.setStars(trailReviewDto.getStars());
        trailReview.setDescription(trailReviewDto.getDescription());
        trailReview.setDate(trailReviewDto.getDate());
        owner.addTrailReview(trailReview);
        trail.addReview(trailReview);
        trailReview.setOwner(owner);
        trailReview.setTrail(trail);
        
        trailRepository.save(trail);
        applicationUserRepository.save(owner);

        log.info("A trail review has been successfully created: " 
                    + trailReview.getStars());
        return true;
    }

    @Override
    public boolean addPhoto(final TrailPhotoRequestDto trailPhotoDto) {
        final ApplicationUser owner = EntityUtils.findEntityById(
            applicationUserRepository,
            trailPhotoDto.getIdOwner(), 
            "Invalid user ID (photoOwner)"
        );
        final Trail trail = EntityUtils.findEntityById(
            trailRepository,
            trailPhotoDto.getIdTrail(),
            "Invalid trail ID (photo)"
        );
    
        final TrailPhoto trailPhoto = createTrailPhoto(trailPhotoDto);
        trailPhoto.setOwner(owner);
        trailPhoto.setTrail(trail);
        owner.addTrailPhoto(trailPhoto);
        trail.addPhoto(trailPhoto);

        trailRepository.save(trail);
        applicationUserRepository.save(owner);

        log.info("A trail photo has been successfully created. Owner: " 
                    + trailPhoto.getOwner().getFirstName());
        return true;
    }

    private TrailPhoto createTrailPhoto(final TrailPhotoRequestDto photoDto) {
        final TrailPhoto trailPhoto = new TrailPhoto();

        final Blob trailBlobImage = BlobProxy.generateProxy(photoDto.getBytesImage());
        trailPhoto.setImage(trailBlobImage);

        final Position position = new Position();
        final PositionDto positionDto = photoDto.getPosition();
        position.setLatitude(positionDto.getLatitude());
        position.setLongitude(positionDto.getLongitude());
        trailPhoto.setPosition(position);

        return trailPhoto;
    }

    private static final int TRAILS_PER_REQUEST = 10;
    
    @Override
    @Transactional
    public List<TrailResponseDto> getTrails(int page) {
        if (page < 0) 
            throw new IllegalArgumentException("Page number must be positive.");

        Pageable pageable = PageRequest.of(
            page, 
            TRAILS_PER_REQUEST, 
            Sort.by("stars").descending().and(Sort.by("id").ascending())
        );
        
        return trailRepository.findAll(pageable)
            .stream()
            .map(trailDtoConverter::convertTrailEntityToTrailResponseDto)
            .collect(Collectors.toList());
    }
}
