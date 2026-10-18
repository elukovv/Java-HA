package org.example;

public class Mage extends Hero implements Castable, Restable {

    private final int maxMana;
    private int mana;

    public int getMana() {
        return mana;
    }

    public Mage(String name, int maxHealth, int baseAttack, int maxMana) {

        super(name, maxHealth, baseAttack);

        if (maxMana < MIN_STAT_VALUE) {
            throw new IllegalArgumentException("Значение маны должно быть больше 0.");
        }

        this.maxMana = maxMana;
    }

    @Override
    public void attack(Hero target) {
        if (target == null) {
            throw new IllegalArgumentException("Цель атаки не может быть null.");
        }
        if (mana >= 10) {
            int damage = getBaseAttack() * 2;
            mana -= 10;
            target.takeDamage(damage);
            System.out.println("Нанесено " + damage + " единиц урона! Мана после удара: " + mana);
        } else if (mana + 5 <= maxMana){
            int damage = getBaseAttack() / 2;
            mana += 5;
            target.takeDamage(damage);
            System.out.println("Нанесено " + damage + " единиц урона. Мана после удара: " + mana);
        } else {
            int damage = getBaseAttack() / 2;
            mana = maxMana;
            target.takeDamage(damage);
            System.out.println("Нанесено " + damage + " единиц урона. Мана после удара: " + mana);
        }
    }

    @Override
    public ActionType makeTurn(Hero target) {
        if (target == null) {
            throw new IllegalArgumentException("Цель атаки не может быть null.");
        }
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
        return mana >= 25;
    }

    @Override
    public void castSpecialSkill(Hero target) {
        if (target == null) {
            throw new IllegalArgumentException("Цель атаки не может быть null.");
        }
        if (canCast()) {
            int damage = getBaseAttack() * 3;
            mana -= 25;
            target.takeDamage(damage);
            System.out.println("Маг нанес " + damage + " урона!");
        } else {
            System.out.println("Маг попытался произнести заклинание, но маны не хватило!");
        }
    }

    @Override
    public boolean needsRest() {
        return (mana == 0 || getHealth() < 0.3 * getMaxHealth());
    }

    @Override
    public void rest() {
        mana = maxMana;
        heal(getBaseAttack());
        System.out.println("Мана полностью восстановлена, здоровье восстановлено на " + getBaseAttack() + " очков.");
    }
}
