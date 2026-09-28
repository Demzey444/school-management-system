package com.niit.sms.controller;

import com.niit.sms.security.SecurityUtils;
import com.niit.sms.service.ProfileService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService profileService;
    private final SecurityUtils securityUtils;

    public ProfileController(ProfileService profileService, SecurityUtils securityUtils) {
        this.profileService = profileService;
        this.securityUtils = securityUtils;
    }

    @GetMapping("/me")
    public Map<String, Object> me() {
        return profileService.profileFor(securityUtils.currentUser());
    }
}