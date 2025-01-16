package ru.job4j.collection;

import java.util.HashSet;
import java.util.List;

public class FullSearch {
    public static HashSet<String> extractNumber(List<Task1> list) {
        HashSet<String> numbers = new HashSet<>();
        for (Task1 task : list); {
            numbers.add(task.getNumber());
        }
        return numbers;
    }
}
