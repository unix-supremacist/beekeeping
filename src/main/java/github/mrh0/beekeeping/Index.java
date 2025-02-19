package github.mrh0.beekeeping;

import dev.architectury.registry.registries.DeferredRegister;
import github.mrh0.beekeeping.bee.Specie;
import github.mrh0.beekeeping.bee.SpeciesRegistry;
import github.mrh0.beekeeping.bee.genes.Gene;
import github.mrh0.beekeeping.biome.BiomeTemperature;
import github.mrh0.beekeeping.blocks.analyzer.AnalyzerBlock;
import github.mrh0.beekeeping.blocks.analyzer.AnalyzerBlockEntity;
import github.mrh0.beekeeping.blocks.apiary.ApiaryBlock;
import github.mrh0.beekeeping.blocks.apiary.ApiaryBlockEntity;
import github.mrh0.beekeeping.blocks.beehive.BeehiveBlock;
import github.mrh0.beekeeping.config.Config;
import github.mrh0.beekeeping.group.ItemGroup;
import github.mrh0.beekeeping.recipe.BeeBreedingRecipe;
import github.mrh0.beekeeping.recipe.BeeProduceRecipe;
import github.mrh0.beekeeping.screen.analyzer.AnalyzerMenu;
import github.mrh0.beekeeping.screen.apiary.ApiaryMenu;
import io.github.fabricators_of_create.porting_lib.biome.BiomeDictionary;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.feature.Feature;

