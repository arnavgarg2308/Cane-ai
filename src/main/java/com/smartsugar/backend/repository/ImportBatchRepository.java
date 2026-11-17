
package com.smartsugar.backend.repository;

import com.smartsugar.backend.entity.ImportBatch;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImportBatchRepository
        extends JpaRepository<ImportBatch, Long> {

    Optional<ImportBatch> findByFileHash(String fileHash);

    boolean existsByFileHash(String fileHash);
}
