package ru.itmo.labsolutions.cli.commands;

import ru.itmo.labsolutions.cli.InputHelper;
import ru.itmo.labsolutions.domain.Preparation;
import ru.itmo.labsolutions.domain.enums.FinalQuantityUnit;
import ru.itmo.labsolutions.service.PreparationService;

public final class PrepAddCommand {
    private final PreparationService service;
    private final InputHelper input;

    public PrepAddCommand(PreparationService service, InputHelper input) {
        this.service = service;
        this.input = input;
    }

    public void execute(String args) {
        if (args.isEmpty()) {
            System.out.println("Ошибка: укажите ID раствора. Формат: prep_add <id>");
            return;
        }

        long solutionId = input.parseId(args);
        if (solutionId <= 0) return;

        System.out.println("--- Добавление приготовления для раствора #" + solutionId + " ---");
        double finalQty = input.readDouble("Итоговый объем/масса: ");

        FinalQuantityUnit unit = input.parsePrepUnit(
                input.readRequiredString("Единицы (ML, G): ")
        );
        if (unit == null) {
            System.out.println("Ошибка: недопустимые единицы.");
            return;
        }

        String comment = input.readOptionalString("Комментарий (можно пусто): ");

        Preparation prep = new Preparation(solutionId, finalQty, unit, comment);
        String res = service.add(prep);

        System.out.println(res.equals("OK") ? "OK: Приготовление записано, ID: " + prep.getId() : "Ошибка: " + res);
    }
}
