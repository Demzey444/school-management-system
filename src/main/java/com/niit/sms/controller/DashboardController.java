package com.niit.sms.controller;

import com.niit.sms.dto.AdminDashboardResponse;
import com.niit.sms.dto.StudentDashboardResponse;
import com.niit.sms.dto.TeacherDashboardResponse;
import com.niit.sms.model.User;
import com.niit.sms.security.SecurityUtils;
import com.niit.sms.service.DashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;
    private final SecurityUtils securityUtils;

    public DashboardController(DashboardService dashboardService, SecurityUtils securityUtils) {
        this.dashboardService = dashboardService;
        this.securityUtils = securityUtils;
    }

    @GetMapping("/admin")
    public AdminDashboardResponse admin() {
        return dashboardService.adminStats();
    }

    @GetMapping("/teacher")
    public TeacherDashboardResponse teacher() {
        User u = securityUtils.currentUser();
        return dashboardService.teacherStats(u.getProfileId());
    }

    @GetMapping("/student")
    public StudentDashboardResponse student() {
        User u = securityUtils.currentUser();
        return dashboardService.studentStats(u.getProfileId());
    }
}