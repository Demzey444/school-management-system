package com.niit.sms.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "classes")
public class SchoolClass {

    @Id
    private String id;

    @Indexed(unique = true)
    private String name;

    private String level;
    private Integer capacity;
    private String classTeacherId;
    private String classTeacherName;

    @CreatedDate
    private Instant createdAt;

    public SchoolClass() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public String getClassTeacherId() { return classTeacherId; }
    public void setClassTeacherId(String classTeacherId) { this.classTeacherId = classTeacherId; }

    public String getClassTeacherName() { return classTeacherName; }
    public void setClassTeacherName(String classTeacherName) { this.classTeacherName = classTeacherName; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
