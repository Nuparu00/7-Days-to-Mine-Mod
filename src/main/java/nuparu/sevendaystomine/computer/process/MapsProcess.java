package nuparu.sevendaystomine.computer.process;

import net.minecraft.block.material.MapColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.client.gui.monitor.Screen;
import nuparu.sevendaystomine.client.gui.monitor.elements.Button;
import nuparu.sevendaystomine.client.util.RenderUtils;
import nuparu.sevendaystomine.computer.application.ApplicationRegistry;
import nuparu.sevendaystomine.util.ColorRGBA;

public class MapsProcess extends WindowedProcess {

	int scale;
	int xCenter;
	int zCenter;
	Biome[] biomeData;

	DynamicTexture mapTexture;
	int[] mapTextureData;
    ResourceLocation location;
    
	public MapsProcess() {
		super();
	}

	public MapsProcess(double x, double y, double width, double height) {
		super(x, y, width, height);
		this.application = ApplicationRegistry.INSTANCE.getByString("transit");
		mapTexture = new DynamicTexture(128, 128);
		mapTextureData = mapTexture.getTextureData();
		for (int i = 0; i < this.mapTextureData.length; ++i) {
			this.mapTextureData[i] = 0;
		}

		location = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("maps", this.mapTexture);
		scale = 2;
		
		 int i = 128 * (1 << scale);
	        int j = MathHelper.floor((computerTE.getPos().getX() + 64.0D) / (double)i);
	        int k = MathHelper.floor((computerTE.getPos().getZ() + 64.0D) / (double)i);
	        this.xCenter = j * i + i / 2 - 64;
	        this.zCenter = k * i + i / 2 - 64;
		generateBiomeData();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void clientInit() {
		super.clientInit();
		

	}

	@SideOnly(Side.CLIENT)
	public void clientTick() {
		super.clientTick();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void initWindow() {
		super.initWindow();

	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onButtonPressed(Button button, int mouseButton) {
		super.onButtonPressed(button, mouseButton);
		if (isMinimized())
			return;

	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		return nbt;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void render(float partialTicks) {
		super.render(partialTicks);
		drawWindow(getTitle(), new ColorRGBA(47, 47, 47), new ColorRGBA(67, 68, 71));
		GlStateManager.pushMatrix();
		if (mapTextureData != null && mapTextureData.length != 0) {
			/*RenderUtils.drawTexturedRect(location, screen.getRelativeX(0), screen.getRelativeY(0)+Screen.screen.ySize * title_bar_height, 0, 0, this.width,
					this.height - Screen.screen.ySize * title_bar_height, this.width, this.width, 1, 20);*/
			RenderUtils.drawTexturedRect(location, screen.getRelativeX(0), screen.getRelativeY(0)+Screen.screen.ySize * title_bar_height, 0, 0, 128,128,128,128, 1, 20);
		}
		GlStateManager.popMatrix();
		RenderUtils.drawString("X: " + xCenter,10 , 10, 0xffffff);
		RenderUtils.drawString("Z: " + zCenter,10 , 20, 0xffffff);

	}

	public void generateBiomeData() {
		scale = MathHelper.clamp(scale, 0, 16);
		int i = 1 << scale;

		int x = (0 / i - 64) * i;
		int z = (0 / i - 64) * i;

		biomeData = computerTE.getWorld().getBiomeProvider().getBiomes((Biome[]) null, x, z, 128 * i, 128 * i, false);

		byte[] colors = new byte[16384];

		for (int l = 0; l < 128; ++l) {
			for (int i1 = 0; i1 < 128; ++i1) {
				int j1 = l * i;
				int k1 = i1 * i;
				Biome biome = biomeData[j1 + k1 * 128 * i];

				MapColor mapcolor = MapColor.AIR;


				int l1 = 3;
				int i2 = 8;

				if (l > 0 && i1 > 0 && l < 127 && i1 < 127) {
					if (biomeData[(l - 1) * i + (i1 - 1) * i * 128 * i].getBaseHeight() >= 0.0F) {
						--i2;
					}

					if (biomeData[(l - 1) * i + (i1 + 1) * i * 128 * i].getBaseHeight() >= 0.0F) {
						--i2;
					}

					if (biomeData[(l - 1) * i + i1 * i * 128 * i].getBaseHeight() >= 0.0F) {
						--i2;
					}

					if (biomeData[(l + 1) * i + (i1 - 1) * i * 128 * i].getBaseHeight() >= 0.0F) {
						--i2;
					}

					if (biomeData[(l + 1) * i + (i1 + 1) * i * 128 * i].getBaseHeight() >= 0.0F) {
						--i2;
					}

					if (biomeData[(l + 1) * i + i1 * i * 128 * i].getBaseHeight() >= 0.0F) {
						--i2;
					}

					if (biomeData[l * i + (i1 - 1) * i * 128 * i].getBaseHeight() >= 0.0F) {
						--i2;
					}

					if (biomeData[l * i + (i1 + 1) * i * 128 * i].getBaseHeight() >= 0.0F) {
						--i2;
					}

					if (biome.getBaseHeight() < 0.0F) {
						mapcolor = MapColor.ADOBE;

						if (i2 > 7 && i1 % 2 == 0) {
							l1 = (l + (int) (MathHelper.sin((float) i1 + 0.0F) * 7.0F)) / 8 % 5;

							if (l1 == 3) {
								l1 = 1;
							} else if (l1 == 4) {
								l1 = 0;
							}
						} else if (i2 > 7) {
							
						} else if (i2 > 5) {
							l1 = 1;
						} else if (i2 > 3) {
							l1 = 0;
						} else if (i2 > 1) {
							l1 = 0;
						}
					} else if (i2 > 0) {
						mapcolor = MapColor.BROWN;

						if (i2 > 3) {
							l1 = 1;
						} else {
							l1 = 3;
						}
					}
					else {
						mapcolor = biome.topBlock.getMaterial().getMaterialMapColor();	

					}
				}/*
				if (!BiomeDictionary.hasType(biome, BiomeDictionary.Type.OCEAN)
						&& Math.abs(RoadDecoratorWorldGen.getNoiseValue(x + j1, z + k1, 0)) < 0.005d) {
					mapcolor = MapColor.BROWN_STAINED_HARDENED_CLAY;
				}*/
				


				if (mapcolor != MapColor.AIR) {
					colors[l + i1 * 128] = (byte) (mapcolor.colorIndex * 4 + l1);
				}

			}
		}

		for (int j = 0; j < 16384; ++j) {
			int k = colors[j] & 255;

			if (k / 4 == 0) {
				this.mapTextureData[j] = (j + j / 128 & 1) * 8 + 16 << 24;
			} else {
				this.mapTextureData[j] = MapColor.COLORS[k / 4].getMapColor(k & 3);
			}
		}
		this.mapTexture.updateDynamicTexture();
	}

	@SideOnly(Side.CLIENT)
	public void keyTyped(char typedChar, int keyCode) {
		super.keyTyped(typedChar, keyCode);
		if (!isFocused() || isMinimized())
			return;
		switch(keyCode) {
		default : break;
		case 30 : this.xCenter-=32; generateBiomeData(); break;
		case 32 : this.xCenter+=32; generateBiomeData(); break;
		case 17 : this.zCenter-=32; generateBiomeData(); break;
		case 31 : this.zCenter+=32; generateBiomeData(); break;
		case 78 : this.scale+=1; generateBiomeData(); break;
		case 74 : this.scale-=1; generateBiomeData(); break;
		}
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		super.mouseClicked(mouseX, mouseY, mouseButton);

	}

}
