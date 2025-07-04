package com.example.chat.domain.repository;

import com.example.chat.domain.entity.ChatSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatSessionRepository extends JpaRepository<ChatSession, Long> {
    @Query("SELECT s FROM ChatSession s WHERE s.userId = :userId AND s.isDeleted = false ORDER BY s.isPinned DESC, s.updatedAt DESC")
    List<ChatSession> findRecentByUserId(@Param("userId") String userId);

    @Query("SELECT s FROM ChatSession s WHERE s.userId = :userId AND s.isDeleted = false ORDER BY s.isPinned DESC, s.lastMessageAt DESC")
    List<ChatSession> findRecentByUserIdLastMsg(@Param("userId") String userId);

    @Query("SELECT s FROM ChatSession s WHERE s.shareToken = :shareToken AND s.isShared = true")
    ChatSession findByShareToken(@Param("shareToken") String shareToken);
} 