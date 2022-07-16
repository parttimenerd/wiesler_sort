package de.wiesler;

import java.util.Arrays;
import java.util.Random;

public final class BMC {
    public static void testAll() {
        for (int i = 0; i >= 0; i++) {
            Constants.testContracts(i);
            Buffers.testContracts(i);
            SampleParameters.testContracts(i);
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100000; i++) {
            Random random = new Random();
            var arr = random.ints().limit(random.nextInt(1, 100)).toArray();
            var arr2 = arr.clone();
            Sorter.sort(arr);
            Arrays.sort(arr2);
            if (!Arrays.equals(arr, arr2)) {
                System.err.println("err");
            }
        }
    }
}
