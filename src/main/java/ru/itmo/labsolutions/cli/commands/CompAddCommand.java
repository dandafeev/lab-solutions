package ru.itmo.labsolutions.cli.commands;

import ru.itmo.labsolutions.cli.InputHelper;
import ru.itmo.labsolutions.domain.PreparationComponent;
import ru.itmo.labsolutions.domain.enums.ComponentUnit;
import ru.itmo.labsolutions.service.PreparationComponentService;

public final class CompAddCommand {
    private final PreparationComponentService service;
    private final InputHelper input;

    public CompAddCommand(PreparationComponentService service, InputHelper input) {
        this.service = service;
        this.input = input;
    }

    public void execute(String args) {
        if (args.isEmpty()) {
            System.out.println("Ошибка: укажите ID приготовления. Формат: comp_add <id>");
            return;
        }

        long prepId = input.parseId(args);
        if (prepId <= 0) return;

        System.out.println("Добавление компонента к приготовлению #" + prepId );
        long batchId = input.readLong("ID партии (batch_id): ");
        double quantity = input.readDouble("Количество: ");

        ComponentUnit unit = input.parseCompUnit(
                input.readRequiredString("Единицы (G, ML): ")
        );
        if (unit == null) {
            System.out.println("Ошибка: недопустимые единицы.");
            return;
        }

        PreparationComponent comp = new PreparationComponent(prepId, batchId, quantity, unit);
        String res = service.add(comp);

        System.out.println(res.equals("OK") ? "OK: Компонент добавлен, ID: " + comp.getId() : "Ошибка: " + res);
    }
}