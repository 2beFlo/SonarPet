package com.github.dsh105.echopet.entity.pet;

import java.lang.reflect.Field;
import java.util.ArrayList;

import com.github.dsh105.echopet.api.event.PetAttackEvent;
import com.github.dsh105.echopet.api.event.PetRideJumpEvent;
import com.github.dsh105.echopet.api.event.PetRideMoveEvent;
import com.github.dsh105.echopet.data.PetType;
import net.minecraft.server.v1_6_R2.*;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_6_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_6_R2.entity.CraftEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.github.dsh105.echopet.EchoPet;
import com.github.dsh105.echopet.entity.pathfinder.PetGoalSelector;
import com.github.dsh105.echopet.entity.pathfinder.goals.PetGoalFloat;
import com.github.dsh105.echopet.entity.pathfinder.goals.PetGoalFly;
import com.github.dsh105.echopet.entity.pathfinder.goals.PetGoalFollowOwner;
import com.github.dsh105.echopet.entity.pathfinder.goals.PetGoalLookAtPlayer;
import com.github.dsh105.echopet.entity.pathfinder.goals.PetGoalRandomLookaround;
import com.github.dsh105.echopet.menu.MenuOption;
import com.github.dsh105.echopet.menu.PetMenu;
import com.github.dsh105.echopet.util.MenuUtil;

public abstract class EntityPet extends EntityCreature implements IMonster {
	
	public EntityLiving goalTarget = null;
	protected Pet pet;

	protected int particle = 0;
	protected int particleCounter = 0;
	
	private Field jump = null;
	private double jumpHeight;
	private float rideSpeed;
	
	public PetGoalSelector petGoalSelector;
	
	/*public EntityPet(World world) {
		super(world);
		this.die();
	}*/
	
	public EntityPet(World world, Pet pet) {
		super(world);
		try {
			pet.setCraftPet(new CraftPet(world.getServer(), this));
			//pet.setCraftPet(pet.getPetType().getNewCraftInstance(this));
			//setPet(pet);
			this.pet = pet;
			((LivingEntity) this.getBukkitEntity()).setMaxHealth(pet.getPetType().getMaxHealth());
			this.setHealth(pet.getPetType().getMaxHealth());
			this.jumpHeight = EchoPet.getPluginInstance().DO.getRideJumpHeight();
			this.rideSpeed = EchoPet.getPluginInstance().DO.getRideSpeed();
			this.jump = EntityLiving.class.getDeclaredField("bd");
	        this.jump.setAccessible(true);
			setPathfinding();
		} catch (Exception e) {
			EchoPet.getPluginInstance().severe(e, "Error creating new pet entity.");
			this.remove();
		}
	}

	public void attack(Entity entity) {
		PetAttackEvent attackEvent = new PetAttackEvent(this.getPet(), entity.getBukkitEntity(), this.getPet().getPetType().getAttackDamage());
		EchoPet.getPluginInstance().getServer().getPluginManager().callEvent(attackEvent);
		if (!attackEvent.isCancelled()) {
			if (entity instanceof EntityPlayer) {
				if (!((Boolean) EchoPet.getPluginInstance().DO.getConfigOption("canAttackPlayers", false))) {
					return;
				}
			}
			entity.damageEntity(DamageSource.mobAttack(this), (float) attackEvent.getDamage());
		}
	}
	
