package com.joaquinogallar.personalblog.comment.entity;

import com.joaquinogallar.personalblog.post.entity.Post;
import com.joaquinogallar.personalblog.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "commets")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String authorName;

    private String authorEmail;

    @Lob
    private String content;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private Boolean approved;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private UserEntity user;

}
