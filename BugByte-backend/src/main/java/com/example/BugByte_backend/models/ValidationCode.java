package com.example.BugByte_backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="validation_code")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ValidationCode {

    @Id
    @OneToOne
    @JoinColumn(name = "id", nullable = false)
    private User user;

    @Column(name = "code", nullable = false, unique = true)
    private String code;
}
