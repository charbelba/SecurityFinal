package com.example.SecurityFinal.Api.Entity;

import com.example.SecurityFinal.Api.Security.AuditListener;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners({AuditingEntityListener.class, AuditListener.class})
@Data
@AllArgsConstructor
@NoArgsConstructor

public abstract class BaseEntity {

    @CreatedDate
    @Column(updatable = false)
    @JsonIgnore
    private LocalDateTime createdDate;


    @LastModifiedDate
    @JsonIgnore
    private LocalDateTime lastModifiedDate;


    @LastModifiedBy
    @JsonIgnore
    private String lastModifiedBy;

}
