import java.util.*;

public class StudentRegistry {

    public static void main(String[] args) {
        Map<Integer, Student> hashStudents = new HashMap<>();
        NavigableMap<Integer, Student> treeStudents = new TreeMap<>(Collections.reverseOrder());


    }

    static List<Student> findStudentsByGradeRange(
            Map<Integer, Student> students,
            double minGrade,
            double maxGrade) {
        if (minGrade < 0.0 || maxGrade > 10.0 || maxGrade < 0.0 || minGrade > 10.0 || minGrade > maxGrade) {
            throw new IllegalArgumentException("Некорректный диапазон оценок");
        }

        List<Student> result = new ArrayList<>();

        for (Student student : students.values()) {
            if (student.grade() >= minGrade && student.grade() <= maxGrade) {
                result.add(student);
            }
        }

        return result;
    }

    public static List<Student> getTopNStudentsById(
            NavigableMap<Integer, Student> students,
            int n) {

        if (n < 0) {
            throw new IllegalArgumentException("Количество не может быть отрицательным");
        }

        List<Student> result = new ArrayList<>();

        for (Student student : students.values()) {
            if (result.size() == n) {
                break;
            }
            result.add(student);
        }

        return result;
    }
}
