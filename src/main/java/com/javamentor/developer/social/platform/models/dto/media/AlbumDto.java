package com.javamentor.developer.social.platform.models.dto.media;

import com.javamentor.developer.social.platform.models.util.OnCreate;
import com.javamentor.developer.social.platform.models.util.OnUpdate;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AlbumDto {

    @ApiModelProperty(notes = "Автоматически генерируемый ID. Не указывать при создании, " +
            "обязательно указывать при изменении объекта")
    @Null(groups = OnCreate.class, message = "'id' Must be null when creating")
    @NotNull(groups = OnUpdate.class, message = "'id' Must not accept null values when updating")
    private Long id;

    @ApiModelProperty(notes = "Название альбома")
    @NotBlank(groups = {OnCreate.class, OnUpdate.class}, message = "'name' Must not be empty when creating and updating")
    private String name;

    @ApiModelProperty(notes = "Адрес иконки (превью) альбома")
    private String icon;
}
