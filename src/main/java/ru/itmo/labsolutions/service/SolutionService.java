package ru.itmo.labsolutions.service;

import ru.itmo.labsolutions.domain.Solution;
import ru.itmo.labsolutions.validation.SolutionValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class SolutionService {

    private final List<Solution> solutions = new ArrayList<>();
    private long nextId = 1;

    public String add(Solution solution) {
        if (solution == null) return "Ошибка: объект раствора null";

        String error = SolutionValidator.validate(solution);
        if (error != null) return error;

        solution.setId(nextId++);
        solutions.add(solution);
        return "OK";
    }

    public Optional<Solution> getById(long id) {
        return solutions.stream()
                .filter(s -> s.getId() == id)
                .findFirst();
    }

    public List<Solution> listAll() {
        return new ArrayList<>(solutions);
    }

    public String update(long id, Solution updatedSolution) {
        Optional<Solution> existingOpt = getById(id);
        if (existingOpt.isEmpty()) return "Ошибка: раствор с id=" + id + " не найден";

        String error = SolutionValidator.validate(updatedSolution);
        if (error != null) return error;

        Solution existing = existingOpt.get();
        existing.setName(updatedSolution.getName());
        existing.setConcentration(updatedSolution.getConcentration());
        existing.setConcentrationUnit(updatedSolution.getConcentrationUnit());
        existing.setSolvent(updatedSolution.getSolvent());

        return "OK";
    }

    public String remove(long id) {
        Optional<Solution> existingOpt = getById(id);
        if (existingOpt.isEmpty()) return "Ошибка: раствор с id=" + id + " не найден";

        solutions.remove(existingOpt.get());
        return "OK";
    }


    public List<Solution> listByNameContains(String query) {
        if (query == null || query.isBlank()) {
            return new ArrayList<>(solutions);
        }
        String lowerQuery = query.toLowerCase();
        return solutions.stream()
                .filter(s -> s.getName().toLowerCase().contains(lowerQuery))
                .toList();
    }
}