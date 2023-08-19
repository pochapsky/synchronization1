package org.example;

import java.util.*;
import java.util.Random;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {

        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            Runnable logic = () -> {
                int robotCommandCounter;
                robotCommandCounter = counterOfRobotCommand(generateRoute("RLRFR", 100), "R");
                synchronized (sizeToFreq) {
                    if (sizeToFreq.containsKey(robotCommandCounter)) {
                        sizeToFreq.replace(robotCommandCounter, sizeToFreq.get(robotCommandCounter) + 1);
                    } else {
                        sizeToFreq.put(robotCommandCounter, 1);
                    }
                }
            };
            Thread thread = new Thread(logic);
            thread.start();
            threads.add(thread);
        }

        for (Thread thread : threads) {
            thread.join();
        }
        Map.Entry<Integer, Integer> maxEntry = maximumNumberOfEntriesInMap(sizeToFreq);
        System.out.printf("Самое частое количество повторений %d (встретилось %d раз)", maxEntry.getKey(), maxEntry.getValue());
        System.out.println("\n Другие размеры:");
        for (Map.Entry<Integer, Integer> mapCommandCount : sizeToFreq.entrySet()) {

            if (maxEntry.getKey() != mapCommandCount.getKey()) {
                System.out.println(" - " + mapCommandCount.getKey() + " " + "(" + mapCommandCount.getValue() +
                        " раз)");

            }
        }
    }


    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    public static int counterOfRobotCommand(String text, String sub) {
        int count = 0;
        int pos = 0;
        while ((pos = text.indexOf(sub, pos)) != -1) {
            count++;
            pos += sub.length();
        }
        return count;
    }

    public static Map.Entry<Integer, Integer> maximumNumberOfEntriesInMap(Map<Integer, Integer> sizeToFreq) {
        Map.Entry<Integer, Integer> maxEntry = sizeToFreq.entrySet().stream()
                .max(Comparator.comparing(Map.Entry::getValue))
                .orElse(null);
        return maxEntry;
    }
}