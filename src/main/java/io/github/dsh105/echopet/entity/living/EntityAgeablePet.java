package io.github.dsh105.echopet.entity.living;

import net.minecraft.server.v1_6_R3.World;

public abstract class EntityAgeablePet extends EntityLivingPet {

    public EntityAgeablePet(World world) {
        super(world);
    }

    public EntityAgeablePet(World world, LivingPet pet) {
        super(world, pet);
    }

    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.a(12, new Integer(0));
    }

    public abstract void setBaby(boolean flag);

    public boolean isBaby() {
        return this.datawatcher.getInt(12) < 0;
    }

    @Override
    public SizeCategory getSizeCategory() {
        if (this.isBaby()) {
            return SizeCategory.TINY;
        } else {
            return SizeCategory.REGULAR;
        }
    }
}