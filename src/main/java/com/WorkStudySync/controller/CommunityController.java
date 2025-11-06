package com.WorkStudySync.controller;

import java.util.List;
import java.util.UUID;

import com.WorkStudySync.entity.CommentEntity;
import com.WorkStudySync.entity.CommunityPostEntity;
import com.WorkStudySync.payload.request.CommentRequest;
import com.WorkStudySync.payload.request.CommunityPostRequest;
import com.WorkStudySync.payload.response.BaseResponse;
import com.WorkStudySync.service.imp.CommunityServiceImp;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/community")
@CrossOrigin
public class CommunityController {

    @Autowired
    private CommunityServiceImp communityService;

    private String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    // ========== POST ENDPOINTS ==========
    
    @PostMapping("/posts")
    public ResponseEntity<?> createPost(@Valid @RequestBody CommunityPostRequest postRequest) {
        try {
            String userEmail = getCurrentUserEmail();
            CommunityPostEntity post = communityService.createPost(userEmail, postRequest);
            
            BaseResponse response = new BaseResponse();
            response.setStatusCode(201);
            response.setMessage("Post created successfully");
            response.setData(post);
            
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            BaseResponse response = new BaseResponse();
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/posts")
    public ResponseEntity<?> getAllPosts() {
        try {
            List<CommunityPostEntity> posts = communityService.getAllPosts();
            
            BaseResponse response = new BaseResponse();
            response.setStatusCode(200);
            response.setMessage("Posts retrieved successfully");
            response.setData(posts);
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            BaseResponse response = new BaseResponse();
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<?> getPostById(@PathVariable UUID postId) {
        try {
            CommunityPostEntity post = communityService.getPostById(postId);
            
            BaseResponse response = new BaseResponse();
            response.setStatusCode(200);
            response.setMessage("Post retrieved successfully");
            response.setData(post);
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            BaseResponse response = new BaseResponse();
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/posts/{postId}")
    public ResponseEntity<?> updatePost(@PathVariable UUID postId, @Valid @RequestBody CommunityPostRequest postRequest) {
        try {
            String userEmail = getCurrentUserEmail();
            CommunityPostEntity post = communityService.updatePost(postId, userEmail, postRequest);
            
            BaseResponse response = new BaseResponse();
            response.setStatusCode(200);
            response.setMessage("Post updated successfully");
            response.setData(post);
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            BaseResponse response = new BaseResponse();
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable UUID postId) {
        try {
            String userEmail = getCurrentUserEmail();
            communityService.deletePost(postId, userEmail);
            
            BaseResponse response = new BaseResponse();
            response.setStatusCode(200);
            response.setMessage("Post deleted successfully");
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            BaseResponse response = new BaseResponse();
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/posts/search")
    public ResponseEntity<?> searchPosts(@RequestParam String keyword) {
        try {
            List<CommunityPostEntity> posts = communityService.searchPosts(keyword);
            
            BaseResponse response = new BaseResponse();
            response.setStatusCode(200);
            response.setMessage("Search results retrieved successfully");
            response.setData(posts);
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            BaseResponse response = new BaseResponse();
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/posts/my-posts")
    public ResponseEntity<?> getMyPosts() {
        try {
            String userEmail = getCurrentUserEmail();
            List<CommunityPostEntity> posts = communityService.getPostsByUser(userEmail);
            
            BaseResponse response = new BaseResponse();
            response.setStatusCode(200);
            response.setMessage("Your posts retrieved successfully");
            response.setData(posts);
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            BaseResponse response = new BaseResponse();
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ========== COMMENT ENDPOINTS ==========
    
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<?> createComment(@PathVariable UUID postId, @Valid @RequestBody CommentRequest commentRequest) {
        try {
            String userEmail = getCurrentUserEmail();
            CommentEntity comment = communityService.createComment(postId, userEmail, commentRequest);
            
            BaseResponse response = new BaseResponse();
            response.setStatusCode(201);
            response.setMessage("Comment created successfully");
            response.setData(comment);
            
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            BaseResponse response = new BaseResponse();
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<?> getCommentsByPost(@PathVariable UUID postId) {
        try {
            List<CommentEntity> comments = communityService.getCommentsByPost(postId);
            
            BaseResponse response = new BaseResponse();
            response.setStatusCode(200);
            response.setMessage("Comments retrieved successfully");
            response.setData(comments);
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            BaseResponse response = new BaseResponse();
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable UUID commentId) {
        try {
            String userEmail = getCurrentUserEmail();
            communityService.deleteComment(commentId, userEmail);
            
            BaseResponse response = new BaseResponse();
            response.setStatusCode(200);
            response.setMessage("Comment deleted successfully");
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            BaseResponse response = new BaseResponse();
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}