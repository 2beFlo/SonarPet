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

package io.github.dsh105.echopet.menu.main;

import com.dsh105.dshutils.util.EnumUtil;
import com.dsh105.dshutils.util.StringUtil;
import io.github.dsh105.echopet.EchoPetPlugin;
import io.github.dsh105.echopet.api.event.PetMenuOpenEvent;
import io.github.dsh105.echopet.api.entity.pet.Pet;
import io.github.dsh105.echopet.api.entity.PetData;
import io.github.dsh105.echopet.menu.Menu;
import io.github.dsh105.echopet.util.Lang;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;


public class PetMenu implements Menu {

    Inventory inv;
    private int size;
    private Pet pet;
    private ArrayList<MenuOption> options = new ArrayList<MenuOption>();

    public PetMenu(Pet pet, ArrayList<MenuOption> options, int size) {
        this.pet = pet;
        this.size = size;
        this.inv = Bukkit.createInventory(pet.getOwner(), size, "EchoPet DataMenu");
        this.options = options;
        for (MenuOption o : this.options) {
            if (o.item.getMenuType() == DataMenu.DataMenuType.BOOLEAN) {
                MenuItem mi = o.item;
                if (EnumUtil.isEnumType(PetData.class, mi.toString())) {
                    PetData pd = PetData.valueOf(mi.toString());
                    if (pet.getPetData().contains(pd)) {
                        this.inv.setItem(o.position, o.item.getBoolean(false));
                    } else {
                        this.inv.setItem(o.position, o.item.getBoolean(true));
                    }
                } else {
                    if (mi.toString().equals("HAT")) {
                        if (pet.isHat()) {
                            this.inv.setItem(o.position, o.item.getBoolean(false));
                        } else {
                            this.inv.setItem(o.position, o.item.getBoolean(true));
                        }
                    }
                    if (mi.toString().equals("RIDE")) {
                        if (pet.isOwnerRiding()) {
                            this.inv.setItem(o.position, o.item.getBoolean(false));
                        } else {
                            this.inv.setItem(o.position, o.item.getBoolean(true));
                        }
                    }
                }
            } else {
                this.inv.setItem(o.position, o.item.getItem());
            }
        }
        int book = size - 1;
        this.inv.setItem(book, DataMenuItem.CLOSE.getItem());
    }

    @Override
    public void open(boolean sendMessage) {
        PetMenuOpenEvent menuEvent = new PetMenuOpenEvent(this.pet.getOwner(), PetMenuOpenEvent.MenuType.MAIN);
        EchoPetPlugin.getInstance().getServer().getPluginManager().callEvent(menuEvent);
        if (menuEvent.isCancelled()) {
            return;
        }
        this.pet.getOwner().openInventory(this.inv);
        if (sendMessage) {
            Lang.sendTo(this.pet.getOwner(), Lang.OPEN_MENU.toString().replace("%type%", StringUtil.capitalise(this.pet.getPetType().toString().replace("_", " "))));
        }
    }
}
