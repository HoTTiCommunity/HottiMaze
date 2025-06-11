package com.example.HottiMaze.service;

import com.example.HottiMaze.dto.PostCreateDto;
import com.example.HottiMaze.dto.PostDto;
import com.example.HottiMaze.dto.PostUpdateDto;
import com.example.HottiMaze.entity.Category;
import com.example.HottiMaze.entity.Post;
import com.example.HottiMaze.entity.Vote;
import com.example.HottiMaze.repository.CategoryRepository;
import com.example.HottiMaze.repository.PostRepository;
import com.example.HottiMaze.repository.CommentRepository;
import com.example.HottiMaze.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final CommentRepository commentRepository;
    private final VoteRepository voteRepository; // VoteRepository 주입

    /** 전체 게시글 조회 */
    @Transactional(readOnly = true)
    public List<PostDto> getAllPosts() {
        return postRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /** 카테고리 ID로 게시글 조회 */
    @Transactional(readOnly = true)
    public List<PostDto> getPostsByCategory(Long categoryId) {
        return postRepository.findAllByCategory_Id(categoryId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * 카테고리 이름으로 게시글 조회
     * (DB에 저장된 정확한 카테고리 이름과 일치해야 함)
     */
    @Transactional(readOnly = true)
    public List<PostDto> getPostsByCategoryName(String categoryName) {
        Category category = categoryRepository.findByName(categoryName)
                .orElseThrow(() ->
                        new IllegalArgumentException("존재하지 않는 카테고리입니다: " + categoryName));
        return postRepository.findAllByCategory(category).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /** 전체 카테고리 목록 조회 */
    @Transactional(readOnly = true)
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    /** 카테고리별 게시글 개수 조회 */
    @Transactional(readOnly = true)
    public long getPostCountByCategory(Long categoryId) {
        return postRepository.countByCategory_Id(categoryId);
    }

    /** 단일 게시글 조회 (DTO) */
    @Transactional(readOnly = true)
    public PostDto getPostById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다: " + postId));
        return convertToDto(post);
    }

    /** 단일 게시글 조회 (Entity) */
    @Transactional(readOnly = true)
    public Post getPostEntityById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다. ID: " + postId));
    }

    /** 게시글 생성 */
    @Transactional
    public PostDto createPost(PostCreateDto createDto) {
        Category category = categoryRepository.findById(createDto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다: " + createDto.getCategoryId()));

        Post post = new Post();
        post.setTitle(createDto.getTitle());
        post.setContent(createDto.getContent());
        post.setAuthor(createDto.getAuthor());
        post.setCategory(category);
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        post.setViewCount(0);
        post.setGaechu(0);
        post.setBechu(0);

        Post savedPost = postRepository.save(post);
        return convertToDto(savedPost);
    }

    /** 게시글 수정 */
    @Transactional
    public PostDto updatePost(Long postId, PostUpdateDto updateDto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다: " + postId));

        Category category = categoryRepository.findById(updateDto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다: " + updateDto.getCategoryId()));

        post.setTitle(updateDto.getTitle());
        post.setContent(updateDto.getContent());
        post.setCategory(category);
        post.setUpdatedAt(LocalDateTime.now());

        Post updatedPost = postRepository.save(post);
        return convertToDto(updatedPost);
    }

    /** 게시글 삭제 */
    @Transactional
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다: " + postId));

        // 해당 게시글에 달린 모든 댓글 먼저 삭제
        commentRepository.deleteByPostId(postId);

        // 해당 게시글에 달린 모든 투표 먼저 삭제
        voteRepository.deleteByPost(post); // 추가: 게시글 삭제 시 투표도 삭제

        postRepository.delete(post);
    }

    /** 개추 (추천) 증가 */
    @Transactional
    public void gaechuPost(Long postId, String username) { // username 파라미터 추가
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다: " + postId));

        Optional<Vote> existingVote = voteRepository.findByPostIdAndUsername(postId, username);

        if (existingVote.isPresent()) {
            Vote vote = existingVote.get();
            if (vote.isLike()) {
                // 이미 좋아요를 눌렀으면 취소
                voteRepository.delete(vote);
                post.setGaechu(post.getGaechu() - 1);
            } else {
                // 싫어요를 눌렀으면 좋아요로 변경
                vote.setLike(true);
                voteRepository.save(vote);
                post.setGaechu(post.getGaechu() + 1);
                post.setBechu(post.getBechu() - 1);
            }
        } else {
            // 새롭게 좋아요 투표
            Vote newVote = new Vote();
            newVote.setPost(post);
            newVote.setUsername(username);
            newVote.setLike(true);
            voteRepository.save(newVote);
            post.setGaechu(post.getGaechu() + 1);
        }
        postRepository.save(post);
    }

    /** 비추 (비추천) 증가 */
    @Transactional
    public void bechuPost(Long postId, String username) { // username 파라미터 추가
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다: " + postId));

        Optional<Vote> existingVote = voteRepository.findByPostIdAndUsername(postId, username);

        if (existingVote.isPresent()) {
            Vote vote = existingVote.get();
            if (!vote.isLike()) {
                // 이미 싫어요를 눌렀으면 취소
                voteRepository.delete(vote);
                post.setBechu(post.getBechu() - 1);
            } else {
                // 좋아요를 눌렀으면 싫어요로 변경
                vote.setLike(false);
                voteRepository.save(vote);
                post.setBechu(post.getBechu() + 1);
                post.setGaechu(post.getGaechu() - 1);
            }
        } else {
            // 새롭게 싫어요 투표
            Vote newVote = new Vote();
            newVote.setPost(post);
            newVote.setUsername(username);
            newVote.setLike(false);
            voteRepository.save(newVote);
            post.setBechu(post.getBechu() + 1);
        }
        postRepository.save(post);
    }

    /** 내부: 엔티티 → DTO 변환 */
    private PostDto convertToDto(Post post) {
        PostDto dto = new PostDto();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setNickname(post.getAuthor());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUpdatedAt(post.getUpdatedAt());
        dto.setViewCount(post.getViewCount());
        dto.setGaechu(post.getGaechu());
        dto.setBechu(post.getBechu());
        return dto;
    }
}