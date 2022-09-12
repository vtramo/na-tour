package com.natour.natour.services;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.natour.natour.exceptions.EntityByIdNotFoundException;
import com.natour.natour.model.entity.ApplicationUser;
import com.natour.natour.model.entity.Chat;
import com.natour.natour.repositories.ChatRepository;
import com.natour.natour.services.chat.ChatService;
import com.natour.natour.services.chat.impl.ChatServiceImpl;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@DisplayName("A ChatService")
public class ChatServiceUnitTest {
    
    @MockBean
    private ChatRepository chatRepository;

    @InjectMocks
    private ChatService chatService = new ChatServiceImpl();

    @Nested
    @DisplayName("When updating unread messages from a user's chat")
    class UpdateUnreadMessagesMethodTest {

        private static final Long EXISTING_CHAT_ID = 11L;
        private static final Long UNKNOWN_CHAT_ID = 0L;

        {
            configureChatRepositoryMockBean();
        }

        private void configureChatRepositoryMockBean() {
            when(chatRepository.findById(EXISTING_CHAT_ID))
                .thenAnswer(this::withExistingMockChat);

            when(chatRepository.save(any()))
                .thenReturn(null);
        }

        private Optional<Chat> withExistingMockChat(InvocationOnMock invocation) {
            ApplicationUser vtramo = new ApplicationUser();
            vtramo.setUsername("vtramo");
            ApplicationUser lauraa = new ApplicationUser();
            lauraa.setUsername("lauraa");

            Optional<Chat> optionalChat = Optional.of(
                new Chat(
                    (Long)invocation.getArgument(0),
                    "vtramo",
                    "lauraa",
                    null
                )
            );

            Chat chat = optionalChat.get();
            chat.addUser(vtramo); chat.addUser(lauraa);

            return optionalChat;
        }

        @Test
        @DisplayName("should update them correctly if the chat exists, the " +
            "user belongs to that chat and you pass a positive number.")
        public void testUpdateExistingChatExistingUserWithPositiveNumber() {
            final int expectedTotalUnreadMessages = 5;

            final Chat chat = chatService.updateUnreadMessages(
                EXISTING_CHAT_ID,
                "vtramo",
                5
            );

            assertTrue(
                chat.getTotUnreadMessageCounterByUsername("vtramo")
                    == expectedTotalUnreadMessages
            );
        }  

        @Test
        @DisplayName("should throw an IllegalArgumentException if the chat exists " + 
         "but the provided username doesn't belong to it.")
        public void testUpdateExistingChatButWithNotExistingUser() {
            assertThrows(
                IllegalArgumentException.class,
                () -> chatService.updateUnreadMessages(
                    EXISTING_CHAT_ID,
                    "unknown", 
                    100
                )
            );
        } 

        @Test
        @DisplayName("should throw an EntityByIdNotFoundException if the chat " +
            "doesn't exist.")
        public void testUpdateNotExistingChat() {
            assertThrows(
                EntityByIdNotFoundException.class,
                () -> chatService.updateUnreadMessages(
                    UNKNOWN_CHAT_ID, 
                    "vtramo", 
                    1
                )
            );
        }

        @Test
        @DisplayName("should update them correctly if the chat exists, the " +
        "user belongs to that chat and you pass zero.")
        public void testUpdateExistingChatExistingUserWithZero() {
            final int expectedTotalUnreadMessages = 0;

            final Chat chat = chatService.updateUnreadMessages(
                EXISTING_CHAT_ID,
                "vtramo",
                0
            );

            assertTrue(
                chat.getTotUnreadMessageCounterByUsername("vtramo")
                    == expectedTotalUnreadMessages
            );
        }
    }
}
