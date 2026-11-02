import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class CollectionPerformanceExperiment {

    static final int N = 10_000;

    static long checksum = 0;

    public static void main(String[] args) {

        String[] operations = {
                "Добавление в конец",
                "Добавление в начало",
                "Вставка в середину",
                "Доступ по индексу",
                "Удаление из начала",
                "Удаление из конца"
        };

        System.out.printf("%-25s %15s %15s%n",
                "Операция", "ArrayList (мс)", "LinkedList (мс)");

        for (int operation = 0; operation < 6; operation++) {

            for (int i = 0; i < 2; i++) {
                runExperiment(false, operation);
                runExperiment(true, operation);
            }

            long[] arrayListTimes = new long[5];
            long[] linkedListTimes = new long[5];

            for (int i = 0; i < 5; i++) {
                arrayListTimes[i] = runExperiment(false, operation);
                linkedListTimes[i] = runExperiment(true, operation);
            }

            Arrays.sort(arrayListTimes);
            Arrays.sort(linkedListTimes);

            double arrayListResult =
                    arrayListTimes[5 / 2] / 1_000_000.0;

            double linkedListResult =
                    linkedListTimes[5 / 2] / 1_000_000.0;

            System.out.printf("%-25s %15.3f %15.3f%n",
                    operations[operation],
                    arrayListResult,
                    linkedListResult);
        }

        System.out.println("Контрольная сумма: " + checksum);
    }

    public static long runExperiment(boolean linked, int operation) {

        List<Integer> list;

        if (linked) {
            list = new LinkedList<>();
        } else {
            list = new ArrayList<>();
        }

        if (operation == 3 || operation == 4 || operation == 5) {
            for (int i = 0; i < N; i++) {
                list.add(i);
            }
        }

        long sum = 0;

        long start = System.nanoTime();

        if (operation == 0) {
            for (int i = 0; i < N; i++) {
                list.add(i);
            }
        }

        if (operation == 1) {
            for (int i = 0; i < N; i++) {
                list.add(0, i);
            }
        }

        if (operation == 2) {
            for (int i = 0; i < N; i++) {
                list.add(list.size() / 2, i);
            }
        }

        if (operation == 3) {
            for (int i = 0; i < N; i++) {
                sum += list.get(i);
            }
        }

        if (operation == 4) {
            for (int i = 0; i < N; i++) {
                sum += list.remove(0);
            }
        }

        if (operation == 5) {
            for (int i = 0; i < N; i++) {
                sum += list.remove(list.size() - 1);
            }
        }

        long end = System.nanoTime();

        if (operation == 0 || operation == 1 || operation == 2) {
            for (int i = 0; i < list.size(); i++) {
                sum += list.get(i);
            }
        }

        checksum += sum;

        return end - start;
    }
}