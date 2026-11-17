package com.payflow.identity.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "users")
public class User {

    @Id
     @GeneratedValue (
            strategy = GenerationType.IDENTITY)
    private Long id;
    @Column (
        name="full_name", nullable = false, length = 100)
        private String fullName;
       
    @Column (name="email", nullable = false, unique = true, length = 255)
        private String email;

    @Column (name="password", nullable = false, length = 255)
        private String password;
        
    @CreationTimestamp 
    @Column (
        name="created_at", nullable = false, updatable = false)
        private LocalDateTime createdAt;

    @UpdateTimestamp 
    @Column (
        name="updated_at")
        private LocalDateTime updatedAt;

        protected User() {
        }

        public User(String fullName, String email, String password) {
            this.fullName = fullName;
            this.email = email;
            this.password = password;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
        }

        public LocalDateTime getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
        }


        }




