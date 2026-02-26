package ru.yandex.practicum.sleeptracker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

public class SleeplessNightsAnalysis implements SleepAnalysis {

    @Override
    public SleepAnalysisResult<Integer> apply(List<SleepingSession> sessions) {

        if (sessions.isEmpty()) {
            return new SleepAnalysisResult<>("Бессонные ночи", 0);
        }

        List<SleepingSession> sorted = sessions.stream()
                .sorted(Comparator.comparing(SleepingSession::getStart))
                .toList();

        LocalDate firstNight = sorted.getFirst().getStart().toLocalDate();

        if (sorted.getFirst().getStart().toLocalTime().isAfter(LocalTime.NOON)) {
            firstNight = firstNight.plusDays(1);
        }
        LocalDate lastNight = sorted.getLast().getEnd().toLocalDate().minusDays(1);

        int sleeplessCount = 0;

        for (LocalDate date = firstNight;
             !date.isAfter(lastNight);
             date = date.plusDays(1)) {

            LocalDateTime nightStart = date.atStartOfDay(); // 00:00
            LocalDateTime nightEnd = date.atTime(6, 0);     // 06:00

            boolean sleptAtNight = sorted.stream().anyMatch(s ->
                    s.getStart().isBefore(nightEnd) &&
                            s.getEnd().isAfter(nightStart)
            );

            if (!sleptAtNight) {
                sleeplessCount++;
            }
        }

        return new SleepAnalysisResult<>("Бессонные ночи", sleeplessCount);
    }
}