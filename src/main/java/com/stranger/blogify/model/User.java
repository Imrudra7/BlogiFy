package com.stranger.blogify.model;



import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

public class User {

    @Id
    private String id;
    private String name;

    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String status; // Active | Slept
    private Instant createdAt;
    private Instant updatedAt;

    private List<String> questionsDone;
    private String questionToBeAddedOrRemoved;
    private String AddOrRemove;

    private AuthProvider authProvider;
    private LocalDateTime lastLogin;
    private String role;
    private String provider;
    private boolean active;
    public User(String email, String name) {
    }
    public User (){};

    public String getQuestionToBeAddedOrRemoved() {
        return questionToBeAddedOrRemoved;
    }

    public void setQuestionToBeAddedOrRemoved(String questionToBeAddedOrRemoved) {
        this.questionToBeAddedOrRemoved = questionToBeAddedOrRemoved;
    }

    public String isQuestionAdded() {
        return AddOrRemove;
    }

    public void setAddOrRemove(String addOrRemove) {
        this.AddOrRemove = addOrRemove;
    }

    public String getAddOrRemove() {
        return AddOrRemove;
    }

    public List<String> getQuestionsDone() {
        return questionsDone;
    }

    public void setQuestionsDone(List<String> questionsDone) {
        this.questionsDone = questionsDone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public AuthProvider getAuthProvider() {
        return authProvider;
    }

    public void setAuthProvider(AuthProvider authProvider) {
        this.authProvider = authProvider;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin=lastLogin;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getProvider() {
        return provider;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }
}
