package mod.steamnsteel.world.orenodes;

import mod.steamnsteel.utility.ChunkUtility;
import mod.steamnsteel.world.WorldGen;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class SteamNSteelDecorator extends BiomeDecorator
{
    private BiomeDecorator providedDecorator;

    public SteamNSteelDecorator(BiomeDecorator d)
    {
        providedDecorator = d;
    }

    @Override
    public void decorate(World worldIn, Random random, BiomeGenBase biome, BlockPos pos)
    {
        if (worldIn.getWorldType() == WorldGen.steamNSteelWorldType)
        {
            for (int f = 0; f < 3; ++f)
            {
                final PathBezier curve = new PathBezier(
                        new Vec3(pos.getX() + random.nextGaussian() * 8+8, pos.getY() + random.nextGaussian() * 25+25*f, pos.getZ() + random.nextGaussian() * 8+8),
                        new Vec3(pos.getX() + random.nextGaussian() * 8+8, pos.getY() + random.nextGaussian() * 25+25*f, pos.getZ() + random.nextGaussian() * 8+8),
                        new Vec3(pos.getX() + random.nextGaussian() * 8+8, pos.getY() + random.nextGaussian() * 25+25*f, pos.getZ() + random.nextGaussian() * 8+8),
                        new Vec3(pos.getX() + random.nextGaussian() * 8+8, pos.getY() + random.nextGaussian() * 25+25*f, pos.getZ() + random.nextGaussian() * 8+8)
                );

                final double[] cp = new double[3];
                final double[] cp2 = new double[3];

                {//curve position at t=0.0
                    final Vec3 v = curve.getControlPointP0();
                    cp[0] = v.xCoord;
                    cp[1] = v.yCoord;
                    cp[2] = v.zCoord;
                }

                for (double i = 0d; i < 1d; i += 1 / 1000d)
                {
                    curve.getPosition(i, cp2);

                    worldIn.setBlockState(new BlockPos((int) cp2[0], (int) cp2[1], (int) cp2[2]), Blocks.iron_ore.getDefaultState());
                }

                worldIn.setBlockState(new BlockPos(curve.getControlPointP0()), Blocks.obsidian.getDefaultState());
                worldIn.setBlockState(new BlockPos(curve.getControlPointP1()), Blocks.obsidian.getDefaultState());
                worldIn.setBlockState(new BlockPos(curve.getControlPointP2()), Blocks.obsidian.getDefaultState());
                worldIn.setBlockState(new BlockPos(curve.getControlPointP3()), Blocks.obsidian.getDefaultState());

                SteamNSteelWorldType.oreSeamWorldData.addPathToWorld(ChunkUtility.toChunk(pos.getX() >> 4, pos.getZ() >> 4), curve);
            }
        } else
        {
            providedDecorator.decorate(worldIn, random, biome, pos);
        }
    }

    private static double getDistance(double[] a, double[] b)
    {
        return Math.sqrt(Math.pow(a[0]-b[0], 2) + Math.pow(a[1]-b[1], 2) + Math.pow(a[2]-b[1], 2));
    }

    @Override
    protected void generateOres()
    {
        //no standard ores lets gen our own.
        //or test to see if this works
    }

    @Override
    protected void genStandardOre1(int blockCount, WorldGenerator generator, int minHeight, int maxHeight)
    {
    }

    @Override
    protected void genStandardOre2(int blockCount, WorldGenerator generator, int centerHeight, int spread)
    {
    }
}
