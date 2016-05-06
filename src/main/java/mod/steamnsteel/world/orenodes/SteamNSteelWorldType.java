package mod.steamnsteel.world.orenodes;

import net.minecraft.world.WorldType;

public class SteamNSteelWorldType extends WorldType
{
    public static String NAME = "steamnsteel";
    public static OreSeamWorldData oreSeamWorldData = new OreSeamWorldData(NAME + ".SeamData");

    public SteamNSteelWorldType()
    {
        super(NAME);
    }
}
