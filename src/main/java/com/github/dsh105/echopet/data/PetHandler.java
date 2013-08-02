package com.github.dsh105.echopet.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager.Profession;

import com.github.dsh105.echopet.EchoPet;
import com.github.dsh105.echopet.data.PetData.Type;
import com.github.dsh105.echopet.entity.pet.IAgeablePet;
import com.github.dsh105.echopet.entity.pet.Pet;
import com.github.dsh105.echopet.entity.pet.blaze.BlazePet;
import com.github.dsh105.echopet.entity.pet.creeper.CreeperPet;
import com.github.dsh105.echopet.entity.pet.enderman.EndermanPet;
import com.github.dsh105.echopet.entity.pet.horse.HorseArmour;
import com.github.dsh105.echopet.entity.pet.horse.HorseMarking;
import com.github.dsh105.echopet.entity.pet.horse.HorsePet;
import com.github.dsh105.echopet.entity.pet.horse.HorseType;
import com.github.dsh105.echopet.entity.pet.horse.HorseVariant;
import com.github.dsh105.echopet.entity.pet.magmacube.MagmaCubePet;
import com.github.dsh105.echopet.entity.pet.ocelot.OcelotPet;
import com.github.dsh105.echopet.entity.pet.pig.PigPet;
import com.github.dsh105.echopet.entity.pet.pigzombie.PigZombiePet;
import com.github.dsh105.echopet.entity.pet.sheep.SheepPet;
import com.github.dsh105.echopet.entity.pet.skeleton.SkeletonPet;
import com.github.dsh105.echopet.entity.pet.slime.SlimePet;
import com.github.dsh105.echopet.entity.pet.villager.VillagerPet;
import com.github.dsh105.echopet.entity.pet.wither.WitherPet;
import com.github.dsh105.echopet.entity.pet.wolf.WolfPet;
import com.github.dsh105.echopet.entity.pet.zombie.ZombiePet;
import com.github.dsh105.echopet.util.EnumUtil;
import com.github.dsh105.echopet.util.Lang;
import com.github.dsh105.echopet.util.StringUtil;


public class PetHandler {
	
	private static EchoPet ec;
	
	private ArrayList<Pet> pets = new ArrayList<Pet>();
	
	public PetHandler(EchoPet ec) {
		PetHandler.ec = ec;
	}
	
	public static PetHandler getInstance() {
		return ec.PH;
	}

	public ArrayList<Pet> getPets() {
		return pets;
	}

	public Pet loadPets(Player p, boolean findDefault, boolean sendMessage) {
		EchoPet ec = EchoPet.getPluginInstance();
		if (ec.DO.sqlOverride()) {
			Pet pet = ec.SPH.createPetFromDatabase(p);
			if (pet == null) {
				return null;
			}
			else {
				if (sendMessage) {
					p.sendMessage(Lang.DATABASE_PET_LOAD.toString().replace("%petname%", pet.getPetName().toString()));
				}
			}
			return pet;
		}

		if (ec.getPetConfig().get("default." + p.getName() + ".pet.type") != null && findDefault) {
			Pet pi = ec.PH.createPetFromFile("default", p);
			if (pi == null) {
				return null;
			}
			else {
				if (sendMessage) {
					p.sendMessage(Lang.DEFAULT_PET_LOAD.toString().replace("%petname%", pi.getPetName().toString()));
				}
			}
			return pi;
		}

		if (ec.DO.autoLoadPets(p)) {
			if (ec.getPetConfig().get("autosave." + p.getName() + ".pet.type") != null) {
				Pet pi = ec.PH.createPetFromFile("autosave", p);
				if (pi == null) {
					return null;
				}
				else {
					if (sendMessage) {
						p.sendMessage(Lang.AUTOSAVE_PET_LOAD.toString().replace("%petname%", pi.getPetName().toString()));
					}
				}
				return pi;
			}
		}
		return null;
	}
    
