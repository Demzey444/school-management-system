package com.niit.sms.service;

import com.niit.sms.dto.ClassRequest;
import com.niit.sms.exception.DuplicateResourceException;
import com.niit.sms.exception.ResourceNotFoundException;
import com.niit.sms.model.SchoolClass;
import com.niit.sms.model.Teacher;
import com.niit.sms.repository.ClassRepository;
import com.niit.sms.repository.TeacherRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassService {

    private final ClassRepository classRepository;
    private final TeacherRepository teacherRepository;

    public ClassService(ClassRepository classRepository, TeacherRepository teacherRepository) {
        this.classRepository = classRepository;
        this.teacherRepository = teacherRepository;
    }

    public List<SchoolClass> findAll() {
        return classRepository.findAll();
    }

    public SchoolClass findById(String id) {
        return classRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Class not found with id: " + id));
    }

    public SchoolClass create(ClassRequest req) {
        if (classRepository.existsByName(req.name())) {
            throw new DuplicateResourceException("A class named '" + req.name() + "' already exists");
        }
        SchoolClass c = new SchoolClass();
        applyRequest(c, req);
        return classRepository.save(c);
    }

    public SchoolClass update(String id, ClassRequest req) {
        SchoolClass c = findById(id);
        if (!c.getName().equals(req.name()) && classRepository.existsByName(req.name())) {
            throw new DuplicateResourceException("A class named '" + req.name() + "' already exists");
        }
        applyRequest(c, req);
        return classRepository.save(c);
    }

    public void delete(String id) {
        SchoolClass c = findById(id);
        classRepository.delete(c);
    }

    private void applyRequest(SchoolClass c, ClassRequest req) {
        c.setName(req.name());
        c.setLevel(req.level());
        c.setCapacity(req.capacity());

        if (req.classTeacherId() != null && !req.classTeacherId().isBlank()) {
            Teacher t = teacherRepository.findById(req.classTeacherId())
                    .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + req.classTeacherId()));
            c.setClassTeacherId(t.getId());
            c.setClassTeacherName(t.getFullName());
        } else {
            c.setClassTeacherId(null);
            c.setClassTeacherName(null);
        }
    }
}