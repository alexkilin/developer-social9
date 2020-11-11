package com.javamentor.developer.social.platform.dao.abstracts.dto;


import com.javamentor.developer.social.platform.models.dto.LanguageDto;
<<<<<<< src/main/java/com/javamentor/developer/social/platform/dao/abstracts/dto/UserDtoDao.java
import com.javamentor.developer.social.platform.models.dto.UserFriendDto;
=======
import com.javamentor.developer.social.platform.models.dto.users.UserDto;
>>>>>>> src/main/java/com/javamentor/developer/social/platform/dao/abstracts/dto/UserDtoDao.java

import java.util.List;
import java.util.Optional;

public interface UserDtoDao {
    List<UserDto> getUserDtoList();
    Optional<UserDto> getUserDtoById(Long id);
    List<LanguageDto> getUserLanguageDtoById(Long id);
    List<UserFriendDto> getUserFriendsDtoById(Long id, int currentPage, int itemsOnPage);
}
