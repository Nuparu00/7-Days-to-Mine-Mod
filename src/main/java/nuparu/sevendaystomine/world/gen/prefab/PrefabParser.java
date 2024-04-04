package nuparu.sevendaystomine.world.gen.prefab;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.block.Block;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import nuparu.sevendaystomine.util.Utils;
import nuparu.sevendaystomine.util.json.JsonPrefab;
import nuparu.sevendaystomine.world.gen.prefab.buffered.BufferedBlock;
import nuparu.sevendaystomine.world.gen.prefab.buffered.BufferedEntity;

public class PrefabParser {

	public static final PrefabParser INSTANCE = new PrefabParser();

	public Prefab getPrefabFromResource(ResourceLocation res) {
		InputStream in = getClass().getResourceAsStream(new StringBuilder().append("/assets/")
				.append(res.getResourceDomain()).append("/").append(res.getResourcePath()).toString());
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		Gson gson = new Gson();
		JsonElement je = gson.fromJson(reader, JsonElement.class);
		JsonObject json = je.getAsJsonObject();

		try {
			reader.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		if (!json.has("name")) {
			return null;
		}
		String name = json.get("name").getAsString();

		if (!json.has("offsetX")) {
			return null;
		}
		int offsetX = json.get("offsetX").getAsInt();

		if (!json.has("offsetY")) {
			return null;
		}
		int offsetY = json.get("offsetY").getAsInt();

		if (!json.has("offsetZ")) {
			return null;
		}
		int offsetZ = json.get("offsetZ").getAsInt();

		if (!json.has("underground")) {
			return null;
		}
		
		if (!json.has("width")) {
			return null;
		}
		int width = json.get("width").getAsInt();

		if (!json.has("height")) {
			return null;
		}
		int height = json.get("height").getAsInt();

		if (!json.has("length")) {
			return null;
		}
		int length = json.get("length").getAsInt();
		
		boolean underground = json.get("underground").getAsBoolean();

		if (!json.has("requiredRoom")) {
			return null;
		}
		double requiredRoom = json.get("requiredRoom").getAsDouble();

		if (!json.has("requiredFlatness")) {
			return null;
		}
		double requiredFlatness = json.get("requiredFlatness").getAsDouble();

		if (!json.has("weight")) {
			return null;
		}
		int weight = json.get("weight").getAsInt();

		if (!json.has("type")) {
			return null;
		}
		String typeAsString = json.get("type").getAsString();
		EnumPrefabType type = EnumPrefabType.fromString(typeAsString);
		if (!json.has("biomeTypes")) {
			return null;
		}
		
		JsonArray biomes = json.get("biomeTypes").getAsJsonArray();
		List<EnumStructureBiomeType> biomeTypes = new ArrayList<EnumStructureBiomeType>();
		for (JsonElement je2 : biomes) {
			EnumStructureBiomeType biome = EnumStructureBiomeType.fromString(je2.getAsString());
			biomeTypes.add(biome);
		}
		
		int pedestalLayer = 255;
		
		List<BufferedBlock> blocks = new ArrayList<BufferedBlock>();
		JsonArray blockz = json.get("blocks").getAsJsonArray();
		for (JsonElement je2 : blockz) {
			JsonObject blockObj = je2.getAsJsonObject();
			int x = blockObj.get("x").getAsInt();
			int y = blockObj.get("y").getAsInt();
			int z = blockObj.get("z").getAsInt();
			String tile = blockObj.get("tile").getAsString();
			int meta = blockObj.get("metadata").getAsInt();
			String loottable = blockObj.get("loottable").getAsString();
			String data = blockObj.get("NBTData").getAsString();
			NBTTagCompound nbt = null;
			if (!data.isEmpty()) {
				try {
					nbt = JsonToNBT.getTagFromJson(data);
				} catch (NBTException e) {
				}
			}
			Block block = Block.getBlockFromName(tile);
			BufferedBlock buffered = new BufferedBlock(x, y, z, block, meta, nbt, loottable);
			blocks.add(buffered);
			if(y < pedestalLayer) {
				pedestalLayer = y;
			}
		}
		
		List<BufferedEntity> entities = new ArrayList<BufferedEntity>();
		JsonArray entitiez = json.get("entities").getAsJsonArray();
		for (JsonElement je2 : entitiez) {
			JsonObject blockObj = je2.getAsJsonObject();
			int x = blockObj.get("x").getAsInt();
			int y = blockObj.get("y").getAsInt();
			int z = blockObj.get("z").getAsInt();
			int yaw = blockObj.get("yaw").getAsInt();
			int pitch = blockObj.get("pitch").getAsInt();
			String entity = blockObj.get("entity").getAsString();
			String data = blockObj.get("NBTData").getAsString();
			NBTTagCompound nbt = null;
			if (!data.isEmpty()) {
				try {
					nbt = JsonToNBT.getTagFromJson(data);
				} catch (NBTException e) {
				}
			}
			BufferedEntity buffered = new BufferedEntity(entity,x,y,z,yaw,pitch,nbt);
			entities.add(buffered);
		}

		return new Prefab(name, width, height, length, offsetX, offsetY, offsetZ, underground, requiredRoom, requiredFlatness, weight, type, biomeTypes, blocks, entities, pedestalLayer);
	}
	
	public Prefab getPrefabFromFile(String fileName) throws FileNotFoundException {
		File file = new File("./resources/prefabs/" + fileName + ".prfb");
		file.getParentFile().mkdirs();
		String json;
		try {
			json = Utils.readFile(file.getAbsolutePath(),Charset.defaultCharset());
			Gson gson = new GsonBuilder().create(); 
			JsonPrefab jPrefab = gson.fromJson(json, JsonPrefab.class);
			return jPrefab.toPrefab();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
