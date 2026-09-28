package com.niit.sms.service;

import com.niit.sms.dto.ResultRequest;
import com.niit.sms.exception.DuplicateResourceException;
import com.niit.sms.model.Result;
import com.niit.sms.model.Student;
import com.niit.sms.model.Subject;
import com.niit.sms.model.enums.Grade;
import com.niit.sms.model.enums.Term;
import com.niit.sms.repository.ResultRepository;
import com.niit.sms.repository.StudentRepository;
import com.niit.sms.repository.SubjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ResultServiceTest {

    private ResultRepository resultRepository;
    private StudentRepository studentRepository;
    private SubjectRepository subjectRepository;
    private GradingService gradingService;
    private ResultService resultService;

    @BeforeEach
    void setup() {
        resultRepository = mock(ResultRepository.class);
        studentRepository = mock(StudentRepository.class);
        subjectRepository = mock(SubjectRepository.class);
        gradingService = new GradingService();
        resultService = new ResultService(resultRepository, studentRepository, subjectRepository, gradingService);
    }

    @Test
    void shouldCreateResultWithComputedTotalAndGrade() {
        when(resultRepository.findByStudentIdAndSubjectIdAndSessionAndTerm(any(), any(), any(), any()))
                .thenReturn(Optional.empty());
        when(studentRepository.findById("s1")).thenReturn(Optional.of(student("s1")));
        when(subjectRepository.findById("sub1")).thenReturn(Optional.of(subject("sub1")));
        when(resultRepository.save(any(Result.class))).thenAnswer(i -> i.getArgument(0));

        Result r = resultService.create(new ResultRequest("s1", "sub1", "2025/2026", Term.FIRST, 30, 55));

        assertThat(r.getTotalScore()).isEqualTo(85);
        assertThat(r.getGrade()).isEqualTo(Grade.A);
        assertThat(r.getRemark()).isEqualTo("Excellent");
    }

    @Test
    void shouldRejectDuplicateResult() {
        when(resultRepository.findByStudentIdAndSubjectIdAndSessionAndTerm(any(), any(), any(), any()))
                .thenReturn(Optional.of(new Result()));

        assertThatThrownBy(() ->
                resultService.create(new ResultRequest("s1", "sub1", "2025/2026", Term.FIRST, 30, 55)))
                .isInstanceOf(DuplicateResourceException.class);
    }

    private Student student(String id) {
        Student s = new Student();
        s.setId(id);
        s.setFullName("John Doe");
        s.setAdmissionNumber("STU/2025/001");
        return s;
    }

    private Subject subject(String id) {
        Subject s = new Subject();
        s.setId(id);
        s.setName("Mathematics");
        return s;
    }
}