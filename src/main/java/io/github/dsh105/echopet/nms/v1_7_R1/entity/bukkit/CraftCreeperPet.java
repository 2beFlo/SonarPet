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
import io.github.dsh105.echopet.api.entity.pet.type.CreeperPet;
import io.github.dsh105.echopet.nms.v1_7_R1.entity.CraftPet;
import io.github.dsh105.echopet.nms.v1_7_R1.entity.EntityPet;
import org.bukkit.entity.Creeper;

@EntityPetType(petType = PetType.CREEPER)
public class CraftCreeperPet extends CraftPet implements Creeper {

    public CraftCreeperPet(EntityPet entity) {
        super(entity);
    }

    @Override
    public boolean isPowered() {
        Pet p = this.getPet();
        if (p instanceof CreeperPet) {
            return ((CreeperPet) p).isPowered();
        }
        return false;
    }

    @Override
    public void setPowered(boolean b) {
        /*Pet p = this.getPet();
        if (p instanceof CreeperPet) {
            ((CreeperPet) p).setPowered(b);
        }*/
    }
}