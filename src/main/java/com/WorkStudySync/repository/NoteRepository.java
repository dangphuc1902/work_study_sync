package com.WorkStudySync.repository;

import java.util.List;
import java.util.UUID;

import com.WorkStudySync.entity.NoteEntity;
import com.WorkStudySync.entity.UserEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends JpaRepository<NoteEntity, UUID> {
    
    // Tìm notes theo user
    List<NoteEntity> findByUser(UserEntity user);
    
    // Tìm notes đã chia sẻ
    List<NoteEntity> findByIsShared(Boolean isShared);
    
    // Tìm notes theo user và isShared
    List<NoteEntity> findByUserAndIsShared(UserEntity user, Boolean isShared);
    
    // Tìm notes theo title (tìm kiếm)
    @Query("SELECT n FROM NoteEntity n WHERE n.user = :user AND LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<NoteEntity> searchNotesByTitle(@Param("user") UserEntity user, @Param("keyword") String keyword);
    
    // Tìm notes theo content (tìm kiếm)
    @Query("SELECT n FROM NoteEntity n WHERE n.user = :user AND (LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(n.content) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<NoteEntity> searchNotes(@Param("user") UserEntity user, @Param("keyword") String keyword);
    
    // Tìm các notes được chia sẻ trong cộng đồng
    @Query("SELECT DISTINCT n FROM NoteEntity n JOIN n.communityPosts WHERE n.isShared = true ORDER BY n.createdAt DESC")
    List<NoteEntity> findSharedNotesInCommunity();
}