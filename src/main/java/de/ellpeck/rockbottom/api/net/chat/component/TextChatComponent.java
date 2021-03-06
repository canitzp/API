/*
 * This file ("ChatComponentText.java") is part of the RockBottomAPI by Ellpeck.
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

package de.ellpeck.rockbottom.api.net.chat.component;

import de.ellpeck.rockbottom.api.IGameInstance;
import de.ellpeck.rockbottom.api.assets.IAssetManager;
import de.ellpeck.rockbottom.api.data.set.DataSet;

public class TextChatComponent extends ChatComponent {

    private String text;

    public TextChatComponent() {
    }

    public TextChatComponent(String text) {
        this.text = text;
    }

    @Override
    public String getDisplayString(IGameInstance game, IAssetManager manager) {
        return this.text;
    }

    @Override
    public String getUnformattedString() {
        return this.text;
    }

    @Override
    public void save(DataSet set) {
        super.save(set);
        set.addString("text", this.text);
    }

    @Override
    public void load(DataSet set) {
        super.load(set);
        this.text = set.getString("text");
    }
}
