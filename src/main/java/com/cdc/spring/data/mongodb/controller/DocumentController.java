package com.cdc.spring.data.mongodb.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.cdc.spring.data.mongodb.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import com.cdc.spring.data.mongodb.model.Document;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class DocumentController {

  @Autowired
  DocumentRepository documentRepository;

  @GetMapping("/documents")
  public ResponseEntity<List<Document>> getAllDocuments(@RequestParam(required = false) String title) {
    try {
      List<Document> documents = new ArrayList<Document>();

      if (title == null)
        documentRepository.findAll().forEach(documents::add);
      else
        documentRepository.findByTitleContaining(title).forEach(documents::add);

      if (documents.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }

      return new ResponseEntity<>(documents, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/documents/{id}")
  public ResponseEntity<Document> getDocumentById(@PathVariable("id") String id) {
    Optional<Document> documentData = documentRepository.findById(id);

    if (documentData.isPresent()) {
      return new ResponseEntity<>(documentData.get(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @PostMapping("/documents")
  public ResponseEntity<Document> createDocument(@RequestBody Document document) {
    try {
      Document _document = documentRepository.save(new Document(document.getTitle(), document.getDescription(), false));
      return new ResponseEntity<>(_document, HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PutMapping("/documents/{id}")
  public ResponseEntity<Document> updateDocument(@PathVariable("id") String id, @RequestBody Document document) {
    Optional<Document> documentData = documentRepository.findById(id);

    if (documentData.isPresent()) {
      Document _document = documentData.get();
      _document.setTitle(document.getTitle());
      _document.setDescription(document.getDescription());
      _document.setPublished(document.isPublished());
      return new ResponseEntity<>(documentRepository.save(_document), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping("/documents/{id}")
  public ResponseEntity<HttpStatus> deleteDocument(@PathVariable("id") String id) {
    try {
      documentRepository.deleteById(id);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping("/documents")
  public ResponseEntity<HttpStatus> deleteAllDocuments() {
    try {
      documentRepository.deleteAll();
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/documents/published")
  public ResponseEntity<List<Document>> findByPublished() {
    try {
      List<Document> documents = documentRepository.findByPublished(true);

      if (documents.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }
      return new ResponseEntity<>(documents, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

}
