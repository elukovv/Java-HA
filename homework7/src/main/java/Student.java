public record Student(int id, String name, double grade) {

    public Student {
        if (id <= 0) {
            throw new IllegalArgumentException("ID должен быть положительным");
        }

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Имя не должно быть пустым");
        }

        if (!Double.isFinite(grade) || grade < 0.0 || grade > 10.0) {
            throw new IllegalArgumentException("Оценка должна быть от 0 до 10");
        }
    }
}