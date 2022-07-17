package github.mrh0.beekeeping.group;

import github.mrh0.beekeeping.Beekeeping;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ItemGroup extends CreativeModeTab {
    public static ItemGroup BEES;

    public ItemGroup() {
        super(CreativeModeTab.TABS.length - 1, Beekeeping.MODID+":bees");
        BEES = this;
    }
    @Override
    public net.minecraft.world.item.ItemStack makeIcon() {
        return new ItemStack(Items.BEE_NEST);
    }
}
