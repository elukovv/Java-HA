package org.example;

public interface WithdrawalOperations {

    double withdraw(double balance, double amount, BankType bankType);

    default double applyCommission(Double amount, BankType bankType) {
        if (amount == null || bankType == null) {
            return 0.00;
        }

        double commission = amount * bankType.getCommission();

        return Math.round(commission * 100.0) / 100.0;
    }
}
