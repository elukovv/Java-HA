package org.example;

public class Warrior extends Hero implements Restable, Castable {

    private final int armor;

    public int getArmor() {
        return armor;
    }

    public Warrior(String name, int maxHealth, int baseAttack, int armor) {

        super(name, maxHealth, baseAttack);

        if (armor < MIN_STAT_VALUE) {
            throw new IllegalArgumentException("Значение брони должно быть больше 0.");
        }

        this.armor = armor;
    }

    @Override
    public void takeDamage(int damage) {
        int finalDamage = 1;
        if (damage > armor && damage > 0) {
            finalDamage = damage - armor;
        }
        super.takeDamage(finalDamage);
    }

    @Override
    public void attack(Hero target) {
        if (target == null) {
            throw new IllegalArgumentException("Цель атаки не может быть null.");
        }
        target.takeDamage(getBaseAttack());
        System.out.println("Атака успешна! Нанесено урона: " + getBaseAttack());
    }

    @Override
    public ActionType makeTurn(Hero target) {

        if (needsRest()) {
            rest();
            return ActionType.REST;
        } else if (canCast()) {
            castSpecialSkill(target);
            return ActionType.SPECIAL_SKILL;
        } else {
            attack(target);
            return ActionType.BASE_ATTACK;
        }
    }



    @Override
    public boolean canCast() {
        return getHealth() < 0.5 * getMaxHealth();
    }

    @Override
    public void castSpecialSkill(Hero target) {
        if (target == null) {
            throw new IllegalArgumentException("Цель атаки не может быть null.");
        }
        if (canCast()) {
            int damage = getBaseAttack() + (armor * 2);
            target.takeDamage(damage);
            System.out.println("Воин нанес " + damage + " урона!");
        } else {
            System.out.println("Воин попытался ударить щитом, но потерял равновесие!");
        }
    }

    @Override
    public boolean needsRest() {
        return getHealth() < 0.15 * getMaxHealth();
    }

    @Override
    public void rest() {
        super.heal(armor * 2);
        System.out.println("Воин отдыхает и восстанавливается! Текущее здоровье:  " + getHealth());
    }

}
