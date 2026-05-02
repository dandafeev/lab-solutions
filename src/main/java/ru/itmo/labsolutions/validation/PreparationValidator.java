package ru.itmo.labsolutions.validation;

import ru.itmo.labsolutions.domain.Preparation;


public final class PreparationValidator {

    private PreparationValidator() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static String validate(Preparation p) {
        if (p == null) return "Ошибка: объект приготовления не может быть null";

        if (p.getSolutionId() <= 0) {
            return "Ошибка: solutionId должен быть положительным числом";
        }
        if (p.getFinalQuantity() <= 0) {
            return "Ошибка: итоговое количество должно быть больше 0";
        }
        if (p.getFinalUnit() == null) {
            return "Ошибка: единицы итогового количества не указаны";
        }
        if (p.getComment() != null && p.getComment().length() > 128) {
            return "Ошибка: комментарий слишком длинный (макс. 128 символов)";
        }

        return null;
    }
}