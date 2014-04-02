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

package io.github.dsh105.echopet.compat.api.util.protocol.wrapper;

import io.github.dsh105.echopet.compat.api.plugin.EchoPet;
import io.github.dsh105.echopet.compat.api.util.ReflectionUtil;
import io.github.dsh105.echopet.compat.api.util.protocol.Packet;
import io.github.dsh105.echopet.compat.api.util.protocol.PacketFactory;
import io.github.dsh105.echopet.compat.api.util.reflection.SafeMethod;

public class WrapperPacketPlayOutChat extends Packet {

    public WrapperPacketPlayOutChat() {
        super(PacketFactory.PacketType.CHAT);
    }

    public void setMessage(String chatComponent) {
        if (!EchoPet.isUsingNetty()) {
            if (!(chatComponent instanceof String)) {
                throw new IllegalArgumentException("Chat component for 1.6 chat packet must be a String!");
            }
        }
        this.write("a", new SafeMethod(ReflectionUtil.getNMSClass("ChatSerializer"), "a", String.class).invoke(null, chatComponent));
    }

    public String getMessage() {
        if (!EchoPet.isUsingNetty()) {
            return (String) this.read("message");
        }
        return (String) new SafeMethod(ReflectionUtil.getNMSClass("ChatSerializer"), "a", ReflectionUtil.getNMSClass("IChatBaseComponent")).invoke(null, this.read("a"));
    }
}