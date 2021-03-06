package de.ellpeck.rockbottom.api.item;

import de.ellpeck.rockbottom.api.RockBottomAPI;
import de.ellpeck.rockbottom.api.assets.IAssetManager;
import de.ellpeck.rockbottom.api.assets.font.FormattingCode;
import de.ellpeck.rockbottom.api.util.reg.ResourceName;

import java.util.List;

public class BasicItemTool extends BasicItem {
    protected final int durability;

    public BasicItemTool(ResourceName name, int durability) {
        super(name);
        this.durability = durability;
        this.maxAmount = 1;
    }

    @Override
    public void describeItem(IAssetManager manager, ItemInstance instance, List<String> desc, boolean isAdvanced, boolean isRealItem) {
        if (isRealItem) {
            String condition = "info.durability.low";
            int highest = this.getHighestPossibleMeta() + 1;
            float percent = (float) (highest - instance.getMeta()) / highest;
            if (percent > 0.9) condition = "info.durability.high";
            else if (percent > 0.7) condition = "info.durability.medium_high";
            else if (percent > 0.4) condition = "info.durability.medium";
            else if (percent > 0.2) condition = "info.durability.medium_low";
            desc.add(RockBottomAPI.getGame().getAssetManager().localize(ResourceName.intern(condition)) + " " + FormattingCode.RESET_COLOR + instance.getDisplayName());
        } else super.describeItem(manager, instance, desc, isAdvanced, false);
    }

    @Override
    public boolean useMetaAsDurability() {
        return true;
    }

    @Override
    public int getHighestPossibleMeta() {
        return this.durability - 1;
    }
}
