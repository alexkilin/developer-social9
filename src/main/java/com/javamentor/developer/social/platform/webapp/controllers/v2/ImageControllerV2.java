package com.javamentor.developer.social.platform.webapp.controllers.v2;

import com.javamentor.developer.social.platform.models.dto.AlbumCreateDto;
import com.javamentor.developer.social.platform.models.dto.AlbumDto;
import com.javamentor.developer.social.platform.models.dto.ImageCreateDto;
import com.javamentor.developer.social.platform.models.dto.ImageDto;
import com.javamentor.developer.social.platform.models.entity.album.Album;
import com.javamentor.developer.social.platform.models.entity.album.AlbumImage;
import com.javamentor.developer.social.platform.models.entity.media.Media;
import com.javamentor.developer.social.platform.models.util.OnCreate;
import com.javamentor.developer.social.platform.service.abstracts.dto.AlbumDtoService;
import com.javamentor.developer.social.platform.service.abstracts.dto.ImageDtoService;
import com.javamentor.developer.social.platform.service.abstracts.model.album.AlbumImageService;
import com.javamentor.developer.social.platform.service.abstracts.model.album.AlbumService;
import com.javamentor.developer.social.platform.service.abstracts.model.media.ImageService;
import com.javamentor.developer.social.platform.service.abstracts.model.media.MediaService;
import com.javamentor.developer.social.platform.service.abstracts.model.user.UserService;
import com.javamentor.developer.social.platform.webapp.converters.AlbumConverter;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;


@RestController
@Validated
@RequestMapping(value = "/api/v2/images")
@SuppressWarnings("deprecation")
@Api(value = "ImageApi-v2", description = "Операции над изображениями")
public class ImageControllerV2 {

    private final ImageDtoService imageDTOService;
    private final ImageService imageService;
    private final AlbumDtoService albumDtoService;
    private final AlbumImageService albumImageService;
    private final UserService userService;
    private final AlbumConverter albumConverter;
    private final AlbumService albumService;
    private final MediaService mediaService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ImageControllerV2(ImageDtoService imageDTOService, ImageService imageService, AlbumDtoService albumDtoService, AlbumImageService albumImageService, UserService userService, AlbumConverter albumConverter, AlbumService albumService, MediaService mediaService) {
        this.imageDTOService = imageDTOService;
        this.imageService = imageService;
        this.albumDtoService = albumDtoService;
        this.albumImageService = albumImageService;
        this.userService = userService;
        this.albumConverter = albumConverter;
        this.albumService = albumService;
        this.mediaService = mediaService;
    }

