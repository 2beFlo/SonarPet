/*
 * This file is part of EchoPet.
 *
 * EchoPet is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * EchoPet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with EchoPet.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.dsh105.echopet.entity.type.blaze;

import io.github.dsh105.echopet.entity.EntityPet;
import io.github.dsh105.echopet.entity.EntityPetType;
import io.github.dsh105.echopet.entity.Pet;
import io.github.dsh105.echopet.entity.PetType;
import org.bukkit.entity.Player;

@EntityPetType(petType = PetType.BLAZE)
public class BlazePet extends Pet {

    boolean onFire;

    public BlazePet(Player owner) {
        super(owner);
    }

    public BlazePet(String owner, EntityPet entityPet) {
        super(owner, entityPet);
    }

    public void setOnFire(boolean flag) {
        ((EntityBlazePet) getEntityPet()).setOnFire(flag);
        this.onFire = flag;
    }

    public boolean isOnFire() {
        return this.onFire;
    }
}
