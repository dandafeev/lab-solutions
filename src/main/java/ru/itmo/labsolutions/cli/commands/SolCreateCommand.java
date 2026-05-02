package ru.itmo.labsolutions.cli.commands;

import ru.itmo.labsolutions.cli.InputHelper;
import ru.itmo.labsolutions.domain.Solution;
import ru.itmo.labsolutions.domain.enums.SolutionConcentrationUnit;
import ru.itmo.labsolutions.service.SolutionService;

public final class SolCreateCommand {
    private final SolutionService service;
    private final InputHelper input;

    public SolCreateCommand(SolutionService service, InputHelper input) {
        this.service = service;
        this.input = input;
    }

    public void execute() {
        System.out.println("Создание раствора");
        String name = input.readRequiredString("Название: ");
        double concentration = input.readDouble("Концентрация: ");

        SolutionConcentrationUnit unit = input.parseSolutionUnit(
                input.readRequiredString("Единицы (PERCENT, MOL_PER_L, G_PER_L): ")
        );
        if (unit == null) {
            System.out.println("Ошибка: недопустимые единицы концентрации.");
            return;
        }

        String solvent = input.readOptionalString("Растворитель (можно пусто): ");

        Solution solution = new Solution(name, concentration, unit, solvent);
        String res = service.add(solution);

        if (res.equals("OK")) {
            System.out.println("OK: Раствор создан, ID: " + solution.getId());
        } else {
            System.out.println("Ошибка: " + res);
        }
    }
}
