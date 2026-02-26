package ru.yandex.practicum.sleeptracker;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChronotypeAnalysis implements SleepAnalysis {

    @Override
    public SleepAnalysisResult<Chronotype> apply(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) return new SleepAnalysisResult<>("Хронотип", Chronotype.PIGEON);

        // фильтруем дневные сессии (спим с 6 до 0 утра считается ночью)
        List<SleepingSession> nightSessions = sessions.stream()
                .filter(this::isNightSleep)
                .collect(Collectors.toList());

        Map<Chronotype, Long> counts = nightSessions.stream()
                .map(this::classify)
                .collect(Collectors.groupingBy(c -> c, Collectors.counting()));

        long owls = counts.getOrDefault(Chronotype.OWL, 0L);
        long larks = counts.getOrDefault(Chronotype.LARK, 0L);
        long pigeons = counts.getOrDefault(Chronotype.PIGEON, 0L);

        Chronotype result =
                (owls > larks && owls > pigeons) ? Chronotype.OWL :
                        (larks > owls && larks > pigeons) ? Chronotype.LARK :
                                Chronotype.PIGEON;

        return new SleepAnalysisResult<>("Хронотип", result);
    }

    private boolean isNightSleep(SleepingSession s) {
        LocalTime start = s.getStart().toLocalTime();
        LocalTime end = s.getEnd().toLocalTime();

        // ночь — пересечение хотя бы с 0:00-6:00
        return start.isBefore(LocalTime.of(6, 0)) || end.isAfter(LocalTime.of(0, 0));
    }

    private Chronotype classify(SleepingSession s) {
        LocalTime start = s.getStart().toLocalTime();
        LocalTime end = s.getEnd().toLocalTime();

        if (start.isAfter(LocalTime.of(23,0)) && end.isAfter(LocalTime.of(9,0))) {
            return Chronotype.OWL;
        }

        if (start.isBefore(LocalTime.of(22,0)) && end.isBefore(LocalTime.of(7,0))) {
            return Chronotype.LARK;
        }

        return Chronotype.PIGEON;
    }
}