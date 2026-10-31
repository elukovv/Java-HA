/**
 * Интерфейс списка, предоставляющего доступ к элементам по индексу
 * и возможность последовательного обхода.
 *
 * @param <E> тип элементов списка
 */
public interface CustomList<E> extends Iterable<E> {

    /**
     * Добавляет элемент в конец списка.
     *
     * @param element добавляемый элемент
     * @throws NullPointerException если элемент равен null
     */
    void add(E element);

    /**
     * Возвращает элемент по указанному индексу.
     *
     * @param index индекс элемента
     * @return элемент по указанному индексу
     * @throws IndexOutOfBoundsException если индекс находится вне диапазона [0, size)
     */
    E get(int index);

    /**
     * Удаляет элемент по указанному индексу и возвращает его.
     *
     * @param index индекс удаляемого элемента
     * @return удалённый элемент
     * @throws IndexOutOfBoundsException если индекс находится вне диапазона [0, size)
     */
    E remove(int index);

    /**
     * Возвращает количество элементов списка.
     *
     * @return количество элементов
     */
    int size();

    /**
     * Проверяет, пуст ли список.
     *
     * @return true, если список пуст, иначе false
     */
    boolean isEmpty();
}