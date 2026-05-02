package ru.itmo.labsolutions.cli;

import ru.itmo.labsolutions.domain.*;
import ru.itmo.labsolutions.domain.enums.*;
import ru.itmo.labsolutions.service.PreparationComponentService;
import ru.itmo.labsolutions.service.PreparationService;
import ru.itmo.labsolutions.service.SolutionService;

import java.util.Scanner;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CliRunner {
    private final SolutionService solutionService;
    private final PreparationService preparationService;
    private final PreparationComponentService componentService;
    private final InputHelper input;
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public CliRunner(SolutionService solutionService,
                     PreparationService preparationService,
                     PreparationComponentService componentService) {
        this.solutionService = solutionService;
        this.preparationService = preparationService;
        this.componentService = componentService;
        this.input = new InputHelper(new Scanner(System.in));
    }

    public void run() {
        System.out.println("=== ЛР1: Учет растворов и приготовлений ===");
        printHelp();

        while (true) {
            System.out.print("\n> ");
            String commandLine = input.readOptionalString("").trim();
            if (commandLine.isBlank()) continue;

            String[] parts = commandLine.split("\\s+", 2);
            String command = parts[0].toLowerCase();
            String args = parts.length > 1 ? parts[1] : "";

            try {
                switch (command) {
                    case "help" -> printHelp();
                    case "exit" -> {
                        System.out.println("Завершение работы.");
                        return;
                    }

                    case "sol_create" -> handleSolCreate();
                    case "prep_add" -> handlePrepAdd(args);
                    case "comp_add" -> handleCompAdd(args);

                    case "sol_list" -> handleSolList(args);
                    case "sol_show" -> handleSolShow(args);
                    case "prep_list" -> handlePrepList(args);
                    case "prep_show" -> handlePrepShow(args);
                    case "comp_list" -> handleCompList(args);

                    case "prep_update" -> handlePrepUpdate(args);
                    case "prep_delete" -> handlePrepDelete(args);

                    default -> System.out.println("Ошибка: неизвестная команда '" + command + "'. Введите 'help'.");
                }
            } catch (Exception e) {
                System.out.println("Ошибка выполнения: " + e.getMessage());
            }
        }
    }

    private void printHelp() {
        System.out.println("\n📜 Доступные команды:");
        System.out.println("  sol_create            — Создать раствор");
        System.out.println("  sol_list [--q TEXT]   — Список растворов (поиск)");
        System.out.println("  sol_show <id>         — Карточка раствора");
        System.out.println("  prep_add <sol_id>     — Добавить приготовление");
        System.out.println("  prep_list <sol_id> [--last N] — Список приготовлений");
        System.out.println("  prep_show <id>        — Карточка приготовления");
        System.out.println("  comp_add <prep_id>    — Добавить компонент");
        System.out.println("  comp_list <prep_id>   — Список компонентов");
        System.out.println("  prep_update <id> field=value... — Обновить приготовление");
        System.out.println("  prep_delete <id>      — Удалить приготовление");
        System.out.println("  help / exit           — Справка / Выход");
    }


    private void handleSolCreate() {
        System.out.println("--- Создание раствора ---");
        String name = input.readRequiredString("Название: ");
        double concentration = input.readDouble("Концентрация: ");
        SolutionConcentrationUnit unit = parseSolutionUnit(input.readRequiredString("Единицы (PERCENT, MOL_PER_L, G_PER_L): "));
        if (unit == null) { System.out.println("Ошибка: недопустимые единицы концентрации."); return; }
        String solvent = input.readOptionalString("Растворитель (можно пусто): ");
        Solution solution = new Solution(name, concentration, unit, solvent);
        String res = solutionService.add(solution);
        System.out.println(res.equals("OK") ? "✅ OK: Раствор создан, ID: " + solution.getId() : "❌ Ошибка: " + res);
    }

    private void handlePrepAdd(String args) {
        if (args.isEmpty()) { System.out.println("Ошибка: укажите ID раствора. Формат: prep_add <id>"); return; }
        long solutionId = parseId(args); if (solutionId <= 0) return;
        System.out.println("--- Добавление приготовления для раствора #" + solutionId + " ---");
        double finalQty = input.readDouble("Итоговый объем/масса: ");
        FinalQuantityUnit unit = parsePrepUnit(input.readRequiredString("Единицы (ML, G): "));
        if (unit == null) { System.out.println("Ошибка: недопустимые единицы."); return; }
        String comment = input.readOptionalString("Комментарий (можно пусто): ");
        Preparation prep = new Preparation(solutionId, finalQty, unit, comment);
        String res = preparationService.add(prep);
        System.out.println(res.equals("OK") ? "✅ OK: Приготовление записано, ID: " + prep.getId() : "❌ Ошибка: " + res);
    }

    private void handleCompAdd(String args) {
        if (args.isEmpty()) { System.out.println("Ошибка: укажите ID приготовления. Формат: comp_add <id>"); return; }
        long prepId = parseId(args); if (prepId <= 0) return;
        System.out.println("--- Добавление компонента к приготовлению #" + prepId + " ---");
        long batchId = input.readLong("ID партии (batch_id): ");
        double quantity = input.readDouble("Количество: ");
        ComponentUnit unit = parseCompUnit(input.readRequiredString("Единицы (G, ML): "));
        if (unit == null) { System.out.println("Ошибка: недопустимые единицы."); return; }
        PreparationComponent comp = new PreparationComponent(prepId, batchId, quantity, unit);
        String res = componentService.add(comp);
        System.out.println(res.equals("OK") ? "✅ OK: Компонент добавлен, ID: " + comp.getId() : "❌ Ошибка: " + res);
    }


    private void handleSolList(String args) {
        String query = "";
        if (args.contains("--q")) {
            int idx = args.indexOf("--q");
            query = args.substring(idx + 3).trim();
            if (query.length() > 64) { System.out.println("Ошибка: запрос слишком длинный (макс. 64)"); return; }
        }
        var list = solutionService.listByNameContains(query);
        if (list.isEmpty()) { System.out.println("Растворы не найдены."); return; }
        System.out.println("ID\tName\tConc\tUnit\tSolvent");
        for (Solution s : list) {
            System.out.printf("%d\t%s\t%s\t%s\t%s%n", s.getId(), s.getName(), s.getConcentration(), s.getConcentrationUnit(), s.getSolvent() != null ? s.getSolvent() : "-");
        }
    }

    private void handleSolShow(String args) {
        long id = parseId(args); if (id <= 0) return;
        var opt = solutionService.getById(id);
        if (opt.isEmpty()) { System.out.println("Ошибка: раствор с id=" + id + " не найден"); return; }
        Solution s = opt.get();
        long prepCount = preparationService.listBySolutionId(id).size();
        System.out.println("Solution#" + s.getId());
        System.out.println("name: " + s.getName());
        System.out.println("concentration: " + s.getConcentration() + " " + s.getConcentrationUnit());
        System.out.println("solvent: " + (s.getSolvent() != null ? s.getSolvent() : "-"));
        System.out.println("preparations: " + prepCount);
    }

    private void handlePrepList(String args) {
        String[] parts = args.trim().split("\\s+");
        if (parts.length == 0 || parts[0].isEmpty()) { System.out.println("Ошибка: укажите ID раствора. Формат: prep_list <id> [--last N]"); return; }
        long solId = parseId(parts[0]); if (solId <= 0) return;

        int limit = 0;
        if (args.contains("--last")) {
            int idx = args.indexOf("--last");
            String lastPart = args.substring(idx + 6).trim();
            if (!lastPart.isEmpty()) try { limit = Integer.parseInt(lastPart); } catch (NumberFormatException ignored) {}
        }

        var list = preparationService.listBySolutionId(solId);
        if (list.isEmpty()) { System.out.println("Приготовления не найдены."); return; }

        if (limit > 0 && list.size() > limit) {
            list = list.subList(list.size() - limit, list.size());
        }

        System.out.println("ID\tFinalQty\tUnit\tTime\tComment");
        for (Preparation p : list) {
            String time = p.getPreparedAt() != null ? DATE_FMT.format(p.getPreparedAt().atZone(ZoneId.systemDefault())) : "-";
            System.out.printf("%d\t%s\t%s\t%s\t%s%n", p.getId(), p.getFinalQuantity(), p.getFinalUnit(), time, p.getComment() != null ? p.getComment() : "-");
        }
    }

    private void handlePrepShow(String args) {
        long id = parseId(args); if (id <= 0) return;
        var opt = preparationService.getById(id);
        if (opt.isEmpty()) { System.out.println("Ошибка: preparation с id=" + id + " не найден"); return; }
        Preparation p = opt.get();
        long compCount = componentService.listByPreparationId(id).size();
        System.out.println("Preparation#" + p.getId());
        System.out.println("solution_id: " + p.getSolutionId());
        System.out.println("finalQty: " + p.getFinalQuantity() + " " + p.getFinalUnit());
        System.out.println("comment: " + (p.getComment() != null ? p.getComment() : "-"));
        System.out.println("components: " + compCount);
    }

    private void handleCompList(String args) {
        long prepId = parseId(args); if (prepId <= 0) return;
        var list = componentService.listByPreparationId(prepId);
        if (list.isEmpty()) { System.out.println("Компоненты не найдены."); return; }
        System.out.println("ID\tBatch\tQty\tUnit");
        for (PreparationComponent c : list) {
            System.out.printf("%d\t%d\t%s\t%s%n", c.getId(), c.getBatchId(), c.getQuantity(), c.getUnit());
        }
    }

    // === МОДИФИКАЦИЯ ===
    private void handlePrepUpdate(String args) {
        String[] parts = args.trim().split("\\s+", 2);
        if (parts.length < 2 || parts[0].isEmpty()) {
            System.out.println("Ошибка: укажите ID и поля для обновления. Пример: prep_update 10 comment=\"test\"");
            return;
        }

        long id;
        try {
            id = Long.parseLong(parts[0]);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: ID должен быть числом.");
            return;
        }

        var opt = preparationService.getById(id);
        if (opt.isEmpty()) {
            System.out.println("Ошибка: preparation с id=" + id + " не найден");
            return;
        }

        Preparation existing = opt.get();
        Preparation updated = new Preparation(
                existing.getSolutionId(),
                existing.getFinalQuantity(),
                existing.getFinalUnit(),
                existing.getComment()
        );
        updated.setId(id);
        updated.setPreparedAt(existing.getPreparedAt());
        updated.setCreatedAt(existing.getCreatedAt());
        updated.setOwnerUsername(existing.getOwnerUsername());

        String updates = parts[1];
        Matcher m = Pattern.compile("(\\w+)=(\"[^\"]*\"|\\S+)").matcher(updates);

        boolean hasChanges = false;
        while (m.find()) {
            String key = m.group(1).toLowerCase();
            String value = m.group(2).replaceAll("^\"|\"$", "");

            try {
                switch (key) {
                    case "finalquantity" -> {
                        updated.setFinalQuantity(Double.parseDouble(value));
                        hasChanges = true;
                    }
                    case "finalunit" -> {
                        FinalQuantityUnit unit = parsePrepUnit(value);
                        if (unit == null) {
                            System.out.println("Ошибка: неверные единицы (ML или G)");
                            return;
                        }
                        updated.setFinalUnit(unit);
                        hasChanges = true;
                    }
                    case "comment" -> {
                        updated.setComment(value);
                        hasChanges = true;
                    }
                    default -> {
                        System.out.println("Ошибка: поле '" + key + "' нельзя изменять.");
                        return;
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: значение для '" + key + "' должно быть числом.");
                return;
            }
        }

        if (!hasChanges) {
            System.out.println("Ошибка: не указаны поля для обновления.");
            return;
        }

        String res = preparationService.update(id, updated);
        if (res.equals("OK")) {
            System.out.println("✅ OK: Обновлено");
        } else {
            System.out.println("❌ Ошибка: " + res);
        }
    }

    private void handlePrepDelete(String args) {
        long id = parseId(args);
        if (id <= 0) return;

        String res = preparationService.remove(id);
        if (res.equals("OK")) {
            System.out.println("✅ OK: Удалено");
        } else {
            System.out.println("❌ Ошибка: " + res);
        }
    }


    private long parseId(String args) {
        if (args == null || args.trim().isEmpty()) { System.out.println("Ошибка: укажите ID."); return -1; }
        try { return Long.parseLong(args.trim()); }
        catch (NumberFormatException e) { System.out.println("Ошибка: ID должен быть числом."); return -1; }
    }

    private SolutionConcentrationUnit parseSolutionUnit(String s) {
        try { return SolutionConcentrationUnit.valueOf(s.toUpperCase()); }
        catch (IllegalArgumentException e) { return null; }
    }

    private FinalQuantityUnit parsePrepUnit(String s) {
        try { return FinalQuantityUnit.valueOf(s.toUpperCase()); }
        catch (IllegalArgumentException e) { return null; }
    }

    private ComponentUnit parseCompUnit(String s) {
        try { return ComponentUnit.valueOf(s.toUpperCase()); }
        catch (IllegalArgumentException e) { return null; }
    }
}