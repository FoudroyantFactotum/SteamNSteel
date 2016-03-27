package mod.steamnsteel.client.renderer.entity;

import mod.steamnsteel.TheMod;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class SteamNSteelLivingRender extends RenderLiving
{
    private static final String TEXTURE_FILE_EXTENSION = ".png";
    private static final String TEXTURE_LOCATION = "textures/models/";
    private final ResourceLocation texture;

    public SteamNSteelLivingRender(RenderManager rendermanagerIn, ModelBase modelbaseIn, String name, float shadowsizeIn)
    {
        super(rendermanagerIn, modelbaseIn, shadowsizeIn);
        this.texture = getResourceLocation(name);
    }

    public static ResourceLocation getResourceLocation(String name)
    {
        return new ResourceLocation(TheMod.MOD_ID.toLowerCase(), getTexturePath(name));
    }

    @SuppressWarnings("StringConcatenationMissingWhitespace")
    private static String getTexturePath(String name)
    {
        return TEXTURE_LOCATION + name + TEXTURE_FILE_EXTENSION;
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return texture;
    }
}
