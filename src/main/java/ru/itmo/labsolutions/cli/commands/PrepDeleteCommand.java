package ru.itmo.labsolutions.cli.commands;

import ru.itmo.labsolutions.cli.InputHelper;
import ru.itmo.labsolutions.service.PreparationService;

public final class PrepDeleteCommand {
    private final PreparationService service;
    private final InputHelper input;

    public PrepDeleteCommand(PreparationService service, InputHelper input) {
        this.service = service;
        this.input = input;
    }

    public void execute(String args) {
        long id = input.parseId(args);
        if (id <= 0) return;

        String res = service.remove(id);
        System.out.println(res.equals("OK") ? "OK: Удалено" : "Ошибка: " + res);
    }
}