    public void removeAllPets() {
	    Iterator<Pet> i = pets.listIterator();
	    while (i.hasNext()) {
		    Pet p = i.next();
		    saveFileData("autosave", p);
		    ec.SPH.saveToDatabase(p, false);
		    p.removePet();
		    i.remove();
	    }
    }
	
	public Pet createPet(Player owner, PetType petType, boolean sendMessageOnFail) {
		if (getPet(owner) != null) {
			removePets(owner);
		}
		if (!ec.DO.allowPetType(petType)) {
			if (sendMessageOnFail) {
				owner.sendMessage(Lang.PET_TYPE_DISABLED.toString().replace("%type%", StringUtil.capitalise(petType.toString())));
			}
			return null;
		}
		Pet pi = petType.getNewPetInstance(owner, petType);
		//Pet pi = new Pet(owner, petType);
		forceAllValidData(pi);
		pets.add(pi);
		//saveFileData("autosave", pi);
		//ec.SPH.saveToDatabase(pi, false);
		return pi;
	}
	
	public Pet createPet(Player owner, PetType petType, PetType mountType, boolean sendFailMessage) {
		if (getPet(owner) != null) {
			removePets(owner);
		}
		if (!ec.DO.allowPetType(petType)) {
			if (sendFailMessage) {
				owner.sendMessage(Lang.PET_TYPE_DISABLED.toString().replace("%type%", StringUtil.capitalise(petType.toString())));
			}
			return null;
		}
		Pet pi = petType.getNewPetInstance(owner, petType);
		//Pet pi = new Pet(owner, petType);
		pi.createMount(mountType, true);
		forceAllValidData(pi);
		pets.add(pi);
		//saveFileData("autosave", pi);
		//ec.SPH.saveToDatabase(pi, false);
		return pi;
	}
	
	public List<Pet> getAllPetData() {
		return pets;
	}
	
	public Pet getPet(Player player) {
		for (Pet pi : pets) {
			if (pi.getOwner() == player) {
				return pi;
			}
		}
		return null;
	}
	
	public Pet getPet(Entity pet) {
		for (Pet pi : pets) {
			if (pi.getEntityPet() == pet || pi.getMount().getEntityPet() == pet) {
				return pi;
			}
			if (pi.getCraftPet() == pet || pi.getMount().getCraftPet() == pet) {
				return pi;
			}
		}
		return null;
	}
	
	// *May* add support for multiple pets
	/*public List<Pet> getPets(Player player) {
		List<Pet> tempList = new ArrayList<Pet>();
		
		for (Pet pi : pets) {
			if (pi.getOwner() == player) tempList.add(pi);
		}
		
		return tempList.isEmpty() ? null : tempList;
	}
	
	public int getMaxPets() {
		return ec.getConfig().getInt("maxPets");
	}*/
	
	// Force all data specified in config file and notify player.
	public void forceAllValidData(Pet pi) {
		ArrayList<PetData> tempData = new ArrayList<PetData>();
		for (PetData data : PetData.values()) {
			if (ec.DO.forceData(pi.getPetType(), data)) {
				tempData.add(data);
			}
		}
		setData(pi, tempData.toArray(new PetData[tempData.size()]), true);
		
		ArrayList<PetData> tempMountData = new ArrayList<PetData>();
		if (pi.getMount() != null) {
			for (PetData data : PetData.values()) {
				if (ec.DO.forceData(pi.getPetType(), data)) {
					tempMountData.add(data);
				}
			}
			setData(pi.getMount(), tempMountData.toArray(new PetData[tempData.size()]), true);
		}
		
		if ((Boolean) ec.DO.getConfigOption("sendForceMessage", true)) {
			String dataToString = " ";
			if (!tempMountData.isEmpty()) {
				dataToString = StringUtil.dataToString(tempData);
			}
			else {
				dataToString = StringUtil.dataToString(tempData, tempMountData);
			}
			if (dataToString != " ") {
				pi.getOwner().sendMessage(Lang.DATA_FORCE_MESSAGE.toString()
						.replace("%data%", dataToString));
			}
		}
	}

