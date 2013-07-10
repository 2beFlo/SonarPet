package com.github.dsh105.echopet.entity.pet.magmacube;

import org.bukkit.entity.Player;

import com.github.dsh105.echopet.data.PetType;
import com.github.dsh105.echopet.entity.pet.Pet;


public class MagmaCubePet extends Pet {
	
	int size;
	
	public MagmaCubePet(Player owner, PetType petType) {
		super(owner, petType);
	}
	
	public void setSize(int i) {
		((EntityMagmaCubePet) getPet()).setSize(i);
		this.size = i;
	}
	
	public int getSize() {
		return this.size;
	}
}
