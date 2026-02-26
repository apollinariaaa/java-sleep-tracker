package ru.yandex.practicum.sleeptracker;
import java.util.List;
import java.util.function.Function;

public class MinDurationAnalysis
        implements SleepAnalysis {

    @Override
    public SleepAnalysisResult<Long> apply(List<SleepingSession> sessions) {

        long min = sessions.stream()
                .mapToLong(SleepingSession::getDurationMinutes)
                .min()
                .orElse(0);

        return new SleepAnalysisResult<>(
                "Минимальная продолжительность сна (мин)",
                min
        );
    }
}