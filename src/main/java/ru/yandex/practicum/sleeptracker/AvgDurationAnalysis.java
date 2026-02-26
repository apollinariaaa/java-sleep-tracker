package ru.yandex.practicum.sleeptracker;
import java.util.List;
import java.util.function.Function;

public class AvgDurationAnalysis
        implements Function<List<SleepingSession>, SleepAnalysisResult<Double>> {

    @Override
    public SleepAnalysisResult<Double> apply(List<SleepingSession> sessions) {

        double avg = sessions.stream()
                .mapToLong(SleepingSession::getDurationMinutes)
                .average()
                .orElse(0);

        return new SleepAnalysisResult<>(
                "Средняя продолжительность сна (мин)",
                avg
        );
    }
}
