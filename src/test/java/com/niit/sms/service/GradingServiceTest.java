package com.niit.sms.service;

import com.niit.sms.model.enums.Grade;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GradingServiceTest {

    private final GradingService gradingService = new GradingService();

    @Test
    void shouldCalculateTotalScore() {
        assertThat(gradingService.calculateTotal(30, 55)).isEqualTo(85);
        assertThat(gradingService.calculateTotal(0, 0)).isEqualTo(0);
        assertThat(gradingService.calculateTotal(40, 60)).isEqualTo(100);
    }

    @Test
    void shouldRejectTestScoreOutOfRange() {
        assertThatThrownBy(() -> gradingService.calculateTotal(-1, 30))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> gradingService.calculateTotal(41, 30))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldRejectExamScoreOutOfRange() {
        assertThatThrownBy(() -> gradingService.calculateTotal(30, -1))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> gradingService.calculateTotal(30, 61))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldAssignGradeAFor70To100() {
        assertThat(gradingService.calculateGrade(70)).isEqualTo(Grade.A);
        assertThat(gradingService.calculateGrade(85)).isEqualTo(Grade.A);
        assertThat(gradingService.calculateGrade(100)).isEqualTo(Grade.A);
    }

    @Test
    void shouldAssignGradeBFor60To69() {
        assertThat(gradingService.calculateGrade(60)).isEqualTo(Grade.B);
        assertThat(gradingService.calculateGrade(69)).isEqualTo(Grade.B);
    }

    @Test
    void shouldAssignGradeCFor50To59() {
        assertThat(gradingService.calculateGrade(50)).isEqualTo(Grade.C);
        assertThat(gradingService.calculateGrade(59)).isEqualTo(Grade.C);
    }

    @Test
    void shouldAssignGradeDFor45To49() {
        assertThat(gradingService.calculateGrade(45)).isEqualTo(Grade.D);
        assertThat(gradingService.calculateGrade(49)).isEqualTo(Grade.D);
    }

    @Test
    void shouldAssignGradeEFor40To44() {
        assertThat(gradingService.calculateGrade(40)).isEqualTo(Grade.E);
        assertThat(gradingService.calculateGrade(44)).isEqualTo(Grade.E);
    }

    @Test
    void shouldAssignGradeFForBelow40() {
        assertThat(gradingService.calculateGrade(0)).isEqualTo(Grade.F);
        assertThat(gradingService.calculateGrade(39)).isEqualTo(Grade.F);
    }

    @Test
    void shouldReturnRemarkMatchingGrade() {
        assertThat(gradingService.remarkFor(Grade.A)).isEqualTo("Excellent");
        assertThat(gradingService.remarkFor(Grade.F)).isEqualTo("Fail");
    }
}