import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Реализация списка на основе динамического массива.
 * При заполнении массива его ёмкость увеличивается в 1,5 раза.
 *
 * @param <E> тип элементов списка
 */
public class CustomArrayList<E> implements CustomList<E> {
    private Object[] elements;
    private int size;

    /**
     * Создаёт пустой список с начальной ёмкостью 10 элементов.
     */
    public CustomArrayList() {
        elements = new Object[10];
        size = 0;
    }

    /**
     * Добавляет элемент в конец списка.
     * При необходимости увеличивает ёмкость массива.
     *
     * @param element добавляемый элемент
     * @throws NullPointerException если элемент равен null
     */
    public void add(E element) {
        if (element == null) {
            throw new NullPointerException();
        }
        if (size == elements.length) {
            int newCapacity = elements.length + elements.length / 2;

            if (newCapacity == elements.length) {
                newCapacity++;
            }

            elements = Arrays.copyOf(elements, newCapacity);
        }

        elements[size] = element;
        size++;
    }

    /**
     * Возвращает элемент по указанному индексу.
     *
     * @param index индекс элемента
     * @return элемент по указанному индексу
     * @throws IndexOutOfBoundsException если индекс находится вне диапазона [0, size)
     */
    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        return (E) elements[index];
    }

    /**
     * Удаляет элемент по индексу, сдвигает последующие элементы влево
     * и очищает освободившуюся ячейку массива.
     *
     * @param index индекс удаляемого элемента
     * @return удалённый элемент
     * @throws IndexOutOfBoundsException если индекс находится вне диапазона [0, size)
     */
    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        E removedElement = (E) elements[index];
        System.arraycopy(
                elements, index + 1,
                elements, index,
                size - index - 1
        );
        size--;
        elements[size] = null;
        return removedElement;
    }

    /**
     * Возвращает количество элементов списка.
     *
     * @return количество элементов
     */
    public int size() {
        return size;
    }

    /**
     * Проверяет, пуст ли список.
     *
     * @return true, если список пуст, иначе false
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Возвращает итератор для последовательного обхода элементов списка.
     *
     * @return итератор, обходящий элементы от первого до последнего
     */
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int index = 0;

            public boolean hasNext() {
                return index < size;
            }

            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return (E) elements[index++];
            }
        };
    }
}
