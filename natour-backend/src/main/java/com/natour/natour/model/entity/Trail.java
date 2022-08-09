package com.natour.natour.model.entity;

import java.sql.Blob;
import java.util.List;

import javax.persistence.*;

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

    private String description;

    private TrailDifficulty difficulty;

    @OneToMany(mappedBy="trail", cascade=CascadeType.ALL)
    private List<RoutePoint> routePoints;

    @OneToOne(cascade=CascadeType.ALL)
    private TrailDuration duration;

    @ManyToOne(cascade=CascadeType.ALL)
    private ApplicationUser owner;
}
