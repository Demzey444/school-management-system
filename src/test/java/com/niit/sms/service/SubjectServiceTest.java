package com.niit.sms.service;

import com.niit.sms.dto.SubjectRequest;
import com.niit.sms.exception.DuplicateResourceException;
import com.niit.sms.model.Subject;
import com.niit.sms.repository.ClassRepository;
import com.niit.sms.repository.SubjectRepository;
import com.niit.sms.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SubjectServiceTest {

    private SubjectRepository subjectRepository;
    private ClassRepository classRepository;
    private TeacherRepository teacherRepository;
    private SubjectService subjectService;

    @BeforeEach
    void setup() {
        subjectRepository = mock(SubjectRepository.class);
        classRepository = mock(ClassRepository.class);
        teacherRepository = mock(TeacherRepository.class);
        subjectService = new SubjectService(subjectRepository, classRepository, teacherRepository);
    }

    @Test
    void shouldCreateSubject() {
        when(subjectRepository.existsByCode("MTH-J1A")).thenReturn(false);
        when(subjectRepository.save(any(Subject.class))).thenAnswer(i -> i.getArgument(0));

        Subject s = subjectService.create(new SubjectRequest("MTH-J1A", "Mathematics", null, null, "desc"));

        assertThat(s.getCode()).isEqualTo("MTH-J1A");
        assertThat(s.getName()).isEqualTo("Mathematics");
        assertThat(s.getClassId()).isNull();
        assertThat(s.getTeacherId()).isNull();
    }

    @Test
    void shouldRejectDuplicateCode() {
        when(subjectRepository.existsByCode("MTH-J1A")).thenReturn(true);

        assertThatThrownBy(() -> subjectService.create(new SubjectRequest("MTH-J1A", "Maths", null, null, null)))
                .isInstanceOf(DuplicateResourceException.class);
    }
}