package ru.itmo.labsolutions.cli.commands;

import ru.itmo.labsolutions.cli.InputHelper;
import ru.itmo.labsolutions.domain.Preparation;
import ru.itmo.labsolutions.service.PreparationComponentService;
import ru.itmo.labsolutions.service.PreparationService;

import java.util.Optional;

public final class PrepShowCommand {
    private final PreparationService preparationService;
    private final PreparationComponentService componentService;
    private final InputHelper input;

    public PrepShowCommand(PreparationService preparationService,
                           PreparationComponentService componentService,
                           InputHelper input) {
        this.preparationService = preparationService;
        this.componentService = componentService;
        this.input = input;
    }

    public void execute(String args) {
        long id = input.parseId(args);
        if (id <= 0) return;

        Optional<Preparation> opt = preparationService.getById(id);
        if (opt.isEmpty()) {
            System.out.println("Ошибка: preparation с id=" + id + " не найден");
            return;
        }

        Preparation p = opt.get();
        long compCount = componentService.listByPreparationId(id).size();

        System.out.println("Preparation#" + p.getId());
        System.out.println("solution_id: " + p.getSolutionId());
        System.out.println("finalQty: " + p.getFinalQuantity() + " " + p.getFinalUnit());
        System.out.println("comment: " + (p.getComment() != null ? p.getComment() : "-"));
        System.out.println("components: " + compCount);
    }
}
