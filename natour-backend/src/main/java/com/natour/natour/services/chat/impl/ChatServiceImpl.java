package com.natour.natour.services.chat.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.natour.natour.model.dto.ChatMessageDto;
import com.natour.natour.model.dto.ChatRequestDto;
import com.natour.natour.model.dto.ImageDto;
import com.natour.natour.model.entity.ApplicationUser;
import com.natour.natour.model.entity.Chat;
import com.natour.natour.model.entity.ChatMessage;
import com.natour.natour.model.entity.Trail;
import com.natour.natour.repositories.ApplicationUserRepository;
import com.natour.natour.repositories.ChatRepository;
import com.natour.natour.repositories.TrailRepository;
import com.natour.natour.services.chat.ChatService;
import com.natour.natour.util.BlobUtils;

import lombok.extern.java.Log;

import static com.natour.natour.util.EntityUtils.findEntityById;

@Service
@Log
public class ChatServiceImpl implements ChatService {

    private static final String QUEUE_SUBS = "/queue/messages";

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private TrailRepository trailRepository;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public boolean create(final ChatRequestDto chatDto) {
        final ApplicationUser user1 = findEntityById(
            applicationUserRepository, 
            chatDto.getIdUser1(), 
            "Invalid user ID 1 (chat)"
        );

        final ApplicationUser user2 = findEntityById(
            applicationUserRepository, 
            chatDto.getIdUser2(), 
            "Invalid user ID 2 (chat)"
        );

        final Chat chat = new Chat(
            user1.getId() + user2.getId(),
            user1.getUsername(),
            user2.getUsername(),
            chatDto.getIdTrail()
        );

        chat.addUser(user1);
        chat.addUser(user2);
        user1.addChat(chat);
        user2.addChat(chat);

        chatRepository.save(chat);
        applicationUserRepository.save(user1);
        applicationUserRepository.save(user2);

        log.info("A chat between " 
                    + user1.getUsername()  
                    + " and " + user2.getUsername()
                    + " was created."
                );

        return true;
    }

    @Override
    public boolean sendMessage(ChatMessageDto chatMessageDto) {
        final Chat chat = findEntityById(
            chatRepository, 
            chatMessageDto.getIdChat(), 
            "Invalid chat ID (message)"
        );
        final String usernameOwner = chatMessageDto.getUsernameOwner();
        final String usernameRecipient = chatMessageDto.getUsernameRecipient();

        chat.addMessage(
            new ChatMessage(
                null, 
                chat, 
                chatMessageDto.getIdTrail(),
                usernameOwner, 
                chatMessageDto.getDate(), 
                chatMessageDto.getMessage(),
                chatMessageDto.getTime()
            )
        );

        chat.setTotUnreadMessageCounterByUsername(
            usernameRecipient,
            chat.getTotUnreadMessageCounterByUsername(usernameRecipient) + 1
        );

        chatRepository.save(chat);

        simpMessagingTemplate.convertAndSendToUser(
            chatMessageDto.getUsernameRecipient(), 
            QUEUE_SUBS, 
            chatMessageDto
        );

        log.info("A message was sent to " + chatMessageDto.getUsernameRecipient()
                    + " by " + usernameOwner 
                    + ". Message: " + chatMessageDto.getMessage());

        return true;
    }

    @Override
    public void updateUnreadMessages(
        Long chatId, 
        String usernameOwner, 
        int totalUnreadMessages
    ) {
        final Chat chat = findEntityById(
            chatRepository, 
            chatId, 
            "Invalid chat id (unread messages)"
        );

        chat.setTotUnreadMessageCounterByUsername(usernameOwner, totalUnreadMessages);
        chatRepository.save(chat);
        
        log.info("Total unread messages counter username " + usernameOwner 
                    + " set to " + totalUnreadMessages);
    }

    @Override
    public ImageDto getTrailChatImage(long trailId) {
        final Trail trail = findEntityById(
            trailRepository, 
            trailId, 
            "Invalid trail id (chat image)"
        );
        
        log.info("Trail chat image successfully obtained " + trailId);

        return new ImageDto(BlobUtils.getBytesFromBlob(trail.getImage()));
    }

    @Override
    public void setTrailToChat(long chatId, long idTrail) {
        final Chat chat = findEntityById(
            chatRepository,
            chatId,
            "Invalid chat id (set trail to chat)"
        );
        
        chat.setIdTrail(idTrail);
        chatRepository.save(chat);

        log.info("Trail id successfully set to chat id: " + chatId);
    }
}
