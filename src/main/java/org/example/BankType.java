package org.example;

public enum BankType {
    NEO("НеоКредит Банк", 0.01),
    AUM("Арум Финтех", 0.02),
    VTA("Вектор Альянс Банк", 0.00);

    public String name;
    public double commission;

    BankType(String name, double commission) {
        this.name = name;
        this.commission = commission;
    }

    public String getName() {
        return name;
    }

    public double getCommission() {
        return commission;
    }
}
