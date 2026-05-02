package ru.itmo.labsolutions.cli;

import ru.itmo.labsolutions.domain.enums.*;
import java.util.Scanner;

public final class InputHelper {
    private final Scanner scanner;

    public InputHelper(Scanner scanner) {
        this.scanner = scanner;
    }

    public String readRequiredString(String prompt) {
        System.out.print(prompt);
        String line = scanner.nextLine().trim();
        while (line.isBlank()) {
            System.out.println("Ошибка: значение не может быть пустым. Попробуйте снова.");
            System.out.print(prompt);
            line = scanner.nextLine().trim();
        }
        return line;
    }

    public String readOptionalString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public double readDouble(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextDouble()) {
            System.out.println("Ошибка: введите корректное число.");
            scanner.next();
            System.out.print(prompt);
        }
        double val = scanner.nextDouble();
        scanner.nextLine();
        return val;
    }

    public long readLong(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextLong()) {
            System.out.println("Ошибка: введите корректное целое число (ID).");
            scanner.next();
            System.out.print(prompt);
        }
        long val = scanner.nextLong();
        scanner.nextLine();
        return val;
    }

    public long parseId(String args) {
        if (args == null || args.trim().isEmpty()) {
            System.out.println("Ошибка: укажите ID.");
            return -1;
        }
        try {
            return Long.parseLong(args.trim());
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: ID должен быть числом.");
            return -1;
        }
    }

    public SolutionConcentrationUnit parseSolutionUnit(String s) {
        try { return SolutionConcentrationUnit.valueOf(s.toUpperCase()); }
        catch (IllegalArgumentException e) { return null; }
    }

    public FinalQuantityUnit parsePrepUnit(String s) {
        try { return FinalQuantityUnit.valueOf(s.toUpperCase()); }
        catch (IllegalArgumentException e) { return null; }
    }

    public ComponentUnit parseCompUnit(String s) {
        try { return ComponentUnit.valueOf(s.toUpperCase()); }
        catch (IllegalArgumentException e) { return null; }
    }
}