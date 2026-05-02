package ru.itmo.labsolutions.cli.commands;

import ru.itmo.labsolutions.cli.InputHelper;
import ru.itmo.labsolutions.domain.Solution;
import ru.itmo.labsolutions.service.PreparationService;
import ru.itmo.labsolutions.service.SolutionService;

import java.util.Optional;

public final class SolShowCommand {
    private final SolutionService solutionService;
    private final PreparationService preparationService;
    private final InputHelper input;

    public SolShowCommand(SolutionService solutionService, PreparationService preparationService, InputHelper input) {
        this.solutionService = solutionService;
        this.preparationService = preparationService;
        this.input = input;
    }

    public void execute(String args) {
        long id = input.parseId(args);
        if (id <= 0) return;

        Optional<Solution> opt = solutionService.getById(id);
        if (opt.isEmpty()) {
            System.out.println("Ошибка: раствор с id=" + id + " не найден");
            return;
        }

        Solution s = opt.get();
        long prepCount = preparationService.listBySolutionId(id).size();

        System.out.println("Solution#" + s.getId());
        System.out.println("name: " + s.getName());
        System.out.println("concentration: " + s.getConcentration() + " " + s.getConcentrationUnit());
        System.out.println("solvent: " + (s.getSolvent() != null ? s.getSolvent() : "-"));
        System.out.println("preparations: " + prepCount);
    }
}