	public void setPathfinding() {
		try {
			this.petGoalSelector = new PetGoalSelector();
			this.getNavigation().b(true);
			
			petGoalSelector.addGoal("Float", new PetGoalFloat(this));

			petGoalSelector.addGoal("FollowOwner", new PetGoalFollowOwner(this, this.getSizeCategory().getStartWalk(), this.getSizeCategory().getStopWalk(), this.getSizeCategory().getTeleport()));
			petGoalSelector.addGoal("LookAtPlayer", new PetGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
			//petGoalSelector.addGoal("RandomLookAround", new PetGoalRandomLookaround(this));
			
			if ((Boolean) EchoPet.getPluginInstance().DO.getConfigOption("flyTeleport", false)) {
				petGoalSelector.addGoal("Fly", new PetGoalFly(this));
			}
			
		} catch (Exception e) {
			EchoPet.getPluginInstance().severe(e, "Error creating new pet entity.");
		}
	}

	public Pet getPet() {
		return this.pet;
	}
	
	public Player getOwner() {
		return pet.getOwner();
	}
	
	public Location getLocation() {
		//return new Location(this.world.getWorld(), this.locX, this.locY, this.locZ);
		return this.pet.getLocation();
	}

	//getMaxHealth() - 1.5.2
	//ax() - 1.6.1
	@Override
	protected void ay() {
		super.ay();
	}
	
	@Override
	public CraftEntity getBukkitEntity() {
		if (this.bukkitEntity == null) {
			this.bukkitEntity = new CraftPet(this.world.getServer(), this);
		}
		return this.bukkitEntity;
	}
	
	//bb() - 1.6.1
	@Override
	public boolean be() {return true;}
	
	//bb() - 1.5.2
	protected abstract String r(); //idle sound
	
	//aL() - 1.6.1
	protected abstract String aO(); //death sound
	
	public abstract SizeCategory getSizeCategory();
	
	// Overriden from EntityLiving - Most importantly overrides pathfinding selectors
	//1.6.1 - EntityInsentient
	//be() - 1.6.1
	@Override
	protected void bh() {
		//++this.bC; - 1.5.2 age
		++this.aV; //1.6 age
		//this.world.methodProfiler.a("checkDespawn");
		
		this.bo(); //bo() - 1.6.1
		//this.world.methodProfiler.b();
		
		//this.world.methodProfiler.a("sensing");
		this.getEntitySenses().a(); // Gets the field 'bP', as in EntityLiving class
		//this.bq.a() - 1.6
		//this.world.methodProfiler.b();
		
		//this.world.methodProfiler.a("targetSelector");
		//this.targetSelector.a();
		//this.world.methodProfiler.b();
		
		//this.world.methodProfiler.a("goalSelector");
		//this.goalSelector.a();
		this.petGoalSelector.run(); // This is where I tick my own pathfinder selector
		//this.world.methodProfiler.b();
		
		//this.world.methodProfiler.a("navigation");
		//this.getNavigation().e(); //1.5.2
		this.getNavigation().f();
		//this.world.methodProfiler.b();
		
		//this.world.methodProfiler.a("mob tick");
		//this.bp(); - 1.5.2
		this.bj(); //bg() - 1.6.1
		//this.world.methodProfiler.b();
		
		//this.world.methodProfiler.a("controls");
		
		//this.world.methodProfiler.a("move");
		this.getControllerMove().c();
		
		//this.world.methodProfiler.c("look");
		this.getControllerLook().a();
		//this.h.a() - 1.6
		
		//this.world.methodProfiler.c("jump");
		this.getControllerJump().b();
		//this.world.methodProfiler.b();
		//this.world.methodProfiler.b();
	}
	
	//Entity
	//public boolean a_(EntityHuman human) { 1.5
	public boolean a(EntityHuman human) { //1.6
		/*if (super.a_(human)) {
			return false;
		}*/
		
		if (human.getBukkitEntity() == this.getOwner().getPlayer()) {
			if ((Boolean) EchoPet.getPluginInstance().DO.getConfigOption("petMenuOnInteract", true)) {
				ArrayList<MenuOption> options = MenuUtil.createOptionList(getPet().getPetType());
				int size = this.getPet().getPetType() == PetType.HORSE ? 18 : 9;
				PetMenu menu = new PetMenu(getPet(), options, size);
				menu.open(true);
			}
			return true;
		}
		return false;
	}

	public void setLocation(Location l) {
		this.setLocation(l.getX(), l.getY(), l.getZ(), l.getPitch(), l.getYaw());
		this.world = ((CraftWorld) l.getWorld()).getHandle();
	}
	
	public void teleport(Location l) {
		this.getPet().getCraftPet().teleport(l);
	}
	
	public void remove() {
		bukkitEntity.remove();
	}
	
	@Override
	public void e(float f, float f1) { //f = sidewards, f1 = forwards/backwards

		// Can only jump over half slabs if the rider is not the owner
		// I like this idea
		// https://github.com/Bukkit/mc-dev/blob/master/net/minecraft/server/EntityHorse.java#L914
		if (this.passenger == null || !(this.passenger instanceof EntityHuman)) {
			super.e(f, f1);
			this.Y = 0.5F;
			return;
		}
		EntityHuman human = (EntityHuman) this.passenger;
		if (human.getBukkitEntity() != this.getOwner().getPlayer()) {
			super.e(f, f1);
			this.Y = 0.5F;
			return;
		}

		PetRideMoveEvent moveEvent = new PetRideMoveEvent(this.getPet(), f1, f);
		EchoPet.getPluginInstance().getServer().getPluginManager().callEvent(moveEvent);
		if (moveEvent.isCancelled()) {
			return;
		}

		float forMot = moveEvent.getForwardMotionSpeed();
		float sideMot = moveEvent.getSidewardMotionSpeed();

		this.Y = 1.0F;
		
		this.lastYaw = this.yaw = this.passenger.yaw;
        this.pitch = this.passenger.pitch * 0.5F;
        this.b(this.yaw, this.pitch);
        this.aP = this.aN = this.yaw;
        
        sideMot = ((EntityLiving) this.passenger).be * 0.5F;
        forMot = ((EntityLiving) this.passenger).bf;
        
        if (forMot <= 0.0F) {
            forMot *= 0.25F;
        }
        sideMot *= 0.75F;
        
        this.i(this.rideSpeed);
        super.e(sideMot, forMot);

        //https://github.com/Bukkit/mc-dev/blob/master/net/minecraft/server/EntityLiving.java#L1322-L1334

        if (jump != null && this.onGround) {
        	try {
    			if (jump.getBoolean(this.passenger)) {
				    PetRideJumpEvent rideEvent = new PetRideJumpEvent(this.getPet(), this.jumpHeight);
				    EchoPet.getPluginInstance().getServer().getPluginManager().callEvent(rideEvent);
				    if (!rideEvent.isCancelled()) {
					    this.motY = rideEvent.getJumpHeight();
				    }
    			}
    		} catch (Exception e) {
    			EchoPet.getPluginInstance().severe(e, "Failed to initiate Pet Jumping Motion for " + this.getOwner().getName() + "'s Pet.");
    		}
        }
	}

	@Override
	public void l_() {
		super.l_();
		if (this.particle == this.particleCounter) {
			this.particle = 0;
			this.particleCounter = this.random.nextInt(50);
		}
		else {
			this.particle++;
		}
	}
}