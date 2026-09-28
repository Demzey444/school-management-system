package com.niit.sms.repository;

import com.niit.sms.model.Subject;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface SubjectRepository extends MongoRepository<Subject, String> {
    Optional<Subject> findByCode(String code);
    boolean existsByCode(String code);
    List<Subject> findByClassId(String classId);
    List<Subject> findByTeacherId(String teacherId);
}
