package com.niit.sms.service;

import com.niit.sms.dto.SubjectRequest;
import com.niit.sms.exception.DuplicateResourceException;
import com.niit.sms.exception.ResourceNotFoundException;
import com.niit.sms.model.SchoolClass;
import com.niit.sms.model.Subject;
import com.niit.sms.model.Teacher;
import com.niit.sms.repository.ClassRepository;
import com.niit.sms.repository.SubjectRepository;
import com.niit.sms.repository.TeacherRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final ClassRepository classRepository;
    private final TeacherRepository teacherRepository;

    public SubjectService(SubjectRepository subjectRepository,
                          ClassRepository classRepository,
                          TeacherRepository teacherRepository) {
        this.subjectRepository = subjectRepository;
        this.classRepository = classRepository;
        this.teacherRepository = teacherRepository;
    }

    public List<Subject> findAll(String classId, String teacherId) {
        if (classId != null && !classId.isBlank()) {
            return subjectRepository.findByClassId(classId);
        }
        if (teacherId != null && !teacherId.isBlank()) {
            return subjectRepository.findByTeacherId(teacherId);
        }
        return subjectRepository.findAll();
    }

    public Subject findById(String id) {
        return subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id: " + id));
    }

    public Subject create(SubjectRequest req) {
        if (subjectRepository.existsByCode(req.code())) {
            throw new DuplicateResourceException("A subject with code '" + req.code() + "' already exists");
        }
        Subject s = new Subject();
        applyRequest(s, req);
        return subjectRepository.save(s);
    }

    public Subject update(String id, SubjectRequest req) {
        Subject s = findById(id);
        if (!s.getCode().equals(req.code()) && subjectRepository.existsByCode(req.code())) {
            throw new DuplicateResourceException("A subject with code '" + req.code() + "' already exists");
        }
        applyRequest(s, req);
        return subjectRepository.save(s);
    }

    public void delete(String id) {
        Subject s = findById(id);
        subjectRepository.delete(s);
    }

    private void applyRequest(Subject s, SubjectRequest req) {
        s.setCode(req.code());
        s.setName(req.name());
        s.setDescription(req.description());

        if (req.classId() != null && !req.classId().isBlank()) {
            SchoolClass c = classRepository.findById(req.classId())
                    .orElseThrow(() -> new ResourceNotFoundException("Class not found with id: " + req.classId()));
            s.setClassId(c.getId());
            s.setClassName(c.getName());
        } else {
            s.setClassId(null);
            s.setClassName(null);
        }

        if (req.teacherId() != null && !req.teacherId().isBlank()) {
            Teacher t = teacherRepository.findById(req.teacherId())
                    .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + req.teacherId()));
            s.setTeacherId(t.getId());
            s.setTeacherName(t.getFullName());
        } else {
            s.setTeacherId(null);
            s.setTeacherName(null);
        }
    }
}