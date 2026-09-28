package com.niit.sms.config;

import com.niit.sms.model.SchoolClass;
import com.niit.sms.model.Student;
import com.niit.sms.model.Subject;
import com.niit.sms.model.Teacher;
import com.niit.sms.model.User;
import com.niit.sms.model.enums.Gender;
import com.niit.sms.model.enums.Role;
import com.niit.sms.repository.ClassRepository;
import com.niit.sms.repository.StudentRepository;
import com.niit.sms.repository.SubjectRepository;
import com.niit.sms.repository.TeacherRepository;
import com.niit.sms.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final ClassRepository classRepository;
    private final SubjectRepository subjectRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository,
                      StudentRepository studentRepository,
                      TeacherRepository teacherRepository,
                      ClassRepository classRepository,
                      SubjectRepository subjectRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.classRepository = classRepository;
        this.subjectRepository = subjectRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            log.info("DataSeeder: users already exist, skipping seed.");
            return;
        }

        log.info("DataSeeder: seeding demo data...");

        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@school.com");
        admin.setPassword(passwordEncoder.encode("school@2026"));
        admin.setRole(Role.ADMIN);
        userRepository.save(admin);

        SchoolClass jss1a = new SchoolClass();
        jss1a.setName("JSS 1A");
        jss1a.setLevel("JSS 1");
        jss1a.setCapacity(40);
        classRepository.save(jss1a);

        Teacher t1 = new Teacher();
        t1.setStaffNumber("TCH/2025/001");
        t1.setFullName("Mrs. Ada Obi");
        t1.setEmail("teacher@school.com");
        t1.setPhone("08011112222");
        t1.setGender(Gender.FEMALE);
        t1.setQualification("B.Sc Mathematics");
        t1.setClassIds(List.of(jss1a.getId()));
        teacherRepository.save(t1);

        User t1User = new User();
        t1User.setUsername("teacher");
        t1User.setEmail("teacher@school.com");
        t1User.setPassword(passwordEncoder.encode("teacher123"));
        t1User.setRole(Role.TEACHER);
        t1User.setProfileId(t1.getId());
        userRepository.save(t1User);

        t1.setUserId(t1User.getId());
        teacherRepository.save(t1);

        jss1a.setClassTeacherId(t1.getId());
        jss1a.setClassTeacherName(t1.getFullName());
        classRepository.save(jss1a);

        Subject math = new Subject();
        math.setCode("MTH-J1A");
        math.setName("Mathematics");
        math.setClassId(jss1a.getId());
        math.setClassName(jss1a.getName());
        math.setTeacherId(t1.getId());
        math.setTeacherName(t1.getFullName());
        math.setDescription("Junior secondary mathematics");
        subjectRepository.save(math);

        t1.setSubjectIds(List.of(math.getId()));
        teacherRepository.save(t1);

        Teacher t2 = new Teacher();
        t2.setStaffNumber("TCH/2025/002");
        t2.setFullName("Mr. Bello Ade");
        t2.setEmail("bello@school.com");
        t2.setPhone("08033334444");
        t2.setGender(Gender.MALE);
        t2.setQualification("B.Sc Physics");
        teacherRepository.save(t2);

        User t2User = new User();
        t2User.setUsername("bello");
        t2User.setEmail("bello@school.com");
        t2User.setPassword(passwordEncoder.encode("bello123"));
        t2User.setRole(Role.TEACHER);
        t2User.setProfileId(t2.getId());
        userRepository.save(t2User);

        t2.setUserId(t2User.getId());
        teacherRepository.save(t2);

        Student student = new Student();
        student.setAdmissionNumber("STU/2025/001");
        student.setFullName("John Doe");
        student.setEmail("student@school.com");
        student.setPhone("08055556666");
        student.setGender(Gender.MALE);
        student.setDateOfBirth(LocalDate.of(2010, 5, 14));
        student.setClassId(jss1a.getId());
        student.setClassName(jss1a.getName());
        student.setAddress("12 Lagos Street");
        studentRepository.save(student);

        User sUser = new User();
        sUser.setUsername("student");
        sUser.setEmail("student@school.com");
        sUser.setPassword(passwordEncoder.encode("student123"));
        sUser.setRole(Role.STUDENT);
        sUser.setProfileId(student.getId());
        userRepository.save(sUser);

        student.setUserId(sUser.getId());
        studentRepository.save(student);

        log.info("DataSeeder: seeded users -> admin/school@2026, teacher/teacher123, bello/bello123, student/student123");
    }
}