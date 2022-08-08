package com.natour.natour.model.entity;

import java.sql.Blob;

import javax.persistence.*;

import com.natour.natour.model.TrailDifficulty;
import com.natour.natour.model.TrailDuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Entity
@Data
@AllArgsConstructor
@Table(name = "NT_TRAILS")
public class Trail {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String name;

    @Lob
    private Blob image;

    private TrailDifficulty difficulty;

    @OneToOne
    private TrailDuration duration;

    @ManyToOne
    private ApplicationUser owner;
}
