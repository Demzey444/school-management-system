package com.niit.sms.repository;

import com.niit.sms.model.Teacher;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends MongoRepository<Teacher, String> {

    Optional<Teacher> findByStaffNumber(String staffNumber);
    Optional<Teacher> findByUserId(String userId);
    boolean existsByStaffNumber(String staffNumber);

    @Query("{ $or: [ " +
           "{ 'fullName': { $regex: ?0, $options: 'i' } }, " +
           "{ 'staffNumber': { $regex: ?0, $options: 'i' } }, " +
           "{ 'email': { $regex: ?0, $options: 'i' } } ] }")
    List<Teacher> search(String term);
}
