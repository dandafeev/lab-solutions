package ru.itmo.labsolutions.cli.commands;

import ru.itmo.labsolutions.cli.InputHelper;
import ru.itmo.labsolutions.domain.Preparation;
import ru.itmo.labsolutions.service.PreparationService;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public final class PrepListComponentCommand {
    private final PreparationService service;
    private final InputHelper input;
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public PrepListComponentCommand(PreparationService service, InputHelper input) {
        this.service = service;
        this.input = input;
    }

    public void execute(String args) {
        String[] parts = args.trim().split("\\s+");
        if (parts.length == 0 || parts[0].isEmpty()) {
            System.out.println("Ошибка: укажите ID раствора. Формат: prep_list <id> [--last N]");
            return;
        }

        long solId = input.parseId(parts[0]);
        if (solId <= 0) return;

        int limit = 0;
        if (args.contains("--last")) {
            int idx = args.indexOf("--last");
            String lastPart = args.substring(idx + 6).trim();
            if (!lastPart.isEmpty()) {
                try { limit = Integer.parseInt(lastPart); } catch (NumberFormatException ignored) {}
            }
        }

        List<Preparation> list = service.listBySolutionId(solId);
        if (list.isEmpty()) { System.out.println("Приготовления не найдены"); return; }

        if (limit > 0 && list.size() > limit) {
            list = list.subList(list.size() - limit, list.size());
        }

        System.out.println("ID\tFinalQty\tUnit\tTime\tComment");
        for (Preparation p : list) {
            String time = p.getPreparedAt() != null ? DATE_FMT.format(p.getPreparedAt().atZone(ZoneId.systemDefault())) : "-";
            System.out.printf("%d\t%s\t%s\t%s\t%s%n",
                    p.getId(), p.getFinalQuantity(), p.getFinalUnit(), time,
                    p.getComment() != null ? p.getComment() : "-");
        }
    }
}
