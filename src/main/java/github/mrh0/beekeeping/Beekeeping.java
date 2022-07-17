package github.mrh0.beekeeping;

import com.mojang.logging.LogUtils;
import github.mrh0.beekeeping.config.Config;
import github.mrh0.beekeeping.group.ItemGroup;
import github.mrh0.beekeeping.network.TogglePacket;
import github.mrh0.beekeeping.screen.analyzer.AnalyzerScreen;
import github.mrh0.beekeeping.screen.apiary.ApiaryScreen;
import me.pepperbell.simplenetworking.SimpleChannel;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.slf4j.Logger;

@Mod(Beekeeping.MODID)
public class Beekeeping {
    public static final String MODID = "beekeeping";
    public static final Logger LOGGER = LogUtils.getLogger();

    private static final String PROTOCOL = "1";
    public static final SimpleChannel NETWORK = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(MODID, "main"))
            .clientAcceptedVersions(PROTOCOL::equals)
            .serverAcceptedVersions(PROTOCOL::equals)
            .networkProtocolVersion(() -> PROTOCOL)
            .simpleChannel();

    public Beekeeping() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        new ItemGroup();
        Index.register(eventBus);

        eventBus.addListener(this::setup);
        eventBus.addListener(this::clientSetup);
        eventBus.addListener(this::postInit);
        MinecraftForge.EVENT_BUS.register(this);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);
        Config.loadConfig(Config.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve("beekeeping-common.toml"));
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("Beekeeping Init!");
    }

    public void postInit(FMLLoadCompleteEvent evt) {
        int i = 0;
        NETWORK.registerMessage(i++, TogglePacket.class, TogglePacket::encode, TogglePacket::decode, TogglePacket::handle);
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        MenuScreens.register(Index.ANALYZER_MENU.get(), AnalyzerScreen::new);
        MenuScreens.register(Index.APIARY_MENU.get(), ApiaryScreen::new);
    }

    public static ResourceLocation get(String resource) {
        return new ResourceLocation(Beekeeping.MODID, resource);
    }
}
