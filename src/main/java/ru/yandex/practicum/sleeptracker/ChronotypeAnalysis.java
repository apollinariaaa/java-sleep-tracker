package ru.yandex.practicum.sleeptracker;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ChronotypeAnalysis
        implements Function<List<SleepingSession>, SleepAnalysisResult<Chronotype>> {

    @Override
    public SleepAnalysisResult<Chronotype> apply(List<SleepingSession> sessions) {

        Map<Chronotype, Long> stats = sessions.stream()
                .filter(this::isNightSleep)
                .collect(Collectors.groupingBy(
                        this::classify,
                        Collectors.counting()
                ));

        long owls = stats.getOrDefault(Chronotype.OWL, 0L);
        long larks = stats.getOrDefault(Chronotype.LARK, 0L);
        long pigeons = stats.getOrDefault(Chronotype.PIGEON, 0L);

        Chronotype result =
                (owls > larks && owls > pigeons) ? Chronotype.OWL :
                        (larks > owls && larks > pigeons) ? Chronotype.LARK :
                                Chronotype.PIGEON;

        return new SleepAnalysisResult<>(
                "Определённый хронотип пользователя",
                result
        );
    }

    private boolean isNightSleep(SleepingSession s) {
        return s.getStart().toLocalTime().isBefore(LocalTime.NOON);
    }

    private Chronotype classify(SleepingSession s) {

        LocalTime start = s.getStart().toLocalTime();
        LocalTime end = s.getEnd().toLocalTime();

        if (start.isAfter(LocalTime.of(23,0)) &&
                end.isAfter(LocalTime.of(9,0))) {
            return Chronotype.OWL;
        }

        if (start.isBefore(LocalTime.of(22,0)) &&
                end.isBefore(LocalTime.of(7,0))) {
            return Chronotype.LARK;
        }

        return Chronotype.PIGEON;
    }
}
