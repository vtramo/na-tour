package com.natour.natour.model.entity;

import java.sql.Blob;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "NT_TRAIL_PHOTOS")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TrailPhoto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private Blob image;

    @OneToOne
    private Position position;

    @ManyToOne(cascade=CascadeType.ALL)
    private ApplicationUser owner;

    @ManyToOne(cascade=CascadeType.ALL)
    private Trail trail;
}
