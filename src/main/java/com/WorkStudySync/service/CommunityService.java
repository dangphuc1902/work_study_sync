package com.WorkStudySync.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.WorkStudySync.entity.CommentEntity;
import com.WorkStudySync.entity.CommunityPostEntity;
import com.WorkStudySync.entity.NoteEntity;
import com.WorkStudySync.entity.UserEntity;
import com.WorkStudySync.payload.request.CommentRequest;
import com.WorkStudySync.payload.request.CommunityPostRequest;
import com.WorkStudySync.repository.CommentRepository;
import com.WorkStudySync.repository.CommunityPostRepository;
import com.WorkStudySync.repository.NoteRepository;
import com.WorkStudySync.repository.UserRepository;
import com.WorkStudySync.service.imp.CommunityServiceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommunityService implements CommunityServiceImp {

    @Autowired
    private CommunityPostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NoteRepository noteRepository;

    @Override
    public CommunityPostEntity createPost(String userEmail, CommunityPostRequest postRequest) {
        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        CommunityPostEntity post = new CommunityPostEntity();
        post.setUser(user);
        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());

        // Nếu có noteId, liên kết với note
        if (postRequest.getNoteId() != null) {
            NoteEntity note = noteRepository.findById(postRequest.getNoteId())
                    .orElseThrow(() -> new RuntimeException("Note not found"));
            
            // Kiểm tra xem note có thuộc user này không hoặc đã được share
            if (!note.getUser().getUserId().equals(user.getUserId()) && !note.getIsShared()) {
                throw new RuntimeException("You don't have permission to share this note");
            }
            
            post.setNote(note);
        }

        post.setCreatedAt(LocalDateTime.now());

        return postRepository.save(post);
    }

    @Override
    public CommunityPostEntity updatePost(UUID postId, String userEmail, CommunityPostRequest postRequest) {
        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        CommunityPostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (!post.getUser().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("You don't have permission to update this post");
        }

        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());

        return postRepository.save(post);
    }

    @Override
    public void deletePost(UUID postId, String userEmail) {
        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        CommunityPostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (!post.getUser().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("You don't have permission to delete this post");
        }

        postRepository.delete(post);
    }

    @Override
    public CommunityPostEntity getPostById(UUID postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
    }

    @Override
    public List<CommunityPostEntity> getAllPosts() {
        return postRepository.findAllOrderByCreatedAtDesc();
    }

    @Override
    public List<CommunityPostEntity> searchPosts(String keyword) {
        return postRepository.searchPosts(keyword);
    }

    @Override
    public List<CommunityPostEntity> getPostsByUser(String userEmail) {
        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return postRepository.findByUser(user);
    }

    @Override
    public CommentEntity createComment(UUID postId, String userEmail, CommentRequest commentRequest) {
        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        CommunityPostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        CommentEntity comment = new CommentEntity();
        comment.setPost(post);
        comment.setUser(user);
        comment.setContent(commentRequest.getContent());
        comment.setCreatedAt(LocalDateTime.now());

        return commentRepository.save(comment);
    }

    @Override
    public void deleteComment(UUID commentId, String userEmail) {
        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        CommentEntity comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!comment.getUser().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("You don't have permission to delete this comment");
        }

        commentRepository.delete(comment);
    }

    @Override
    public List<CommentEntity> getCommentsByPost(UUID postId) {
        CommunityPostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        return commentRepository.findByPost(post);
    }
}