package org.example;

import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);

    public static int readPositiveInt(String message) {
        System.out.println(message);

        while (true) {
            while (!scanner.hasNextInt()) {
                System.out.println("Ошибка! Введите целое число.");
                scanner.next();
            }

            int value = scanner.nextInt();

            if (value >= 1) {
                return value;
            }

            System.out.println("Ошибка! Число должно быть не меньше 1.");
        }
    }

    public static Hero createHero() {

        int type;

        do {
            System.out.println("Выберите тип персонажа:");
            System.out.println("1. Воин");
            System.out.println("2. Маг");

            while (!scanner.hasNextInt()) {
                System.out.println("Введите число 1 или 2.");
                scanner.next();
            }

            type = scanner.nextInt();

            if (type != 1 && type != 2) {
                System.out.println("Ошибка! Введите 1 или 2.");
            }
        } while (type != 1 && type != 2);

        scanner.nextLine();

        System.out.println("Введите имя: ");
        String name = scanner.nextLine();

        int maxHealth = readPositiveInt("Введите максимальное здоровье:");

        int baseAttack = readPositiveInt("Введите базовую атаку:");


        Hero hero;
        if (type == 1) {
            int armor = readPositiveInt("Введите показатель брони:");
            hero = new Warrior(name, maxHealth, baseAttack, armor);
        } else {
            int maxMana = readPositiveInt("Введите максимальный запас маны:");
            hero = new Mage(name, maxHealth, baseAttack, maxMana);
        }
        return hero;
    }

    public static void main(String args[]) {
        System.out.println("Создайте первого персонажа.");
        Hero hero1 = createHero();
        System.out.println("Создайте второго персонажа.");
        Hero hero2 = createHero();

        Arena arena = new Arena(hero1, hero2);
        arena.startTournament();
    }

}
