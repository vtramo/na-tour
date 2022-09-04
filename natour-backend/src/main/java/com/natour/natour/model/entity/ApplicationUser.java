package com.natour.natour.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.Email;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "NT_USERS")
public class ApplicationUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(unique = true)
    private String username;

    @NonNull
    private String firstName;

    @NonNull
    private String lastName;

    @NonNull
    @Email
    private String email;

    @NonNull
    @JsonIgnore
    private String password;

    @OneToMany(mappedBy="owner", cascade=CascadeType.ALL)
    private List<Trail> trails = new LinkedList<>();

    @OneToMany(cascade=CascadeType.ALL)
    @JoinTable(
        name="NT_USERS_FAVORITE_TRAILS",
        joinColumns={@JoinColumn(name="user_id")},
        inverseJoinColumns={@JoinColumn(name="favorite_trail_id")}
    )
    private List<Trail> favoriteTrails = new LinkedList<>();

    @OneToMany(mappedBy="owner", cascade=CascadeType.ALL)
    private List<TrailReview> trailReviews = new LinkedList<>();

    @OneToMany(mappedBy="owner", cascade=CascadeType.ALL)
    private List<TrailPhoto> trailPhotos = new LinkedList<>();

    @ManyToMany
    @JoinTable(
        name="NT_USERS_CHATS",
        joinColumns={@JoinColumn(name="user_id")},
        inverseJoinColumns={@JoinColumn(name="chat_id")}
    )
    private List<Chat> chats = new LinkedList<>();

    public ApplicationUser(
        Long id,
        String username,
        String firstName,
        String lastName,
        String email,
        String password
    ) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public void addTrail(@NonNull Trail trail) {
        trails.add(trail);
    }

    public void addTrailReview(@NonNull TrailReview review) {
        trailReviews.add(review);
    }

    public void addTrailPhoto(@NonNull TrailPhoto photo) {
        trailPhotos.add(photo);
    }

    public void addChat(@NonNull Chat chat) {
        chats.add(chat);
    }
}
