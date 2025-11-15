package com.joaquinogallar.personalblog.post.repository;

import com.joaquinogallar.personalblog.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
}
