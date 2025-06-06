package com.example.HottiMaze.repository;

import com.example.HottiMaze.entity.Post;
import com.example.HottiMaze.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByCategory(Category category);
    List<Post> findAllByCategory_Name(String categoryName);
    List<Post> findAllByCategory_Id(Long categoryId);
    long countByCategory_Id(Long categoryId); // Add this method
}
