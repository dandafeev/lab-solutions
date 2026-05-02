package ru.itmo.labsolutions.cli.commands;

import ru.itmo.labsolutions.domain.Solution;
import ru.itmo.labsolutions.service.SolutionService;

import java.util.List;

public final class SolListCommand {
    private final SolutionService service;

    public SolListCommand(SolutionService service) {
        this.service = service;
    }

    public void execute(String args) {
        String query = "";
        if (args.contains("--q")) {
            int idx = args.indexOf("--q");
            query = args.substring(idx + 3).trim();
            if (query.length() > 64) {
                System.out.println("Ошибка: запрос слишком длинный (макс. 64)");
                return;
            }
        }

        List<Solution> list = service.listByNameContains(query);
        if (list.isEmpty()) { System.out.println("Растворы не найдены."); return; }

        System.out.println("ID\tName\tConc\tUnit\tSolvent");
        for (Solution s : list) {
            System.out.printf("%d\t%s\t%s\t%s\t%s%n",
                    s.getId(), s.getName(), s.getConcentration(), s.getConcentrationUnit(),
                    s.getSolvent() != null ? s.getSolvent() : "-");
        }
    }
}