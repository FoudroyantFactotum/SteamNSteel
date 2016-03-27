package mod.steamnsteel.client.renderer.model;

import mod.steamnsteel.TheMod;
import net.minecraft.client.model.ModelBase;
import net.minecraft.util.ResourceLocation;

public class SteamNSteelEntityModel extends ModelBase
{
    private static final String MODEL_FILE_EXTENSION = ".obj";
    private static final String MODEL_LOCATION = "models/entity/";

    static ResourceLocation getResourceLocation(String name)
    {
        return new ResourceLocation(TheMod.MOD_ID.toLowerCase(), getModelPath(name));
    }

    @SuppressWarnings("StringConcatenationMissingWhitespace")
    private static String getModelPath(String name)
    {
        return MODEL_LOCATION + name + MODEL_FILE_EXTENSION;
    }
}
