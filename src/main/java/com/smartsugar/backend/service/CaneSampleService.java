
package com.smartsugar.backend.service;

import com.smartsugar.backend.entity.CaneSample;
import com.smartsugar.backend.repository.CaneSampleRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CaneSampleService {

    private final CaneSampleRepository caneSampleRepository;

    public CaneSampleService(CaneSampleRepository caneSampleRepository) {
        this.caneSampleRepository = caneSampleRepository;
    }

    // Fetch all cane samples
    public List<CaneSample> getAllSamples() {
        return caneSampleRepository.findAll();
    }

    // Fetch sample by ID
    public Optional<CaneSample> getSampleById(Long id) {
        return caneSampleRepository.findById(id);
    }

    // Save a cane sample
    public CaneSample saveSample(CaneSample sample) {
        return caneSampleRepository.save(sample);
    }

    // Delete sample by ID
    public void deleteSample(Long id) {
        caneSampleRepository.deleteById(id);
    }

    // Get total number of samples
    public long getTotalSamples() {
        return caneSampleRepository.count();
    }
}
