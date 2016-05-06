package mod.steamnsteel.world.orenodes;

import gnu.trove.map.hash.TIntObjectHashMap;
import mod.steamnsteel.utility.SteamNSteelException;
import net.minecraft.nbt.NBTTagCompound;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;

public abstract class Path
{
    public static final String TYPE_ID = "pathType";
    static {
        PathType.createMapable();
    }

    public enum PathType
    {
        Bezier(0, PathBezier.class);

        int id;
        Class<? extends Path> clazz;

        static final TIntObjectHashMap<PathType> idToPathType = new TIntObjectHashMap<>();
        static final Hashtable<Class<? extends Path>, PathType> clazzToPathType = new Hashtable<>();
        static final Hashtable<Class<? extends Path>, Constructor<? extends Path>> clazzToNBTConstructor = new Hashtable<>();

        static void createMapable()
        {
             PathType lastRead = null;
            try
            {
                for (final PathType pt : PathType.values())
                {
                    lastRead = pt;
                    final Constructor<? extends Path> nbtConstructor = pt.getClazz().getConstructor(NBTTagCompound.class);
                    nbtConstructor.setAccessible(true);

                    clazzToNBTConstructor.put(pt.getClazz(), nbtConstructor);
                    idToPathType.put(pt.getID(), pt);
                    clazzToPathType.put(pt.getClazz(), pt);
                }
            } catch (NoSuchMethodException e)
            {
                throw new SteamNSteelException("Where did the constructor for NBTTag reading on " + lastRead + " go?", e);
            }
        }

        PathType(int id, Class<? extends Path> clazz)
        {
            this.id = id;
            this.clazz = clazz;
        }

        public int getID()
        {
            return id;
        }

        public Class<? extends Path> getClazz()
        {
            return clazz;
        }
    }

    public abstract void getPosition(final double t, final double[] pos);

    public abstract double getThicknessAt(final double t);

    public abstract void writePathFromNBT(final NBTTagCompound nbt);

    //todo date and translate subSystem
    public static Path readFromNBT(final NBTTagCompound nbt)
    {
        try
        {
            final Class<? extends Path> clazz = PathType.idToPathType.get(nbt.getInteger(TYPE_ID)).getClazz();
            final Constructor<? extends Path> constructor = PathType.clazzToNBTConstructor.get(clazz);

            return constructor.newInstance(nbt);

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e)
        {
            throw new SteamNSteelException("Bad class in Path", e);
        }
    }

    public void writeToNBT(final NBTTagCompound nbt)
    {
        nbt.setInteger(TYPE_ID, PathType.clazzToPathType.get(getClass()).getID());

        writePathFromNBT(nbt);
    }
}