	public void updateFileData(String type, Pet pet, ArrayList<PetData> list, boolean b) {
		ec.SPH.updateDatabase(pet.getOwner(), list, b, pet.isMount());
		String w = pet.getOwner().getWorld().getName();
		String path = type + "." + w + "." + pet.getOwner().getName();
		for (PetData pd : list) {
			ec.getPetConfig().set(path + ".pet.data." + pd.toString().toLowerCase(), b);
		}
		ec.getPetConfig().saveConfig();
	}
	
	public Pet createPetFromFile(String type, Player p) {
		if (ec.DO.autoLoadPets(p)) {
			String w = p.getWorld().getName();
			String path = type + "." + w + "." + p.getName();
			if (ec.getPetConfig().get(path) != null) {
				PetType petType = PetType.valueOf(ec.getPetConfig().getString(path + ".pet.type"));
				String name = ec.getPetConfig().getString(path + ".pet.name");
				if (name.equalsIgnoreCase("") || name == null) {
					name = petType.getDefaultName(name);
				}
				if (petType == null) return null;
				if (!ec.DO.allowPetType(petType)) {
					return null;
				}
				Pet pi = petType.getNewPetInstance(p, petType);
				//Pet pi = new Pet(p, petType);
				
				pi.setName(name);
				
				ArrayList<PetData> listTrue = new ArrayList<PetData>();
				ArrayList<PetData> listFalse = new ArrayList<PetData>();
				ConfigurationSection cs = ec.getPetConfig().getConfigurationSection(path + ".pet.data");
				if (cs != null) {
					for (String key : cs.getKeys(false)) {
						if (EnumUtil.isEnumType(PetData.class, key.toUpperCase())) {
							PetData pd = PetData.valueOf(key.toUpperCase());
							if (pd.isType(Type.BOOLEAN) && !ec.getPetConfig().getBoolean(path + ".pet.data." + key)) {
								listFalse.add(pd);
							}
							else {
								listTrue.add(pd);
							}
						}
						else {
							ec.log(ChatColor.RED + "Error whilst loading data Pet Save Data for " + pi.getOwner().getName() + ". Unknown enum type: " + key);
						}
					}
				}
				
				if (!listTrue.isEmpty()) {
					setData(pi, listTrue.toArray(new PetData[listTrue.size()]), true);
				}
				if (!listFalse.isEmpty()) {
					setData(pi, listFalse.toArray(new PetData[listFalse.size()]), false);
				}
				
				forceAllValidData(pi);
				
				pets.add(pi);
				return pi;
			}
		}
		return null;
	}
	
	public void removePets(Player player) {
		Iterator<Pet> i = pets.listIterator();
		while (i.hasNext()) {
			Pet p = i.next();
			if (p.getOwner() == player) {
				saveFileData("autosave", p);
				ec.SPH.saveToDatabase(p, false);
				p.removePet();
				i.remove();
			}
		}
	}
	
	public void removePet(Pet pi) {
		//saveFileData("autosave", pi);
		pi.removePet();
		pets.remove(pi);
	}
	
