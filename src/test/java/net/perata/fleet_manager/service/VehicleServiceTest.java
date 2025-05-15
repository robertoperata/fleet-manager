package net.perata.fleet_manager.service;

import net.perata.fleet_manager.domain.Vehicle;
import org.instancio.Instancio;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.stream.IntStream;

class VehicleServiceTest {


    /*
    table size 2097152
    Ratio = 1000000/999765 = 1.000235
    Rehash ratio = 1000000/796105 = 1.256116
    addTime = 72ms
    containsTime = 46ms

tableSize = 2097152
Ratio = 1000000/999872 = 1.000128
Rehash ratio = 1000000/795671 = 1.256801
addTime = 109ms
containsTime = 73ms



     */
//    @Test
    void test() throws InterruptedException {

        Collection<Vehicle> vehicles = new ArrayList<>();


        IntStream.range(0, 1_000_000).forEach(i -> vehicles.add(Instancio.create(Vehicle.class)));
        long distinctHashCodes = vehicles.stream()
                .mapToInt(Vehicle::hashCode)
                .distinct()
                .count();
        int tableSize = Integer.highestOneBit((int) (vehicles.size() / 0.75)) << 1;
        System.out.println("tableSize = " + tableSize);
        System.out.printf(Locale.US, "Ratio = %d/%d = %f%n",
                vehicles.size(), distinctHashCodes, (double) vehicles.size() / distinctHashCodes);

        long rehashedHashCodes = vehicles.stream()
                .mapToInt(Vehicle::hashCode)
                .map(VehicleServiceTest::hash)
                .map(hash -> hash & (tableSize - 1))
                .distinct()
                .count();
        System.out.printf(Locale.US, "Rehash ratio = %d/%d = %f%n",
                vehicles.size(), rehashedHashCodes, (double) vehicles.size() / rehashedHashCodes);

        LongAccumulator addTimes = new LongAccumulator(Long::min, Long.MAX_VALUE);
        LongAccumulator containsTimes = new LongAccumulator(Long::min, Long.MAX_VALUE);
        for (int i = 0; i < 3; i++) {
            Set<Vehicle> vehicleSet;
            long addTime = System.nanoTime();
            try {
                vehicleSet = new HashSet<>(vehicles);
            } finally {
                addTime = System.nanoTime() - addTime;
                addTimes.accumulate(addTime / 1_000_000);
                System.out.printf("addTime = %dms%n", (addTime / 1_000_000));
            }
            long containsTime = System.nanoTime();
            try {
                for (Vehicle vehicle : vehicles) {
                    if (!vehicleSet.contains(vehicle))
                        throw new AssertionError("Oh no!!!");
                }
            } finally {
                containsTime = System.nanoTime() - containsTime;
                containsTimes.accumulate(containsTime / 1_000_000);
                System.out.printf("containsTime = %dms%n", (containsTime / 1_000_000));
            }

            vehicleSet = null;

            System.gc();
            Thread.sleep(100);
        }
    }

//    @Test
    void test1() {
//        Map<Integer, String> map = new HashMap<>();
//        Map<Integer, String> map = new ConcurrentHashMap<>();
        Map<Integer, String> map = new ConcurrentSkipListMap<>();
        map.put(1, null);
        System.out.println("key exists: " + map.containsKey(1));
        map.computeIfAbsent(1, k -> "1");
        System.out.println("key exists: " +  map.containsKey(1));
        System.out.println("map " + map.get(1));
        String s = map.computeIfAbsent(1, k -> "1");
        System.out.println("S: " + s);
    }
/*
ratio = 2073600/2073589 = 1.000005
addTime = 76ms
containsTime = 53ms


 */

//    @Test
    void test2() throws InterruptedException {
        Collection<Pixel> pixels = new ArrayList<>();
        for (int x = 0; x < 1920; x++) {
            for (int y = 0; y < 1080; y++) {
                pixels.add(new Pixel(x, y));
            }
        }

        long distinctHashCodes = pixels.stream()
                .mapToInt(Record::hashCode)
                .distinct()
                .count();
        int tableSize = Integer.highestOneBit((int) (pixels.size() / 0.75)) << 1;
        System.out.println("tableSize = " + tableSize);
        System.out.printf(Locale.US, "Rehash ratio = %d/%d = %f%n",
                pixels.size(), distinctHashCodes, (double) pixels.size() / distinctHashCodes);
        long rehashedHashCodes = pixels.stream()
                .mapToInt(Record::hashCode)
                .map(VehicleServiceTest::hash)
                .map(hash -> hash & (tableSize - 1))
                .distinct()
                .count();
        System.out.printf(Locale.US, "Rehash ratio = %d/%d = %f%n",
                pixels.size(), rehashedHashCodes, (double) pixels.size() / rehashedHashCodes);

        LongAccumulator addTimes = new LongAccumulator(Long::min, Long.MAX_VALUE);
        LongAccumulator containsTimes = new LongAccumulator(Long::min, Long.MAX_VALUE);
        for (int i = 0; i < 3; i++) {
            Set<Pixel> pixelSet;
            long addTime = System.nanoTime();
            try {
                // pixelSet = Set.copyOf(pixels); // BE CAREFUL
                pixelSet = new HashSet<>(pixels);
            } finally {
                addTime = System.nanoTime() - addTime;
                addTimes.accumulate(addTime / 1_000_000);
                System.out.printf("addTime = %dms%n", (addTime / 1_000_000));
            }
            long containsTime = System.nanoTime();
            try {
                for (Pixel pixel : pixels) {
                    if (!pixelSet.contains(pixel))
                        throw new AssertionError("Oh no!!!");
                }
            } finally {
                containsTime = System.nanoTime() - containsTime;
                containsTimes.accumulate(containsTime / 1_000_000);
                System.out.printf("containsTime = %dms%n", (containsTime / 1_000_000));
            }
            pixelSet = null;
            System.gc();
            Thread.sleep(100);
        }
    }

    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    record Pixel(int x, int y) implements Comparable<Pixel> {
        @Override
        public int compareTo(Pixel o) {
            int result = Integer.compare(x, o.x);
            if (result != 0) return result;
            return Integer.compare(y, o.y);
        }
        //*/


        @Override
        public int hashCode() {
            // return x * 31 + y;
            return (x + ", " + y).hashCode();
            // return x << 16 ^ y;
            // as distinct as possible
            // as small as possible > 0
//            return x * 1080 + y;
        }
    }
}