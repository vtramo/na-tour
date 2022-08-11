package com.natour.natour.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.natour.natour.model.dto.DurationDto;
import com.natour.natour.model.dto.PositionDto;
import com.natour.natour.model.dto.RoutePointDto;
import com.natour.natour.model.dto.TrailRequestDto;
import com.natour.natour.model.dto.TrailPhotoRequestDto;
import com.natour.natour.model.dto.TrailReviewRequestDto;
import com.natour.natour.model.dto.TrailResponseDto;
import com.natour.natour.services.trail.TrailService;
import com.natour.natour.util.MultipartFileUtils;

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
        consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
        produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Save the provided trail in the NaTour application and " +
                         "returns true if it has succeeded, false otherwise")
    public boolean save(
        @RequestParam("idOwner") Long idOwner,
        @RequestParam("trailName") String trailName,
        @RequestPart("trailDifficulty") TrailDifficulty trailDifficulty,
        @RequestPart("trailDuration") DurationDto trailDuration,
        @RequestParam("trailDescription") String trailDescription,
        @RequestPart("routePoints") List<RoutePointDto> routePoints,
        @RequestPart("image") MultipartFile image
    ) { 
        byte[] trailImage = MultipartFileUtils.getBytesFromMultipartFile(
            image, 
            "image"
        );
        return trailService.saveTrail(
            new TrailRequestDto(
                idOwner,
                trailName,
                trailDifficulty,
                trailDuration,
                trailDescription,
                routePoints,
                trailImage
            )
        );
    }

    @PostMapping(
        path = "/review",
        consumes = {MediaType.APPLICATION_JSON_VALUE},
        produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Adds the provided trail review to the provided trail and " +
                         "returns true if it has succeeded, false otherwise")
    public boolean addReview(@RequestBody TrailReviewRequestDto review) {
        return trailService.addReview(review);
    }

    @PostMapping(
        path = "/photo",
        consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
        produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Adds the provided trail photo to the provided trail and " +
                         "returns true if it has succeeded, false otherwise")
    public boolean addPhoto(
        @RequestParam("idOwner") Long idOwner,
        @RequestParam("idTrail") Long idTrail,
        @RequestPart("image") MultipartFile image,
        @RequestPart("position") PositionDto position
    ) {
        byte[] trailPhoto = MultipartFileUtils.getBytesFromMultipartFile(
            image,
            "image"
        );
        return trailService.addPhoto(
            new TrailPhotoRequestDto(
                idOwner,
                idTrail,
                trailPhoto,
                position
            )
        );
    }

    @GetMapping(
        path = "/{page}",
        consumes = MediaType.ALL_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Returns a list of 10 trails from the page provided")
    public List<TrailResponseDto> getTrails(@PathVariable("page") int page) {
        return trailService.getTrails(page);
    }
}
 