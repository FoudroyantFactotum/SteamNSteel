package mod.steamnsteel.utility;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vec3;

public final class NBTUtility
{
    public static NBTTagCompound vec3ToNBT(final Vec3 v)
    {
        final NBTTagCompound nbt = new NBTTagCompound();

        nbt.setDouble("x", v.xCoord);
        nbt.setDouble("y", v.yCoord);
        nbt.setDouble("z", v.zCoord);

        return nbt;
    }

    public static Vec3 nbtToVec3(final NBTTagCompound nbt)
    {
        return new Vec3(
                nbt.getDouble("x"),
                nbt.getDouble("y"),
                nbt.getDouble("z")
        );
    }
}
