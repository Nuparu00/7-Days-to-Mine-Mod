package com.nuparu.sevendaystomine.tileentity;

import java.util.Iterator;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.electricity.ElectricConnection;
import com.nuparu.sevendaystomine.electricity.IVoltage;
import com.nuparu.sevendaystomine.util.Utils;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class TileEntityCombustionGenerator extends TileEntityGeneratorBase {

	public TileEntityCombustionGenerator() {
		super();
	}

	@Override
	public void update() {

		boolean flag = false;

		int lava = countAdjacentMats(Material.LAVA);
		int air = countAdjacentBlocks(Blocks.AIR);
		int water = countAdjacentMats(Material.WATER);
		int ice = countAdjacentMats(Material.ICE);
		int packedIce = countAdjacentMats(Material.PACKED_ICE);
		int snow = countAdjacentMats(Material.SNOW);
		int fire = countAdjacentMats(Material.FIRE);

		int hot = fire + lava * 2;
		int cold = snow + packedIce * 2 + ice * 2 + water;
		temperatureLimit = (float) (0.35 + (hot * 0.65) - (cold * 0.65));

		if (this.isBurning()) {
			--this.burnTime;
			if (voltage < capacity) {
				storePower(getPowerPerUpdate());
			}
			if (temperature < temperatureLimit) {

				temperature += (0.0002 * hot);
			}

			flag = true;
		}

		ItemStack itemstack = getStackInSlot(0);
		if (!this.isBurning() && voltage < capacity) {
			this.burnTime = Utils.getItemBurnTime(itemstack);
			if (this.isBurning()) {
				if (!itemstack.isEmpty()) {
					Item item = itemstack.getItem();
					itemstack.shrink(1);

					if (itemstack.isEmpty()) {
						ItemStack item1 = item.getContainerItem(itemstack);
						inventory.set(0, item1);
					}
				}
			}else if(isBurning) {
				isBurning = false;
			}
		}

		if (temperature > 0) {
			temperature -= (0.00001 * ((2 * cold) + air));
		}

		if (temperature > 1) {
			this.world.createExplosion((Entity) null, pos.getX(), pos.getY(), pos.getZ(), 2, true);
		}
		if (temperature < 0) {
			temperature = 0;
		}

		Iterator<ElectricConnection> iterator = outputs.iterator();
		while (iterator.hasNext()) {
			ElectricConnection connection = iterator.next();
			IVoltage voltage = connection.getTo(world);
			if (voltage != null) {
				long l = voltage.tryToSendPower(getOutputForConnection(connection), connection);
				this.voltage -= l;
				if (l != 0) {
					flag = true;
				}
			} else {
				iterator.remove();
				flag = true;
			}
		}

		iterator = inputs.iterator();
		while (iterator.hasNext()) {
			ElectricConnection connection = iterator.next();
			IVoltage voltage = connection.getFrom(world);
			if (voltage == null) {
				iterator.remove();
				flag = true;
			}
		}

		if (flag) {
			this.markDirty();
		}
	}
	
	@Override
	public long getPowerPerUpdate() {
		long out = isBurning() ? (long) (basePower + (basePower * temperature)) : 0;
		if (out > getMaxOutput()) {
			out = getMaxOutput();
		}
		return out;
	}

	@Override
	public String getName() {
		return this.hasCustomName() ? this.customName : "container.generator.combustion";
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
		return null;
	}

	@Override
	public String getGuiID() {
		return SevenDaysToMine.MODID + ":generator.combustion";
	}
}