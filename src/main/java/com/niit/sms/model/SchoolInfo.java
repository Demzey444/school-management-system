package com.niit.sms.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "school_info")
public class SchoolInfo {

    @Id
    private String id;

    private String name;
    private String address;
    private String phone;
    private String email;
    private String motto;
    private String currentSession;
    private String currentTerm;

    @LastModifiedDate
    private Instant updatedAt;

    public SchoolInfo() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMotto() { return motto; }
    public void setMotto(String motto) { this.motto = motto; }

    public String getCurrentSession() { return currentSession; }
    public void setCurrentSession(String currentSession) { this.currentSession = currentSession; }

    public String getCurrentTerm() { return currentTerm; }
    public void setCurrentTerm(String currentTerm) { this.currentTerm = currentTerm; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
