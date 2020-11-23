package com.javamentor.developer.social.platform.webapp.controllers.v2;

import com.javamentor.developer.social.platform.models.dto.media.AlbumCreateDto;
import com.javamentor.developer.social.platform.models.dto.media.AlbumDto;
import com.javamentor.developer.social.platform.models.dto.media.image.AlbumImageDto;
import com.javamentor.developer.social.platform.models.dto.media.image.ImageCreateDto;
import com.javamentor.developer.social.platform.models.dto.media.image.ImageDto;
import com.javamentor.developer.social.platform.models.entity.album.Album;
import com.javamentor.developer.social.platform.models.entity.album.AlbumImage;
import com.javamentor.developer.social.platform.models.entity.media.Image;
import com.javamentor.developer.social.platform.models.entity.media.Media;
import com.javamentor.developer.social.platform.models.util.OnCreate;
import com.javamentor.developer.social.platform.service.abstracts.dto.AlbumImageDtoService;
import com.javamentor.developer.social.platform.service.abstracts.dto.ImageDtoService;
import com.javamentor.developer.social.platform.service.abstracts.model.album.AlbumImageService;
import com.javamentor.developer.social.platform.service.abstracts.model.album.AlbumService;
import com.javamentor.developer.social.platform.service.abstracts.model.media.ImageService;
import com.javamentor.developer.social.platform.service.abstracts.model.media.MediaService;
import com.javamentor.developer.social.platform.service.abstracts.model.user.UserService;
import com.javamentor.developer.social.platform.service.impl.dto.page.PageDtoService;
import com.javamentor.developer.social.platform.webapp.converters.AlbumImageConverter;
import com.javamentor.developer.social.platform.webapp.converters.ImageConverter;
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
import java.util.*;

@RestController
@Validated
@RequestMapping(value = "/api/v2/images")
@SuppressWarnings("deprecation")
@Api(value = "ImageApi-v2", description = "Операции над изображениями")
public class ImageControllerV2 {

    private final ImageDtoService imageDTOService;
    private final ImageService imageService;
    private final AlbumImageDtoService albumImageDtoService;
    private final AlbumImageService albumImageService;
    private final UserService userService;
    private final AlbumImageConverter albumImageConverter;
    private final AlbumService albumService;
    private final MediaService mediaService;
    private final ImageConverter imageConverter;
    private final PageDtoService pageDtoService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ImageControllerV2(ImageDtoService imageDTOService, ImageService imageService, AlbumImageDtoService albumImageDtoService,
                             AlbumImageService albumImageService, UserService userService, AlbumImageConverter albumImageConverter,
                             AlbumService albumService, MediaService mediaService, ImageConverter imageConverter,
                             PageDtoService pageDtoService) {
        this.imageDTOService = imageDTOService;
        this.imageService = imageService;
        this.albumImageDtoService = albumImageDtoService;
        this.albumImageService = albumImageService;
        this.userService = userService;
        this.albumImageConverter = albumImageConverter;
        this.albumService = albumService;
        this.mediaService = mediaService;
        this.imageConverter = imageConverter;
        this.pageDtoService = pageDtoService;
    }

