package com.javamentor.developer.social.platform.models.dto.chat;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChatDto {
    private String title;
    private LocalDateTime persistDate;
    private List<UserDto> users;
}
