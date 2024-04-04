package nuparu.sevendaystomine.item;

import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.config.ModConfig;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.util.Utils;

public class ItemFuelTool extends ItemQualityTool implements IReloadable {

	public SoundEvent refillSound;

	public ItemFuelTool(float attackDamageIn, float attackSpeedIn, ToolMaterial materialIn,
			Set<Block> effectiveBlocksIn) {
		super(attackDamageIn, attackSpeedIn, materialIn, effectiveBlocksIn);
		String toolClass = "";
		if (this instanceof ItemAuger) {
			toolClass = "pickaxe";
		} else if (this instanceof ItemChainsaw) {
			toolClass = "axe";
		}
		ObfuscationReflectionHelper.setPrivateValue(ItemTool.class, this, toolClass, "toolClass");
	}

	public ItemFuelTool(ToolMaterial materialIn, Set<Block> effectiveBlocksIn) {
		super(materialIn, effectiveBlocksIn);

		String toolClass = "";
		if (this instanceof ItemAuger) {
			toolClass = "pickaxe";
		} else if (this instanceof ItemChainsaw) {
			toolClass = "axe";
		}
		ObfuscationReflectionHelper.setPrivateValue(ItemTool.class, this, toolClass, "toolClass");

	}

	@Override
	public void onCreated(ItemStack itemstack, World world, EntityPlayer player) {
		super.onCreated(itemstack, world, player);
		initNBT(itemstack);

	}

	public void initNBT(ItemStack itemstack) {
		if (itemstack.getTagCompound() == null) {
			itemstack.setTagCompound(new NBTTagCompound());
		}
		itemstack.getTagCompound().setInteger("FuelMax", 1000);
		itemstack.getTagCompound().setInteger("FuelCurrent", 0);
		itemstack.getTagCompound().setInteger("ReloadTime", 0);
		itemstack.getTagCompound().setBoolean("Reloading", false);
	}

	@Override
	public void onReloadStart(World world, EntityPlayer player, ItemStack stack, int reloadTime) {
		stack.getTagCompound().setLong("NextFire",
				world.getTotalWorldTime() + (long) Math.ceil((reloadTime / 1000d) * 20));
	}

	@Override
	public void onReloadEnd(World world, EntityPlayer player, ItemStack stack, ItemStack bullet) {
		if (bullet != null && !bullet.isEmpty() && stack.getTagCompound().hasKey("FuelCurrent")
				&& stack.getTagCompound().hasKey("FuelMax")) {

			stack.getTagCompound().setBoolean("Reloading", false);
			int toReload = getCapacity(stack, player) - getAmmo(stack, player);
			int reload = Math.min((int) Math.floor(toReload / 5),
					Utils.getItemCount(player.inventory, bullet.getItem()));

			setAmmo(stack, player, getAmmo(stack, player) + reload * 5);
			player.inventory.clearMatchingItems(bullet.getItem(), -1, reload, null);
		}
	}

	@Override
	public int getAmmo(ItemStack stack, EntityPlayer player) {
		if (stack == null || stack.isEmpty() || stack.getTagCompound() == null
				|| !stack.getTagCompound().hasKey("FuelCurrent"))
			return -1;
		return stack.getTagCompound().getInteger("FuelCurrent");
	}

	@Override
	public int getCapacity(ItemStack stack, EntityPlayer player) {
		return 1000;
	}

	@Override
	public void setAmmo(ItemStack stack, EntityPlayer player, int ammo) {
		if (stack.getTagCompound() == null) {
			stack.setTagCompound(new NBTTagCompound());
		}
		stack.getTagCompound().setInteger("FuelCurrent", ammo);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
		if (stack.getTagCompound() != null) {
			if (stack.getTagCompound().hasKey("FuelCurrent") && stack.getTagCompound().getInteger("FuelCurrent") < 0F) {

				stack.getTagCompound().setInteger("FuelCurrent", 0);
			}
			if (stack.getTagCompound().hasKey("FuelMax") && stack.getTagCompound().getInteger("FuelMax") != this.getCapacity(stack, null)) {

				stack.getTagCompound().setInteger("FuelMax", this.getCapacity(stack, null));
			}
		}

	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos,
			EntityLivingBase entityLiving) {
		NBTTagCompound nbt = stack.getTagCompound();
		setAmmo(stack, null, getAmmo(stack, null) - 1);
		return !worldIn.isRemote;
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		return 1 - ((double) getAmmo(stack, null) / (double) getCapacity(stack, null));
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return true;
	}

	@SuppressWarnings("null")
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);

		if (stack == null)
			return;
		tooltip.add(getAmmo(stack, null) + "/" + getCapacity(stack, null));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (this.isInCreativeTab(tab)) {
			EntityPlayer player = Minecraft.getMinecraft().player;
			ItemStack stack = new ItemStack(this, 1, 0);
			if (player != null) {
				setQuality(stack,
						(int) Math.min(Math.max(Math.floor(player.experienceTotal / ModConfig.players.xpPerQuality), 1),
								ModConfig.players.maxQuality));
				NBTTagCompound nbt = stack.getTagCompound();
				nbt.setInteger("FuelMax", 1000);
				nbt.setInteger("FuelCurrent", 1000);
				nbt.setInteger("ReloadTime", 90000);
				nbt.setBoolean("Reloading", false);
			}
			items.add(stack);
		}
	}

	@Override
	public Item getReloadItem(ItemStack stack) {
		return ModItems.GAS_CANISTER;
	}

	@Override
	public int getReloadTime(ItemStack stack) {
		return 200;
	}

	@Override
	public SoundEvent getReloadSound() {
		return SoundEvents.ITEM_BUCKET_FILL;
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		return this.getAmmo(stack, null) > 0 ? super.onLeftClickEntity(stack, player, entity) : true;
	}

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
		NBTTagCompound nbt = stack.getTagCompound();
		System.out.println(nbt.toString());
		if (nbt != null && nbt.hasKey("FuelCurrent") && nbt.getInteger("FuelCurrent") > 0) {
			nbt.setInteger("FuelCurrent", Math.max(0, nbt.getInteger("FuelCurrent") - 1));
		}
		return true;
	}

}