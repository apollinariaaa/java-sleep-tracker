package ru.yandex.practicum.sleeptracker;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.function.Function;
import java.util.stream.LongStream;

public class SleeplessNightsAnalysis
        implements Function<List<SleepingSession>, SleepAnalysisResult<Long>> {

    @Override
    public SleepAnalysisResult<Long> apply(List<SleepingSession> sessions) {

        if (sessions.isEmpty()) {
            return new SleepAnalysisResult<>("Количество бессонных ночей", 0L);
        }

        LocalDate startDate = adjustStartDate(sessions.get(0));
        LocalDate endDate = sessions.get(sessions.size() - 1).getEnd().toLocalDate();

        long totalNights = ChronoUnit.DAYS.between(startDate, endDate);

        long sleepless = LongStream.range(0, totalNights)
                .mapToObj(startDate::plusDays)
                .filter(date -> isSleepless(date, sessions))
                .count();

        return new SleepAnalysisResult<>("Количество бессонных ночей", sleepless);
    }

    private LocalDate adjustStartDate(SleepingSession first) {
        return first.getStart().getHour() < 12
                ? first.getStart().toLocalDate().minusDays(1)
                : first.getStart().toLocalDate();
    }

    private boolean isSleepless(LocalDate date, List<SleepingSession> sessions) {

        LocalDateTime nightStart = date.atStartOfDay();
        LocalDateTime nightEnd = date.atTime(6, 0);

        return sessions.stream()
                .noneMatch(s ->
                        s.getStart().isBefore(nightEnd) &&
                                s.getEnd().isAfter(nightStart)
                );
    }
}