    @ApiOperation(value = "Создать изображение")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Изображение создано", response = ImageDto.class)})
    @PostMapping(value = "")
    @Validated(OnCreate.class)
    public ResponseEntity<?> createImage(@ApiParam(value = "объект изображения") @RequestBody @Valid ImageCreateDto imageCreateDto) {
        ImageDto imageDto = imageDTOService.create(imageCreateDto);
        logger.info(String.format("Изображение %s создано", imageDto.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(imageDto);
    }

    @ApiOperation(value = "Удалить изображение")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Изображение удалено", response = String.class),
            @ApiResponse(code = 404, message = "Изображение не найдено", response = String.class)})
    @DeleteMapping(value = "/{imageId}")
    public ResponseEntity<?> deleteImage(@ApiParam(value = "Id изображения") @NotNull @PathVariable Long imageId) {
        if(!imageService.existById(imageId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Image with id %s not found", imageId));
        }
        imageService.deleteById(imageId);
        logger.info(String.format("Изображение %s удалено", imageId));
        return ResponseEntity.ok("Deleted");
    }

    @ApiOperation(value = "Получить изображение по Id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Изображение получено", response = ImageDto.class),
            @ApiResponse(code = 404, message = "Изображение не найдено", response = String.class)})
    @GetMapping(value = "/{imageId}")
    public ResponseEntity<?> getImageById(@ApiParam(value = "Id изображения") @NotNull @PathVariable Long imageId) {
        Optional<ImageDto> optional = imageDTOService.getById(imageId);
        if(!optional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Image with id %s not found", imageId));
        }
        logger.info(String.format("Изображение %s получено", imageId));
        return ResponseEntity.status(HttpStatus.OK).body(optional.get());
    }

    @ApiOperation(value = "Получить все изображения по Id пользователя")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Изображения получены", response = ImageDto.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = "Пользователь не найден", response = String.class),
            @ApiResponse(code = 404, message = "Изображения не найдены", response = String.class)})
    @GetMapping(value = "")
    public ResponseEntity<?> getAllImagesOfUser(@ApiParam(value = "Id пользователя", example = "60") @RequestParam("userId") @NotNull Long userId,
                                                @ApiParam(value = "Отступ", example = "0") @RequestParam(value = "offset", defaultValue = "0", required = false) @NotNull Integer offset,
                                                @ApiParam(value = "Количество данных на страницу", example = "5") @RequestParam(value = "limit", defaultValue = "20",  required = false) @NotNull Integer limit) {
        if(!userService.existById(userId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("No user with id %s found", userId));
        }
        List<ImageDto> imageDtoList = imageDTOService.getAllByUserId(offset, limit, userId);
        if(imageDtoList.size() == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("No images for user id %s", userId));
        }
        logger.info(String.format("Изображения пользователя %s отправлены", userId));
        return ResponseEntity.status(HttpStatus.OK).body(imageDtoList);
    }

    @ApiOperation(value = "Создать фотоальбом")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Альбом создан", response = AlbumDto.class)})
    @Validated(OnCreate.class)
    @PostMapping(value = "/albums")
    public ResponseEntity<?> createAlbum(@ApiParam(value = "объект альбома") @Valid @NotNull @RequestBody AlbumCreateDto albumCreateDto) {
        AlbumDto albumDto = albumDtoService.createAlbumImage(albumCreateDto);
        logger.info(String.format("Фотоальбом %s создан", albumDto.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(albumDto);
    }

    @ApiOperation(value = "Удалить фотоальбом")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Фотоальбом удален", response = String.class),
            @ApiResponse(code = 404, message = "Фотоальбом не найден", response = String.class)})
    @DeleteMapping(value = "/albums/{albumId}")
    public ResponseEntity<?> deleteAlbum(@ApiParam(value = "Id альбома") @NotNull @PathVariable Long albumId) {
        if(!albumImageService.existById(albumId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Album with id %s not found", albumId));
        }
        albumImageService.deleteById(albumId);
        logger.info(String.format("Фотоальбом %s удален", albumId));
        return ResponseEntity.ok("Deleted");
    }

    @ApiOperation(value = "Добавить существующее изображение в фотоальбом")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Изображение добавлено", response = String.class),
            @ApiResponse(code = 404, message = "Фотоальбом не найден", response = String.class),
            @ApiResponse(code = 404, message = "Изображение не найдено", response = String.class)})
    @PutMapping(value = "/albums/{albumId}/images")
    public ResponseEntity<?> addImageToAlbum(@ApiParam(value = "Id альбома", example = "11") @PathVariable @NotNull Long albumId,
                                             @ApiParam(value = "Id изображения", example = "31") @RequestParam(value = "id") @NotNull Long imageId) {
        if (!albumImageService.existById(albumId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Album with id %s not found", albumId));
        }
        if (!imageService.existById(imageId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Image with id %s not found", imageId));
        }
        Album album = albumService.getById(albumId).get();
        Media media = mediaService.getById(imageId).get();
        media.setAlbum(album);
        mediaService.update(media);
        logger.info(String.format("Изображение %s добавлено в фотоальбом %s", imageId, albumId));
        return ResponseEntity.ok().body(String.format("Image %s added to album %s", imageId, albumId));
    }

    @ApiOperation(value = "Удалить существующее изображение из фотоальбома")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Изображение удалено", response = String.class),
            @ApiResponse(code = 404, message = "Фотоальбом не найден", response = String.class),
            @ApiResponse(code = 404, message = "Изображение не найдено", response = String.class)})
    @DeleteMapping(value = "/albums/{albumId}/images")
    public ResponseEntity<?> removeImageFromAlbum(@ApiParam(value = "Id альбома", example = "11") @PathVariable @NotNull Long albumId,
                                             @ApiParam(value = "Id изображения", example = "31") @RequestParam(value = "id") @NotNull Long imageId) {
        if (!albumImageService.existById(albumId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Album with id %s not found", albumId));
        }
        if (!imageService.existById(imageId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Image with id %s not found", imageId));
        }
        Media media = mediaService.getById(imageId).get();
        media.setAlbum(null);
        mediaService.update(media);
        logger.info(String.format("Изображение %s удалено из фотоальбома %s", imageId, albumId));
        return ResponseEntity.ok().body(String.format("Image %s removed from album %s", imageId, albumId));
    }

    @ApiOperation(value = "Получить содержимое фотоальбома")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "", response = ImageDto.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = "", response = String.class),
            @ApiResponse(code = 404, message = "", response = String.class)
    })
    @GetMapping(value = "/albums/{albumId}/images")
    public ResponseEntity<?> getImagesFromAlbumById(
            @ApiParam(value = "Id фотоальбома", example = "101") @PathVariable @NotNull Long albumId,
            @ApiParam(value = "Отступ", example = "0") @RequestParam("offset") @NotNull int offset,
            @ApiParam(value = "Количество данных на страницу", example = "5") @RequestParam("limit") @NotNull int limit) {
        if(!albumImageService.existById(albumId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Album with id %s not found", albumId));
        }
        List<ImageDto> imageDtoList = imageDTOService.getAllByAlbumId(offset, limit, albumId);
        logger.info(String.format("Изображения из фотоальбома %s отправлены", albumId));
        return ResponseEntity.ok(imageDtoList);
    }

    @ApiOperation(value = "Получить фотоальбом по Id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Фотоальбом получен", response = AlbumDto.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = "Фотоальбом не найден", response = String.class)})
    @GetMapping(value = "/albums/{albumId}")
    public ResponseEntity<?> getImageAlbumById(
            @ApiParam(value = "Id альбома", example = "11") @PathVariable @NotNull Long albumId) {
        Optional<AlbumImage> optionalAlbum = albumImageService.getOptionalById(albumId);
        if(!optionalAlbum.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Album with id %s not found", albumId));
        }
        logger.info(String.format("Фотоальбом %s отправлен", albumId));
        return ResponseEntity.ok(albumConverter.toAlbumDto(optionalAlbum.get()));
    }

    @ApiOperation(value = "Получить все фотоальбомы пользователя")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Фотоальбомы получены", response = AlbumDto.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = "Фотоальбомы не найдены", response = String.class)})
    @GetMapping(value = "/albums")
    public ResponseEntity<?> getAllImageAlbumsOfUser(
            @ApiParam(value = "Id пользователя", example = "60") @RequestParam("userId") @NotNull Long userId,
            @ApiParam(value = "Отступ", example = "0") @RequestParam("offset") @NotNull int offset,
            @ApiParam(value = "Количество данных на страницу", example = "5") @RequestParam("limit") @NotNull int limit) {
        if(!userService.existById(userId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("No user with id %s found", userId));
        }
        List<AlbumDto> albumDtoList = albumDtoService.getAllByUserId(userId);
        if(albumDtoList.size() == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("No albums for user id %s", userId));
        }
        logger.info(String.format("Фотоальбомы пользователя %s отправлены", userId));
        return ResponseEntity.status(HttpStatus.OK).body(albumDtoList);
    }

}