public class Index {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Beekeeping.MODID);
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Beekeeping.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Beekeeping.MODID);
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.CONTAINERS, Beekeeping.MODID);
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Beekeeping.MODID);

    static {
        species();
        blocks();
        items();
        blockEntities();
        menus();
        tags();
        recipes();
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
        BLOCK_ENTITIES.register(eventBus);
        MENUS.register(eventBus);
        SERIALIZERS.register(eventBus);
    }

    //  SPECIE
    public static void species() {
        var r = SpeciesRegistry.instance;
        r.register(new Specie("common", 0xFFfff2cc)
                .setProduce(Items.HONEYCOMB, 3, 5)
                .addBeehive(types -> types.contains(BiomeDictionary.Type.PLAINS),
                        Config.BEEHIVE_COMMON_TRIES.get(), Config.BEEHIVE_COMMON_RARITY.get()));

        r.register(new Specie("forest", 0xFF93c47d)
                .setProduce(Items.HONEYCOMB, 3, 5)
                .addBeehive(types -> types.contains(BiomeDictionary.Type.FOREST),
                        Config.BEEHIVE_FOREST_TRIES.get(), Config.BEEHIVE_FOREST_RARITY.get()));

        r.register(new Specie("tempered", 0xFFb6d7a8)
                .setProduce(Items.HONEYCOMB, 7, 9)
                .setTemperatureGene(Gene::random5Narrow)
                .breedFrom("common", "forest"));

        r.register(new Specie("tropical", 0xFF6aa84f)
                .setProduce(Items.HONEYCOMB, 5, 7)
                .addBeehive(types -> types.contains(BiomeDictionary.Type.JUNGLE),
                        Config.BEEHIVE_TROPICAL_TRIES.get(), Config.BEEHIVE_TROPICAL_RARITY.get())
                .setLifetimeGene(Gene::random5Narrow)
                .setPreferredTemperature(BiomeTemperature.WARM));

        r.register(new Specie("coco", 0xFF783f04)
                .setProduce(Items.HONEYCOMB, 5, 7, Items.COCOA_BEANS, 3, 7)
                .setPreferredTemperature(BiomeTemperature.WARM)
                .breedFrom("tropical", "dugout"));

        r.register(new Specie("upland", 0xFFff9900)
                .setProduce(Items.HONEYCOMB, 3, 7, Items.HONEY_BLOCK, 1, 2)
                .addBeehive(types -> types.contains(BiomeDictionary.Type.SAVANNA),
                        Config.BEEHIVE_UPLAND_TRIES.get(), Config.BEEHIVE_UPLAND_RARITY.get())
                .setLifetimeGene(Gene::random5Narrow)
                .setWeatherGene(Gene::strict)
                .setPreferredTemperature(BiomeTemperature.WARMEST));

        r.register(new Specie("dune", 0xFFfbbc04)
                .setProduce(Items.HONEYCOMB, 5, 7)
                .addBeehive(types -> types.contains(BiomeDictionary.Type.SANDY) && types.contains(BiomeDictionary.Type.HOT),
                        Config.BEEHIVE_DUNE_TRIES.get(), Config.BEEHIVE_DUNE_RARITY.get())
                .setLifetimeGene(Gene::random5Narrow)
                .setWeatherGene(Gene::strict)
                .setPreferredTemperature(BiomeTemperature.WARMEST));

        r.register(new Specie("snowy", 0xFFefefef)
                .setProduce(Items.HONEYCOMB, 5, 7, Items.SNOWBALL, 8, 16)
                .addBeehive(types -> types.contains(BiomeDictionary.Type.SNOWY),
                        Config.BEEHIVE_SNOWY_TRIES.get(), Config.BEEHIVE_SNOWY_RARITY.get())
                .setTemperatureGene(Gene::random3Low)
                .setPreferredTemperature(BiomeTemperature.COLD));

        r.register(new Specie("frozen", 0xFFd0e0e3)
                .setProduce(Items.HONEYCOMB, 5, 7, Items.ICE, 3, 7)
                .setPreferredTemperature(BiomeTemperature.COLD)
                .breedFrom("snowy", "dugout"));

        r.register(new Specie("glacial", 0xFFa2c4c9)
                .setFoil()
                .setProduce(Items.HONEYCOMB, 5, 7, Items.PACKED_ICE, 3, 7)
                .setPreferredTemperature(BiomeTemperature.COLDEST)
                .breedFrom("frozen", "nocturnal"));

        r.register(new Specie("fungal", 0xFF660000)
                .setProduce(Items.HONEYCOMB, 5, 7, Items.RED_MUSHROOM, 0.5d, 0.8d)
                .addBeehive(types -> types.contains(BiomeDictionary.Type.MUSHROOM) || types.contains(BiomeDictionary.Type.SWAMP),
                        Config.BEEHIVE_FUNGAL_TRIES.get(), Config.BEEHIVE_FUNGAL_RARITY.get())
                .setTemperatureGene(Gene::random3High)
                .setProduceGene(Gene::random5High));

        r.register(new Specie("mossy", 0xFF38761d)
                .setProduce(Items.HONEYCOMB, 5, 7, Items.MOSS_BLOCK, 0.5d, 0.8d)
                .setTemperatureGene(Gene::random3Low)
                .setProduceGene(Gene::normal5)
                .breedFrom("dugout", "fungal"));

        r.register(new Specie("fair", 0xFF00ff00)
                .setProduce(Items.HONEYCOMB, 5, 7, Items.SUGAR, 8, 16)
                .breedFrom("coco", "snowy")
                .setFoil());

        r.register(new Specie("dugout", 0xFF7f6000)
                .setProduce(Items.HONEYCOMB, 5, 7)
                .addBeehive(types -> types.contains(BiomeDictionary.Type.UNDERGROUND),
                        Config.BEEHIVE_DUGOUT_TRIES.get(), Config.BEEHIVE_DUGOUT_RARITY.get(), PlacementUtils.FULL_RANGE, Feature.RANDOM_PATCH,
                        pos -> pos.getY() < Config.BEEHIVE_DUGOUT_MAX_HEIGHT.get() && pos.getY() > Config.BEEHIVE_DUGOUT_MIN_HEIGHT.get())
                .setTemperatureGene(Gene::random3High)
                .setLightGene(Gene::any)
                .setPreferredTemperature(BiomeTemperature.COLD));

        r.register(new Specie("nocturnal", 0xFF073763)
                .setProduce(Items.HONEYCOMB, 5, 7, Items.GLOWSTONE_DUST, 0.5d, 0.8d)
                .setNocturnal()
                .breedFrom("ender", "dugout"));

        r.register(new Specie("malignant", 0xFF999999)
                .setProduce(Items.HONEYCOMB, 5, 7, Items.BONE_MEAL, 3, 7)
                .addBeehive(types -> types.contains(BiomeDictionary.Type.MESA) || types.contains(BiomeDictionary.Type.WASTELAND), Config.BEEHIVE_MALIGNANT_TRIES.get(), Config.BEEHIVE_MALIGNANT_RARITY.get())
                .setPreferredTemperature(BiomeTemperature.WARM)
                .setDark());

        r.register(new Specie("wicked", 0xFF666666)
                .setProduce(Items.HONEYCOMB, 5, 7, Items.SPIDER_EYE, 1, 3)
                .setPreferredTemperature(BiomeTemperature.WARM)
                .setDark()
                .breedFrom("malignant", "upland"));

        r.register(new Specie("withered", 0xFF434343)
                .setProduce(Items.HONEYCOMB, 5, 7, Items.WITHER_SKELETON_SKULL, 0.02, 0.05)
                .setPreferredTemperature(BiomeTemperature.WARM)
                .setDark()
                .setFoil()
                .breedFrom("wicked", "demonic"));

        r.register(new Specie("scorched", 0xFFff9900)
                .setProduce(Items.HONEYCOMB, 5, 7, Items.COAL, 2, 5)
                .addBeehive(types -> types.contains(BiomeDictionary.Type.NETHER),
                        Config.BEEHIVE_SCORCHED_TRIES.get(), Config.BEEHIVE_SCORCHED_RARITY.get(),
                        PlacementUtils.FULL_RANGE, Feature.RANDOM_PATCH,
                        pos -> pos.getY() < Config.BEEHIVE_SCORCHED_MAX_HEIGHT.get() && pos.getY() > Config.BEEHIVE_SCORCHED_MIN_HEIGHT.get())
                .setPreferredTemperature(BiomeTemperature.WARMEST)
                .setLightGene(Gene::any)
                .setDark());

        r.register(new Specie("magmatic", 0xFFff6d01)
                .setProduce(Items.HONEYCOMB, 5, 7, Items.MAGMA_CREAM, 0.5d, 0.8d)
                .setPreferredTemperature(BiomeTemperature.WARMEST)
                .setLightGene(Gene::any)
                .setDark()
                .breedFrom("scorched", "dune"));

        r.register(new Specie("infernal", 0xFFff0000)
                .setProduce(Items.HONEYCOMB, 5, 7, Items.GUNPOWDER, 1, 3)
                .setPreferredTemperature(BiomeTemperature.WARMEST)
                .setLightGene(Gene::any)
                .setDark()
                .breedFrom("magmatic", "wicked"));

        r.register(new Specie("demonic", 0xFF990000)
                .setProduce(Items.HONEYCOMB, 5, 7, Items.BLAZE_POWDER, 0.5d, 0.8d)
                .setPreferredTemperature(BiomeTemperature.WARMEST)
                .setLightGene(Gene::any)
                .setFoil()
                .setDark()
                .breedFrom("infernal", "nocturnal"));

        r.register(new Specie("ender", 0xFF134f5c)
                .setProduce(Items.HONEYCOMB, 5, 7, Items.ENDER_PEARL, 0.2d, 0.4d)
                .addBeehive(types -> types.contains(BiomeDictionary.Type.END), Config.BEEHIVE_ENDER_TRIES.get(), Config.BEEHIVE_ENDER_RARITY.get())
                .setPreferredTemperature(BiomeTemperature.COLD)
                .setLightGene(Gene::any)
                .setDark());
    }

    //  ITEM
    public static void items() {
        for(Specie specie : SpeciesRegistry.instance.getAll()) {
            ITEMS.register(specie.getName() + "_drone", () -> specie.buildDroneItem());
            ITEMS.register(specie.getName() + "_princess", () -> specie.buildPrincessItem());
            ITEMS.register(specie.getName() + "_queen", () -> specie.buildQueenItem());
        }
    }

    //  BLOCK
    public static RegistryObject<AnalyzerBlock> ANALYZER_BLOCK;
    public static RegistryObject<ApiaryBlock> APIARY_BLOCK;

    public static void blocks() {
        ANALYZER_BLOCK = BLOCKS.register("analyzer", () -> new AnalyzerBlock());
        ITEMS.register("analyzer", () -> new BlockItem(ANALYZER_BLOCK.get(), new Item.Properties().tab(ItemGroup.BEES)));
        APIARY_BLOCK = BLOCKS.register("apiary", () -> new ApiaryBlock());
        ITEMS.register("apiary", () -> new BlockItem(APIARY_BLOCK.get(), new Item.Properties().tab(ItemGroup.BEES)));

        for(Specie specie : SpeciesRegistry.instance.getAll()) {
            if(!specie.hasBeehive())
                continue;
            specie.beehive.block = BLOCKS.register(specie.beehive.getName(), () -> new BeehiveBlock(BlockBehaviour.Properties.of(Blocks.BEEHIVE.defaultBlockState().getMaterial()), specie));
            ITEMS.register(specie.beehive.getName(), () -> new BlockItem(specie.beehive.block.get(), new Item.Properties().tab(ItemGroup.BEES)));
        }
    }

    //  BLOCK ENTITY
    public static RegistryObject<BlockEntityType<AnalyzerBlockEntity>> ANALYZER_BLOCK_ENTITY;
    public static RegistryObject<BlockEntityType<ApiaryBlockEntity>> APIARY_BLOCK_ENTITY;

    public static void blockEntities() {
        ANALYZER_BLOCK_ENTITY = BLOCK_ENTITIES.register("analyzer_block_entity", () ->
                BlockEntityType.Builder.of(AnalyzerBlockEntity::new, ANALYZER_BLOCK.get()).build(null));
        APIARY_BLOCK_ENTITY = BLOCK_ENTITIES.register("apiary_block_entity", () ->
                BlockEntityType.Builder.of(ApiaryBlockEntity::new, APIARY_BLOCK.get()).build(null));
    }

    //   MENU
    public static RegistryObject<MenuType<AnalyzerMenu>> ANALYZER_MENU;
    public static RegistryObject<MenuType<ApiaryMenu>> APIARY_MENU;

    private static <T extends AbstractContainerMenu>RegistryObject<MenuType<T>> registerMenuType(IContainerFactory<T> factory, String name) {
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }

    public static void menus() {
        ANALYZER_MENU = registerMenuType(AnalyzerMenu::new, "analyzer");
        APIARY_MENU = registerMenuType(ApiaryMenu::new, "apiary");
    }

    //  TAG
    public static TagKey<Item> BEES_TAG;
    public static TagKey<Item> DRONE_BEES_TAG;
    public static TagKey<Item> PRINCESS_BEES_TAG;
    public static TagKey<Item> QUEEN_BEES_TAG;
    public static TagKey<Block> BEEHIVE_TAG;

    public static void tags() {
        BEES_TAG = ItemTags.create(new ResourceLocation("beekeeping", "bees"));
        DRONE_BEES_TAG = ItemTags.create(new ResourceLocation("beekeeping", "drone_bees"));
        PRINCESS_BEES_TAG = ItemTags.create(new ResourceLocation("beekeeping", "princess_bees"));
        QUEEN_BEES_TAG = ItemTags.create(new ResourceLocation("beekeeping", "queen_bees"));
        BEEHIVE_TAG = BlockTags.create(new ResourceLocation("beekeeping", "beehives"));
    }

    //  RECIPE
    public static RegistryObject<RecipeSerializer<BeeBreedingRecipe>> BEE_BREEDING_RECIPE;
    public static RegistryObject<RecipeSerializer<BeeProduceRecipe>> BEE_PRODUCE_RECIPE;

    public static void recipes() {
        BEE_BREEDING_RECIPE = SERIALIZERS.register("bee_breeding", () -> BeeBreedingRecipe.Serializer.INSTANCE);
        BEE_PRODUCE_RECIPE = SERIALIZERS.register("bee_produce", () -> BeeProduceRecipe.Serializer.INSTANCE);
    }
}
