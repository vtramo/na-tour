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
import com.natour.natour.model.entity.ApplicationUser;
import com.natour.natour.model.entity.RoutePoint;
import com.natour.natour.model.entity.Trail;
import com.natour.natour.model.entity.TrailDuration;
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
    public boolean saveTrail(SomeSortOfTrail someSortOfTrail) {
        final ApplicationUser trailOwner = findTrailOwner(someSortOfTrail.getIdOwner());
        final Blob trailBlobImage = createBlobImage(someSortOfTrail.getImage());

        Trail trail = createTrail(someSortOfTrail, trailBlobImage, trailOwner);
        trailOwner.addTrail(trail);
        addRoutePointsToTrail(trail, someSortOfTrail);
        trailRepository.save(trail);

        applicationUserRepository.save(trailOwner);

        log.info("A trail has been successfully created: " + trail.getName());
        return true;
    }

    private void addRoutePointsToTrail(Trail trail, SomeSortOfTrail someSortOfTrail) {
        List<RoutePoint> routePoints = trail.getRoutePoints();
        someSortOfTrail.getRoutePoints().stream().forEach(
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

    private Blob createBlobImage(MultipartFile image) {
        if (image.getContentType().matches("image"))
            throw new IllegalArgumentException("The content type must be a image!");

        try {
            return BlobProxy.generateProxy(image.getBytes());
        } catch (IOException e) {
            log.warning("Error in creating the blob image");
            throw new RuntimeException();
        }
    }

    private ApplicationUser findTrailOwner(Long id) {
        Optional<ApplicationUser> owner = applicationUserRepository.findById(id);
        if (owner.isEmpty()) {
            log.warning("The owner of this trail doesn't exist");
            throw new RuntimeException("Trail owner doesn't exist");
        }
        return owner.get();
    }

    private Trail createTrail(
        SomeSortOfTrail someSortOfTrail, 
        Blob trailBlobImage, 
        ApplicationUser trailOwner
    ) {
        return new Trail(
            null,
            someSortOfTrail.getName(),
            trailBlobImage,
            someSortOfTrail.getDescription(),
            someSortOfTrail.getDifficulty(),
            new LinkedList<RoutePoint>(),
            new TrailDuration(someSortOfTrail.getTrailDuration()),
            trailOwner
        );
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
    
}
