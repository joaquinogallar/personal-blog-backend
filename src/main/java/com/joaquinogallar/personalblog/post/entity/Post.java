package com.joaquinogallar.personalblog.post.entity;

import com.joaquinogallar.personalblog.comment.entity.Comment;
import com.joaquinogallar.personalblog.tag.entity.Tag;
import com.joaquinogallar.personalblog.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String content;

    @Column(unique = true)
    private String slug;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Boolean published;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private UserEntity author;

    @ManyToMany
    @JoinTable(
            name = "post-tags",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments;
}
