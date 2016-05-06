package mod.steamnsteel.world.orenodes;

import gnu.trove.map.hash.TLongObjectHashMap;
import mod.steamnsteel.utility.ChunkUtility;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.world.WorldSavedData;

import java.util.ArrayList;
import java.util.List;

public class OreSeamWorldData extends WorldSavedData
{
    private final TLongObjectHashMap<List<Path>> oreNodes = new TLongObjectHashMap<>();

    public static final String ORE_PATHS = "orePaths";
    public static final String ORE_PATHS_CHUNK = "chunk";
    public static final String ORE_PATHS_CONTROL_PATH = "controlPoints";

    public static final int NBT_COMPOUND_ID = 10;

    public OreSeamWorldData(String name)
    {
        super(name);
    }

    public void addPathToWorld(final long chunk, final Path p)
    {
        synchronized (oreNodes)
        {
            List<Path> paths = oreNodes.get(chunk);

            if (paths == null)
            {
                paths = new ArrayList<>();
            }

            paths.add(p);

            oreNodes.put(chunk, paths);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        synchronized (oreNodes)
        {
            final NBTTagList nbtData = nbt.getTagList(ORE_PATHS, NBT_COMPOUND_ID);

            for (int i = 0; i < nbtData.tagCount(); ++i)
            {
                final NBTTagCompound nbtOrePathsData = nbtData.getCompoundTagAt(i);

                final long chunk = nbtOrePathsData.getLong(ORE_PATHS_CHUNK);
                final NBTTagList nbtPaths = nbtOrePathsData.getTagList(ORE_PATHS_CONTROL_PATH, NBT_COMPOUND_ID);

                final List<Path> paths = new ArrayList<>(nbtPaths.tagCount());

                for (int ii = 0; ii < nbtPaths.tagCount(); ++ii)
                {
                    paths.add(Path.readFromNBT(nbtPaths.getCompoundTagAt(ii)));
                }

                oreNodes.put(chunk, paths);
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        final NBTTagList nbtData = new NBTTagList();

        synchronized (oreNodes)
        {
            oreNodes.forEachEntry((chunk, paths) ->
            {
                final NBTTagCompound nbtOrePathsData = new NBTTagCompound();
                final NBTTagList nbtPaths = new NBTTagList();

                for (final Path p : paths)
                {
                    final NBTTagCompound nbtPath = new NBTTagCompound();

                    p.writePathFromNBT(nbtPath);

                    nbtPaths.appendTag(nbtPath);
                }

                nbtOrePathsData.setLong(ORE_PATHS_CHUNK, chunk);
                nbtOrePathsData.setTag(ORE_PATHS_CONTROL_PATH, nbtPaths);

                nbtData.appendTag(nbtOrePathsData);

                return true;
            });
        }

        nbt.setTag(ORE_PATHS, nbtData);
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder();
        sb.append('\n');
        final long chunkID;

        if (Minecraft.getMinecraft().thePlayer != null)
        {
            final BlockPos pos = Minecraft.getMinecraft().thePlayer.getPosition();

            chunkID = ChunkUtility.toChunk(pos.getX() >> 4, pos.getZ() >> 4);
        } else {
            chunkID = 0L;
        }

        synchronized (oreNodes)
        {
            oreNodes.forEachEntry((chunk, paths) ->
            {
                if (chunk == chunkID)
                {
                    sb.append("chunk(");
                    sb.append(ChunkUtility.toChunkX(chunk));
                    sb.append(", ");
                    sb.append(ChunkUtility.toChunkZ(chunk));
                    sb.append(") ");
                    sb.append(paths);
                    sb.append('\n');

                    return false;
                }

                return true;
            });
        }

        return sb.toString();
    }
}
