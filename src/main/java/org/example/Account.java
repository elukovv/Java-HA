package org.example;

public class Account {

    public int cardNumber;
    public int pinCode;
    public double balance;
    public BankType BankType;

    public Account(int cardNumber, int pinCode, double balance, BankType BankType) {

        if (cardNumber >= 10000 && cardNumber <= 99999) {
            this.cardNumber = cardNumber;
        } else {
            this.cardNumber = 10000;
        }

        if (pinCode >= 100 && pinCode <= 999) {
            this.pinCode = pinCode;
        } else {
            this.pinCode = 100;
        }

        if (BankType == null) {
            this.BankType = BankType.NEO;
        } else {
            this.BankType = BankType;
        }

        if (balance >= 0) {
            this.balance = Math.round(balance * 100.0) / 100.0;
        } else {
            this.balance = 0.00;
        }
    }

    public int getCardNumber() {
        return cardNumber;
    }

    public int getPinCode() {
        return pinCode;
    }

    public double getBalance() {
        return balance;
    }

    public BankType getBankType() {
        return BankType;
    }

    public String toString() {
        return String.format("%s Карта: %05d, Баланс: %.2f руб.", BankType.getName(), cardNumber, balance);
    }
}
