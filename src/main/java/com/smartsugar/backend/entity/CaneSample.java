
package com.smartsugar.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "cane_samples")
public class CaneSample {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    protected CaneSample() {
    }

    public Long getId() {
        return id;
    }
}
