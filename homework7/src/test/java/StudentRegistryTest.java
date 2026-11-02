import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

public class StudentRegistryTest {

    private Map<Integer, Student> createHashMap() {
        Map<Integer, Student> students = new HashMap<>();

        students.put(15, new Student(15, "Аня", 9.0));
        students.put(8, new Student(8, "Петя", 7.0));
        students.put(23, new Student(23, "Маша", 8.0));
        students.put(12, new Student(12, "Иван", 6.0));

        return students;
    }

    private NavigableMap<Integer, Student> createTreeMap() {
        NavigableMap<Integer, Student> students = new TreeMap<>(Collections.reverseOrder());

        students.put(15, new Student(15, "Аня", 9.0));
        students.put(8, new Student(8, "Петя", 7.0));
        students.put(23, new Student(23, "Маша", 8.0));
        students.put(12, new Student(12, "Иван", 6.0));

        return students;
    }

    @Test
    public void testFindStudentsByGradeRange() {
        Map<Integer, Student> students = createHashMap();

        List<Student> result = StudentRegistry.findStudentsByGradeRange(students, 7.0, 9.0);

        Set<Student> expected = new HashSet<>();

        expected.add(new Student(15, "Аня", 9.0));
        expected.add(new Student(8, "Петя", 7.0));
        expected.add(new Student(23, "Маша", 8.0));

        assertEquals(expected, new HashSet<>(result));
    }

    @Test
    public void testFindStudentsIncludesBothBoundaries() {
        Map<Integer, Student> students = new HashMap<>();

        Student first = new Student(1, "Аня", 5.0);
        Student second = new Student(2, "Петя", 7.0);
        Student third = new Student(3, "Маша", 9.0);

        students.put(1, first);
        students.put(2, second);
        students.put(3, third);

        List<Student> result = StudentRegistry.findStudentsByGradeRange(students, 5.0, 9.0);

        assertEquals(3, result.size());
        assertTrue(result.contains(first));
        assertTrue(result.contains(second));
        assertTrue(result.contains(third));
    }

    @Test
    public void testFindStudentsInEmptyMap() {
        Map<Integer, Student> students = new HashMap<>();

        List<Student> result = StudentRegistry.findStudentsByGradeRange(students, 0.0, 10.0);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindStudentsInvalidRange() {
        Map<Integer, Student> students = createHashMap();

        assertThrows(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() {
                StudentRegistry.findStudentsByGradeRange(students, 9.0, 5.0);
            }
        });
    }

    @Test
    public void testFindStudentsInvalidBounds() {
        Map<Integer, Student> students = createHashMap();

        assertThrows(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() {
                StudentRegistry.findStudentsByGradeRange(students, -1.0, 5.0);
            }
        });

        assertThrows(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() {
                StudentRegistry.findStudentsByGradeRange(students, 5.0, 11.0);
            }
        });
    }

    @Test
    public void testGetTopNStudentsById() {
        NavigableMap<Integer, Student> students = createTreeMap();

        List<Student> result = StudentRegistry.getTopNStudentsById(students, 2);

        List<Student> expected = new ArrayList<>();

        expected.add(new Student(23, "Маша", 8.0));
        expected.add(new Student(15, "Аня", 9.0));

        assertEquals(expected, result);
    }

    @Test
    public void testGetTopNWithZero() {
        NavigableMap<Integer, Student> students = createTreeMap();

        List<Student> result = StudentRegistry.getTopNStudentsById(students, 0);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetTopNGreaterThanSize() {
        NavigableMap<Integer, Student> students = createTreeMap();

        List<Student> result = StudentRegistry.getTopNStudentsById(students, 10);

        assertEquals(4, result.size());
        assertEquals(23, result.get(0).id());
        assertEquals(15, result.get(1).id());
        assertEquals(12, result.get(2).id());
        assertEquals(8, result.get(3).id());
    }

    @Test
    public void testGetTopNFromEmptyMap() {
        NavigableMap<Integer, Student> students = new TreeMap<>(Collections.reverseOrder());

        List<Student> result = StudentRegistry.getTopNStudentsById(students, 3);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetTopNNegative() {
        NavigableMap<Integer, Student> students = createTreeMap();

        assertThrows(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() {
                StudentRegistry.getTopNStudentsById(students, -1);
            }
        });
    }
}