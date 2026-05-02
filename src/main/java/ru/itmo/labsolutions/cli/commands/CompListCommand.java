package ru.itmo.labsolutions.cli.commands;

import ru.itmo.labsolutions.cli.InputHelper;
import ru.itmo.labsolutions.domain.PreparationComponent;
import ru.itmo.labsolutions.service.PreparationComponentService;

import java.util.List;

public final class CompListCommand {
    private final PreparationComponentService service;
    private final InputHelper input;

    public CompListCommand(PreparationComponentService service, InputHelper input) {
        this.service = service;
        this.input = input;
    }

    public void execute(String args) {
        long prepId = input.parseId(args);
        if (prepId <= 0) return;

        List<PreparationComponent> list = service.listByPreparationId(prepId);
        if (list.isEmpty()) { System.out.println("Компоненты не найдены."); return; }

        System.out.println("ID\tBatch\tQty\tUnit");
        for (PreparationComponent c : list) {
            System.out.printf("%d\t%d\t%s\t%s%n", c.getId(), c.getBatchId(), c.getQuantity(), c.getUnit());
        }
    }
}