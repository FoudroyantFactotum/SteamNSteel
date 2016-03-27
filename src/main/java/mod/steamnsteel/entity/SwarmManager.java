package mod.steamnsteel.entity;

import com.google.gson.reflect.TypeToken;
import mod.steamnsteel.utility.position.ChunkCoord;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

import java.util.*;

public class SwarmManager extends WorldSavedData
{
    public static final Map<World, SwarmManager> swarmManagers = new HashMap<>();

    public static final String SWARMS = "Swarms";
    public static final String SWARM_TYPE = "SwarmType";
    public static final String SWARM_DATA = "Data";

    private static final int NBT_TAG_TYPE_COMPOUND = 10;

    private World world;
    private int tickCounter;
    private final List<Swarm<? extends ISwarmer>> swarmList = new ArrayList<Swarm<? extends ISwarmer>>();

    public SwarmManager(String string)
    {
        super(string);
    }

    public SwarmManager(World world)
    {
        this("swarms");
        this.world = world;

        markDirty();
    }

    public <T extends EntityLiving & ISwarmer> void addSwarm(Swarm<T> swarm)
    {
        swarmList.add(swarm);
    }

    public void setWorld(World world)
    {
        this.world = world;

        for (final Swarm swarm : swarmList)
        {
            swarm.setWorld(world);
        }
    }

    public <T extends EntityLiving & ISwarmer> Swarm<T> getSwarmAt(ChunkCoord chunk, Class<T> clazz)
    {
        for (Swarm swarm : swarmList)
        {
            if (swarm.entityClass == clazz && swarm.getHomeChunkCoord().equals(chunk))
            {
                return swarm;
            }
        }

        return null;
    }

    public void tick()
    {
        world.theProfiler.startSection("swarmUpdate");
        tickCounter++;

        final Iterator<Swarm<? extends ISwarmer>> iterator = swarmList.iterator();

        while (iterator.hasNext())
        {
            final Swarm swarm = iterator.next();

            world.theProfiler.startSection("swarmX" + swarm.getHomeChunkCoord().getX() + ":Z" + swarm.getHomeChunkCoord().getZ());

            swarm.update(tickCounter);

            if (!swarm.isValid())
            {
                iterator.remove();
            }

            world.theProfiler.endSection();
        }

        world.theProfiler.endSection();

        //Mark for save every 30 seconds
        if (tickCounter % 600 == 0)
        {
            markDirty();
        }
    }

    /**
     * Returns the nearest {@link mod.steamnsteel.entity.Swarm} for the provided entity and it's class.
     * @param entity The entity
     * @param clazz The class
     * @param maxDistance Max distance the swarm can be away
     * @param <T> The type
     * @return The swarm
     */
    @SuppressWarnings("unchecked")
    public <T extends EntityLiving & ISwarmer> Swarm<T> getNearestSwarmToEntity(T entity, Class<T> clazz, float maxDistance)
    {
        double distance = Float.MAX_VALUE;
        Swarm<T> nearestSwarm = null;

        for (Swarm<? extends ISwarmer> swarm : swarmList)
        {
            //Has to exact match
            if (swarm.entityClass == clazz)
            {
                final ChunkCoord coord = swarm.getHomeChunkCoord();
                final double dis = entity.getDistanceSq(coord.getX()*16 + 8, swarm.getHomeBlockCoord().getY(), coord.getZ()*16 + 8);

                if ((nearestSwarm == null || dis < distance) && dis <= maxDistance)
                {
                    nearestSwarm = (Swarm<T>) swarm;
                    distance = dis;
                }
            }
        }

        return nearestSwarm;
    }

    public <T extends EntityLiving & ISwarmer> Swarm<T> getSwarmForClass(Class<T> entityClass)
    {
        //TODO remove hardcode? subclasses?
        if (entityClass == SteamSpiderEntity.class)
        {
            return new Swarm<>(world, entityClass);
        }

        return null;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound)
    {
        //Load swarm list
        final NBTTagList nbttaglist = nbtTagCompound.getTagList(SWARMS, NBT_TAG_TYPE_COMPOUND);

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            final NBTTagCompound tagCompound = nbttaglist.getCompoundTagAt(i);

            final SwarmType swarmType = SwarmType.values()[tagCompound.getInteger(SWARM_TYPE)];
            final Swarm swarm = getSwarmForClass(swarmType.typeToken.getRawType());

            swarm.entityClass = swarmType.typeToken.getRawType();
            swarm.readFromNBT(tagCompound.getCompoundTag(SWARM_DATA));

            addSwarm(swarm);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound)
    {
        //Save swarm list
        final NBTTagList nbtTagList = new NBTTagList();

        for (final Swarm swarm : swarmList)
        {
            if (swarm.shouldPersist())
            {
                final NBTTagCompound swarmNBT = new NBTTagCompound();
                final NBTTagCompound swarmInternalNBT = new NBTTagCompound();

                swarm.writeToNBT(swarmInternalNBT);

                swarmNBT.setInteger(SWARM_TYPE, swarm.getSwarmType().ordinal());
                swarmNBT.setTag(SWARM_DATA, swarmInternalNBT);

                nbtTagList.appendTag(swarmNBT);
            }
        }

        nbtTagCompound.setTag(SWARMS, nbtTagList);
    }

    public enum SwarmType
    {
        STEAMSPIDERSWARM(new TypeToken<SteamSpiderEntity>(){});

        public final TypeToken typeToken;

        SwarmType(TypeToken type)
        {
            this.typeToken = type;
        }
    }
}