    @ApiOperation(value = "Создать изображение")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Изображение создано", response = ImageDto.class)})
    @PostMapping
    @Validated(OnCreate.class)
    public ResponseEntity<?> createImage(@ApiParam(value = "объект изображения")
                                             @RequestBody @Valid ImageCreateDto imageCreateDto) {
        Image newImage = imageConverter.toEntity(imageCreateDto);
        imageService.create(newImage);
        logger.info(String.format("Изображение %s создано", newImage.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(imageConverter.toImageDto(newImage));
    }

    @ApiOperation(value = "Удалить изображение")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Изображение удалено", response = String.class),
            @ApiResponse(code = 404, message = "Изображение не найдено", response = String.class)})
    @DeleteMapping(value = "/{imageId}")
    public ResponseEntity<?> deleteImage(@ApiParam(value = "Id изображения")
                                             @NotNull @PathVariable Long imageId) {
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
    public ResponseEntity<?> getImageById(@ApiParam(value = "Id изображения")
                                              @NotNull @PathVariable Long imageId) {
        Optional<ImageDto> optional = imageDTOService.getById(imageId);
        if(!optional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Image with id %s not found", imageId));
        }
        logger.info(String.format("Изображение %s получено", imageId));
        return ResponseEntity.ok().body(optional.get());
    }

    @ApiOperation(value = "Получить все изображения по Id пользователя")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Изображения получены", response = ImageDto.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = "Пользователь не найден", response = String.class)})
    @GetMapping(params = {"currentPage", "itemsOnPage", "userId"})
    public ResponseEntity<?> getAllImagesOfUser(@ApiParam(value = "Id пользователя", example = "60") @RequestParam("userId") @NotNull Long userId,
                                                @ApiParam(value = "Текущая страница", example = "0")
                                                @RequestParam("currentPage") int currentPage,
                                                @ApiParam(value = "Количество данных на страницу", example = "15")
                                                @RequestParam("itemsOnPage") int itemsOnPage) {
        if(!userService.existById(userId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("No user with id %s found", userId));
        }
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("userId", userId);
        parameters.put("methodName", "getAllImagesOfUser");
        parameters.put("currentPage", currentPage);
        parameters.put("itemsOnPage", itemsOnPage);
        logger.info(String.format("Отправлен список пустой или с изображениями пользователя с id: %s", userId));
        return ResponseEntity.status(HttpStatus.OK).body(pageDtoService.getPageDto(parameters));
    }

    @ApiOperation(value = "Создать фотоальбом")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Альбом создан", response = AlbumDto.class)})
    @Validated(OnCreate.class)
    @PostMapping(value = "/albums")
    public ResponseEntity<?> createAlbum(@ApiParam(value = "объект альбома")
                                             @Valid @NotNull @RequestBody AlbumCreateDto albumCreateDto) {
        AlbumImage newAlbumImage = albumImageConverter.toAlbumImage(albumCreateDto);
        logger.info(String.format("Фотоальбом %s создан", newAlbumImage.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(albumImageConverter.toAlbumImageDto(newAlbumImage));
    }

    @ApiOperation(value = "Удалить фотоальбом")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Фотоальбом удален", response = String.class),
            @ApiResponse(code = 404, message = "Фотоальбом не найден", response = String.class)})
    @DeleteMapping(value = "/albums/{albumId}")
    public ResponseEntity<?> deleteAlbum(@ApiParam(value = "Id альбома")
                                             @NotNull @PathVariable Long albumId) {
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
            @ApiResponse(code = 404, message = "Изображение не найдено", response = String.class)
    })
    @PutMapping(value = "/albums/{albumId}/images")
    public ResponseEntity<?> addImageToAlbum(
            @ApiParam(value = "Id альбома", example = "11")
            @PathVariable @NotNull Long albumId,
            @ApiParam(value = "Id изображения", example = "31")
            @RequestParam(value = "id") @NotNull Long imageId) {

        Optional<AlbumImage> optionalAlbumImage = albumImageService.getById(albumId);
        Optional<Image> optionalImage = imageService.getById(imageId);
        Optional<Media> optionalMedia = mediaService.getById(imageId);
        Optional<Album> optionalAlbum = albumService.getById(albumId);

        if (!optionalAlbumImage.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Album with id %s not found", albumId));
        }
        if (!optionalImage.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Image with id %s not found", imageId));
        }
        if (optionalMedia.isPresent() && optionalAlbum.isPresent()) {
            Album album = optionalAlbum.get();
            Media media = optionalMedia.get();
            media.setAlbum(album);
            mediaService.update(media);
            logger.info(String.format("Изображение %s добавлено в фотоальбом %s", imageId, albumId));
        }

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

        Optional<AlbumImage> optionalAlbumImage = albumImageService.getById(albumId);
        Optional<Image> optionalImage = imageService.getById(imageId);
        Optional<Media> optionalMedia = mediaService.getById(imageId);

        if (!optionalAlbumImage.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Album with id %s not found", albumId));
        }
        if (!optionalImage.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Image with id %s not found", imageId));
        }
        if (optionalMedia.isPresent()) {
            Media media = optionalMedia.get();

            if (Objects.nonNull(media.getAlbum())) {
                media.setAlbum(null);
                mediaService.update(media);
                logger.info(String.format("Изображение %s удалено из фотоальбома %s", imageId, albumId));
            }
        }

        return ResponseEntity.ok().body(String.format("Image %s removed from album %s", imageId, albumId));
    }

