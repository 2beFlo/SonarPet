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

package com.dsh105.echopet.compat.api.util.protocol.wrapper;

import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.compat.api.util.ReflectionUtil;
import com.dsh105.echopet.compat.api.util.protocol.Packet;
import com.dsh105.echopet.compat.api.util.protocol.PacketFactory;
import com.dsh105.echopet.compat.api.reflection.SafeMethod;

public class WrapperPacketPlayOutChat extends Packet {

    private static String field_a = ReflectionUtil.isServerMCPC() ? "field_73476_b" : "a";

    public WrapperPacketPlayOutChat() {
        super(PacketFactory.PacketType.CHAT);
    }

    public void setMessage(String chatComponent) {
        if (!EchoPet.isUsingNetty()) {
            if (!(chatComponent instanceof String)) {
                throw new IllegalArgumentException("Chat component for 1.6 chat packet must be a String!");
            }
        }
        this.write(field_a, new SafeMethod(ReflectionUtil.getNMSClass("ChatSerializer"), "a", String.class).invoke(null, chatComponent));
    }

    public String getMessage() {
        if (!EchoPet.isUsingNetty()) {
            return (String) this.read("message");
        }
        return (String) new SafeMethod(ReflectionUtil.getNMSClass("ChatSerializer"), "a", ReflectionUtil.getNMSClass("IChatBaseComponent")).invoke(null, this.read(field_a));
    }
}