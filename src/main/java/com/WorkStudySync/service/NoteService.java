package com.WorkStudySync.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.WorkStudySync.entity.NoteEntity;
import com.WorkStudySync.entity.UserEntity;
import com.WorkStudySync.payload.request.NoteRequest;
import com.WorkStudySync.repository.NoteRepository;
import com.WorkStudySync.repository.UserRepository;
import com.WorkStudySync.service.imp.NoteServiceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoteService implements NoteServiceImp {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public NoteEntity createNote(String userEmail, NoteRequest noteRequest) {
        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        NoteEntity note = new NoteEntity();
        note.setUser(user);
        note.setTitle(noteRequest.getTitle());
        note.setContent(noteRequest.getContent());
        note.setIsShared(noteRequest.getIsShared() != null ? noteRequest.getIsShared() : false);
        note.setCreatedAt(LocalDateTime.now());
        note.setUpdatedAt(LocalDateTime.now());

        return noteRepository.save(note);
    }

    @Override
    public NoteEntity updateNote(UUID noteId, String userEmail, NoteRequest noteRequest) {
        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        NoteEntity note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        if (!note.getUser().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("You don't have permission to update this note");
        }

        note.setTitle(noteRequest.getTitle());
        note.setContent(noteRequest.getContent());
        if (noteRequest.getIsShared() != null) {
            note.setIsShared(noteRequest.getIsShared());
        }
        note.setUpdatedAt(LocalDateTime.now());

        return noteRepository.save(note);
    }

    @Override
    public void deleteNote(UUID noteId, String userEmail) {
        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        NoteEntity note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        if (!note.getUser().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("You don't have permission to delete this note");
        }

        noteRepository.delete(note);
    }

    @Override
    public NoteEntity getNoteById(UUID noteId, String userEmail) {
        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        NoteEntity note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        // Cho phép xem note nếu là chủ sở hữu hoặc note được chia sẻ
        if (!note.getUser().getUserId().equals(user.getUserId()) && !note.getIsShared()) {
            throw new RuntimeException("You don't have permission to view this note");
        }

        return note;
    }

    @Override
    public List<NoteEntity> getAllNotesByUser(String userEmail) {
        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return noteRepository.findByUser(user);
    }

    @Override
    public List<NoteEntity> getSharedNotes() {
        return noteRepository.findSharedNotesInCommunity();
    }

    @Override
    public List<NoteEntity> searchNotes(String userEmail, String keyword) {
        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return noteRepository.searchNotes(user, keyword);
    }

    @Override
    public NoteEntity toggleShareStatus(UUID noteId, String userEmail) {
        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        NoteEntity note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        if (!note.getUser().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("You don't have permission to modify this note");
        }

        note.setIsShared(!note.getIsShared());
        note.setUpdatedAt(LocalDateTime.now());

        return noteRepository.save(note);
    }
}