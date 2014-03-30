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

package io.github.dsh105.echopet.nms.v1_7_R1.entity.bukkit;

import io.github.dsh105.echopet.api.entity.EntityPetType;
import io.github.dsh105.echopet.api.entity.PetType;
import io.github.dsh105.echopet.api.entity.pet.Pet;
import io.github.dsh105.echopet.api.entity.pet.type.SheepPet;
import io.github.dsh105.echopet.nms.v1_7_R1.entity.EntityPet;
import org.bukkit.DyeColor;
import org.bukkit.entity.Sheep;

@EntityPetType(petType = PetType.SHEEP)
public class CraftSheepPet extends CraftAgeablePet implements Sheep {

    public CraftSheepPet(EntityPet entity) {
        super(entity);
    }

    @Override
    public boolean isSheared() {
        Pet p = this.getPet();
        if (p instanceof SheepPet) {
            return ((SheepPet) p).isSheared();
        }
        return false;
    }

    @Override
    public void setSheared(boolean b) {
        /*Pet p = this.getPet();
        if (p instanceof SheepPet) {
            ((SheepPet) p).setSheared(b);
        }*/
    }

    @Override
    public DyeColor getColor() {
        Pet p = this.getPet();
        if (p instanceof SheepPet) {
            return ((SheepPet) p).getColor();
        }
        return null;
    }

    @Override
    public void setColor(DyeColor dyeColor) {
        /*Pet p = this.getPet();
        if (p instanceof SheepPet) {
            ((SheepPet) p).setColor(dyeColor);
        }*/
    }
}