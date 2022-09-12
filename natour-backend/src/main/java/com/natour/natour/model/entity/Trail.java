package com.natour.natour.model.entity;

import java.sql.Blob;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.*;

import com.natour.natour.model.Stars;
import com.natour.natour.model.TrailDifficulty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "NT_TRAILS")
public class Trail {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String name;

    @Lob
    private Blob image;

    @Lob
    @Column
    private String description;

    private TrailDifficulty difficulty;

    @OneToMany(mappedBy="trail", cascade=CascadeType.ALL)
    private List<RoutePoint> routePoints;

    @OneToMany(mappedBy="trail", cascade=CascadeType.ALL)
    private List<TrailReview> trailReviews = new LinkedList<>();

    private Stars stars = Stars.ZERO;

    @OneToMany(mappedBy="trail", cascade=CascadeType.ALL)
    private List<TrailPhoto> trailPhotos = new LinkedList<>();

    @OneToOne(cascade=CascadeType.ALL)
    private TrailDuration duration;

    @ManyToOne(cascade=CascadeType.ALL)
    private ApplicationUser owner;

    public Trail(
        Long id,
        String name,
        Blob image,
        String description,
        TrailDifficulty difficulty,
        List<RoutePoint> routePoints,
        TrailDuration duration,
        ApplicationUser owner
    ) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.description = description;
        this.difficulty = difficulty;
        this.routePoints = routePoints;
        this.duration = duration;
        this.owner = owner;
    }

    /* ONLY FOR TESTING */
    public Trail(long id, Stars stars) {
        this.id = id;
        this.stars = stars;
    }

    public void addReview(TrailReview review) {
        trailReviews.add(review);
        calculateStars();
    }

    private void calculateStars() {
        class Sum { double totalStars; }
        Sum sum = new Sum();
        trailReviews.stream().forEach(review -> {
            sum.totalStars += review.getStars().ordinal();
        });
        stars = Stars.getStarsFromAvgReviews(sum.totalStars / trailReviews.size());
    }

    public void addPhoto(TrailPhoto photo) {
        trailPhotos.add(photo);
    }
}
