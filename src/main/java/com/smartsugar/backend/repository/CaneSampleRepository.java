
package com.smartsugar.backend.repository;

import com.smartsugar.backend.entity.CaneSample;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaneSampleRepository
        extends JpaRepository<CaneSample, Long> {

}