	public void saveFileData(String type, Pet pi) {
		clearFileData(type, pi);
		try {
			String oName = pi.getOwner().getName();
			String path = type + "." + oName;
			PetType petType = pi.getPetType();
			
			ec.getPetConfig().set(path + ".pet.type", petType.toString());
			ec.getPetConfig().set(path + ".pet.name", pi.getNameToString());
			
			for (PetData pd : pi.getAllData(true)) {
				ec.getPetConfig().set(path + ".pet.data." + pd.toString().toLowerCase(), true);
			}
			for (PetData pd : pi.getAllData(false)) {
				if (pd.isType(Type.BOOLEAN)) {
					ec.getPetConfig().set(path + ".pet.data." + pd.toString().toLowerCase(), false);
				}
				else {
					ec.getPetConfig().set(path + ".pet.data." + pd.toString().toLowerCase(), true);
				}
			}
			
			if (pi.getMount() != null) {
				PetType mountType = pi.getMount().getPetType();
				
				ec.getPetConfig().set(path + ".mount.type", mountType.toString());
				ec.getPetConfig().set(path + ".mount.name", pi.getMount().getNameToString());
				for (PetData pd : pi.getMount().getAllData(true)) {
					ec.getPetConfig().set(path + ".mount.data." + pd.toString().toLowerCase(), true);
				}
				for (PetData pd : pi.getMount().getAllData(false)) {
					if (pd.isType(Type.BOOLEAN)) {
						ec.getPetConfig().set(path + ".mount.data." + pd.toString().toLowerCase(), false);
					}
					else {
						ec.getPetConfig().set(path + ".mount.data." + pd.toString().toLowerCase(), true);
					}
				}
			}
		} catch (Exception e) {}
		ec.getPetConfig().saveConfig();
	}

	public void saveFileData(String type, Player p, UnorganisedPetData UPD, UnorganisedPetData UMD) {
		this.saveFileData(type, p.getName(), UPD, UMD);
	}
	
	public void saveFileData(String type, String name, UnorganisedPetData UPD, UnorganisedPetData UMD) {
		clearFileData(type, name);
		PetType pt = UPD.petType;
		PetData[] data = UPD.petDataList.toArray(new PetData[UPD.petDataList.size()]);
		String petName = UPD.petName;
		if (UPD.petName == null || UPD.petName.equalsIgnoreCase("")) {
			petName = pt.getDefaultName(name);
		}
		PetType mountType = UMD.petType;
		PetData[] mountData = UMD.petDataList.toArray(new PetData[UMD.petDataList.size()]);
		String mountName = UMD.petName;
		if (UMD.petName == null || UMD.petName.equalsIgnoreCase("")) {
			mountName = pt.getDefaultName(name);
		}

		String path = type + "." + name;
		try {
			ec.getPetConfig().set(path + ".pet.type", pt.toString());
			ec.getPetConfig().set(path + ".pet.name", petName);
			
			for (PetData pd : data) {
				ec.getPetConfig().set(path + ".pet.data." + pd.toString().toLowerCase(), true);
			}
			
			if (mountData != null && mountType != null) {
				ec.getPetConfig().set(path + ".mount.type", mountType.toString());
				ec.getPetConfig().set(path + ".mount.name", mountName);
				for (PetData pd : mountData) {
					ec.getPetConfig().set(path + ".mount.data." + pd.toString().toLowerCase(), true);
				}
				
			}
		} catch (Exception e) {}
		ec.getPetConfig().saveConfig();
	}

	public void saveFileData(String type, Player p, UnorganisedPetData UPD) {
		this.saveFileData(type, p.getName(), UPD);
	}
	
	public void saveFileData(String type, String name, UnorganisedPetData UPD) {
		clearFileData(type, name);
		PetType pt = UPD.petType;
		PetData[] data = UPD.petDataList.toArray(new PetData[UPD.petDataList.size()]);
		String petName = UPD.petName;

		String path = type +"." + name;
		try {
			ec.getPetConfig().set(path + ".pet.type", pt.toString());
			ec.getPetConfig().set(path + ".pet.name", petName);
			
			for (PetData pd : data) {
				ec.getPetConfig().set(path + ".pet.data." + pd.toString().toLowerCase(), true);
			}
			
		} catch (Exception e) {}
		ec.getPetConfig().saveConfig();
	}
	
	public void clearAllFileData() {
		for (String key : ec.getPetConfig().getKeys(true)) {
			if (ec.getPetConfig().get(key) != null) {
				ec.getPetConfig().set(key, null);
			}
		}
		ec.getPetConfig().saveConfig();
	}

	public void clearFileData(String type, Pet pi) {
		clearFileData(type, pi.getOwner());
	}

