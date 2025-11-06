package com.WorkStudySync.service.imp;

import java.util.List;
import java.util.UUID;

import com.WorkStudySync.entity.CommentEntity;
import com.WorkStudySync.entity.CommunityPostEntity;
import com.WorkStudySync.payload.request.CommentRequest;
import com.WorkStudySync.payload.request.CommunityPostRequest;

public interface CommunityServiceImp {
    
    // Post operations
    CommunityPostEntity createPost(String userEmail, CommunityPostRequest postRequest);
    CommunityPostEntity updatePost(UUID postId, String userEmail, CommunityPostRequest postRequest);
    void deletePost(UUID postId, String userEmail);
    CommunityPostEntity getPostById(UUID postId);
    List<CommunityPostEntity> getAllPosts();
    List<CommunityPostEntity> searchPosts(String keyword);
    List<CommunityPostEntity> getPostsByUser(String userEmail);
    
    // Comment operations
    CommentEntity createComment(UUID postId, String userEmail, CommentRequest commentRequest);
    void deleteComment(UUID commentId, String userEmail);
    List<CommentEntity> getCommentsByPost(UUID postId);
}