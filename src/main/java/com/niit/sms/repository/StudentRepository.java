package com.niit.sms.repository;

import com.niit.sms.model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends MongoRepository<Student, String> {

    Optional<Student> findByAdmissionNumber(String admissionNumber);
    Optional<Student> findByUserId(String userId);
    List<Student> findByClassId(String classId);
    boolean existsByAdmissionNumber(String admissionNumber);

    @Query("{ $or: [ " +
           "{ 'fullName': { $regex: ?0, $options: 'i' } }, " +
           "{ 'admissionNumber': { $regex: ?0, $options: 'i' } }, " +
           "{ 'email': { $regex: ?0, $options: 'i' } } ] }")
    List<Student> search(String term);
}
