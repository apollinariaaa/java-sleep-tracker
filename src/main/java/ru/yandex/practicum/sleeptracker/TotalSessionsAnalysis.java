package ru.yandex.practicum.sleeptracker;
import java.util.List;

public class TotalSessionsAnalysis
        implements SleepAnalysis {

    @Override
    public SleepAnalysisResult<Integer> apply(List<SleepingSession> sessions) {
        return new SleepAnalysisResult<>(
                "Общее количество сессий сна",
                sessions.size()
        );
    }
}
