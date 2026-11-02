import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class CustomArrayListTest {

    @Test
    void newListShouldBeEmpty() {
        CustomArrayList<String> list = new CustomArrayList<>();

        assertEquals(0, list.size());
        assertTrue(list.isEmpty());
    }

    @Test
    void addShouldPreserveOrder() {
        CustomArrayList<String> list = new CustomArrayList<>();

        list.add("A");
        list.add("B");
        list.add("C");

        assertEquals(3, list.size());
        assertFalse(list.isEmpty());
        assertEquals("A", list.get(0));
        assertEquals("B", list.get(1));
        assertEquals("C", list.get(2));
    }

    @Test
    void addMoreThanTenElementsShouldPreserveAllElements() {
        CustomArrayList<Integer> list = new CustomArrayList<>();

        for (int i = 0; i < 25; i++) {
            list.add(i);
        }

        assertEquals(25, list.size());

        for (int i = 0; i < 25; i++) {
            assertEquals(i, list.get(i));
        }
    }

    @Test
    void getShouldReturnFirstMiddleAndLastElements() {
        CustomArrayList<String> list = new CustomArrayList<>();

        list.add("A");
        list.add("B");
        list.add("C");
        list.add("D");
        list.add("E");

        assertEquals("A", list.get(0));
        assertEquals("C", list.get(2));
        assertEquals("E", list.get(4));
    }

    @Test
    void getShouldThrowExceptionForNegativeIndex() {
        CustomArrayList<String> list = new CustomArrayList<>();
        list.add("A");

        assertThrows(
                IndexOutOfBoundsException.class,
                new org.junit.jupiter.api.function.Executable() {
                    @Override
                    public void execute() {
                        list.get(-1);
                    }
                }
        );
    }

    @Test
    void getShouldThrowExceptionWhenIndexEqualsSize() {
        CustomArrayList<String> list = new CustomArrayList<>();
        list.add("A");

        assertThrows(
                IndexOutOfBoundsException.class,
                new org.junit.jupiter.api.function.Executable() {
                    @Override
                    public void execute() {
                        list.get(1);
                    }
                }
        );
    }

    @Test
    void removeShouldThrowExceptionForNegativeIndex() {
        CustomArrayList<String> list = new CustomArrayList<>();
        list.add("A");

        assertThrows(
                IndexOutOfBoundsException.class,
                new org.junit.jupiter.api.function.Executable() {
                    @Override
                    public void execute() {
                        list.remove(-1);
                    }
                }
        );
    }

    @Test
    void removeShouldThrowExceptionWhenIndexEqualsSize() {
        CustomArrayList<String> list = new CustomArrayList<>();
        list.add("A");

        assertThrows(
                IndexOutOfBoundsException.class,
                new org.junit.jupiter.api.function.Executable() {
                    @Override
                    public void execute() {
                        list.remove(1);
                    }
                }
        );
    }

    @Test
    void removeFirstElementShouldShiftRemainingElements() {
        CustomArrayList<String> list = new CustomArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");

        assertEquals("A", list.remove(0));

        assertEquals(2, list.size());
        assertEquals("B", list.get(0));
        assertEquals("C", list.get(1));
    }

    @Test
    void removeMiddleElementShouldShiftRemainingElements() {
        CustomArrayList<String> list = new CustomArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");
        list.add("D");
        list.add("E");

        assertEquals("C", list.remove(2));

        assertEquals(4, list.size());
        assertEquals("A", list.get(0));
        assertEquals("B", list.get(1));
        assertEquals("D", list.get(2));
        assertEquals("E", list.get(3));
    }

    @Test
    void removeLastElementShouldDecreaseSize() {
        CustomArrayList<String> list = new CustomArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");

        assertEquals("C", list.remove(2));

        assertEquals(2, list.size());
        assertEquals("A", list.get(0));
        assertEquals("B", list.get(1));
    }

    @Test
    void removeOnlyElementShouldMakeListEmpty() {
        CustomArrayList<String> list = new CustomArrayList<>();
        list.add("A");

        assertEquals("A", list.remove(0));

        assertEquals(0, list.size());
        assertTrue(list.isEmpty());
    }

    @Test
    void addNullShouldThrowNullPointerException() {
        CustomArrayList<String> list = new CustomArrayList<>();

        assertThrows(
                NullPointerException.class,
                new org.junit.jupiter.api.function.Executable() {
                    @Override
                    public void execute() {
                        list.add(null);
                    }
                }
        );

        assertEquals(0, list.size());
    }

    @Test
    void iteratorShouldTraverseElementsInOrder() {
        CustomArrayList<String> list = new CustomArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");

        Iterator<String> iterator = list.iterator();

        assertTrue(iterator.hasNext());
        assertEquals("A", iterator.next());

        assertTrue(iterator.hasNext());
        assertEquals("B", iterator.next());

        assertTrue(iterator.hasNext());
        assertEquals("C", iterator.next());

        assertFalse(iterator.hasNext());
    }

    @Test
    void iteratorOverEmptyListShouldHaveNoElements() {
        CustomArrayList<String> list = new CustomArrayList<>();

        Iterator<String> iterator = list.iterator();

        assertFalse(iterator.hasNext());
    }

    @Test
    void nextShouldThrowExceptionWhenIteratorIsExhausted() {
        CustomArrayList<String> list = new CustomArrayList<>();
        list.add("A");

        Iterator<String> iterator = list.iterator();
        iterator.next();

        assertThrows(
                NoSuchElementException.class,
                new org.junit.jupiter.api.function.Executable() {
                    @Override
                    public void execute() {
                        iterator.next();
                    }
                }
        );
    }

    @Test
    void removedElementShouldNoLongerAppearInIterator() {
        CustomArrayList<String> list = new CustomArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");

        list.remove(1);

        Iterator<String> iterator = list.iterator();
        List<String> actual = new ArrayList<>();

        while (iterator.hasNext()) {
            actual.add(iterator.next());
        }

        List<String> expected = new ArrayList<>();
        expected.add("A");
        expected.add("C");

        assertEquals(expected, actual);
    }
}