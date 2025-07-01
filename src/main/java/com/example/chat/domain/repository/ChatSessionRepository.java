package com.example.chat.domain.repository;

import com.example.chat.domain.entity.ChatSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatSessionRepository extends JpaRepository<ChatSession, Long> {
    List<ChatSession> findByUserIdAndIsDeletedFalseOrderByIsPinnedDescUpdatedAtDesc(String userId);
} 