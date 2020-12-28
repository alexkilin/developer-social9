package com.javamentor.developer.social.platform.service.impl;

import com.javamentor.developer.social.platform.models.entity.album.Album;
import com.javamentor.developer.social.platform.models.entity.album.AlbumAudios;
import com.javamentor.developer.social.platform.models.entity.album.AlbumImage;
import com.javamentor.developer.social.platform.models.entity.album.AlbumVideo;
import com.javamentor.developer.social.platform.models.entity.chat.GroupChat;
import com.javamentor.developer.social.platform.models.entity.chat.Message;
import com.javamentor.developer.social.platform.models.entity.chat.SingleChat;
import com.javamentor.developer.social.platform.models.entity.comment.Comment;
import com.javamentor.developer.social.platform.models.entity.comment.CommentType;
import com.javamentor.developer.social.platform.models.entity.comment.MediaComment;
import com.javamentor.developer.social.platform.models.entity.comment.PostComment;
import com.javamentor.developer.social.platform.models.entity.group.Group;
import com.javamentor.developer.social.platform.models.entity.group.GroupCategory;
import com.javamentor.developer.social.platform.models.entity.group.GroupHasUser;
import com.javamentor.developer.social.platform.models.entity.like.*;
import com.javamentor.developer.social.platform.models.entity.media.*;
import com.javamentor.developer.social.platform.models.entity.post.Bookmark;
import com.javamentor.developer.social.platform.models.entity.post.Post;
import com.javamentor.developer.social.platform.models.entity.post.Tag;
import com.javamentor.developer.social.platform.models.entity.post.Topic;
import com.javamentor.developer.social.platform.models.entity.post.UserTabs;
import com.javamentor.developer.social.platform.models.entity.token.VerificationToken;
import com.javamentor.developer.social.platform.models.entity.user.*;
import com.javamentor.developer.social.platform.service.abstracts.model.album.AlbumAudioService;
import com.javamentor.developer.social.platform.service.abstracts.model.album.AlbumImageService;
import com.javamentor.developer.social.platform.service.abstracts.model.album.AlbumService;
import com.javamentor.developer.social.platform.service.abstracts.model.album.AlbumVideoService;
import com.javamentor.developer.social.platform.service.abstracts.model.chat.GroupChatService;
import com.javamentor.developer.social.platform.service.abstracts.model.chat.MessageService;
import com.javamentor.developer.social.platform.service.abstracts.model.chat.SingleChatService;
import com.javamentor.developer.social.platform.service.abstracts.model.comment.MediaCommentService;
import com.javamentor.developer.social.platform.service.abstracts.model.comment.PostCommentService;
import com.javamentor.developer.social.platform.service.abstracts.model.group.GroupCategoryService;
import com.javamentor.developer.social.platform.service.abstracts.model.group.GroupHasUserService;
import com.javamentor.developer.social.platform.service.abstracts.model.group.GroupService;
import com.javamentor.developer.social.platform.service.abstracts.model.like.CommentLikeService;
import com.javamentor.developer.social.platform.service.abstracts.model.like.MessageLikeService;
import com.javamentor.developer.social.platform.service.abstracts.model.like.PostLikeService;
import com.javamentor.developer.social.platform.service.abstracts.model.media.AudiosService;
import com.javamentor.developer.social.platform.service.abstracts.model.media.ImageService;
import com.javamentor.developer.social.platform.service.abstracts.model.media.PlaylistService;
import com.javamentor.developer.social.platform.service.abstracts.model.media.VideosService;
import com.javamentor.developer.social.platform.service.abstracts.model.post.BookmarkService;
import com.javamentor.developer.social.platform.service.abstracts.model.post.TagService;
import com.javamentor.developer.social.platform.service.abstracts.model.post.TopicService;
import com.javamentor.developer.social.platform.service.abstracts.model.post.UserTabsService;
import com.javamentor.developer.social.platform.service.abstracts.model.token.VerificationTokenService;
import com.javamentor.developer.social.platform.service.abstracts.model.user.FollowerService;
import com.javamentor.developer.social.platform.service.abstracts.model.user.FriendService;
import com.javamentor.developer.social.platform.service.abstracts.model.user.UserService;
import com.javamentor.developer.social.platform.service.abstracts.util.VerificationEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class TestDataInitService {

    private final LocalDateTime userLocalDate = LocalDateTime.of(2014, 11, 11, 17, 45);
    private final LocalDateTime userLocalDateNow = LocalDateTime.now();
    private final Date dateDateClass = new Date(1212121212121L);

    // You can change size of init data by variable "k"
    private final int k = 1;
    private final int numOfUsers = 100 * k;
    private final int numOfMedias = 100 * k;
    private final int numOfChats = 20 * k;
    private final int numOfMessages = 100 * k;
    private final int numOfAlbums = 100 * k;
    private final int numOfFriends = 500 * k;
    private final int numOfFollowers = 500 * k;
    private final int numOfPosts = 100 * k;
    private final int numOfGroups = 20 * k;
    private final int numOfPostComments = 100 * k;
    private final int numOfPostLikes = 100 * k;
    private final int numOfCommentLikes = 100 * k;
    private final int numOfMessagesLikes = 100 * k;
    private final int numOfMediaComments = 100 * k;
    private final int numOfTags = 100 * k;
    private final int numOfTopics = 100 * k;

    private final User[] users = new User[numOfUsers];
    private final Media[] medias = new Media[numOfMedias];
    private final Message[] messages = new Message[numOfMessages];
    private final Post[] posts = new Post[numOfPosts];
    private final Group[] groups = new Group[numOfGroups];
    private final Comment[] postComments = new Comment[numOfPostComments];
    private final Like[] postLikes = new Like[numOfPostLikes];
    private final Like[] commentLikes = new Like[numOfCommentLikes];
    private final Like[] messageLikes = new Like[numOfMessagesLikes];
    private final Comment[] mediaComments = new Comment[numOfMediaComments];
    private final Album[] album = new Album[numOfAlbums];
    private final Tag[] tags = new Tag[numOfTags];
    private final SingleChat[] singleChats = new SingleChat[numOfChats];
    private Audios[] audios = new Audios[numOfMedias];
    private final Topic[] topics = new Topic[numOfTopics];

    private final UserService userService;
    private final VerificationTokenService verificationTokenService;
    private final FollowerService followerService;
    private final FriendService friendService;
    private final AlbumService albumService;
    private final AudiosService audiosService;
    private final ImageService imageService;
    private final VideosService videosService;
    private final CommentLikeService commentLikeService;
    private final MessageLikeService messageLikeService;
    private final PostLikeService postLikeService;
    private final GroupHasUserService groupHasUserService;
    private final GroupService groupService;
    private final MediaCommentService mediaCommentService;
    private final PostCommentService postCommentService;
    private final MessageService messageService;
    private final AlbumAudioService albumAudiosService;
    private final AlbumImageService albumImageService;
    private final AlbumVideoService albumVideoService;
    private final UserTabsService userTabsService;
    private final TagService tagService;
    private final TopicService topicService;

    private final SingleChatService singleChatService;
    private final GroupChatService groupChatService;
    private final GroupCategoryService groupCategoryService;
    private final BookmarkService bookmarkService;
    private final PlaylistService playlistService;

    @Autowired
    public TestDataInitService(UserService userService,
                               VerificationTokenService verificationTokenService,
                               FollowerService followerService,
                               FriendService friendService,
                               AlbumService albumService,
                               AudiosService audiosService,
                               ImageService imageService,
                               VideosService videosService,
                               CommentLikeService commentLikeService,
                               MessageLikeService messageLikeService,
                               PostLikeService postLikeService,
                               GroupHasUserService groupHasUserService,
                               GroupService groupService,
                               MediaCommentService mediaCommentService,
                               PostCommentService postCommentService,
                               MessageService messageService,
                               AlbumAudioService albumAudioService,
                               AlbumImageService albumImageService,
                               AlbumVideoService albumVideoService,
                               UserTabsService userTabsService,
                               TagService tagService,
                               SingleChatService singleChatService,
                               GroupChatService groupChatService,
                               GroupCategoryService groupCategoryService,
                               BookmarkService bookmarkService,
                               PlaylistService playlistService,
                               TopicService topicService) {
        this.userService = userService;
        this.verificationTokenService = verificationTokenService;
        this.followerService = followerService;
        this.friendService = friendService;
        this.albumService = albumService;
        this.audiosService = audiosService;
        this.imageService = imageService;
        this.videosService = videosService;
        this.commentLikeService = commentLikeService;
        this.messageLikeService = messageLikeService;
        this.postLikeService = postLikeService;
        this.groupHasUserService = groupHasUserService;
        this.groupService = groupService;
        this.mediaCommentService = mediaCommentService;
        this.postCommentService = postCommentService;
        this.messageService = messageService;
        this.albumAudiosService = albumAudioService;
        this.albumImageService = albumImageService;
        this.albumVideoService = albumVideoService;
        this.userTabsService = userTabsService;
        this.tagService = tagService;
        this.singleChatService = singleChatService;
        this.groupChatService = groupChatService;
        this.groupCategoryService = groupCategoryService;
        this.bookmarkService = bookmarkService;
        this.playlistService = playlistService;
        this.topicService = topicService;
    }

    public void createEntity() {
        createUserEntity();
        createMediaEntity();
        createTagEntity();
        createMessageEntity();
        createAlbumEntity();
        createFriendEntity();
        createFollowerEntity();
        createPostEntity();
        createGroupEntity();
        createGroupHasUserEntity();
        createPostCommentEntity();
        createPostLikeEntity();
        createCommentLikeEntity();
        createMessageLikeEntity();
        createMediaCommentEntity();
        createAudiosEntity();
        createImageEntity();
        createVideosEntity();
        createAlbumAudiosEntity();
        createAlbumImageEntity();
        createAlbumVideoEntity();
        createUserTabsEntity();
        createSingleChatEntity();
        createGroupChatEntity();
        createBookmarks();
        createPlaylists();
        addAudiosToUserCollection();
        createTopicEntity();
    }

    private void createUserEntity() {
        Active active = Active.builder()
                .name("ACTIVE")
                .build();
        Active disabled = Active.builder()
                .name("DISABLED")
                .build();

        Language language = Language.builder()
                .name("Russian")
                .build();
        Language language2 = Language.builder()
                .name("English")
                .build();

        Set<Language> langSet = new HashSet<>();
        langSet.add(language);
        Set<Language> langSet2 = new HashSet<>();
        langSet2.add(language2);

        Role roleUser = Role.builder()
                .name("USER")
                .build();

        Role roleAdmin = Role.builder()
                .name("ADMIN")
                .build();

        String name;
        String emailName;
        Active activityTest;
        Set<Language> languageTestSet;
        Role role;
        String statusTest;

        for (int i = 0; i != numOfUsers; i++) {
            if (i == 0) {
                name = "Admin";
                emailName = "admin";
                activityTest = active;
                languageTestSet = langSet;
                role = roleAdmin;
                statusTest = "free";
            } else if (i % 2 == 0) {
                name = "User";
                emailName = "user";
                activityTest = active;
                languageTestSet = langSet;
                role = roleUser;
                statusTest = "free";
            } else {
                name = "User";
                emailName = "user";
                activityTest = disabled;
                languageTestSet = langSet2;
                role = roleUser;
                statusTest = "free";
            }
            users[i] = User.builder()
                    .aboutMe("My description about life - " + name + i)
                    .active(activityTest)
                    .avatar("www.myavatar" + i + ".ru/9090")
                    .city("SPb")
                    .dateOfBirth(dateDateClass)
                    .education("MIT University")
                    .email(emailName + i + "@user.ru")
                    .firstName(name + i)
                    .isEnable(true)
                    .languages(languageTestSet)
                    .lastName("LastNameUser" + i)
                    .lastRedactionDate(userLocalDateNow)
                    .linkSite("www.mysite.ru")
                    .password("userpass" + i)
                    .persistDate(userLocalDate)
                    .role(role)
                    .status(statusTest)
                    .build();
            userService.create(users[i]);
            verificationTokenService.create(
                    VerificationToken.TokenGenerator.getFor(users[i])
            );
        }
    }

    private void createMediaEntity() {
        MediaType mediaType;
        String url;
        for (int i = 0; i != numOfMedias; i++) {
            if (i % 5 == 0) {
                mediaType = MediaType.AUDIO;
                url = "www.myaudio.ru";
            } else if (i % 3 == 0) {
                mediaType = MediaType.VIDEO;
                url = "www.myvideo.ru";
            } else {
                mediaType = MediaType.IMAGE;
                url = "www.myimage.ru";
            }
            medias[i] = Media.builder()
                    .mediaType(mediaType)
                    .persistDateTime(userLocalDateNow)
                    .url(url)
                    .user(users[i])
                    .build();
        }
    }

    private void createTagEntity() {
        for (int i = 0; i != numOfTags; i++) {
            tags[i] = Tag.builder()
                    .text("This is " + i + " tag")
                    .build();

            tagService.create(tags[i]);
        }
    }


    private void createMessageEntity() {
        int num = 0;
        int startNum = numOfMessages / numOfChats;
        for (int j = 0; j != numOfChats; j++) {
            for (int i = num; i != startNum + num; i++) {
                Set<Media> mediaSet = new HashSet<>();
                mediaSet.add(medias[i]);
                messages[i] = Message.builder()
                        .message("Test init message" + i)
                        .is_unread(true)
                        .lastRedactionDate(userLocalDateNow)
                        .media(mediaSet)
                        .userSender(users[i])
                        .persistDate(userLocalDate)
                        .build();
                messageService.create(messages[i]);
            }
            num += startNum;
        }
    }

    private void createAlbumEntity() {
        MediaType mediaType;
        String name;
        for (int i = 0; i != numOfAlbums; i++) {
            if (i % 5 == 0) {
                mediaType = MediaType.AUDIO;
                name = "audioAlbum";
            } else if (i % 3 == 0) {
                mediaType = MediaType.VIDEO;
                name = "videoAlbum";
            } else {
                mediaType = MediaType.IMAGE;
                name = "imageAlbum";
            }
            album[i] = Album.builder()
                    .mediaType(mediaType)
                    .name(name)
                    .build();
        }
    }

    private void createAlbumAudiosEntity() {
        for (int i = 0; i != numOfAlbums; i++) {
            if (i % 5 == 0) {
                albumAudiosService.create(AlbumAudios.builder()
                        .album(album[i])
                        .build());
            }
        }
    }

    private void createAlbumImageEntity() {
        for (int i = 0; i != numOfAlbums; i++) {
            if (i % 5 == 0) {
                continue;
            } else if (i % 3 == 0) {
                continue;
            } else {
                albumImageService.create(AlbumImage.builder()
                        .album(album[i])
                        .build());
            }
        }
    }

    private void createAlbumVideoEntity() {
        for (int i = 0; i != numOfAlbums; i++) {
            if (i % 5 == 0) {
                continue;
            } else if (i % 3 == 0) {
                albumVideoService.create(AlbumVideo.builder()
                        .album(album[i])
                        .build());
            }
        }
    }


    private void createFriendEntity() {
        for (int i = 0; i != numOfFriends; i++) {
            friendService.create(Friend.builder()
                    .user(users[(int) (Math.random() * numOfUsers)])
                    .friend(users[(int) (Math.random() * numOfUsers)])
                    .build());
        }
    }

    private void createFollowerEntity() {
        for (int i = 0; i != numOfFollowers; i++) {
            followerService.create(Follower.builder()
                    .user(users[(int) (Math.random() * numOfUsers)])
                    .follower(users[(int) (Math.random() * numOfUsers)])
                    .build());
        }
    }

    private void createPostEntity() {
        for (int i = 0; i != numOfPosts; i++) {
            Set<Media> mediaSet = new HashSet<>();
            Set<Tag> tagSet = new HashSet<>();
            mediaSet.add(medias[i]);
            tagSet.add(tags[i]);
            posts[i] = Post.builder()
                    .persistDate(userLocalDate)
                    .lastRedactionDate(userLocalDateNow)
                    .media(mediaSet)
                    .tags(tagSet)
                    .text("There is the " + i + " text of this post")
                    .title("The " + i + " test post")
                    .topic(new Topic("This is topic " + i))
                    .user(users[(int) (Math.random() * numOfUsers)])
                    .build();
        }
    }


    private void createGroupEntity() {
        GroupCategory groupCategory1 = GroupCategory.builder().category("Programming").build();
        GroupCategory groupCategory2 = GroupCategory.builder().category("Flowers").build();
        GroupCategory groupCategory;
        int num = 0;
        int startNum = numOfPosts / numOfGroups;
        for (int i = 0; i != numOfGroups; i++) {
            if (i % 2 == 0) {
                groupCategory = groupCategory1;
            } else {
                groupCategory = groupCategory2;
            }
            Set<Post> postSet = new HashSet<>();
            for (int j = num; j != num + startNum; j++) {
                postSet.add(posts[j]);
            }
            groups[i] = Group.builder()
                    .groupCategory(groupCategory)
                    .lastRedactionDate(userLocalDateNow)
                    .linkSite("www.groupsite" + i + ".ru")
                    .name("JAVA IS " + i)
                    .persistDate(userLocalDate)
                    .posts(postSet)
                    .description("This is a description of the group #" + i)
                    .owner(users[(int) (Math.random() * numOfUsers)])
                    .addressImageGroup("This is a address of the group #" + i)
                    .build();
            groupService.create(groups[i]);
            num += startNum;
        }
    }

    private void createGroupHasUserEntity() {
        for (int i = 0; i != numOfUsers; i++) {
            groupHasUserService.create(GroupHasUser.builder()
                    .group(groups[(int) (Math.random() * numOfGroups / 2)])
                    .user(users[i])
                    .persistDate(userLocalDate)
                    .build());
        }
    }

    private void createPostCommentEntity() {
        for (int i = 0; i != numOfPostComments; i++) {
            postComments[i] = Comment.builder()
                    .comment("Test comment POST " + i)
                    .commentType(CommentType.POST)
                    .lastRedactionDate(userLocalDate)
                    .persistDate(userLocalDate)
                    .user(users[(int) (Math.random() * numOfUsers)])
                    .build();
            postCommentService.create(PostComment.builder()
                    .post(posts[(int) (Math.random() * numOfPosts)])
                    .comment(postComments[i])
                    .build());
        }
    }

    private void createPostLikeEntity() {
        for (int i = 0; i != numOfPostLikes; i++) {
            postLikes[i] = Like.builder()
                    .likeType(LikeType.POST)
                    .user(users[(int) (Math.random() * numOfUsers)])
                    .build();
            postLikeService.create(PostLike.builder()
                    .post(posts[(int) (Math.random() * numOfPosts)])
                    .like(postLikes[i])
                    .build());
        }
    }

    private void createCommentLikeEntity() {
        for (int i = 0; i != numOfCommentLikes; i++) {
            commentLikes[i] = Like.builder()
                    .likeType(LikeType.COMMENT)
                    .user(users[(int) (Math.random() * numOfUsers)])
                    .build();
            commentLikeService.create(CommentLike.builder()
                    .like(commentLikes[i])
                    .comment(postComments[(int) (Math.random() * numOfPostComments)])
                    .build());
        }
    }

    private void createMessageLikeEntity() {
        for (int i = 0; i != numOfMessagesLikes; i++) {
            messageLikes[i] = Like.builder()
                    .likeType(LikeType.MESSAGE)
                    .user(users[(int) (Math.random() * numOfUsers)])
                    .build();
            messageLikeService.create(MessageLike.builder()
                    .like(messageLikes[i])
                    .message(messages[(int) (Math.random() * numOfMessages)])
                    .build());
        }
    }

    private void createMediaCommentEntity() {
        for (int i = 0; i != numOfMediaComments; i++) {
            mediaComments[i] = Comment.builder()
                    .comment("Test comment MEDIA " + i)
                    .commentType(CommentType.MEDIA)
                    .lastRedactionDate(userLocalDate)
                    .persistDate(userLocalDate)
                    .user(users[(int) (Math.random() * numOfUsers)])
                    .build();
            mediaCommentService.create(MediaComment.builder()
                    .comment(mediaComments[i])
                    .media(medias[(int) (Math.random() * numOfMedias)])
                    .build());
        }
    }

    private void createAudiosEntity() {
        List<Audios> list = new ArrayList<>();
        for (int i = 0; i != numOfMedias; i++) {
            if (i % 5 == 0) {
                Audios audio = Audios.builder()
                        .author("Test Author " + i)
                        .icon("TestIcon" + i)
                        .name("AudioTestName " + i)
                        .album("AlbumTestName " + i)
                        .length(238)
                        .media(medias[i])
                        .build();

                audiosService.create(audio);
                list.add(audio);
            }
        }
        audios = new Audios[list.size()];
        audios = list.toArray(audios);
    }

    private void createImageEntity() {
        for (int i = 0; i != numOfMedias; i++) {
            if (i % 5 == 0) {
                continue;
            } else if (i % 3 == 0) {
                continue;
            } else {
                imageService.create(Image.builder()
                        .description("Image Media Test File " + i)
                        .media(medias[i])
                        .build());
            }
        }
    }

    private void createVideosEntity() {
        for (int i = 0; i != numOfMedias; i++) {
            if (i % 5 == 0) {
                continue;
            } else if (i % 3 == 0) {
                videosService.create(Videos.builder()
                        .icon("New Test Icon video " + i)
                        .name("Test video " + i)
                        .media(medias[i])
                        .build());
            }
        }
    }

    private void createUserTabsEntity() {
        for (int i = 0; i < 100; i++) {
            UserTabs userTab = UserTabs.builder()
                    .user(users[i])
                    .post(posts[i])
                    .persistDate(posts[i].getPersistDate())
                    .build();
            userTabsService.create(userTab);
        }


    }

    private void createBookmarks() {
        for (int i = 0; i < numOfUsers / 2; i++) {
            bookmarkService.create(Bookmark.builder().post(posts[i]).user(users[i]).build());
            bookmarkService.create(Bookmark.builder().post(posts[i + 3]).user(users[i]).build());
        }
    }

    private void createPlaylists() {
        Optional<User> userOptional = userService.getById(60L);

        if (userOptional.isPresent()) {
            User owner = userOptional.get();
            for (int i = 1; i <= 5; i++) {
                HashSet<Audios> audios = new HashSet<>(audiosService.getPart(i, 3));

                Playlist playlist = Playlist.builder()
                        .image("image" + i)
                        .name("playlistName" + i)
                        .ownerUser(owner)
                        .playlistContent(audios)
                        .build();
                playlistService.create(playlist);
            }
        }
    }

    private void createSingleChatEntity() {
        for (int i = 0; i < numOfChats; i++) {
            HashSet<Message> messagesSet = new HashSet<>();
            messagesSet.add(messages[i * 2 + 1]);
            messagesSet.add(messages[i * 2 + 2]);

            SingleChat singleChat = SingleChat.builder()
                    .userOne(users[i])
                    .userTwo(users[(numOfUsers / 2) + i])
                    .messages(messagesSet)
                    .build();
            singleChatService.create(singleChat);

            HashSet<SingleChat> chatSet = new HashSet<>();
            chatSet.add(singleChat);
        }
    }

    private void createGroupChatEntity() {
        for (int i = 0; i < numOfChats; i++) {
            HashSet<Message> messagesSet = new HashSet<>();
            messagesSet.add(messages[3 * i + 1]);
            messagesSet.add(messages[3 * i + 2]);
            messagesSet.add(messages[3 * i + 3]);

            HashSet<User> usersSet = new HashSet<>();
            usersSet.add(users[3 * i + 1]);
            usersSet.add(users[3 * i + 2]);
            usersSet.add(users[3 * i + 3]);

            GroupChat groupChat = GroupChat.builder()
                    .title("Group chat #" + i)
                    .image("Chat image #" + i)
                    .messages(messagesSet)
                    .users(usersSet)
                    .build();
            groupChatService.create(groupChat);

            usersSet.forEach(user -> {
                HashSet<GroupChat> groupChatsSet = new HashSet<>();
                groupChatsSet.add(groupChat);
                user.setGroupChats(groupChatsSet);
                userService.update(user);
            });
        }
    }

    private void addAudiosToUserCollection() {
        Optional<User> userOptional = userService.getById(60L);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            HashSet<Audios> audiosSet = new HashSet<>(Arrays.asList(audios));
            user.setAudios(audiosSet);
            userService.update(user);
        }
    }


    private void createTopicEntity() {
        for (int i = 0; i != numOfTopics; i++) {
            topics[i] = Topic.builder()
                    .topic("It's Topic " + i)
                    .build();

            topicService.create(topics[i]);
        }
    }
}
