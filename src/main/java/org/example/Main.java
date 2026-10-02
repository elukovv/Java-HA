package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Account account = new Account(12345, 555, 67000.00, BankType.AUM);

        System.out.println("Здравствуйте!");

        System.out.println("Введите номер карты: ");

        if (!scanner.hasNextInt()) {
            System.out.println("Ошибка: номер карты должен быть числом.");
            return;
        }

        int cardNumber = scanner.nextInt();

        System.out.println("Введите пин-код: ");

        if (!scanner.hasNextInt()) {
            System.out.println("Ошибка: пин-код должен быть числом.");
            return;
        }

        int pinCode = scanner.nextInt();

        if (cardNumber != account.getCardNumber() || pinCode != account.getPinCode()) {
            System.out.println("Ошибка доступа.");
            return;
        }



        System.out.println("Авторизация успешна.");
        System.out.println(account);

        CashMachine cashMachine = new CashMachine();

        System.out.print("Введите сумму для внесения: ");

        if (!scanner.hasNextDouble()) {
            System.out.println("Ошибка: сумма должна быть числом.");
            return;
        }

        double depositAmount = scanner.nextDouble();

        account.balance = cashMachine.deposit(account.getBalance(), depositAmount);

        System.out.printf("Баланс после внесения: %.2f руб.%n", account.getBalance());

        System.out.print("Введите сумму для снятия: ");

        if (!scanner.hasNextDouble()) {
            System.out.println("Ошибка: сумма должна быть числом.");
            return;
        }

        double withdrawalAmount = scanner.nextDouble();

        account.balance = cashMachine.withdraw(account.getBalance(), withdrawalAmount, account.getBankType());

        System.out.printf("Баланс после снятия: %.2f руб.%n", account.getBalance());

        scanner.close();
    }
}
