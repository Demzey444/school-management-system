package com.niit.sms.dto;

import com.niit.sms.model.SchoolClass;
import com.niit.sms.model.Subject;

import java.util.List;

public record TeacherDashboardResponse(
        String teacherName,
        long mySubjects,
        long myClasses,
        List<Subject> subjects,
        List<SchoolClass> classes
) {
}