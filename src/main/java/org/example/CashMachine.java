package org.example;

public class CashMachine implements WithdrawalOperations, DepositOperations {

    @Override
    public double deposit(double balance, Double amount) {

        if (amount == null || amount <= 0) {
            return balance;
        }

        double newBalance = balance + amount;

        return Math.round(newBalance * 100.0) / 100.0;
    }
    @Override
    public double withdraw(double balance, double amount, BankType bankType) {

        double commission = applyCommission(amount, bankType);

        double total = amount + commission;

        if (total > balance) {
            System.out.println("Недостаточно средств.");
            return balance;
        }

        double newBalance = balance - total;

        return Math.round(newBalance * 100.0) / 100.0;
    }
}
