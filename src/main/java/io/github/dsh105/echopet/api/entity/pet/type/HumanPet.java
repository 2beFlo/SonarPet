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

package io.github.dsh105.echopet.api.entity.pet.type;

import io.github.dsh105.echopet.api.entity.nms.IEntityPet;
import io.github.dsh105.echopet.api.entity.EntityPetType;
import io.github.dsh105.echopet.api.entity.nms.type.IEntityHumanPet;
import io.github.dsh105.echopet.api.entity.pet.PacketPet;
import io.github.dsh105.echopet.api.entity.PetType;
import net.minecraft.util.com.mojang.authlib.GameProfile;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

@EntityPetType(petType = PetType.HUMAN)
public class HumanPet extends PacketPet {

    public HumanPet(Player owner) {
        super(owner);
    }

    public HumanPet(String owner, IEntityPet entityPet) {
        super(owner, entityPet);
    }

    public void setEquipment(Material material) {
        ((IEntityHumanPet) this.getEntityPet()).setEquipmentId(material.getId());
    }

    public Material getEquipment() {
        return Material.getMaterial(((IEntityHumanPet) this.getEntityPet()).getEquipmentId());
    }

    @Override
    public boolean setPetName(String name, boolean sendFailMessage) {
        name = name.length() > 16 ? name.substring(0, 16) : name;
        boolean success = super.setPetName(name, sendFailMessage);
        this.updateName(name);
        return success;
    }

    @Override
    public boolean setPetName(String name) {
        name = name.length() > 16 ? name.substring(0, 16) : name;
        boolean success = super.setPetName(name);
        this.updateName(name);
        return success;
    }

    private void updateName(String name) {
        if (this.getEntityPet().hasInititiated()) {
            this.getEntityPet().updatePacket();
        }
        IEntityHumanPet human = (IEntityHumanPet) this.getEntityPet();
        if (human.getGameProfile() != null) {
            human.setGameProfile(new GameProfile(human.getGameProfile().getId(), name));
        }
    }

    @Override
    public void teleport(Location to) {
        super.teleport(to);
        if (this.getEntityPet().hasInititiated()) {
            this.getEntityPet().updatePacket();
        }
    }
}
