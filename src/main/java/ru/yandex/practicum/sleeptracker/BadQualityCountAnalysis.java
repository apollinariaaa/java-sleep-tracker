package ru.yandex.practicum.sleeptracker;
import java.util.List;

public class BadQualityCountAnalysis
        implements SleepAnalysis {

    @Override
    public SleepAnalysisResult<Long> apply(List<SleepingSession> sessions) {

        long count = sessions.stream()
                .filter(s -> s.getQuality() == SleepQuality.BAD)
                .count();

        return new SleepAnalysisResult<>(
                "Количество сессий с плохим качеством сна",
                count
        );
    }
}