    @ApiOperation(value = "Получить содержимое фотоальбома")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "", response = ImageDto.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = "", response = String.class),
            @ApiResponse(code = 404, message = "", response = String.class)
    })
    @GetMapping(value = "/albums/{albumId}/images", params = {"currentPage", "itemsOnPage"})
    public ResponseEntity<?> getImagesFromAlbumById(
            @ApiParam(value = "Id фотоальбома", example = "101") @PathVariable @NotNull Long albumId,
            @ApiParam(value = "Текущая страница", example = "0") @RequestParam("currentPage") int currentPage,
            @ApiParam(value = "Количество данных на страницу", example = "15") @RequestParam("itemsOnPage") int itemsOnPage) {
        if(!albumImageService.existById(albumId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Album with id %s not found", albumId));
        }
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("albumId", albumId);
        parameters.put("methodName", "getImagesFromAlbumById");
        parameters.put("currentPage", currentPage);
        parameters.put("itemsOnPage", itemsOnPage);
        logger.info(String.format("Изображения из фотоальбома %s отправлены", albumId));
        return ResponseEntity.ok(pageDtoService.getPageDto(parameters));
    }

    @ApiOperation(value = "Получить фотоальбом по Id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Фотоальбом получен", response = AlbumDto.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = "Фотоальбом не найден", response = String.class)})
    @GetMapping(value = "/albums/{albumId}")
    public ResponseEntity<?> getImageAlbumById(@ApiParam(value = "Id альбома", example = "11")
                                                   @PathVariable @NotNull Long albumId) {
        Optional<AlbumImage> optionalAlbum = albumImageService.getById(albumId);
        if(!optionalAlbum.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Album with id %s not found", albumId));
        }
        logger.info(String.format("Фотоальбом %s отправлен", albumId));
        return ResponseEntity.ok(albumImageConverter.toAlbumImageDto(optionalAlbum.get()));
    }

    @ApiOperation(value = "Получить все фотоальбомы пользователя")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Фотоальбомы получены", response = AlbumDto.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = "Пользователь не найден", response = String.class)})
    @GetMapping(value = "/albums", params = {"currentPage", "itemsOnPage", "userId"})
    public ResponseEntity<?> getAllImageAlbumsOfUser(
            @ApiParam(value = "Id пользователя", example = "60") @RequestParam("userId") @NotNull Long userId,
            @ApiParam(value = "Текущая страница", example = "0") @RequestParam("currentPage") int currentPage,
            @ApiParam(value = "Количество данных на страницу", example = "15") @RequestParam("itemsOnPage") int itemsOnPage) {
        if(!userService.existById(userId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("No user with id %s found", userId));
        }
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("userId", userId);
        parameters.put("methodName", "getAllImageAlbumsOfUser");
        parameters.put("currentPage", currentPage);
        parameters.put("itemsOnPage", itemsOnPage);
        logger.info(String.format("Отправлен список пустой или с альбомами пользователя с id: %s", userId));
        return ResponseEntity.status(HttpStatus.OK).body(pageDtoService.getPageDto(parameters));
    }

}