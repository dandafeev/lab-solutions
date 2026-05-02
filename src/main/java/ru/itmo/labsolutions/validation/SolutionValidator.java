package ru.itmo.labsolutions.validation;

import ru.itmo.labsolutions.domain.Solution;


public final class SolutionValidator {

    private SolutionValidator() {
        throw new UnsupportedOperationException("Utility class");
    }


    public static String validate(Solution s) {
        if (s == null) return "Ошибка: объект раствора не может быть null";

        if (s.getName() == null || s.getName().isBlank()) {
            return "Ошибка: название раствора не может быть пустым";
        }
        if (s.getName().length() > 128) {
            return "Ошибка: название раствора слишком длинное (макс. 128 символов)";
        }
        if (s.getConcentration() < 0) {
            return "Ошибка: концентрация не может быть отрицательной";
        }
        if (s.getConcentrationUnit() == null) {
            return "Ошибка: единицы концентрации не указаны";
        }
        if (s.getSolvent() != null && s.getSolvent().length() > 64) {
            return "Ошибка: название растворителя слишком длинное (макс. 64 символа)";
        }

        return null;
    }
}
