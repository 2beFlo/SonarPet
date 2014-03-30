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

package io.github.dsh105.echopet.nms.v1_7_R1.entity.type;

import io.github.dsh105.echopet.api.entity.EntityPetType;
import io.github.dsh105.echopet.api.entity.EntitySize;
import io.github.dsh105.echopet.api.entity.PetType;
import io.github.dsh105.echopet.api.entity.SizeCategory;
import io.github.dsh105.echopet.api.entity.nms.type.IEntityHumanPet;
import io.github.dsh105.echopet.api.entity.pet.Pet;
import io.github.dsh105.echopet.nms.v1_7_R1.entity.EntityPacketPet;
import io.github.dsh105.echopet.util.protocol.wrapper.WrapperPacketNamedEntitySpawn;
import net.minecraft.server.v1_7_R1.World;
import net.minecraft.util.com.mojang.authlib.GameProfile;

@EntitySize(width = 0.6F, height = 1.8F)
@EntityPetType(petType = PetType.HUMAN)
public class EntityHumanPet extends EntityPacketPet implements IEntityHumanPet {

    protected GameProfile profile;
    protected int equipmentId = 0;

    public EntityHumanPet(World world) {
        super(world);
    }

    public EntityHumanPet(World world, Pet pet) {
        super(world, pet);
    }

    @Override
    public WrapperPacketNamedEntitySpawn getSpawnPacket() {
        WrapperPacketNamedEntitySpawn spawn = new WrapperPacketNamedEntitySpawn();
        spawn.setEntityId(this.id);
        spawn.setGameProfile(this.profile);
        spawn.setX(this.locX);
        spawn.setY(this.locY);
        spawn.setZ(this.locZ);
        spawn.setYaw(this.yaw);
        spawn.setPitch(this.pitch);
        spawn.setEquipmentId(this.equipmentId);
        spawn.setMetadata(this.dataWatcher);
        return spawn;
    }

    @Override
    public void setEquipmentId(int id) {
        this.equipmentId = id;
    }

    @Override
    public int getEquipmentId() {
        return equipmentId;
    }

    @Override
    public GameProfile getGameProfile() {
        return profile;
    }

    @Override
    public void setGameProfile(GameProfile profile) {
        this.profile = profile;
    }

    @Override
    protected String getIdleSound() {
        return "random.breathe";
    }

    @Override
    protected void makeStepSound() {
        this.makeSound("step.grass", 0.15F, 1.0F);
    }

    @Override
    protected String getDeathSound() {
        return "random.classic_hurt";
    }

    @Override
    public SizeCategory getSizeCategory() {
        return SizeCategory.REGULAR;
    }
}