package nuparu.sevendaystomine.world.gen.feature;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import nuparu.sevendaystomine.SevenDaysToMine;

public class WorldGenLookoutBurnt extends WorldGenerator {

	public static final ResourceLocation RES = new ResourceLocation(SevenDaysToMine.MODID, "lookout_burnt");

	@Override
	public boolean generate(World world, Random rand, BlockPos pos) {
		if (!world.isRemote) {

			WorldServer worldserver = (WorldServer) world;
			MinecraftServer minecraftserver = world.getMinecraftServer();
			TemplateManager templatemanager = worldserver.getStructureTemplateManager();

			Template template = templatemanager.getTemplate(minecraftserver, RES);
			if (template == null) {
				return false;
			}

			Rotation rot = Rotation.values()[rand.nextInt(Rotation.values().length)];

			pos = pos.down(2);

			PlacementSettings placementsettings = (new PlacementSettings())
					.setMirror(rand.nextBoolean() ? Mirror.LEFT_RIGHT : Mirror.NONE).setRotation(rot)
					.setIgnoreEntities(false).setChunk((ChunkPos) null).setReplacedBlock((Block) null)
					.setIgnoreStructureBlock(false);

			template.getDataBlocks(pos, placementsettings);
			template.addBlocksToWorld(world, pos.add(0, 1, 0), placementsettings);

			Map<BlockPos, String> map = template.getDataBlocks(pos, placementsettings);

			for (Entry<BlockPos, String> entry : map.entrySet()) {
			}
			return true;
		}
		return false;
	}
}
