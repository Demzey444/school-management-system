package com.niit.sms.service;

import com.niit.sms.dto.ClassRequest;
import com.niit.sms.exception.DuplicateResourceException;
import com.niit.sms.model.SchoolClass;
import com.niit.sms.repository.ClassRepository;
import com.niit.sms.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ClassServiceTest {

    private ClassRepository classRepository;
    private TeacherRepository teacherRepository;
    private ClassService classService;

    @BeforeEach
    void setup() {
        classRepository = mock(ClassRepository.class);
        teacherRepository = mock(TeacherRepository.class);
        classService = new ClassService(classRepository, teacherRepository);
    }

    @Test
    void shouldCreateClass() {
        when(classRepository.existsByName("JSS 2A")).thenReturn(false);
        when(classRepository.save(any(SchoolClass.class))).thenAnswer(i -> i.getArgument(0));

        SchoolClass c = classService.create(new ClassRequest("JSS 2A", "JSS 2", 40, null));

        assertThat(c.getName()).isEqualTo("JSS 2A");
        assertThat(c.getCapacity()).isEqualTo(40);
        assertThat(c.getClassTeacherId()).isNull();
    }

    @Test
    void shouldRejectDuplicateName() {
        when(classRepository.existsByName("JSS 1A")).thenReturn(true);

        assertThatThrownBy(() -> classService.create(new ClassRequest("JSS 1A", "JSS 1", 40, null)))
                .isInstanceOf(DuplicateResourceException.class);
    }
}