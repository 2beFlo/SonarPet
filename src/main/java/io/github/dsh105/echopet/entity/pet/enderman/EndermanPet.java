package io.github.dsh105.echopet.entity.pet.enderman;

import io.github.dsh105.echopet.data.PetType;
import io.github.dsh105.echopet.entity.pet.Pet;
import org.bukkit.entity.Player;


public class EndermanPet extends Pet {

    boolean scream;

    public EndermanPet(Player owner, PetType petType) {
        super(owner, petType);
    }

    public void setScreaming(boolean flag) {
        ((EntityEndermanPet) getEntityPet()).setScreaming(flag);
        this.scream = flag;
    }

    public boolean isScreaming() {
        return this.scream;
    }
}
