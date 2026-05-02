package ru.itmo.labsolutions.cli.commands;

import ru.itmo.labsolutions.cli.InputHelper;
import ru.itmo.labsolutions.domain.Preparation;
import ru.itmo.labsolutions.domain.enums.FinalQuantityUnit;
import ru.itmo.labsolutions.service.PreparationService;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class PrepUpdateCommand {
    private final PreparationService service;
    private final InputHelper input;

    public PrepUpdateCommand(PreparationService service, InputHelper input) {
        this.service = service;
        this.input = input;
    }

    public void execute(String args) {
        String[] parts = args.trim().split("\\s+", 2);
        if (parts.length < 2 || parts[0].isEmpty()) {
            System.out.println("Ошибка: укажите ID и поля. Пример: prep_update 10 comment=\"test\"");
            return;
        }

        long id = input.parseId(parts[0]);
        if (id <= 0) return;

        Optional<Preparation> opt = service.getById(id);
        if (opt.isEmpty()) {
            System.out.println("Ошибка: preparation с id=" + id + " не найден");
            return;
        }

        Preparation existing = opt.get();
        // Создаем копию для безопасного обновления
        Preparation updated = new Preparation(
                existing.getSolutionId(), existing.getFinalQuantity(),
                existing.getFinalUnit(), existing.getComment()
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
                        FinalQuantityUnit unit = input.parsePrepUnit(value);
                        if (unit == null) { System.out.println("Ошибка: неверные единицы (ML или G)"); return; }
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

        if (!hasChanges) { System.out.println("Ошибка: не указаны поля для обновления."); return; }

        String res = service.update(id, updated);
        System.out.println(res.equals("OK") ? "✅ OK: Обновлено" : "❌ Ошибка: " + res);
    }
}