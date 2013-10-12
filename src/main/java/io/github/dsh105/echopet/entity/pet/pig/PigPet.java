package io.github.dsh105.echopet.entity.pet.pig;


import org.bukkit.entity.Player;

import io.github.dsh105.echopet.data.PetType;
import io.github.dsh105.echopet.entity.pet.IAgeablePet;
import io.github.dsh105.echopet.entity.pet.Pet;

public class PigPet extends Pet implements IAgeablePet {
	
	boolean baby;
	boolean saddle;

	public PigPet(Player owner, PetType petType) {
		super(owner, petType);
	}
	
	public void setBaby(boolean flag) {
		((EntityPigPet) getEntityPet()).setBaby(flag);
		this.baby = flag;
	}
	
	public boolean isBaby() {
		return this.baby;
	}
	
	public void setSaddle(boolean flag) {
		((EntityPigPet) getEntityPet()).setSaddle(flag);
		this.saddle = flag;
	}
	
	public boolean hasSaddle() {
		return this.saddle;
	}
}