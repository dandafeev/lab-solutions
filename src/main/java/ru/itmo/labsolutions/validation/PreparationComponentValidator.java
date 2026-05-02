package ru.itmo.labsolutions.validation;

import ru.itmo.labsolutions.domain.PreparationComponent;


public final class PreparationComponentValidator {

    private PreparationComponentValidator() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static String validate(PreparationComponent c) {
        if (c == null) return "Ошибка: объект компонента не может быть null";

        if (c.getPreparationId() <= 0) {
            return "Ошибка: preparationId должен быть положительным числом";
        }
        if (c.getBatchId() <= 0) {
            return "Ошибка: batchId должен быть положительным числом";
        }
        if (c.getQuantity() <= 0) {
            return "Ошибка: количество компонента должно быть больше 0";
        }
        if (c.getUnit() == null) {
            return "Ошибка: единицы количества компонента не указаны";
        }

        return null;
    }
}