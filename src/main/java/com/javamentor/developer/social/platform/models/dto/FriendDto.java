package com.javamentor.developer.social.platform.models.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class FriendDto {

    Long id;

    Long userId;

    Long friendId;
}