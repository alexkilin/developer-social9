package com.javamentor.developer.social.platform.models.dto.media.music;

import com.javamentor.developer.social.platform.models.util.OnCreate;
import com.javamentor.developer.social.platform.models.util.OnUpdate;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AudioLastPlayedDto {


        @ApiModelProperty(notes = "Автоматически генерируемый ID. Не указывать при создании, " +
                "обязательно указывать при изменении объекта")
        @Null(groups = OnCreate.class, message = "'id' Must be null when creating ")
        @NotNull(groups = OnUpdate.class, message = "'id' Must not accept null values when updating AudioDto.class")
        private Long id;

        @ApiModelProperty(notes = "Адрес, на котором располагается аудио объект",
                required = true, example = "/stuff/audio_341.mp3")
        @NotNull(groups = {OnCreate.class, OnUpdate.class}, message = "'url' Must not be null when creating and updating ")
        @NotBlank(groups = {OnCreate.class, OnUpdate.class}, message = "'text' Must not consist of spaces ")
        private String url;

        @ApiModelProperty(notes = "Адрес иконки (превью) аудио объекта", example = "/icons/132_1.jpg")
        @NotNull(groups = {OnCreate.class}, message = "'icon' Must not be null when creating ")
        private String icon;

        @ApiModelProperty(notes = "Название аудио объекта", example = "Voodoo People")
        @NotNull(groups = {OnCreate.class}, message = "'name' Must not be null when creating ")
        private String name;

        @ApiModelProperty(notes = "Автор аудио объекта", example = "The Prodigy")
        @NotNull(groups = {OnCreate.class}, message = "'author' Must not be null when creating ")
        private String author;

        @ApiModelProperty(notes = "Альбом аудио объекта", example = "Music For The Jilted Generation")
        private String album;

        @ApiModelProperty(notes = "Длительность трека в секундах")
        private Integer length;

        @ApiModelProperty(notes = "Количество прослушиваний")
        private Integer listening;

        @ApiModelProperty(notes = "Дата публикования медиа объекта, назначается автоматически при создании", hidden = true, example = "2020-09-14T23:24:17.900994")
        private LocalDateTime persistDateTime;

    }


