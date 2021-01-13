package com.javamentor.developer.social.platform.restv2.controllers;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.SeedStrategy;
import com.google.gson.Gson;
import com.javamentor.developer.social.platform.models.dto.*;
import com.javamentor.developer.social.platform.models.dto.users.UserRegisterDto;
import com.javamentor.developer.social.platform.models.dto.users.UserResetPasswordDto;
import com.javamentor.developer.social.platform.models.dto.users.UserUpdateInfoDto;
import com.javamentor.developer.social.platform.service.abstracts.model.user.UserService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


@DataSet(value = {
        "datasets/restv2/user/userResources/language.yml" ,
        "datasets/restv2/user/userResources/user_languages.yml" ,
        "datasets/restv2/user/userResources/Active.yml" ,
        "datasets/restv2/user/userResources/Role.yml" ,
        "datasets/restv2/user/userResources/userFriends.yml" ,
        "datasets/restv2/user/userResources/User.yml"
}, strategy = SeedStrategy.REFRESH, cleanAfter = true)
@Sql(value = "/create_user_before.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@WithUserDetails(userDetailsServiceBeanName = "custom", value = "admin666@user.ru")
public class UserControllerV2Tests extends AbstractIntegrationTest {

    private final String apiUrl = "/api/v2/users";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    private final Gson gson = new Gson();

    @Test
    void createUser() throws Exception {
        UserRegisterDto userDto = UserRegisterDto.builder()
                .email("jm.platform.noreply@gmail.com")
                .password("AdminPwd123")
                .firstName("AdminFirstName")
                .lastName("AdminLastName")
                .build();

        mockMvc.perform(post(apiUrl + "/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(userDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("jm.platform.noreply@gmail.com"))
                .andExpect(jsonPath("$.password").isNotEmpty())
                .andExpect(jsonPath("$.firstName").value("AdminFirstName"))
                .andExpect(jsonPath("$.lastName").value("AdminLastName"))
                .andExpect(jsonPath("$.activeName").value("ACTIVE"))
                .andExpect(jsonPath("$.roleName").value("USER"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        Assert.assertFalse("Проверка аттрибута подтверждения почты нового пользователя",
                userService.getByEmail("jm.platform.noreply@gmail.com").get().isEnabled());

        userDto.setLastName("NewAdminLastName");
        mockMvc.perform(post(apiUrl + "/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(userDto)))
                .andDo(print())
                .andExpect(status().is(230))
                .andExpect(jsonPath("$.email").value("jm.platform.noreply@gmail.com"))
                .andExpect(jsonPath("$.password").isNotEmpty())
                .andExpect(jsonPath("$.firstName").value("AdminFirstName"))
                .andExpect(jsonPath("$.lastName").value("NewAdminLastName"))
                .andExpect(jsonPath("$.activeName").value("ACTIVE"))
                .andExpect(jsonPath("$.roleName").value("USER"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        // Действительный токен пользователя jm.platform.noreply@gmail.com от 2021-01-13 03:57:17 UTC
        // при времени жизни токена 100 лет
        String validToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqbS5wbGF0Zm9ybS5ub3JlcGx5QGdtYWlsLmNvbSIsImlhdCI6MTY" +
                "xMDUxMDIzNywiZXhwIjo0NzY2MjcwMjM3fQ.qOfFk9VWWespOKqR1ScyPD0mrZ3qavlGz3j7sRs_xcw";
        mockMvc.perform(post(apiUrl + "/verifyemail")
                .contentType(MediaType.TEXT_PLAIN)
                .content(validToken)
                .with(anonymous()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("jm.platform.noreply@gmail.com"))
                .andExpect(jsonPath("$.password").isNotEmpty())
                .andExpect(jsonPath("$.firstName").value("AdminFirstName"))
                .andExpect(jsonPath("$.lastName").value("NewAdminLastName"))
                .andExpect(jsonPath("$.activeName").value("ACTIVE"))
                .andExpect(jsonPath("$.roleName").value("USER"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        Assert.assertTrue("Проверка аттрибута подтверждения почты нового пользователя после подтвеждения",
                userService.getByEmail("jm.platform.noreply@gmail.com").get().isEnabled());

        mockMvc.perform(post(apiUrl + "/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(userDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User with email 'jm.platform.noreply@gmail.com' already registered. Email should be unique"));

        // Просроченный токен пользователя jm.platform.noreply@gmail.com от 2021-01-13 03:48:12 UTC
        // при времени жизни токена 3 cекунды
        String expiredToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqbS5wbGF0Zm9ybS5ub3JlcGx5QGdtYWlsLmNvbSIsImlhdCI6MTY" +
                "xMDUwOTY5MiwiZXhwIjoxNjEwNTA5Njk1fQ.CnUS3Ae5NQngF1_CBnYGFLEHI2GcDS8CQ2t4UMZ1uKA";
        mockMvc.perform(post(apiUrl + "/verifyemail")
                .contentType(MediaType.TEXT_PLAIN)
                .content(expiredToken)
                .with(anonymous()))
                .andExpect(status().is(HttpStatus.REQUEST_TIMEOUT.value()))
                .andExpect(content().string("Registration code is outdated"));

        // Токен несуществующего пользователя some.invalid.email@gmail.com от 2021-01-13 04:37:08 UTC
        // при времени жизни токена 100 лет
        String unknownUserToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzb21lLmludmFsaWQuZW1haWxAZ21haWwuY29tIiwiaWF0Ijox" +
                "NjEwNTEyNjI4LCJleHAiOjQ3NjYyNzI2Mjh9.x5jwfSXL9eXr3nlomc9RG0kIAWYjs49Buey_RMY0CKU";
        mockMvc.perform(post(apiUrl + "/verifyemail")
                .contentType(MediaType.TEXT_PLAIN)
                .content(unknownUserToken)
                .with(anonymous()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(String.format("No matching user record found for verification code '%s'", unknownUserToken)));

        // Некорректный токен
        String notAToken = "eyJ";
        mockMvc.perform(post(apiUrl + "/verifyemail")
                .contentType(MediaType.TEXT_PLAIN)
                .content(notAToken)
                .with(anonymous()))
                .andExpect(status().isNotAcceptable())
                .andExpect(content().string("Registration code is corrupted"));
    }

    @Test
    void updateUser() throws Exception {
        UserUpdateInfoDto userDto = UserUpdateInfoDto.builder()
                .userId(2L)
                .firstName("NewName")
                .lastName("NewLastName")
                .email("newadmin123@admin.ru")
                .build();

        mockMvc.perform(put(apiUrl + "/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(userDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("NewName"))
                .andExpect(jsonPath("$.lastName").value("NewLastName"))
                .andExpect(jsonPath("$.email").value("newadmin123@admin.ru"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void changeUserPassword() throws Exception {
        String pwd = "ocheSlozhniParol111";
        UserResetPasswordDto userResetPasswordDto = UserResetPasswordDto.builder()
                .password(pwd)
                .build();

        mockMvc.perform(patch(apiUrl + "/2/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(userResetPasswordDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Password changed for user 2"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        Assert.assertTrue("Сравнение с паролем в базе, подтверждение изменения" ,
                passwordEncoder.matches(pwd , userService.getById(2L).get().getPassword()));
    }

    @Test
    void getUsers() throws Exception {
        mockMvc.perform(get(apiUrl + "/")
                .param("currentPage" , "1")
                .param("itemsOnPage" , "5"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(5))

                .andExpect(jsonPath("$.items[1].userId").value(2))
                .andExpect(jsonPath("$.items[1].firstName").value("Admin1"))
                .andExpect(jsonPath("$.items[1].lastName").value("LastNameAdmin1"))
                .andExpect(jsonPath("$.items[1].dateOfBirth").value("11.07.2009"))
                .andExpect(jsonPath("$.items[1].education").value("MIT University"))
                .andExpect(jsonPath("$.items[1].aboutMe").value("My description about life - Admin1"))
                .andExpect(jsonPath("$.items[1].avatar").value("www.myavatar0.ru/9090"))
                .andExpect(jsonPath("$.items[1].email").value("admin2@user.ru"))
                .andExpect(jsonPath("$.items[1].password").value("userpass0"))
                .andExpect(jsonPath("$.items[1].city").value("SPb"))
                .andExpect(jsonPath("$.items[1].linkSite").isEmpty())
                .andExpect(jsonPath("$.items[1].profession").value("Plumber"))
                .andExpect(jsonPath("$.items[1].roleName").value("USER"))
                .andExpect(jsonPath("$.items[1].status").value("Pureness and perfection"))
                .andExpect(jsonPath("$.items[1].activeName").value("ACTIVE"));
    }

    @Test
    void updateStatus() throws Exception {
        StatusDto statusDto = StatusDto.builder()
                .userId(2L)
                .status("Outer space exploration")
                .build();

        mockMvc.perform(patch(apiUrl + "/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(statusDto)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Outer space exploration"));

        statusDto = StatusDto.builder()
                .userId(222L)
                .status("Outer space exploration")
                .build();

        mockMvc.perform(patch(apiUrl + "/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(statusDto)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("User with ID: 222 does not exist."));
    }

//    @Test
//    void deleteUser() throws Exception {
//        mockMvc.perform(delete(apiUrl + "/{userId}", 2L))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().string("User with ID: 2 deleted"));
//
//        mockMvc.perform(delete(apiUrl + "/{userId}", 2L))
//                .andDo(print())
//                .andExpect(status().isBadRequest())
//                .andExpect(content().string("User with ID: 2 does not exist."));
//    }

    @Test
    void getUserById() throws Exception {
        mockMvc.perform(get(apiUrl + "/{userId}" , 2L))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(2))
                .andExpect(jsonPath("$.firstName").value("Admin1"))
                .andExpect(jsonPath("$.lastName").value("LastNameAdmin1"))
                .andExpect(jsonPath("$.dateOfBirth").value("11.07.2009"))
                .andExpect(jsonPath("$.education").value("MIT University"))
                .andExpect(jsonPath("$.aboutMe").value("My description about life - Admin1"))
                .andExpect(jsonPath("$.avatar").value("www.myavatar0.ru/9090"))
                .andExpect(jsonPath("$.email").value("admin2@user.ru"))
                .andExpect(jsonPath("$.password").value("userpass0"))
                .andExpect(jsonPath("$.city").value("SPb"))
                .andExpect(jsonPath("$.linkSite").isEmpty())
                .andExpect(jsonPath("$.profession").value("Plumber"))
                .andExpect(jsonPath("$.roleName").value("USER"))
                .andExpect(jsonPath("$.status").value("Pureness and perfection"))
                .andExpect(jsonPath("$.activeName").value("ACTIVE"));

        mockMvc.perform(get(apiUrl + "/{userId}" , 222L))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("User with ID: 222 does not exist."));
    }

    @Test
    void getFriendsOfUserById() throws Exception {
        mockMvc.perform(get(apiUrl + "/{userId}/friends" , 2L)
                .param("currentPage" , "1")
                .param("itemsOnPage" , "5"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(4));

        mockMvc.perform(get(apiUrl + "/{userId}/friends" , 222L)
                .param("currentPage" , "1")
                .param("itemsOnPage" , "5"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("User with ID: 222 does not exist."));
    }

    @Test
    void GetAllUsersWithFilters() throws Exception {
        mockMvc.perform(get(apiUrl + "/GetAllUsersWithFilters")
                .param("currentPage" , "1")
                .param("itemsOnPage" , "10")
                .param("education" , "Harvard"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(1))
                .andExpect(jsonPath("$.items[0].firstName").value("Admin0"));

        mockMvc.perform(get(apiUrl + "/GetAllUsersWithFilters")
                .param("currentPage" , "1")
                .param("itemsOnPage" , "10")
                .param("city" , "SPb"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(4));

        mockMvc.perform(get(apiUrl + "/GetAllUsersWithFilters")
                .param("currentPage" , "1")
                .param("itemsOnPage" , "10")
                .param("city" , "SPb")
                .param("profession" , "Creator"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(1));

        mockMvc.perform(get(apiUrl + "/GetAllUsersWithFilters")
                .param("currentPage" , "1")
                .param("itemsOnPage" , "10")
                .param("startDateOfBirth" , "1990-05-30")
                .param("endDateOfBirth" , "2012-03-30"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(4));

        mockMvc.perform(get(apiUrl + "/GetAllUsersWithFilters")
                .param("currentPage" , "1")
                .param("itemsOnPage" , "10")
                .param("endDateOfBirth" , "2003-12-30"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(2));
    }

}