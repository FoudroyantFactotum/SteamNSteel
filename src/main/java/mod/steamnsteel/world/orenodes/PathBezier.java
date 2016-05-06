package mod.steamnsteel.world.orenodes;

import mod.steamnsteel.utility.BezierCurve;
import mod.steamnsteel.utility.NBTUtility;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vec3;

import java.util.Arrays;

public class PathBezier extends Path
{
    public static final String[] PATH_POINTS = {"p0","p1","p2","p3"};

    private final Vec3[] controlPoints;

    public PathBezier(final Vec3 p0, final Vec3 p1, final  Vec3 p2, final Vec3 p3)
    {
        controlPoints = new Vec3[]{p0, p1, p2, p3};
    }

    public PathBezier(final NBTTagCompound nbt)
    {
        controlPoints = new Vec3[] {
                NBTUtility.nbtToVec3(nbt.getCompoundTag(PATH_POINTS[0])),
                NBTUtility.nbtToVec3(nbt.getCompoundTag(PATH_POINTS[1])),
                NBTUtility.nbtToVec3(nbt.getCompoundTag(PATH_POINTS[2])),
                NBTUtility.nbtToVec3(nbt.getCompoundTag(PATH_POINTS[3]))
        };
    }

    @Override
    public void writePathFromNBT(final NBTTagCompound nbt)
    {
        nbt.setTag(PATH_POINTS[0], NBTUtility.vec3ToNBT(controlPoints[0]));
        nbt.setTag(PATH_POINTS[1], NBTUtility.vec3ToNBT(controlPoints[1]));
        nbt.setTag(PATH_POINTS[2], NBTUtility.vec3ToNBT(controlPoints[2]));
        nbt.setTag(PATH_POINTS[3], NBTUtility.vec3ToNBT(controlPoints[3]));
    }

    @Override
    public void getPosition(final double t, final double[] pos)
    {
        BezierCurve.get3DPointOnCurve(controlPoints, t, pos);
    }

    @Override
    public double getThicknessAt(final double t)
    {
        return 1;
    }

    public Vec3 getControlPointP0()
    {
        return controlPoints[0];
    }

    public Vec3 getControlPointP1()
    {
        return controlPoints[1];
    }

    public Vec3 getControlPointP2()
    {
        return controlPoints[2];
    }

    public Vec3 getControlPointP3()
    {
        return controlPoints[3];
    }

    @Override
    public String toString()
    {
        return Arrays.toString(controlPoints);
    }
}
