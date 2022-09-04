package com.natour.natour.model.entity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Data
@AllArgsConstructor
@Table(name = "NT_CHATS")
public class Chat {
    
    @Id
    private Long id;

    @ManyToMany(mappedBy="chats")
    private Set<ApplicationUser> users = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy="chat", cascade=CascadeType.ALL)
    private List<ChatMessage> messages = new LinkedList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    private Map<String, Integer> unreadMessageCounter = new HashMap<>();

    private String usernameUser1;
    private String usernameUser2;

    private Long idTrail;

    public Chat() { }
    public Chat(Long id, String username1, String username2, Long idTrail) {
        this.id = id;
        this.usernameUser1 = username1;
        this.usernameUser2 = username2;
        this.idTrail = idTrail;
    }

    public void addUser(ApplicationUser user) {
        users.add(user);
        unreadMessageCounter.put(user.getUsername(), 0);
    }

    public void addMessage(ChatMessage message) {
        messages.add(message);
    }

    public Integer getTotUnreadMessageCounterByUsername(String username) {
        return unreadMessageCounter.getOrDefault(username, 0);
    }

    public void setTotUnreadMessageCounterByUsername(String username, int tot) {
        unreadMessageCounter.put(username, tot);
    }
}
