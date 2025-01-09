package com.linkeleven.msa.feed.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.linkeleven.msa.feed.domain.model.File;

public interface FileRepository extends JpaRepository<File, Long> {
}
