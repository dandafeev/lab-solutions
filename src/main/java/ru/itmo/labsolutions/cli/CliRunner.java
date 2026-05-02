package ru.itmo.labsolutions.cli;

import ru.itmo.labsolutions.cli.commands.*;
import ru.itmo.labsolutions.service.PreparationComponentService;
import ru.itmo.labsolutions.service.PreparationService;
import ru.itmo.labsolutions.service.SolutionService;

import java.util.Scanner;

public final class CliRunner {
    private final InputHelper input;

    private final SolCreateCommand cmdSolCreate;
    private final SolListCommand cmdSolList;
    private final SolShowCommand cmdSolShow;
    private final PrepAddCommand cmdPrepAdd;
    private final PrepListComponentCommand cmdPrepList;
    private final PrepShowCommand cmdPrepShow;
    private final CompAddCommand cmdCompAdd;
    private final CompListCommand cmdCompList;
    private final PrepUpdateCommand cmdPrepUpdate;
    private final PrepDeleteCommand cmdPrepDelete;

    public CliRunner(SolutionService solutionService,
                     PreparationService preparationService,
                     PreparationComponentService componentService) {

        this.input = new InputHelper(new Scanner(System.in));

        this.cmdSolCreate = new SolCreateCommand(solutionService, input);
        this.cmdSolList = new SolListCommand(solutionService);
        this.cmdSolShow = new SolShowCommand(solutionService, preparationService, input);

        this.cmdPrepAdd = new PrepAddCommand(preparationService, input);
        this.cmdPrepList = new PrepListComponentCommand(preparationService, input);
        this.cmdPrepShow = new PrepShowCommand(preparationService, componentService, input);

        this.cmdCompAdd = new CompAddCommand(componentService, input);
        this.cmdCompList = new CompListCommand(componentService, input);

        this.cmdPrepUpdate = new PrepUpdateCommand(preparationService, input);
        this.cmdPrepDelete = new PrepDeleteCommand(preparationService, input);
    }

    public void run() {
        System.out.println("ЛР1: Учет растворов и приготовлений");
        printHelp();

        while (true) {
            System.out.print("\n> ");
            String commandLine = input.readOptionalString("").trim();
            if (commandLine.isBlank()) continue;

            String[] parts = commandLine.split("\\s+", 2);
            String command = parts[0].toLowerCase();
            String args = parts.length > 1 ? parts[1] : "";

            try {
                switch (command) {
                    case "help" -> printHelp();
                    case "exit" -> {
                        System.out.println("Завершение работы.");
                        return;
                    }


                    case "sol_create" -> cmdSolCreate.execute();
                    case "sol_list" -> cmdSolList.execute(args);
                    case "sol_show" -> cmdSolShow.execute(args);
                    case "prep_add" -> cmdPrepAdd.execute(args);
                    case "prep_list" -> cmdPrepList.execute(args);
                    case "prep_show" -> cmdPrepShow.execute(args);
                    case "comp_add" -> cmdCompAdd.execute(args);
                    case "comp_list" -> cmdCompList.execute(args);
                    case "prep_update" -> cmdPrepUpdate.execute(args);
                    case "prep_delete" -> cmdPrepDelete.execute(args);

                    default -> System.out.println("Ошибка: неизвестная команда '" + command + "'. Введите 'help'.");
                }
            } catch (Exception e) {
                System.out.println("Ошибка выполнения: " + e.getMessage());
            }
        }
    }

    private void printHelp() {
        System.out.println("\nДоступные команды:");
        System.out.println("  sol_create            — Создать раствор");
        System.out.println("  sol_list [--q TEXT]   — Список растворов");
        System.out.println("  sol_show <id>         — Карточка раствора");
        System.out.println("  prep_add <sol_id>     — Добавить приготовление");
        System.out.println("  prep_list <sol_id>    — Список приготовлений");
        System.out.println("  prep_show <id>        — Карточка приготовления");
        System.out.println("  comp_add <prep_id>    — Добавить компонент");
        System.out.println("  comp_list <prep_id>   — Список компонентов");
        System.out.println("  prep_update <id> f=v  — Обновить приготовление");
        System.out.println("  prep_delete <id>      — Удалить приготовление");
        System.out.println("  help / exit           — Справка / Выход");
    }
}