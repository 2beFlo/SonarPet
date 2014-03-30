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

package io.github.dsh105.echopet.api;

import io.github.dsh105.echopet.api.entity.PetData;
import io.github.dsh105.echopet.api.entity.PetType;

import java.util.ArrayList;

public class UnorganisedPetData {

    public ArrayList<PetData> petDataList;
    public PetType petType;
    public String petName;

    public UnorganisedPetData(ArrayList<PetData> petDataList, PetType petType, String petName) {
        this.petDataList = petDataList;
        this.petType = petType;
        this.petName = petName;
    }
}
