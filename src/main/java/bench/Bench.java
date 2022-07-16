package bench;

import de.wiesler.Sorter;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.profile.Profiler;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.ProfilerConfig;
import org.openjdk.jmh.runner.options.TimeValue;

import java.awt.*;
import java.util.*;

public class Bench {

    @State(Scope.Benchmark)
    public static class ExecutionPlan {

        @Param({ "50", "100", "500", "1000", "5000", "10000", "50000", "100000", "500000", "1000000", "10000000", "100000000" })
        public int length;

        @Param({"random", "sorted", "reversed"})
        public String mode;

        @Param({"10000", "" + Integer.MAX_VALUE})
        public int maxNumber;
        int[] arr;

        @Setup(Level.Invocation)
        public void setUp() {
            arr = new Random().ints(length, 0, maxNumber).toArray();
            switch (mode) {
                case "random":
                    break;
                case "sorted":
                    Arrays.parallelSort(arr);
                    break;
                case "reversed":
                    arr = Arrays.stream(arr).boxed().sorted(Comparator.reverseOrder()).mapToInt(i -> i).toArray();
                    break;
            }
            arr = new Random().ints().limit(length).toArray();
        }

        @TearDown(Level.Invocation)
        public void teardown() {
            for (int i = 1; i < arr.length; i++) {
                if (arr[i] < arr[i - 1]) {
                    System.err.println("Array is not sorted");
                    return;
                }
            }
        }
    }

    @Benchmark
    public static void runWiesler(ExecutionPlan plan) {
        Sorter.sort(plan.arr);
    }

    @Benchmark
    public static void runJDK(ExecutionPlan plan) {
        Arrays.sort(plan.arr);
    }

    public static void main(String[] args) throws Exception {
        Options opts = new OptionsBuilder()
                .include(".*.Bench.*")
                .warmupIterations(2)
                .measurementIterations(5)
                .measurementTime(TimeValue.seconds(1))
                .forks(2)
                .result("results.csv")
                .result("results.json")
                .resultFormat(ResultFormatType.CSV)
                //.addProfiler(GcProfiler.class)
                .build();
        new Runner(opts).run();
    }
}
