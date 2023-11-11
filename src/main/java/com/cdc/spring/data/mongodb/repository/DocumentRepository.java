package com.cdc.spring.data.mongodb.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cdc.spring.data.mongodb.model.Document;

public interface DocumentRepository extends MongoRepository<Document, String> {
  List<Document> findByPublished(boolean published);
  List<Document> findByTitleContaining(String title);
}
