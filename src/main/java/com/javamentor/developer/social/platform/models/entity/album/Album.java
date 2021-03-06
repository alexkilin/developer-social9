package com.javamentor.developer.social.platform.models.entity.album;


import com.javamentor.developer.social.platform.exception.ApiRequestException;
import com.javamentor.developer.social.platform.models.entity.media.Media;
import com.javamentor.developer.social.platform.models.entity.media.MediaType;
import com.javamentor.developer.social.platform.models.entity.user.User;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "albums")
public class Album {

    public Album(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    @Id
    @GeneratedValue(generator = "albums_seq")
    private Long id;

    @NonNull
    private String name;

    private String icon;

    @ManyToOne
    @JoinColumn(name = "user_owner_id")
    private User userOwnerId;

    @Enumerated
    @NotNull
    private MediaType mediaType;

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL)
    private Set<Media> mediaSet = new HashSet<>();

    @Column(name = "persist_date", nullable = false)
    @Type(type = "org.hibernate.type.LocalDateTimeType")
    @CreationTimestamp
    private LocalDateTime persistDate;

    @Column(name = "update_date")
    @Type(type = "org.hibernate.type.LocalDateTimeType")
    @UpdateTimestamp
    private LocalDateTime lastRedactionDate;

    public boolean addMedia(Media media){
        if (media.getMediaType() != mediaType) {
            throw new ApiRequestException(String.format(
                    "У экземпляра Album, связанного с %s, " +
                            "поле MediaType должно принимать значение %s",
                    mediaType.name(), mediaType.toString()));
        }
        return mediaSet.add(media);
    }

    public boolean removeMedia(Media media){
        return mediaSet.remove(media);
    }
}
