package github.mrh0.beekeeping.datagen;

import github.mrh0.beekeeping.Beekeeping;
import github.mrh0.beekeeping.Util;
import github.mrh0.beekeeping.bee.Specie;
import github.mrh0.beekeeping.bee.SpeciesRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockStateGenerator extends BlockStateProvider {
    public BlockStateGenerator(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Beekeeping.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        for(Specie specie : SpeciesRegistry.instance.getAll()) {
            if(specie.hasBeehive()) {
                simpleBlock(specie.beehive.block.get(), models().cubeBottomTop(specie.getName() + "_beehive",
                        Beekeeping.get("block/beehives/" + specie.getName() + "_side"),
                        Beekeeping.get("block/beehives/" + specie.getName() + "_bottom"),
                        Beekeeping.get("block/beehives/" + specie.getName() + "_top")
                ));
                System.out.println("\"block.beekeeping." + specie.getName() + "_beehive\":\"" + Util.capitalize(specie.getName()) + " Beehive\",");
            }
        }
    }
}