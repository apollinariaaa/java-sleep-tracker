package ru.yandex.practicum.sleeptracker;
import java.util.List;
import java.util.function.Function;

public class MaxDurationAnalysis
        implements SleepAnalysis {

    @Override
    public SleepAnalysisResult<Long> apply(List<SleepingSession> sessions) {

        long max = sessions.stream()
                .mapToLong(SleepingSession::getDurationMinutes)
                .max()
                .orElse(0);

        return new SleepAnalysisResult<>(
                "Максимальная продолжительность сна (мин)",
                max
        );
    }
}
