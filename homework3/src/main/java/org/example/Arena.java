package org.example;

public class Arena {
    private final Hero fighter1;
    private final Hero fighter2;
    private int roundCounter;

    public Arena(Hero fighter1, Hero fighter2) {
        this.fighter1 = fighter1;
        this.fighter2 = fighter2;
        this.roundCounter = 0;
    }

    public void startTournament() {

        while (fighter1.isAlive() && fighter2.isAlive()) {

            roundCounter += 1;
            System.out.println("Раунд " + roundCounter + "!");

            Hero attacker;
            Hero defender;

            if ((Math.random()) < 0.5) {
                attacker = fighter1;
                defender = fighter2;
            } else {
                attacker = fighter2;
                defender = fighter1;
            }

            ActionType action = attacker.makeTurn(defender);
            System.out.println("Игрок " + attacker.getName() + " совершил действие: " + action.getDescription());

            if (!defender.isAlive()) {
                System.out.println("Игрок " + defender.getName() + " погиб!");
                break;
            } else {
                action = defender.makeTurn(attacker);
                System.out.println("Игрок " + defender.getName() + " совершил действие: " + action.getDescription());
            }

            if (fighter1 instanceof Mage) {
                System.out.println("Здоровье 1 игрока: " + fighter1.getHealth() + ", количество очков маны: " + ((Mage) fighter1).getMana());
            } else if (fighter1 instanceof Warrior) {
                System.out.println("Здоровье 1 игрока: " + fighter1.getHealth() + ", количество брони: " + ((Warrior) fighter1).getArmor());
            }

            if (fighter2 instanceof Mage) {
                System.out.println("Здоровье 2 игрока: " + fighter2.getHealth() + ", количество очков маны: " + ((Mage) fighter2).getMana());
            } else if (fighter2 instanceof Warrior) {
                System.out.println("Здоровье 2 игрока: " + fighter2.getHealth() + ", количество брони: " + ((Warrior) fighter2).getArmor());
            }
        }

        if (fighter1.isAlive()) {
            System.out.println("Победитель: " + fighter1.getName());
        } else {
            System.out.println("Победитель: " + fighter2.getName());
        }

    }
}
