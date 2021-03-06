/*
 * This file ("ConstructionRecipe.java") is part of the RockBottomAPI by Ellpeck.
 * View the source code at <https://github.com/RockBottomGame/>.
 * View information on the project at <https://rockbottom.ellpeck.de/>.
 *
 * The RockBottomAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The RockBottomAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with the RockBottomAPI. If not, see <http://www.gnu.org/licenses/>.
 *
 * © 2018 Ellpeck
 */

package de.ellpeck.rockbottom.api.construction.compendium;

import de.ellpeck.rockbottom.api.Registries;
import de.ellpeck.rockbottom.api.RockBottomAPI;
import de.ellpeck.rockbottom.api.construction.ConstructionTool;
import de.ellpeck.rockbottom.api.construction.resource.IUseInfo;
import de.ellpeck.rockbottom.api.entity.player.AbstractPlayerEntity;
import de.ellpeck.rockbottom.api.gui.Gui;
import de.ellpeck.rockbottom.api.gui.component.construction.ConstructComponent;
import de.ellpeck.rockbottom.api.inventory.Inventory;
import de.ellpeck.rockbottom.api.item.ItemInstance;
import de.ellpeck.rockbottom.api.tile.entity.IToolStation;
import de.ellpeck.rockbottom.api.tile.entity.TileEntity;
import de.ellpeck.rockbottom.api.util.reg.ResourceName;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class ConstructionRecipe extends PlayerCompendiumRecipe {

    public static final ResourceName ID = ResourceName.intern("recipe");

    protected final List<IUseInfo> inputs;
    protected final List<ItemInstance> outputs;
    protected final List<ConstructionTool> tools;
    protected final boolean manualOnly;

    public ConstructionRecipe(ResourceName name, List<ConstructionTool> tools, List<IUseInfo> inputs, List<ItemInstance> outputs, boolean manualOnly, boolean isKnowledge, float skillReward) {
        super(name, isKnowledge, skillReward);
        this.inputs = inputs;
        this.outputs = outputs;
        this.tools = tools;
        this.manualOnly = manualOnly;
    }

    public ConstructionRecipe(ResourceName name, List<ConstructionTool> tools, boolean isKnowledge, float skillReward, ItemInstance output, IUseInfo... inputs) {
        this(name, tools, Arrays.asList(inputs), Collections.singletonList(output), false, isKnowledge, skillReward);
    }

    public ConstructionRecipe(List<ConstructionTool> tools, boolean isKnowledge, float skillReward, ItemInstance output, IUseInfo... inputs) {
        this(output.getItem().getName(), tools, isKnowledge, skillReward, output, inputs);
    }

    public static ConstructionRecipe forName(ResourceName name) {
        return Registries.CONSTRUCTION_RECIPES.get(name);
    }

    @Override
    public List<IUseInfo> getInputs() {
        return this.inputs;
    }

    @Override
    public List<ItemInstance> getOutputs() {
        return this.outputs;
    }

    public List<ConstructionTool> getTools() {
        return this.tools;
    }

    public boolean usesTools() {
        return this.tools != null && this.tools.size() > 0;
    }

    public boolean canUseTools(IToolStation machine) {
        if (this.usesTools()) {
            if (machine == null)
                return false;
            for (ConstructionTool tool : this.tools) {
                if (!machine.damageTool(tool, true)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public ConstructComponent getConstructButton(Gui gui, AbstractPlayerEntity player, TileEntity machine, boolean canConstruct) {
        return new ConstructComponent(gui, this, this.canUseTools((IToolStation) machine), canConstruct, this.usesTools() && machine == null ? null : () -> {
            RockBottomAPI.getApiHandler().defaultConstruct(player, this, machine);
            return true;
        });
    }

    @Override
    public boolean handleRecipe(AbstractPlayerEntity player, Inventory inputInventory, Inventory outputInventory, TileEntity machine, List<IUseInfo> recipeInputs, List<ItemInstance> ingredients, Function<List<ItemInstance>, List<ItemInstance>> outputGetter, float skillReward) {
        if (this.usesTools()) {
            if (!(machine instanceof IToolStation) || !this.canUseTools((IToolStation) machine)) {
                return false;
            }
            for (ConstructionTool tool : tools) {
                ((IToolStation) machine).damageTool(tool, false);
            }
        }
        return true;
    }

    public boolean showInConstructionTable() {
        return !this.manualOnly;
    }

    public ConstructionRecipe registerManual() {
        if (this.tools != null && this.tools.size() > 0) {
            RockBottomAPI.logger().warning("Registered manual recipe " + this.getName() + " with " + this.getTools().size() + "tools! This should be marked as a construction table recipe.");
        }
        Registries.MANUAL_CONSTRUCTION_RECIPES.register(this.getName(), this);
        return this;
    }

    public ConstructionRecipe registerConstructionTable() {
        if (this.tools == null || this.tools.isEmpty()) {
            RockBottomAPI.logger().warning("Registered construction table recipe " + this.getName() + " with no tools! This should be marked as a manual recipe.");
        }
        Registries.CONSTRUCTION_TABLE_RECIPES.register(this.getName(), this);
        return this;
    }

    @Override
    public String toString() {
        return "ConstructionRecipe{" +
                "infoName=" + this.infoName +
                ", isKnowledge=" + this.isKnowledge +
                ", tools=" + this.tools +
                ", inputs=" + this.inputs +
                ", outputs=" + this.outputs +
                ", skillReward=" + this.skillReward +
                '}';
    }
}
