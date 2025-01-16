package ru.job4j.oop;

public class Calculator {

    private static int x = 5;

    public static int sum(int y) {
        return x + y;
    }

    public static int minus(int z) {
        return z - x;
    }

    public int multiply(int a) {
        return x * a;
    }

    public int divide(int b) {
        return x / b;
    }

    public int sumAllOperation(int s) {
        return sum(s) + minus(s) + multiply(s) + divide(s);
    }

    public static void main(String[] args) {
        int result = sum(10);
        System.out.println(result);
        int result1 = minus(15);
        System.out.println(result1);
        Calculator calculator = new Calculator();
        int result2 = calculator.multiply(5);
        System.out.println(result2);
        int result3 = calculator.divide(5);
        System.out.println(result3);
        int result4 = calculator.sumAllOperation(5);
        System.out.println(result4);
    }
}