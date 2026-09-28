package com.niit.sms.service;

import com.niit.sms.dto.ReportCardResponse;
import com.niit.sms.dto.ResultRequest;
import com.niit.sms.exception.DuplicateResourceException;
import com.niit.sms.exception.ResourceNotFoundException;
import com.niit.sms.model.Result;
import com.niit.sms.model.Student;
import com.niit.sms.model.Subject;
import com.niit.sms.model.enums.Grade;
import com.niit.sms.repository.ResultRepository;
import com.niit.sms.repository.StudentRepository;
import com.niit.sms.repository.SubjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResultService {

    private final ResultRepository resultRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final GradingService gradingService;

    public ResultService(ResultRepository resultRepository,
                         StudentRepository studentRepository,
                         SubjectRepository subjectRepository,
                         GradingService gradingService) {
        this.resultRepository = resultRepository;
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
        this.gradingService = gradingService;
    }

    public List<Result> findAll(String studentId, String classId, String subjectId, String session) {
        if (studentId != null && !studentId.isBlank()) {
            return resultRepository.findByStudentId(studentId);
        }
        if (classId != null && !classId.isBlank()) {
            return resultRepository.findByClassId(classId);
        }
        if (subjectId != null && !subjectId.isBlank()) {
            return resultRepository.findBySubjectId(subjectId);
        }
        return resultRepository.findAll();
    }

    public Result findById(String id) {
        return resultRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Result not found with id: " + id));
    }

    public List<Result> findByStudentSessionTerm(String studentId, String session, com.niit.sms.model.enums.Term term) {
        return resultRepository.findByStudentIdAndSessionAndTerm(studentId, session, term);
    }

    public Result create(ResultRequest req) {
        // Duplicate check
        resultRepository.findByStudentIdAndSubjectIdAndSessionAndTerm(
                req.studentId(), req.subjectId(), req.session(), req.term()
        ).ifPresent(r -> {
            throw new DuplicateResourceException(
                    "A result already exists for this student/subject/session/term combination");
        });

        Student student = studentRepository.findById(req.studentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + req.studentId()));

        Subject subject = subjectRepository.findById(req.subjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id: " + req.subjectId()));

        Result r = new Result();
        populate(r, req, student, subject);
        return resultRepository.save(r);
    }

    public Result update(String id, ResultRequest req) {
        Result r = findById(id);

        Student student = studentRepository.findById(req.studentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + req.studentId()));

        Subject subject = subjectRepository.findById(req.subjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id: " + req.subjectId()));

        populate(r, req, student, subject);
        return resultRepository.save(r);
    }

    public void delete(String id) {
        Result r = findById(id);
        resultRepository.delete(r);
    }

    public ReportCardResponse buildReportCard(String studentId, String session, com.niit.sms.model.enums.Term term) {
        Student s = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));

        List<Result> results = resultRepository.findByStudentIdAndSessionAndTerm(studentId, session, term);

        int totalScore = results.stream().mapToInt(Result::getTotalScore).sum();
        double average = results.isEmpty() ? 0.0 : (double) totalScore / results.size();
        String overallGrade = results.isEmpty() ? "-" : gradingService.calculateGrade((int) Math.round(average)).name();

        return new ReportCardResponse(
                s.getId(),
                s.getFullName(),
                s.getAdmissionNumber(),
                s.getClassName(),
                session,
                term.name(),
                results,
                results.size(),
                totalScore,
                Math.round(average * 100.0) / 100.0,
                overallGrade
        );
    }

    private void populate(Result r, ResultRequest req, Student student, Subject subject) {
        r.setStudentId(student.getId());
        r.setStudentName(student.getFullName());
        r.setAdmissionNumber(student.getAdmissionNumber());
        r.setClassId(student.getClassId());
        r.setClassName(student.getClassName());

        r.setSubjectId(subject.getId());
        r.setSubjectName(subject.getName());

        r.setSession(req.session());
        r.setTerm(req.term());
        r.setTestScore(req.testScore());
        r.setExamScore(req.examScore());

        int total = gradingService.calculateTotal(req.testScore(), req.examScore());
        Grade grade = gradingService.calculateGrade(total);

        r.setTotalScore(total);
        r.setGrade(grade);
        r.setRemark(gradingService.remarkFor(grade));
    }
}