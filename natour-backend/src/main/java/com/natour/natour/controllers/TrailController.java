package com.natour.natour.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.natour.natour.model.dto.Duration;
import com.natour.natour.model.dto.SomeSortOfRoutePoint;
import com.natour.natour.model.dto.SomeSortOfTrail;
import com.natour.natour.services.trail.TrailService;

import io.swagger.v3.oas.annotations.tags.Tag;
import com.natour.natour.model.TrailDifficulty;

@RestController
@RequestMapping("/trail")
@Tag(
    name = "Trail Controller", 
    description = "This REST controller provides services to ..."
)
public class TrailController {

    @Autowired
    private TrailService trailService;

    @PostMapping(
        consumes = {"multipart/form-data"}
    )
    public boolean save(
        @RequestParam("idOwner") Long idOwner,
        @RequestParam("trailName") String trailName,
        @RequestPart("trailDifficulty") TrailDifficulty trailDifficulty,
        @RequestPart("trailDuration") Duration trailDuration,
        @RequestParam("trailDescription") String trailDescription,
        @RequestPart("routePoints") List<SomeSortOfRoutePoint> routePoints,
        @RequestPart("image") MultipartFile image
    ) { 
        System.out.println(trailDuration.getMonths());
        System.out.println(idOwner);
        System.out.println(trailName);
        System.out.println(trailDifficulty);
        System.out.println(routePoints);

        SomeSortOfTrail someSortOfTrail = new SomeSortOfTrail(
            idOwner,
            trailName,
            trailDifficulty,
            trailDuration,
            trailDescription,
            routePoints,
            image
        );

        return trailService.saveTrail(someSortOfTrail);
    }

    @GetMapping(
        produces = MediaType.IMAGE_PNG_VALUE
    )
    public byte[] getImage(@RequestParam("id") Long id) {
        return trailService.getTrail(id);
    }
}
 