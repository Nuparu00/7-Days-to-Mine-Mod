package nuparu.sevendaystomine.util.json;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import nuparu.sevendaystomine.util.Utils;
import nuparu.sevendaystomine.world.gen.prefab.EnumPrefabType;
import nuparu.sevendaystomine.world.gen.prefab.EnumStructureBiomeType;
import nuparu.sevendaystomine.world.gen.prefab.Prefab;
import nuparu.sevendaystomine.world.gen.prefab.buffered.BufferedBlock;
import nuparu.sevendaystomine.world.gen.prefab.buffered.BufferedEntity;

public class JsonPrefab {

	String name;

	int width;
	int height;
	int length;

	int offsetX;
	int offsetY;
	int offsetZ;
	
	int pedestalLayer = 255;

	boolean underground = false;
	double requiredRoom;
	double requiredFlatness;
	int weight;

	EnumPrefabType type = EnumPrefabType.NONE;
	List<EnumStructureBiomeType> biomeTypes;

	List<JsonBlock> blocks;
	List<JsonEntity> entities;

	public JsonPrefab(String name, int width, int height, int length, int offsetX, int offsetY, int offsetZ,
			boolean underground, double requiredRoom, double requiredFlatness, int weight, EnumPrefabType type,
			List<EnumStructureBiomeType> biomeTypes, List<JsonBlock> blocks, List<JsonEntity> entities) {
		this.name = name;
		this.width = width;
		this.height = height;
		this.length = length;

		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.offsetZ = offsetZ;

		this.underground = underground;
		this.requiredRoom = requiredRoom;
		this.requiredFlatness = requiredFlatness;
		this.weight = weight;

		this.type = type;
		this.biomeTypes = biomeTypes;

		this.blocks = blocks;
		this.entities = entities;
	}

	public Prefab toPrefab() {
		return new Prefab(name, width, height, length, offsetX, offsetY, offsetZ, underground, requiredRoom,
				requiredFlatness, weight, type, biomeTypes, getBlocks(), getEntities(), pedestalLayer);
	}

	public List<BufferedBlock> getBlocks() {
		List<BufferedBlock> list = new ArrayList<BufferedBlock>();
		for (JsonBlock block : blocks) {
			Block bl = Block.getBlockFromName(block.block);
			if(block.y < pedestalLayer) {
				pedestalLayer = block.y;
			}
			if (bl == null) {
				Utils.getLogger()
						.info("Couldn't find a tile{" + block.block + "} for file " + name + ". Replacing by Air.");
				bl = Blocks.AIR;
			}
			NBTTagCompound nbt = null;
			if (!block.nbt.isEmpty()) {
				try {
					nbt = JsonToNBT.getTagFromJson(block.nbt);
				} catch (NBTException e) {
					e.printStackTrace();
				}
			}
			BufferedBlock block2 = new BufferedBlock(block.x, block.y, block.z, bl, block.meta, nbt, block.lootTable);
			list.add(block2);
		}
		return list;
	}

	public List<BufferedEntity> getEntities() {
		List<BufferedEntity> list = new ArrayList<BufferedEntity>();
		for (JsonEntity entity : entities) {
			NBTTagCompound nbt = null;
			if (!entity.nbt.isEmpty()) {
				try {
					nbt = JsonToNBT.getTagFromJson(entity.nbt);
				} catch (NBTException e) {
					e.printStackTrace();
				}
			}
			BufferedEntity entity2 = new BufferedEntity(entity.name, entity.x, entity.y, entity.z, entity.yaw,
					entity.pitch, nbt);
			list.add(entity2);
		}
		return list;
	}
}
