package com.javamentor.developer.social.platform.models.dto;

import com.javamentor.developer.social.platform.models.util.OnCreate;
import com.javamentor.developer.social.platform.models.util.OnUpdate;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
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

    @ApiModelProperty(notes = "Автоматически генерируемыЙ ID новости. При создании не указывать, указывать при изменении", example = "null")
    @Null(groups = OnCreate.class, message = "Поле id должно принимать null значение при создании")
    @NotNull(groups = OnUpdate.class, message = "Поле id не должно принимать null значение при обновлении")
    private Long id;

    @NotNull
    @Size(groups = OnCreate.class, max = 50, message = "Значение поля title не может превышать 50 символов")
    @ApiModelProperty(notes = "Заголовок новости, поле не должно быть пустым")
    private String title;

    @NotNull
    @Size(groups = OnCreate.class, max = 1000, message = "Значение поля text не может превышать 1000 символов")
    @ApiModelProperty(notes = "Текст новости, поле не должно быть пустым")
    private String text;

    @ApiModelProperty(notes = "ID пользователя, добавившего пост", example = "1")
    private Long userId;

    @ApiModelProperty(notes = "Имя пользователя")
    private String firstName;

    @ApiModelProperty(notes = "Фамилия пользователя")
    private String lastName;

    @ApiModelProperty(notes = "Ссылка на аватарку пользователя")
    private String avatar;

    @ApiModelProperty(notes = "Дата последнего изменения", hidden = true)
    private LocalDateTime lastRedactionDate;

    @ApiModelProperty(notes = "Дата добавления поста", hidden = true)
    private LocalDateTime persistDate;

    @ApiModelProperty(notes = "Медиаконтент поста")
    private List<MediaPostDto> media;

    @ApiModelProperty(notes = "Тэги новости")
    @Valid
    private List<TagDto> tags;

    @ApiModelProperty(notes = "Количество добавлений в избранное/закладки", example = "0")
    private Long bookmarkAmount;

    @ApiModelProperty(notes = "Количество лайков", example = "0")
    private Long likeAmount;

    @ApiModelProperty(notes = "Количество комментариев", example = "0")
    private Long commentAmount;

    @ApiModelProperty(notes = "Количество репостов", example = "0")
    private Long shareAmount;

    @ApiModelProperty(notes = "Лайк авторизованого юзера")
    private Boolean isLiked;

    @ApiModelProperty(notes = "Находится в закладках авторизованного юзера")
    private Boolean isBookmarked;

    @ApiModelProperty(notes = "\"Репостнуто\" авторизованным юзером")
    private Boolean isShared;
}
