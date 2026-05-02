package ru.itmo.labsolutions.cli;

import ru.itmo.labsolutions.service.PreparationComponentService;
import ru.itmo.labsolutions.service.PreparationService;
import ru.itmo.labsolutions.service.SolutionService;

public class Main {
    public static void main(String[] args) {
        SolutionService solutionService = new SolutionService();
        PreparationService preparationService = new PreparationService(solutionService);
        PreparationComponentService componentService = new PreparationComponentService(preparationService);

        new CliRunner(solutionService, preparationService, componentService).run();
    }
}
