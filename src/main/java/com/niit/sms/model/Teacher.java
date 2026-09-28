package com.niit.sms.model;

import com.niit.sms.model.enums.Gender;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "teachers")
public class Teacher {

    @Id
    private String id;

    private String userId;

    @Indexed(unique = true)
    private String staffNumber;

    private String fullName;
    private String email;
    private String phone;
    private Gender gender;
    private String qualification;
    private List<String> subjectIds = new ArrayList<>();
    private List<String> classIds = new ArrayList<>();

    @CreatedDate
    private Instant dateRegistered;

    public Teacher() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getStaffNumber() { return staffNumber; }
    public void setStaffNumber(String staffNumber) { this.staffNumber = staffNumber; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public Gender getGender() { return gender; }
    public void setGender(Gender gender) { this.gender = gender; }

    public String getQualification() { return qualification; }
    public void setQualification(String qualification) { this.qualification = qualification; }

    public List<String> getSubjectIds() { return subjectIds; }
    public void setSubjectIds(List<String> subjectIds) { this.subjectIds = subjectIds; }

    public List<String> getClassIds() { return classIds; }
    public void setClassIds(List<String> classIds) { this.classIds = classIds; }

    public Instant getDateRegistered() { return dateRegistered; }
    public void setDateRegistered(Instant dateRegistered) { this.dateRegistered = dateRegistered; }
}