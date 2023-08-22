package org.example;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static AtomicInteger len3 = new AtomicInteger(0);
    public static AtomicInteger len4 = new AtomicInteger(0);
    public static AtomicInteger len5 = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {

        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }
            Thread thread1 = new Thread(() -> {
                for (int i = 0; i < texts.length; i++) {
                    if (texts[i].equalsIgnoreCase(new StringBuilder(texts[i]).reverse().toString())) {
                        atomicInc(texts[i]);
                    }
                }
            });

        Thread thread2 = new Thread(() -> {
                for (int i = 0; i < texts.length; i++) {
                    Set<String> set = new HashSet<String>(Arrays.asList(texts[i].split("")));
                    if (set.size() == 1) {
                        atomicInc(texts[i]);
                    }
                }
            });

        Thread thread3 = new Thread(() -> {
                for (int i = 0; i < texts.length; i++) {
                    char[] strSort = texts[i].toCharArray();
                    Arrays.sort(strSort);
                    if (texts[i].equalsIgnoreCase(new String(strSort))) {
                        atomicInc(texts[i]);
                    }
                }
            });

        thread1.start();
        thread2.start();
        thread3.start();

        thread3.join();
        thread2.join();
        thread1.join();

        System.out.println("Красивых слов с длиной 3: " + len3 + " шт");
        System.out.println("Красивых слов с длиной 4: " + len4 + " шт");
        System.out.println("Красивых слов с длиной 5: " + len5 + " шт");
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
    
    public static void atomicInc(String text){
        switch (text.length()) {
            case 3:
                len3.getAndIncrement();
            case 4:
                len4.getAndIncrement();
            case 5:
                len5.getAndIncrement();
        }
    }
}