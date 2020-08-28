package com.javamentor.developer.social.platform.audio;


import com.github.database.rider.core.api.dataset.DataSet;
import com.google.gson.Gson;
import com.javamentor.developer.social.platform.AbstractIntegrationTest;
import com.javamentor.developer.social.platform.models.dto.AlbumDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@DataSet(value = {
        "datasets/audio/usersAudioTest/Active.yml",
        "datasets/audio/usersAudioTest/User.yml",
        "datasets/audio/usersAudioTest/Role.yml",
        "datasets/audio/usersAudioTest/Status.yml",
        "datasets/audio/usersAudioTest/UsersAudiosCollections.yml",
        "datasets/audio/Media.yml",
        "datasets/audio/albumAudioTest/AudioAlbum.yml",
        "datasets/audio/albumAudioTest/Album.yml",
        "datasets/audio/albumAudioTest/UserHasAlbum.yml",
        "datasets/audio/albumAudioTest/AlbumHasAudio.yml",
        "datasets/audio/Audio.yml"}, cleanBefore = true, cleanAfter = true)
class MusicTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private Gson gson = new Gson();


    @Test
    public void getAllAudios() throws Exception {
        this.mockMvc.perform(get("/api/audios/all"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5));
    }

    @Test
    public void getPartAudios() throws Exception {
        this.mockMvc.perform(get("/api/audios/getPart?currentPage=1&itemsOnPage=2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[0].album").value("AlbumTestName 1"))
                .andExpect(jsonPath("$[0].author").value("Test Author 1"))
                .andExpect(jsonPath("$[0].icon").value("TestIcon0"))
                .andExpect(jsonPath("$[0].name").value("AudioTestName 1"))
                .andExpect(jsonPath("$[1].id").value(3))
                .andExpect(jsonPath("$[1].album").value("AlbumTestName 2"))
                .andExpect(jsonPath("$[1].author").value("Test Author 2"))
                .andExpect(jsonPath("$[1].icon").value("TestIcon2"))
                .andExpect(jsonPath("$[1].name").value("AudioTestName 2"));
    }

    @Test
    public void getAudioOfAuthor() throws Exception {
        this.mockMvc.perform(get("/api/audios/author/{author}", "Test Author 2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(3))
                .andExpect(jsonPath("$[0].album").value("AlbumTestName 2"))
                .andExpect(jsonPath("$[0].author").value("Test Author 2"))
                .andExpect(jsonPath("$[0].icon").value("TestIcon2"))
                .andExpect(jsonPath("$[0].name").value("AudioTestName 2"))
                .andExpect(jsonPath("$[1].id").value(5))
                .andExpect(jsonPath("$[1].album").value("AlbumTestName 4"))
                .andExpect(jsonPath("$[1].author").value("Test Author 2"))
                .andExpect(jsonPath("$[1].icon").value("TestIcon2"))
                .andExpect(jsonPath("$[1].name").value("AudioTestName 4"));
    }

    @Test
    public void getAudioOfInvalidAuthor() throws Exception {
        this.mockMvc.perform(get("/api/audios/author/{author}", "Test Author 999"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

    }

    @Test
    public void getAudioOfName() throws Exception {
        this.mockMvc.perform(get("/api/audios/name/{name}", "AudioTestName 2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.album").value("AlbumTestName 2"))
                .andExpect(jsonPath("$.author").value("Test Author 2"))
                .andExpect(jsonPath("$.icon").value("TestIcon2"))
                .andExpect(jsonPath("$.name").value("AudioTestName 2"));
    }

    @Test
    public void getAudioOfInvalidName() throws Exception {
        this.mockMvc.perform(get("/api/audios/name/{name}", "AudioTestName 55555"))
                .andDo(print())
                .andExpect(status().is(400))
                .andExpect(content().string("Invalid parameters"));
    }

    @Test
    public void getAudioOfUser() throws Exception {
        this.mockMvc.perform(get("/api/audios/user/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].album").value("AlbumTestName 0"))
                .andExpect(jsonPath("$[0].author").value("Test Author 0"))
                .andExpect(jsonPath("$[0].icon").value("TestIcon0"))
                .andExpect(jsonPath("$[0].name").value("AudioTestName 0"))
                .andExpect(jsonPath("$[1].id").value(3))
                .andExpect(jsonPath("$[1].album").value("AlbumTestName 2"))
                .andExpect(jsonPath("$[1].author").value("Test Author 2"))
                .andExpect(jsonPath("$[1].icon").value("TestIcon2"))
                .andExpect(jsonPath("$[1].name").value("AudioTestName 2"));
    }

    @Test
    public void PartAudioOfUser() throws Exception {
        this.mockMvc.perform(get("/api/audios/PartAudioOfUser?currentPage=0&itemsOnPage=2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].album").value("AlbumTestName 0"))
                .andExpect(jsonPath("$[0].author").value("Test Author 0"))
                .andExpect(jsonPath("$[0].icon").value("TestIcon0"))
                .andExpect(jsonPath("$[0].name").value("AudioTestName 0"))
                .andExpect(jsonPath("$[1].id").value(3))
                .andExpect(jsonPath("$[1].album").value("AlbumTestName 2"))
                .andExpect(jsonPath("$[1].author").value("Test Author 2"))
                .andExpect(jsonPath("$[1].icon").value("TestIcon2"))
                .andExpect(jsonPath("$[1].name").value("AudioTestName 2"));
    }

    @Test
    public void getAuthorAudioOfUser() throws Exception {
        this.mockMvc.perform(get("/api/audios/AuthorAudioOfUser?author=Test Author 2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(3))
                .andExpect(jsonPath("$[0].album").value("AlbumTestName 2"))
                .andExpect(jsonPath("$[0].author").value("Test Author 2"))
                .andExpect(jsonPath("$[0].icon").value("TestIcon2"))
                .andExpect(jsonPath("$[0].name").value("AudioTestName 2"));
    }

    @Test
    public void getAlbumAudioOfUser() throws Exception {
        this.mockMvc.perform(get("/api/audios/AlbumAudioOfUser?album=AlbumTestName 2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(3))
                .andExpect(jsonPath("$[0].album").value("AlbumTestName 2"))
                .andExpect(jsonPath("$[0].author").value("Test Author 2"))
                .andExpect(jsonPath("$[0].icon").value("TestIcon2"))
                .andExpect(jsonPath("$[0].name").value("AudioTestName 2"));
    }

    @Test
    public void addAudioInCollectionsOfUser() throws Exception {
        this.mockMvc.perform(post("/api/audios/addToUser?audioId=5&userId=1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void addInAlbums() throws Exception {
        this.mockMvc.perform(post("/api/audios/addInAlbums?albumId=1&audioId=1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DataSet(value = {
            "datasets/audio/usersAudioTest/Active.yml",
            "datasets/audio/usersAudioTest/User.yml",
            "datasets/audio/usersAudioTest/Role.yml",
            "datasets/audio/usersAudioTest/Status.yml"}, cleanBefore = true, cleanAfter = true)
    public void createAlbum() throws Exception {
        AlbumDto albumTest = AlbumDto.builder()
                .name("albumAudio")
                .icon("iconTest")
                .build();

        this.mockMvc.perform(post("/api/audios/createAlbum")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(gson.toJson(albumTest)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getFromAlbumOfUser() throws Exception {
        this.mockMvc.perform(get("/api/audios/getFromAlbumOfUser?albumId=1"))
                .andDo(print())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(status().isOk());
    }

    @Test
    public void getAllAlbums() throws Exception {
        this.mockMvc.perform(get("/api/audios/getAllAlbumsFromUser?userId=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

}
