package nuparu.sevendaystomine.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.init.Biomes;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeCache;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.ChunkGeneratorSettings;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import nuparu.sevendaystomine.init.ModBiomes;
import nuparu.sevendaystomine.world.gen.layer.WastelandGenLayer;

public class BiomeProviderWasteland extends BiomeProvider {
	private ChunkGeneratorSettings settings;

	private GenLayer genBiomes;
	private GenLayer biomeIndexLayer;
	private final BiomeCache biomeCache;
	private final List<Biome> biomesToSpawnIn;
	public static List<Biome> allowedBiomes = Lists.newArrayList(ModBiomes.BURNT_FOREST, ModBiomes.BURNT_JUNGLE, ModBiomes.WASTELAND, ModBiomes.WASTELAND_DESERT, ModBiomes.BURNT_TAIGA);

	public BiomeProviderWasteland() {
		this.biomeCache = new BiomeCache(this);
		this.biomesToSpawnIn = new ArrayList<Biome>();
		this.biomesToSpawnIn.addAll(allowedBiomes);
	}

	public BiomeProviderWasteland(long seed, WorldType worldType) {
		this();
		GenLayer[] agenlayer = WastelandGenLayer.initializeAllBiomeGenerators(seed, worldType, this.settings);

		agenlayer = getModdedBiomeGenerators(worldType, seed, agenlayer);
		this.genBiomes = agenlayer[0];
		this.biomeIndexLayer = agenlayer[1];

	}

	public BiomeProviderWasteland(World world) {
		this(world.getSeed(), world.getWorldInfo().getTerrainType());

	}

	@Override
	public Biome[] getBiomesForGeneration(Biome[] biomes, int x, int z, int width, int height) {
		IntCache.resetIntCache();

		if (biomes == null || biomes.length < width * height) {
			biomes = new Biome[width * height];
		}

		int[] aint = this.genBiomes.getInts(x, z, width, height);

		try {
			for (int i = 0; i < width * height; ++i) {
				biomes[i] = Biome.getBiome(aint[i], Biomes.DEFAULT);
			}

			return biomes;
		} catch (Throwable throwable) {
			CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Invalid Biome id");
			CrashReportCategory crashreportcategory = crashreport.makeCategory("RawBiomeBlock");
			crashreportcategory.addCrashSection("biomes[] size", Integer.valueOf(biomes.length));
			crashreportcategory.addCrashSection("x", Integer.valueOf(x));
			crashreportcategory.addCrashSection("z", Integer.valueOf(z));
			crashreportcategory.addCrashSection("w", Integer.valueOf(width));
			crashreportcategory.addCrashSection("h", Integer.valueOf(height));
			throw new ReportedException(crashreport);
		}
	}

	@Override
	@Nullable
	public BlockPos findBiomePosition(int x, int z, int range, List<Biome> biomes, Random random) {
		IntCache.resetIntCache();
		int i = x - range >> 2;
		int j = z - range >> 2;
		int k = x + range >> 2;
		int l = z + range >> 2;
		int i1 = k - i + 1;
		int j1 = l - j + 1;
		int[] aint = this.genBiomes.getInts(i, j, i1, j1);
		BlockPos blockpos = null;
		int k1 = 0;

		for (int l1 = 0; l1 < i1 * j1; ++l1) {
			int i2 = i + l1 % i1 << 2;
			int j2 = j + l1 / i1 << 2;
			Biome biome = Biome.getBiome(aint[l1]);

			if (biomes.contains(biome) && (blockpos == null || random.nextInt(k1 + 1) == 0)) {
				blockpos = new BlockPos(i2, 0, j2);
				++k1;
			}
		}

		return blockpos;
	}

	@Override
	public Biome[] getBiomes(@Nullable Biome[] listToReuse, int x, int z, int width, int length, boolean cacheFlag) {
		IntCache.resetIntCache();

		if (listToReuse == null || listToReuse.length < width * length) {
			listToReuse = new Biome[width * length];
		}

		if (cacheFlag && width == 16 && length == 16 && (x & 15) == 0 && (z & 15) == 0) {
			Biome[] abiome = this.biomeCache.getCachedBiomes(x, z);
			System.arraycopy(abiome, 0, listToReuse, 0, width * length);
			return listToReuse;
		} else {
			int[] aint = this.biomeIndexLayer.getInts(x, z, width, length);

			for (int i = 0; i < width * length; ++i) {
				listToReuse[i] = Biome.getBiome(aint[i], Biomes.DEFAULT);
			}

			return listToReuse;
		}
	}

	@Override
	public boolean areBiomesViable(int x, int z, int radius, List<Biome> allowed) {
		IntCache.resetIntCache();
		int i = x - radius >> 2;
		int j = z - radius >> 2;
		int k = x + radius >> 2;
		int l = z + radius >> 2;
		int i1 = k - i + 1;
		int j1 = l - j + 1;
		int[] aint = this.genBiomes.getInts(i, j, i1, j1);

		try {
			for (int k1 = 0; k1 < i1 * j1; ++k1) {
				Biome biome = Biome.getBiome(aint[k1]);

				if (!allowed.contains(biome)) {
					return false;
				}
			}

			return true;
		} catch (Throwable throwable) {
			CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Invalid Biome id");
			CrashReportCategory crashreportcategory = crashreport.makeCategory("Layer");
			crashreportcategory.addCrashSection("Layer", this.genBiomes.toString());
			crashreportcategory.addCrashSection("x", Integer.valueOf(x));
			crashreportcategory.addCrashSection("z", Integer.valueOf(z));
			crashreportcategory.addCrashSection("radius", Integer.valueOf(radius));
			crashreportcategory.addCrashSection("allowed", allowed);
			throw new ReportedException(crashreport);
		}
	}

	@Override
	public void cleanupCache() {
		this.biomeCache.cleanupCache();
	}
	@Override
	public boolean isFixedBiome()
    {
        return this.settings != null && this.settings.fixedBiome >= 0;
    }
	@Override
    public Biome getFixedBiome()
    {
        return this.settings != null && this.settings.fixedBiome >= 0 ? Biome.getBiomeForId(this.settings.fixedBiome) : null;
    }
	@Override
	public List<Biome> getBiomesToSpawnIn()
    {
        return this.biomesToSpawnIn;
    }

	@Override
    public Biome getBiome(BlockPos pos)
    {
        return this.getBiome(pos, (Biome)null);
    }
	@Override
    public Biome getBiome(BlockPos pos, Biome defaultBiome)
    {
        return this.biomeCache.getBiome(pos.getX(), pos.getZ(), defaultBiome);
    }
	@Override
	public Biome[] getBiomes(@Nullable Biome[] oldBiomeList, int x, int z, int width, int depth)
    {
        return this.getBiomes(oldBiomeList, x, z, width, depth, true);
    }

}
