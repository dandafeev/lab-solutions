package ru.itmo.labsolutions.service;

import ru.itmo.labsolutions.domain.Preparation;
import ru.itmo.labsolutions.validation.PreparationValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class PreparationService {

    private final List<Preparation> preparations = new ArrayList<>();
    private long nextId = 1;
    private final SolutionService solutionService;

    public PreparationService(SolutionService solutionService) {
        this.solutionService = solutionService;
    }

    public String add(Preparation preparation) {
        if (preparation == null) return "Ошибка: объект приготовления null";

        String error = PreparationValidator.validate(preparation);
        if (error != null) return error;

        if (solutionService.getById(preparation.getSolutionId()).isEmpty()) {
            return "Ошибка: раствор с solutionId=" + preparation.getSolutionId() + " не найден.";
        }

        preparation.setId(nextId++);
        preparations.add(preparation);
        return "OK";
    }

    public Optional<Preparation> getById(long id) {
        return preparations.stream()
                .filter(p -> p.getId() == id)
                .findFirst();
    }

    public List<Preparation> listAll() {
        return new ArrayList<>(preparations);
    }

    public String update(long id, Preparation updated) {
        Optional<Preparation> existingOpt = getById(id);
        if (existingOpt.isEmpty()) return "Ошибка: preparation с id=" + id + " не найден";

        String error = PreparationValidator.validate(updated);
        if (error != null) return error;

        if (solutionService.getById(updated.getSolutionId()).isEmpty()) {
            return "Ошибка: раствор с id=" + updated.getSolutionId() + " не найден.";
        }

        Preparation existing = existingOpt.get();
        existing.setFinalQuantity(updated.getFinalQuantity());
        existing.setFinalUnit(updated.getFinalUnit());
        existing.setComment(updated.getComment());

        return "OK";
    }

    public String remove(long id) {
        Optional<Preparation> existingOpt = getById(id);
        if (existingOpt.isEmpty()) return "Ошибка: preparation с id=" + id + " не найден";

        preparations.remove(existingOpt.get());
        return "OK";
    }


    public List<Preparation> listBySolutionId(long solutionId) {
        return preparations.stream()
                .filter(p -> p.getSolutionId() == solutionId)
                .toList();
    }
}