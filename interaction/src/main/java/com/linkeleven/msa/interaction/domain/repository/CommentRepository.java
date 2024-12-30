package com.linkeleven.msa.interaction.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.linkeleven.msa.interaction.domain.model.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
