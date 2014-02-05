package io.github.dsh105.echopet.entity.type.pigzombie;

import io.github.dsh105.echopet.entity.EntityPetType;
import io.github.dsh105.echopet.entity.Pet;
import io.github.dsh105.echopet.entity.PetType;

@EntityPetType(petType = PetType.PIGZOMBIE)
public class PigZombiePet extends Pet {

    boolean baby = false;
    boolean villager = false;
    boolean equipment = false;

    public PigZombiePet(String owner) {
        super(owner);
        //this.equipment = EchoPet.getInstance().options.shouldHaveEquipment(getPet().getPetType());
    }

    public void setBaby(boolean flag) {
        ((EntityPigZombiePet) getEntityPet()).setBaby(flag);
        this.baby = flag;
    }

    public boolean isBaby() {
        return this.baby;
    }

    public void setVillager(boolean flag) {
        ((EntityPigZombiePet) getEntityPet()).setVillager(flag);
        this.villager = flag;
    }

    public boolean isVillager() {
        return this.villager;
    }

}
