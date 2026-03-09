package com.apiece.springboot_twitter.post;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostRepository postRepository;

//    @GetMapping("/posts")
//    public String getPost() {
//        return "안녕하세요!";
//    }

//    @GetMapping("/posts")
//    public Post getPosts(){
//        return new Post(1L, "안녕하세요!", LocalDateTime.now());
//    }

    private final Map<Long, Post> posts = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/posts")
    public Post createPost(@RequestBody Post post) {
        Post newPost = Post.builder()
                .content(post.getContent())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        postRepository.save(newPost);

        return newPost;
    }

    // 게시글 목록 조회
    @GetMapping("/api/posts")
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    //  /api/posts/1
    @GetMapping("api/posts/{id}")
    public Post getPost(@PathVariable Long id) {
        return postRepository
                .findById(id)
                .orElseThrow();
    }

    @PutMapping("/api/posts/{id}")
    public Post updatePost(@PathVariable Long id, @RequestBody Post postRequest) {
        return postRepository.findById(id)
                .map(post -> {
                    post.updateContent(postRequest.getContent());
                    return postRepository.save(post);
                }).orElseThrow();

    }

    @DeleteMapping("/api/posts/{id}")
    public void deletePost(@PathVariable Long id) {
        postRepository.deleteById(id);
    }

    @GetMapping("/api/posts/search")
    public Slice<Post> searchPosts(
            @RequestParam(required = false) Long lastPostId,
            @RequestParam(defaultValue = "3") Integer size
    ) {
        int page = 0;
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        if (lastPostId == null) {
            return postRepository.findSlicesBy(pageable);
        } else {
            return postRepository.findSlicesByIdLessThan(lastPostId, pageable);
        }
    }
}