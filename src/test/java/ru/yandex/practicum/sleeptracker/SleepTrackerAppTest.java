package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class SleepTrackerAppTest {

    // ===== ВСПОМОГАТЕЛЬНЫЙ МЕТОД =====

    private SleepingSession session(String start,
                                    String end,
                                    SleepQuality quality) {
        return TestUtils.session(start, end, quality);
    }

    // =====================================================
    // TOTAL SESSIONS
    // =====================================================

    @Test
    void totalSessions_shouldReturnCorrectCount() {
        List<SleepingSession> sessions = List.of(
                session("01.10.25 22:00", "02.10.25 07:00", SleepQuality.GOOD),
                session("02.10.25 23:00", "03.10.25 07:00", SleepQuality.NORMAL)
        );

        assertEquals(2,
                new TotalSessionsAnalysis().apply(sessions).getValue());
    }

    @Test
    void totalSessions_shouldReturnZeroForEmptyList() {
        assertEquals(0,
                new TotalSessionsAnalysis().apply(List.of()).getValue());
    }

    // =====================================================
    // MIN DURATION
    // =====================================================

    @Test
    void minDuration_shouldFindMinimum() {
        List<SleepingSession> sessions = List.of(
                session("01.10.25 22:00", "02.10.25 07:00", SleepQuality.GOOD), // 540
                session("02.10.25 23:00", "03.10.25 06:00", SleepQuality.NORMAL) // 420
        );

        assertEquals(420,
                new MinDurationAnalysis().apply(sessions).getValue());
    }

    @Test
    void minDuration_shouldReturnZeroIfEmpty() {
        assertEquals(0,
                new MinDurationAnalysis().apply(List.of()).getValue());
    }

    // =====================================================
    // MAX DURATION
    // =====================================================

    @Test
    void maxDuration_shouldFindMaximum() {
        List<SleepingSession> sessions = List.of(
                session("01.10.25 22:00", "02.10.25 07:00", SleepQuality.GOOD), // 540
                session("02.10.25 23:00", "03.10.25 06:00", SleepQuality.NORMAL) // 420
        );

        assertEquals(540,
                new MaxDurationAnalysis().apply(sessions).getValue());
    }

    @Test
    void maxDuration_shouldReturnZeroIfEmpty() {
        assertEquals(0,
                new MaxDurationAnalysis().apply(List.of()).getValue());
    }

    // =====================================================
    // AVG DURATION
    // =====================================================

    @Test
    void avgDuration_shouldCalculateCorrectly() {
        List<SleepingSession> sessions = List.of(
                session("01.10.25 22:00", "02.10.25 07:00", SleepQuality.GOOD), // 540
                session("02.10.25 23:00", "03.10.25 07:00", SleepQuality.NORMAL) // 480
        );

        assertEquals(510.0,
                new AvgDurationAnalysis().apply(sessions).getValue());
    }

    @Test
    void avgDuration_shouldReturnZeroIfEmpty() {
        assertEquals(0,
                new AvgDurationAnalysis().apply(List.of()).getValue());
    }

    // =====================================================
    // BAD QUALITY
    // =====================================================

    @Test
    void badQuality_shouldCountCorrectly() {
        List<SleepingSession> sessions = List.of(
                session("01.10.25 22:00", "02.10.25 07:00", SleepQuality.BAD),
                session("02.10.25 23:00", "03.10.25 07:00", SleepQuality.GOOD),
                session("03.10.25 23:00", "04.10.25 07:00", SleepQuality.BAD)
        );

        assertEquals(2,
                new BadQualityCountAnalysis().apply(sessions).getValue());
    }

    @Test
    void badQuality_shouldReturnZeroIfNone() {
        List<SleepingSession> sessions = List.of(
                session("01.10.25 22:00", "02.10.25 07:00", SleepQuality.GOOD)
        );

        assertEquals(0,
                new BadQualityCountAnalysis().apply(sessions).getValue());
    }

    // =====================================================
    // SLEEPLESS NIGHTS (4 теста)
    // =====================================================

    @Test
    void sleepless_shouldDetectOneNight() {
        List<SleepingSession> sessions = List.of(
                session("01.10.25 22:00", "02.10.25 07:00", SleepQuality.GOOD),
                session("03.10.25 07:00", "03.10.25 09:00", SleepQuality.NORMAL)
        );

        assertEquals(1,
                new SleeplessNightsAnalysis().apply(sessions).getValue());
    }

    @Test
    void sleepFrom2To7_shouldNotBeSleepless() {
        List<SleepingSession> sessions = List.of(
                session("01.10.25 02:00", "01.10.25 07:00", SleepQuality.GOOD)
        );

        assertEquals(0,
                new SleeplessNightsAnalysis().apply(sessions).getValue());
    }

    @Test
    void sleepAfterSeven_shouldBeSleepless() {
        List<SleepingSession> sessions = List.of(
                session("01.10.25 07:00", "01.10.25 10:00", SleepQuality.NORMAL)
        );

        assertEquals(1,
                new SleeplessNightsAnalysis().apply(sessions).getValue());
    }

    @Test
    void sleepless_shouldWorkAcrossMonths() {
        List<SleepingSession> sessions = List.of(
                session("30.10.25 22:00", "31.10.25 07:00", SleepQuality.GOOD),
                session("02.11.25 22:00", "03.11.25 07:00", SleepQuality.GOOD)
        );

        assertEquals(1,
                new SleeplessNightsAnalysis().apply(sessions).getValue());
    }

    // =====================================================
    // CHRONOTYPE
    // =====================================================

    @Test
    void chronotype_shouldDetectOwl() {
        List<SleepingSession> sessions = List.of(
                session("01.10.25 23:30", "02.10.25 09:30", SleepQuality.GOOD)
        );

        assertEquals(Chronotype.OWL,
                new ChronotypeAnalysis().apply(sessions).getValue());
    }

    @Test
    void chronotype_shouldDetectLark() {
        List<SleepingSession> sessions = List.of(
                session("01.10.25 21:00", "02.10.25 06:00", SleepQuality.GOOD)
        );

        assertEquals(Chronotype.LARK,
                new ChronotypeAnalysis().apply(sessions).getValue());
    }

    @Test
    void chronotype_shouldDetectPigeon() {
        List<SleepingSession> sessions = List.of(
                session("01.10.25 22:30", "02.10.25 08:00", SleepQuality.GOOD)
        );

        assertEquals(Chronotype.PIGEON,
                new ChronotypeAnalysis().apply(sessions).getValue());
    }

    @Test
    void chronotype_equalCounts_shouldReturnPigeon() {
        List<SleepingSession> sessions = List.of(
                session("01.10.25 23:30", "02.10.25 09:30", SleepQuality.GOOD),
                session("02.10.25 21:00", "03.10.25 06:00", SleepQuality.GOOD)
        );

        assertEquals(Chronotype.PIGEON,
                new ChronotypeAnalysis().apply(sessions).getValue());
    }
}