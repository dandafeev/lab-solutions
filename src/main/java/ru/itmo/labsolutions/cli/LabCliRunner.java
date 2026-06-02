package ru.itmo.labsolutions.cli;

import ru.itmo.labsolutions.commands.*;
import ru.itmo.labsolutions.service.ComponentService;
import ru.itmo.labsolutions.service.PreparationService;
import ru.itmo.labsolutions.service.SolutionService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LabCliRunner {

    public static void main(String[] args) {
        System.out.println("Введите 'help' для списка команд");

        SolutionService solutionService = new SolutionService();
        PreparationService preparationService = new PreparationService();
        ComponentService componentService = new ComponentService();

        Scanner scanner = new Scanner(System.in);

        List<Command> commands = new ArrayList<>();
        commands.add(new SolCreateCommand(solutionService, scanner));
        commands.add(new SolListCommand(solutionService));
        commands.add(new SolShowCommand(solutionService, preparationService));
        commands.add(new SolUpdateCommand(solutionService));
        commands.add(new SolDeleteCommand(solutionService));

        commands.add(new PrepAddCommand(preparationService, scanner));
        commands.add(new PrepListCommand(preparationService));
        commands.add(new PrepShowCommand(preparationService, componentService));
        commands.add(new PrepUpdateCommand(preparationService));
        commands.add(new PrepDeleteCommand(preparationService));

        commands.add(new CompAddCommand(componentService, scanner));
        commands.add(new CompListCommand(componentService, preparationService));
        commands.add(new CompShowCommand(componentService, preparationService));
        commands.add(new CompDeleteCommand(componentService));


        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) continue;

            String[] parts = input.split("\\s+");
            String commandName = parts[0].toLowerCase();

            try {
                if ("exit".equals(commandName)) {
                    System.out.println("Завершение работы.");
                    return;
                } else if ("help".equals(commandName)) {
                    printHelp();
                    continue;
                }

                // Поиск команды в списке
                Command found = null;
                for (Command cmd : commands) {
                    if (cmd.getName().equals(commandName)) {
                        found = cmd;
                        break;
                    }
                }

                if (found != null) {
                    String result = found.execute(parts);
                    if (!result.isEmpty()) {
                        System.out.println(result);
                    }
                } else {
                    System.out.println("Неизвестная команда. Введите help");
                }

            } catch (NumberFormatException e) {
                System.out.println("Ошибка: id должен быть числом");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }

    private static void printHelp() {
        System.out.println("Доступные команды:");
        System.out.println("  help - показать список команд");
        System.out.println("  exit - выйти из программы");
        System.out.println("  sol_create - добавить новый раствор");
        System.out.println("  sol_list - показать список растворов");
        System.out.println("  sol_show <id> - показать подробности раствора");
        System.out.println("  sol_update <id> field=value... - обновить поля раствора");
        System.out.println("  sol_delete <id> - удалить раствор");
        System.out.println("  prep_add <solution_id> - добавить приготовление");
        System.out.println("  prep_list <sol_id> [--last N] - показать приготовления");
        System.out.println("  prep_show <id> - показать подробности приготовления");
        System.out.println("  prep_update <id> field=value... - обновить приготовление");
        System.out.println("  prep_delete <id> - удалить приготовление");
        System.out.println("  comp_add <preparation_id> - добавить компонент");
        System.out.println("  comp_list <prep_id> - показать компоненты");
        System.out.println("  comp_show <id> - показать компонент");
        System.out.println("  comp_delete <id> - удалить компонент");
    }
}