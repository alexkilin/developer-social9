package com.javamentor.developer.social.platform.webapp.controllers.chat;

import com.javamentor.developer.social.platform.models.dto.chat.ChatDto;
import com.javamentor.developer.social.platform.models.dto.chat.MessageDto;
import com.javamentor.developer.social.platform.service.abstracts.dto.chat.ChatDtoService;
import com.javamentor.developer.social.platform.service.abstracts.dto.chat.MessageDtoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(value = "ChatsController")
public class ChatControllers {

    @Autowired
    ChatDtoService chatDto;

    @Autowired
    MessageDtoService messageDto;


    @GetMapping("/api/user/{userId}/chats")
    @ApiOperation(value = "Список чатов", response = ChatDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code =404, message = "404 error")
    })
    public ResponseEntity<List<ChatDto>> getChatsDto(@PathVariable String userId){
        return ResponseEntity.ok(chatDto.getAllChatDtoByUserId(Long.parseLong(userId)));
    }

    @GetMapping("/api/user/{chatId}/messages")
    @ApiOperation(value = "Список сообщений", response = MessageDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code =404, message = "404 error")
    })
    public ResponseEntity<List<MessageDto>> getMessageDto(@PathVariable String chatId){
        return ResponseEntity.ok(messageDto.getAllMessageDtoByChatId(Long.parseLong(chatId)));
    }
}
