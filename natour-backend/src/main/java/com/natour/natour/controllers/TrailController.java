package com.natour.natour.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.natour.natour.model.dto.Duration;
import com.natour.natour.model.dto.SomeSortOfPosition;
import com.natour.natour.model.dto.SomeSortOfRoutePoint;
import com.natour.natour.model.dto.SomeSortOfTrail;
import com.natour.natour.model.dto.SomeSortOfTrailPhoto;
import com.natour.natour.model.dto.SomeSortOfTrailReview;
import com.natour.natour.services.trail.TrailService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.natour.natour.model.TrailDifficulty;

@RestController
@RequestMapping("/trail")
@Tag(
    name = "Trail Controller", 
    description = "This REST controller provides services to modify trails in NaTour."
)
public class TrailController {

    @Autowired
    private TrailService trailService;

    @PostMapping(
        consumes = {"multipart/form-data"}
    )
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Save the provided trail in the NaTour application and " +
                         "returns true if it has succeeded, false otherwise")
    public boolean save(
        @RequestParam("idOwner") Long idOwner,
        @RequestParam("trailName") String trailName,
        @RequestPart("trailDifficulty") TrailDifficulty trailDifficulty,
        @RequestPart("trailDuration") Duration trailDuration,
        @RequestParam("trailDescription") String trailDescription,
        @RequestPart("routePoints") List<SomeSortOfRoutePoint> routePoints,
        @RequestPart("image") MultipartFile image
    ) { 
        return trailService.saveTrail(
            new SomeSortOfTrail(
                idOwner,
                trailName,
                trailDifficulty,
                trailDuration,
                trailDescription,
                routePoints,
                image
            )
        );
    }

    @PostMapping(
        path = "/review",
        consumes = {MediaType.APPLICATION_JSON_VALUE}
    )
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Add the provided trail review to the provided trail and " +
                         "returns true if it has succeeded, false otherwise")
    public boolean addReview(@RequestBody SomeSortOfTrailReview review) {
        return trailService.addReview(review);
    }

    @PostMapping(
        path = "/photo",
        consumes = {"multipart/form-data"}
    )
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Add the provided trail photo to the provided trail and " +
                         "returns true if it has succeeded, false otherwise")
    public boolean addPhoto(
        @RequestParam("idOwner") Long idOwner,
        @RequestParam("idTrail") Long idTrail,
        @RequestPart("image") MultipartFile image,
        @RequestPart("position") SomeSortOfPosition position
    ) {
        return trailService.addPhoto(
            new SomeSortOfTrailPhoto(
                idOwner,
                idTrail,
                image,
                position
            )
        );
    }

    // TEST END-POINT
    @GetMapping(
        produces = MediaType.IMAGE_PNG_VALUE
    )
    public byte[] getImage(@RequestParam("id") Long id) {
        return trailService.getTrail(id);
    }
}
 