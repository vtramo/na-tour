package com.natour.natour.services.trail.impl;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.natour.natour.model.dto.SomeSortOfTrail;
import com.natour.natour.model.dto.SomeSortOfTrailPhoto;
import com.natour.natour.model.dto.SomeSortOfTrailReview;
import com.natour.natour.model.dto.SomeSortOfPosition;
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

import lombok.extern.java.Log;

@Service
@Log
public class TrailServiceImpl implements TrailService {

    @Autowired
    private TrailRepository trailRepository;
    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Override
    public boolean saveTrail(final SomeSortOfTrail trailDto) {
        final ApplicationUser trailOwner = findOwner(
            trailDto.getIdOwner(), 
            "Invalid user ID (trailOwner)"
        );
        final Blob trailBlobImage = createBlobImage(trailDto.getImage());

        final Trail trail = createTrail(trailDto, trailBlobImage, trailOwner);
        trailOwner.addTrail(trail);

        trailRepository.save(trail);
        applicationUserRepository.save(trailOwner);

        log.info("A trail has been successfully created: " + trail.getName());
        return true;
    }

    private ApplicationUser findOwner(Long id, String exceptionMessage) {
        final Optional<ApplicationUser> owner = 
            applicationUserRepository.findById(id);
        if (owner.isEmpty()) {
            log.warning(exceptionMessage);
            throw new RuntimeException(exceptionMessage);
        }
        return owner.get();
    }

    private Trail createTrail(
        final SomeSortOfTrail trailDto, 
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
        final SomeSortOfTrail trailDto
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

    private Blob createBlobImage(final MultipartFile image) {
        if (image.getContentType().matches("image"))
            throw new IllegalArgumentException("The content type must be a image!");

        try {
            return BlobProxy.generateProxy(image.getBytes());
        } catch (IOException e) {
            log.warning("Error in creating the blob image");
            throw new RuntimeException();
        }
    }

    // TEST METHOD !!!
    @Override
    @Transactional
    public byte[] getTrail(Long id) {
        log.info("id: " + id.toString());
        Optional<Trail> optTrail = trailRepository.findById(id);
        if (optTrail.isPresent()) {
            Trail trail = optTrail.get();
            Blob blob = trail.getImage();
            try {
                return blob.getBinaryStream().readAllBytes();
            } catch (IOException | SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        log.info("null");
        return null;
    }

    @Override
    public boolean addReview(final SomeSortOfTrailReview trailReviewDto) {
        final ApplicationUser owner = findOwner(
            trailReviewDto.getIdOwner(), 
            "Invalid user ID (reviewOwner)"
        );
        final Trail trail = findTrail(
            trailReviewDto.getIdTrail(),
            "Invalid trail ID (review)"
        );

        final TrailReview trailReview = new TrailReview();
        trailReview.setStars(trailReviewDto.getStars());
        trailReview.setDescription(trailReviewDto.getDescription());
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

    private Trail findTrail(Long id, String exceptionMessage) {
        final Optional<Trail> trail = trailRepository.findById(id);

        if (trail.isEmpty()) {
            log.warning(exceptionMessage);
            throw new RuntimeException(exceptionMessage);
        }

        return trail.get();
    }

    @Override
    public boolean addPhoto(final SomeSortOfTrailPhoto trailPhotoDto) {
        final ApplicationUser owner = findOwner(
            trailPhotoDto.getIdOwner(), 
            "Invalid user ID (photoOwner)"
        );
        final Trail trail = findTrail(
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

    private TrailPhoto createTrailPhoto(final SomeSortOfTrailPhoto photoDto) {
        final TrailPhoto trailPhoto = new TrailPhoto();

        final Blob trailBlobImage = createBlobImage(photoDto.getImage());
        trailPhoto.setImage(trailBlobImage);

        final Position position = new Position();
        final SomeSortOfPosition positionDto = photoDto.getPosition();
        position.setLatitude(positionDto.getLatitude());
        position.setLongitude(positionDto.getLongitude());
        trailPhoto.setPosition(position);

        return trailPhoto;
    }
}
