package ru.yandex.practicum.sleeptracker;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SleepTrackerApp {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    private final List<Function<List<SleepingSession>, ? extends SleepAnalysisResult<?>>> analyses =
            List.of(
                    new TotalSessionsAnalysis(),
                    new MinDurationAnalysis(),
                    new MaxDurationAnalysis(),
                    new AvgDurationAnalysis(),
                    new BadQualityCountAnalysis(),
                    new SleeplessNightsAnalysis(),
                    new ChronotypeAnalysis()
            );

    public static void main(String[] args) throws IOException {

        if (args.length == 0) {
            System.out.println("Укажите путь к файлу.");
            return;
        }

        List<SleepingSession> sessions = Files.lines(Path.of(args[0]))
                .filter(line -> !line.isBlank())
                .map(SleepTrackerApp::parseLine)
                .collect(Collectors.toList());

        SleepTrackerApp app = new SleepTrackerApp();

        app.analyses.stream()
                .map(a -> a.apply(sessions))
                .forEach(result ->
                        System.out.println(result.getDescription() + ": " + result.getValue()));
    }

    private static SleepingSession parseLine(String line) {
        String[] parts = line.split(";");
        LocalDateTime start = LocalDateTime.parse(parts[0], FORMATTER);
        LocalDateTime end = LocalDateTime.parse(parts[1], FORMATTER);
        SleepQuality quality = SleepQuality.valueOf(parts[2].trim());
        return new SleepingSession(start, end, quality);
    }
}