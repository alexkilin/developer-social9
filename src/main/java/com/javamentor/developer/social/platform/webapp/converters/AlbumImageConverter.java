package com.javamentor.developer.social.platform.webapp.converters;

import com.javamentor.developer.social.platform.models.dto.media.AlbumCreateDto;
import com.javamentor.developer.social.platform.models.dto.media.image.AlbumImageDto;
import com.javamentor.developer.social.platform.models.entity.album.AlbumImage;
import com.javamentor.developer.social.platform.models.entity.media.MediaType;
import com.javamentor.developer.social.platform.models.entity.user.User;
import com.javamentor.developer.social.platform.service.abstracts.model.user.UserService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Mapper(componentModel = "spring", imports = {MediaType.class})
public abstract class AlbumImageConverter {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Mappings({
            @Mapping(target = "album.mediaType", expression = "java( MediaType.IMAGE)"),
            @Mapping(target = "album.name", source = "albumCreateDto.name"),
            @Mapping(target = "album.icon", source = "albumCreateDto.icon"),
            @Mapping(target = "album.userOwnerId", source = "albumCreateDto.userId", qualifiedByName = "userSetter")
    })
    public abstract AlbumImage toAlbumImage(AlbumCreateDto albumCreateDto);

    @Mappings({
            @Mapping(source = "albumImage.id", target = "id"),
            @Mapping(source = "albumImage.album.icon", target = "icon"),
            @Mapping(source = "albumImage.album.name", target = "name"),
            @Mapping(source = "albumImage.album.persistDate", target = "persistDate"),
            @Mapping(source = "albumImage.album.lastRedactionDate", target = "lastRedactionDate")
    })
    public abstract AlbumImageDto toAlbumImageDto(AlbumImage albumImage);

    @Named("userSetter")
    User userSetter(Long userId) {
        Optional<User> userOptional = userService.getById(userId);

        return userOptional.orElseThrow(() -> new EntityNotFoundException(String.format("User ?? id %s ???? ????????????????????", userId)));
    }
}
