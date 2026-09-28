package com.niit.sms.repository;

import com.niit.sms.model.Result;
import com.niit.sms.model.enums.Term;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ResultRepository extends MongoRepository<Result, String> {

    List<Result> findByStudentId(String studentId);
    List<Result> findByClassId(String classId);
    List<Result> findBySubjectId(String subjectId);
    List<Result> findByStudentIdAndSessionAndTerm(String studentId, String session, Term term);
    List<Result> findByClassIdAndSessionAndTerm(String classId, String session, Term term);

    Optional<Result> findByStudentIdAndSubjectIdAndSessionAndTerm(
            String studentId, String subjectId, String session, Term term);
}
