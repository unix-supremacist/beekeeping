package github.mrh0.beekeeping.world;

import github.mrh0.beekeeping.Beekeeping;
import github.mrh0.beekeeping.config.Config;
import github.mrh0.beekeeping.world.gen.BeehiveGeneration;

@Mod.EventBusSubscriber(modid = Beekeeping.MODID)
public class BeekeepingWorld {
    @SubscribeEvent
    public static void biomeLoadingEvent(final BiomeLoadingEvent event) {
        if(Config.BEEHIVE_GENERATION_ENABLED.get())
            BeehiveGeneration.generateSurfaceBeehives(event);
    }
}