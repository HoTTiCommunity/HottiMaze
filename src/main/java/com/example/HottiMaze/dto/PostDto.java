package com.example.HottiMaze.dto;

import com.example.HottiMaze.entity.Category;
import com.example.HottiMaze.entity.Post;
import com.example.HottiMaze.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {
    private Long id;
    private String title;
    private String content;
    private String nickname;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Post toEntity(Long categoryId, PostDto postDto) {
        Post post = new Post();
        post.setId(postDto.getId());
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        return post;
    }
    public static PostDto fromEntity(Post post) {
        PostDto dto = new PostDto(post.getId(), post.getTitle(), post.getContent(), post.getAuthor(), post.getCreatedAt(), post.getUpdatedAt());
        return dto;
    }
}
