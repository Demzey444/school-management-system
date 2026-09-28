package com.niit.sms.service;

import com.niit.sms.model.enums.Grade;
import org.springframework.stereotype.Service;

@Service
public class GradingService {

    /**
     * Calculates total score (0-100).
     */
    public int calculateTotal(int testScore, int examScore) {
        if (testScore < 0 || testScore > 40) {
            throw new IllegalArgumentException("Test score must be between 0 and 40");
        }
        if (examScore < 0 || examScore > 60) {
            throw new IllegalArgumentException("Exam score must be between 0 and 60");
        }
        return testScore + examScore;
    }

    /**
     * Maps total score to grade.
     * 70-100 => A
     * 60-69  => B
     * 50-59  => C
     * 45-49  => D
     * 40-44  => E
     * 0-39   => F
     */
    public Grade calculateGrade(int total) {
        if (total < 0 || total > 100) {
            throw new IllegalArgumentException("Total score must be between 0 and 100");
        }
        if (total >= 70) return Grade.A;
        if (total >= 60) return Grade.B;
        if (total >= 50) return Grade.C;
        if (total >= 45) return Grade.D;
        if (total >= 40) return Grade.E;
        return Grade.F;
    }

    public String remarkFor(Grade grade) {
        return grade.getDefaultRemark();
    }
}