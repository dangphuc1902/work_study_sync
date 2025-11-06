package com.WorkStudySync.controller;

import java.util.List;
import java.util.UUID;

import com.WorkStudySync.entity.NoteEntity;
import com.WorkStudySync.payload.request.NoteRequest;
import com.WorkStudySync.payload.response.BaseResponse;
import com.WorkStudySync.service.imp.NoteServiceImp;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notes")
@CrossOrigin
public class NoteController {

    @Autowired
    private NoteServiceImp noteService;

    private String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @PostMapping
    public ResponseEntity<?> createNote(@Valid @RequestBody NoteRequest noteRequest) {
        try {
            String userEmail = getCurrentUserEmail();
            NoteEntity note = noteService.createNote(userEmail, noteRequest);
            
            BaseResponse response = new BaseResponse();
            response.setStatusCode(201);
            response.setMessage("Note created successfully");
            response.setData(note);
            
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            BaseResponse response = new BaseResponse();
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllNotes() {
        try {
            String userEmail = getCurrentUserEmail();
            List<NoteEntity> notes = noteService.getAllNotesByUser(userEmail);
            
            BaseResponse response = new BaseResponse();
            response.setStatusCode(200);
            response.setMessage("Notes retrieved successfully");
            response.setData(notes);
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            BaseResponse response = new BaseResponse();
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{noteId}")
    public ResponseEntity<?> getNoteById(@PathVariable UUID noteId) {
        try {
            String userEmail = getCurrentUserEmail();
            NoteEntity note = noteService.getNoteById(noteId, userEmail);
            
            BaseResponse response = new BaseResponse();
            response.setStatusCode(200);
            response.setMessage("Note retrieved successfully");
            response.setData(note);
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            BaseResponse response = new BaseResponse();
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{noteId}")
    public ResponseEntity<?> updateNote(@PathVariable UUID noteId, @Valid @RequestBody NoteRequest noteRequest) {
        try {
            String userEmail = getCurrentUserEmail();
            NoteEntity note = noteService.updateNote(noteId, userEmail, noteRequest);
            
            BaseResponse response = new BaseResponse();
            response.setStatusCode(200);
            response.setMessage("Note updated successfully");
            response.setData(note);
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            BaseResponse response = new BaseResponse();
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{noteId}")
    public ResponseEntity<?> deleteNote(@PathVariable UUID noteId) {
        try {
            String userEmail = getCurrentUserEmail();
            noteService.deleteNote(noteId, userEmail);
            
            BaseResponse response = new BaseResponse();
            response.setStatusCode(200);
            response.setMessage("Note deleted successfully");
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            BaseResponse response = new BaseResponse();
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/shared")
    public ResponseEntity<?> getSharedNotes() {
        try {
            List<NoteEntity> notes = noteService.getSharedNotes();
            
            BaseResponse response = new BaseResponse();
            response.setStatusCode(200);
            response.setMessage("Shared notes retrieved successfully");
            response.setData(notes);
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            BaseResponse response = new BaseResponse();
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchNotes(@RequestParam String keyword) {
        try {
            String userEmail = getCurrentUserEmail();
            List<NoteEntity> notes = noteService.searchNotes(userEmail, keyword);
            
            BaseResponse response = new BaseResponse();
            response.setStatusCode(200);
            response.setMessage("Search results retrieved successfully");
            response.setData(notes);
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            BaseResponse response = new BaseResponse();
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{noteId}/toggle-share")
    public ResponseEntity<?> toggleShareStatus(@PathVariable UUID noteId) {
        try {
            String userEmail = getCurrentUserEmail();
            NoteEntity note = noteService.toggleShareStatus(noteId, userEmail);
            
            BaseResponse response = new BaseResponse();
            response.setStatusCode(200);
            response.setMessage("Note share status updated successfully");
            response.setData(note);
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            BaseResponse response = new BaseResponse();
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}