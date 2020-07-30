package com.javamentor.developer.social.platform.models.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
public class PostDto {

    @ApiModelProperty(notes = "Автоматически генерируемыЙ ID новости. При создании не указывать, указывать при изменении")
    private Long id;

    @NotNull
    @ApiModelProperty(notes = "Заголовок новости, поле не должно быть пустым")
    private String title;

    @NotNull
    @ApiModelProperty(notes = "Текст новсти, поле не должно быть пустым")
    private String text;

    @ApiModelProperty(notes = "Пользователь, добавивший пост")
    private UserDto userDto;

    @ApiModelProperty(notes = "Дата последнего изменения")
    private LocalDateTime lastRedactionDate;

    @ApiModelProperty(notes = "Дата добавления поста")
    private LocalDateTime persistDate;

    @ApiModelProperty(notes = "Медиаконтент поста")
    private List<MediaPostDto> media;

    @ApiModelProperty(notes = "Тэги новости")
    private List<TagDto> tags;
}
