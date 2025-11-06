package com.WorkStudySync.service.imp;

import java.util.List;
import java.util.UUID;

import com.WorkStudySync.entity.NoteEntity;
import com.WorkStudySync.payload.request.NoteRequest;

public interface NoteServiceImp {
    
    // Tạo note mới
    NoteEntity createNote(String userEmail, NoteRequest noteRequest);
    
    // Cập nhật note
    NoteEntity updateNote(UUID noteId, String userEmail, NoteRequest noteRequest);
    
    // Xóa note
    void deleteNote(UUID noteId, String userEmail);
    
    // Lấy note theo ID
    NoteEntity getNoteById(UUID noteId, String userEmail);
    
    // Lấy tất cả notes của user
    List<NoteEntity> getAllNotesByUser(String userEmail);
    
    // Lấy notes đã chia sẻ
    List<NoteEntity> getSharedNotes();
    
    // Tìm kiếm notes
    List<NoteEntity> searchNotes(String userEmail, String keyword);
    
    // Toggle share status
    NoteEntity toggleShareStatus(UUID noteId, String userEmail);
}