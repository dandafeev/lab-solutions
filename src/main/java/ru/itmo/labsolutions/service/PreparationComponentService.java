package ru.itmo.labsolutions.service;

import ru.itmo.labsolutions.domain.PreparationComponent;
import ru.itmo.labsolutions.validation.PreparationComponentValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class PreparationComponentService {

    private final List<PreparationComponent> components = new ArrayList<>();
    private long nextId = 1;
    private final PreparationService preparationService;

    public PreparationComponentService(PreparationService preparationService) {
        this.preparationService = preparationService;
    }

    public String add(PreparationComponent component) {
        if (component == null) return "Ошибка: компонент null";

        String error = PreparationComponentValidator.validate(component);
        if (error != null) return error;

        if (preparationService.getById(component.getPreparationId()).isEmpty()) {
            return "Ошибка: preparation с id=" + component.getPreparationId() + " не найден.";
        }

        component.setId(nextId++);
        components.add(component);
        return "OK";
    }

    public Optional<PreparationComponent> getById(long id) {
        return components.stream()
                .filter(c -> c.getId() == id)
                .findFirst();
    }

    public List<PreparationComponent> listAll() {
        return new ArrayList<>(components);
    }

    public String remove(long id) {
        Optional<PreparationComponent> existingOpt = getById(id);
        if (existingOpt.isEmpty()) return "Ошибка: компонент с id=" + id + " не найден";

        components.remove(existingOpt.get());
        return "OK";
    }


    public List<PreparationComponent> listByPreparationId(long preparationId) {
        return components.stream()
                .filter(c -> c.getPreparationId() == preparationId)
                .toList();
    }
}