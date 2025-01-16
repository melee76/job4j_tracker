package ru.job4j.collection;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class FullSearchTest {

    @Test
    public void extractNumber() {
        List<Task1> tasks = Arrays.asList(
                new Task1("1", "First desc"),
                new Task1("2", "Second desc"),
                new Task1("1", "First desc")
        );
        Set<String> expected = new HashSet<>(Arrays.asList("1", "2"));
        assertThat(FullSearch.extractNumber(tasks)).containsAll(expected);
    }
}