	public void clearFileData(String type, String pName) {
		String path = type + "." + pName;
		if (ec.getPetConfig().get(path + ".pet.type") != null) {
			for (String key : ec.getPetConfig().getConfigurationSection(path).getKeys(false)) {
				for (String key1 : ec.getPetConfig().getConfigurationSection(path + "." + key).getKeys(false)) {
					if (ec.getPetConfig().get(path + "." + key + "." + key1) != null) {
						ec.getPetConfig().set(path + "." + key + "." + key1, null);
					}
				}
				if (ec.getPetConfig().get(path + "." + key) != null) {
					ec.getPetConfig().set(path + "." + key, null);
				}
			}
		}
		ec.getPetConfig().saveConfig();
	}
	
	public void clearFileData(String type, Player p) {
		clearFileData(type, p.getName());
	}
	
	public void setData(Pet pet, PetData[] data, boolean b) {
		ArrayList<PetData> list = new ArrayList<PetData>();
		PetType petType = pet.getPetType();
		
		HorseType ht = HorseType.NORMAL;
		HorseVariant hv = HorseVariant.WHITE;
		HorseMarking hm = HorseMarking.NONE;
		HorseArmour ha = HorseArmour.NONE;
		
		for (PetData pd : data) {
			if (petType.isDataAllowed(pd)) {
				if (pd == PetData.BABY) {
					if (petType == PetType.ZOMBIE) {
						((ZombiePet) pet).setBaby(b);
					}
					else if (petType == PetType.PIGZOMBIE) {
						((PigZombiePet) pet).setBaby(b);
					}
					else {
						((IAgeablePet) pet).setBaby(b);
					}
				}
				
				if (pd == PetData.POWER) {
					((CreeperPet) pet).setPowered(b);
				}
				
				if (pd.isType(Type.SIZE)) {
					int i = 1;
					if (pd == PetData.MEDIUM) {
						i = 2;
					}
					else if (pd == PetData.LARGE) {
						i = 4;
					}
					if (petType == PetType.SLIME) {
						((SlimePet) pet).setSize(i);
					}
					if (petType == PetType.MAGMACUBE) {
						((MagmaCubePet) pet).setSize(i);
					}
				}
				
				if (pd.isType(Type.CAT) && petType == PetType.OCELOT) {
					try {
						org.bukkit.entity.Ocelot.Type t = org.bukkit.entity.Ocelot.Type.valueOf(pd.toString() + "_CAT"); 
						if (t != null) {
							((OcelotPet) pet).setCatType(t);
						}
					} catch (Exception e) {
						EchoPet.getPluginInstance().debug(e, "Encountered exception whilst attempting to convert PetData to Ocelot.Type");
					}
				}
				
				if (pd == PetData.ANGRY) {
					((WolfPet) pet).setAngry(b);
				}
				
				if (pd == PetData.TAMED) {
					((WolfPet) pet).setTamed(b);
				}
				
				if (pd.isType(Type.PROF)) {
					Profession p = Profession.valueOf(pd.toString());
					if (p != null) {
						((VillagerPet) pet).setProfession(p);
					}
				}
				
				if (pd.isType(Type.COLOR) && (petType == PetType.SHEEP || petType == PetType.WOLF)) {
					String s = pd == PetData.LIGHTBLUE ? "LIGHT_BLUE" : pd.toString();
					try { 
						DyeColor dc = DyeColor.valueOf(s);
						if (dc != null) {
							if (petType == PetType.SHEEP) {
								((SheepPet) pet).setColor(dc);
							}
							else if (petType == PetType.WOLF) {
								((WolfPet) pet).setCollarColor(dc);
							}
						}
					} catch (Exception e) {
						EchoPet.getPluginInstance().debug(e, "Encountered exception whilst attempting to convert PetData to DyeColor");
					}
				}
				
				if (pd == PetData.WITHER) {
					((SkeletonPet) pet).setWither(b);
				}
				
				if (pd == PetData.VILLAGER) {
					if (petType == PetType.ZOMBIE) {
						((ZombiePet) pet).setVillager(b);
					}
					else if (petType == PetType.PIGZOMBIE) {
						((PigZombiePet) pet).setVillager(b);
					}
				}
				
				if (pd == PetData.FIRE) {
					((BlazePet) pet).setOnFire(b);
				}
				
				if (pd == PetData.SADDLE) {
					if (petType == PetType.PIG) {
						((PigPet) pet).setSaddle(b);
					}
					else if (petType == PetType.HORSE) {
						((HorsePet) pet).setSaddled(b);
					}
				}
				
				if (pd == PetData.SHEARED) {
					((SheepPet) pet).setSheared(b);
				}
				
				if (pd == PetData.SCREAMING) {
					((EndermanPet) pet).setScreaming(b);
				}
				
				if (pd == PetData.SHIELD) {
					((WitherPet) pet).setShielded(b);
				}
				
				
				if (petType == PetType.HORSE) {
					if (pd == PetData.CHESTED) {
						((HorsePet) pet).setChested(b);
					}
					
					if (pd.isType(Type.HORSE_TYPE)) {
						try { 
							HorseType h = HorseType.valueOf(pd.toString());
							if (h != null) {
								ht = h;
							}
						} catch (Exception e) {
							EchoPet.getPluginInstance().debug(e, "Encountered exception whilst attempting to convert PetData to Ocelot.Type");
						}
					}
					
					if (pd.isType(Type.HORSE_VARIANT)) {
						try { 
							HorseVariant v = HorseVariant.valueOf(pd.toString());
							if (v != null) {
								hv = v;
							}
						} catch (Exception e) {
							EchoPet.getPluginInstance().debug(e, "Encountered exception whilst attempting to convert PetData to Ocelot.Type");
						}
					}
					
					if (pd.isType(Type.HORSE_MARKING)) {
						try { 
							HorseMarking m;
							if (pd == PetData.WHITEPATCH) {
								m = HorseMarking.WHITE_PATCH;
							}
							else if (pd == PetData.WHITESPOT) {
								m = HorseMarking.WHITE_SPOTS;
							}
							else if (pd == PetData.BLACKSPOT) {
								m = HorseMarking.BLACK_SPOTS;
							}
							else {
								m = HorseMarking.valueOf(pd.toString());
							}
							if (m != null) {
								hm = m;
							}
						} catch (Exception e) {
							EchoPet.getPluginInstance().debug(e, "Encountered exception whilst attempting to convert PetData to Ocelot.Type");
						}
					}
					
					if (pd.isType(Type.HORSE_ARMOUR)) {
						try {
							if (pd == PetData.NOARMOUR) {
								ha = HorseArmour.NONE;
							}
							else {
								HorseArmour a = HorseArmour.valueOf(pd.toString());
								if (a != null) {
									ha = a;
								}
							}
						} catch(Exception e) {
							EchoPet.getPluginInstance().debug(e, "Encountered exception whilst attempting to convert PetData to Ocelot.Type");
						}
					}
				}
				
				list.add(pd);
			}
		}
		if (petType == PetType.HORSE) {
			HorsePet hp = (HorsePet) pet;
			hp.setHorseType(ht);
			hp.setVariant(hv, hm);
			hp.setArmour(ha);
		}
		
		if (b) {
			if (!list.isEmpty()) {
				for (PetData d : list) {
					if (pet.dataFalse.contains(d)) {
						pet.dataFalse.remove(d);
					}
					if (!pet.dataTrue.contains(d)) {
						pet.dataTrue.add(d);
					}
				}
				//this.updateFileData("autosave", pet, list, true);
			}
		}
		else {
			if (!list.isEmpty()) {
				for (PetData d : list) {
					if (pet.dataTrue.contains(d)) {
						pet.dataTrue.remove(d);
					}
					if (!pet.dataFalse.contains(d)) {
						pet.dataFalse.add(d);
					}
				}
				//this.updateFileData("autosave", pet, list, false);
			}
		}
	}
}