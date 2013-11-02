package io.github.dsh105.echopet.entity.living.type.horse;


public enum HorseType {

    NORMAL(0),
    DONKEY(1),
    MULE(2),
    ZOMBIE(3),
    SKELETON(4);

    private int id;

    HorseType(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }
}
