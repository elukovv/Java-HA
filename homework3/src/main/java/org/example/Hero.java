package org.example;

public abstract class Hero {

    static final int MIN_STAT_VALUE = 1;
    private final String name;
    private final int maxHealth;
    private final int baseAttack;
    private int health;

    public Hero(String name, int maxHealth, int baseAttack) {

        if (maxHealth < MIN_STAT_VALUE) {
            throw new IllegalArgumentException("Максимальное здоровье должно быть больше 0.");
        }

        if (baseAttack < MIN_STAT_VALUE) {
            throw new IllegalArgumentException("Базовая сила атаки должна быть больше 0.");
        }

        health = maxHealth;
        this.name = name;
        this.maxHealth = maxHealth;
        this.baseAttack = baseAttack;
    }

    public String getName() {return name;}
    public int getBaseAttack() {return baseAttack;}
    public int getHealth() {return health;}
    public int getMaxHealth() {return maxHealth;}

    public abstract void attack(Hero target);
    public abstract ActionType makeTurn(Hero target);

    public void takeDamage(int damage){

        if (damage <= 0) {
            throw new IllegalArgumentException("Урон должен быть больше 0.");
        }

        if (health == 0) {
            return;
        }

        if (health - damage > 0) {
            health -= damage;
        } else {
            health = 0;
            System.out.println(name + " пал в бою!");
        }
    }

    public void heal(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Введите число больше 0.");
        } else if (health + amount > maxHealth) {
            health = maxHealth;
        } else {
            health += amount;
        }
    }

    public boolean isAlive() {
        return health > 0;
    }
}
