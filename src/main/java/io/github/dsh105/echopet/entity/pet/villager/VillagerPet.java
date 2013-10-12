package io.github.dsh105.echopet.entity.pet.villager;

import io.github.dsh105.echopet.data.PetType;
import io.github.dsh105.echopet.entity.pet.IAgeablePet;
import io.github.dsh105.echopet.entity.pet.Pet;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager.Profession;


public class VillagerPet extends Pet implements IAgeablePet {

    boolean baby = false;
    Profession profession = Profession.FARMER;

    public VillagerPet(Player owner, PetType petType) {
        super(owner, petType);
    }

    public boolean isBaby() {
        return this.baby;
    }

    public void setBaby(boolean flag) {
        ((EntityVillagerPet) getEntityPet()).setBaby(flag);
        this.baby = flag;
    }

    public Profession getProfession() {
        return profession;
    }

    public int getProfessionId() {
        return profession.getId();
    }

    public void setProfession(Profession prof) {
        ((EntityVillagerPet) getEntityPet()).setProfession(prof.getId());
        this.profession = prof;
    }

}
