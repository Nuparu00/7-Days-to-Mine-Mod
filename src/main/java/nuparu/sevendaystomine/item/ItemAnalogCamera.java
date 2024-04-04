package nuparu.sevendaystomine.item;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.capability.ExtendedInventoryProvider;
import nuparu.sevendaystomine.capability.IItemHandlerExtended;
import nuparu.sevendaystomine.config.ModConfig;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.network.packets.SchedulePhotoMessage;

public class ItemAnalogCamera extends Item {

	public ItemAnalogCamera() {
		this.setMaxStackSize(1);
		this.setCreativeTab(SevenDaysToMine.TAB_ELECTRICITY);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack itemstack = playerIn.getHeldItem(handIn);

		playerIn.setActiveHand(handIn);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);

	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
		if (entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entityLiving;
			int dur = this.getMaxItemUseDuration(stack) - timeLeft;
			if (dur > 10) {
				IItemHandlerExtended inv = stack.getCapability(ExtendedInventoryProvider.EXTENDED_INV_CAP,
						EnumFacing.UP);
				if (inv == null)
					return;
				ItemStack paper = inv.getStackInSlot(0);
				if (paper.isEmpty() || paper.getItem() != Items.PAPER) {
					worldIn.playSound(null, new BlockPos(player), SoundEvents.UI_BUTTON_CLICK, SoundCategory.PLAYERS,
							worldIn.rand.nextFloat() * 0.2f + 0.9f, worldIn.rand.nextFloat() * 0.2f + 1.9f);
					return;
				}
				if (ModConfig.players.allowPhotos) {
					if (!worldIn.isRemote) {
						PacketManager.schedulePhoto.sendTo(new SchedulePhotoMessage(), (EntityPlayerMP) player);
					}
				}
				paper.shrink(1);
			} else if (player.isSneaking()) {
				player.openGui(SevenDaysToMine.instance, 27, worldIn, (int) player.posX, (int) player.posY,
						(int) player.posZ);
			}
		}
	}

	@Override
	public int getMaxItemUseDuration(ItemStack itemStack) {
		return 82000;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack itemStack) {
		return EnumAction.BOW;
	}

	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return EnumRarity.UNCOMMON;
	}

	@Override
	@Nullable
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound compound) {
		return new ExtendedInventoryProvider(1, "camera");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (this.isInCreativeTab(tab)) {
			EntityPlayer player = Minecraft.getMinecraft().player;
			ItemStack stack = new ItemStack(this, 1, 0);
			if (player != null) {
				setupDimensions(stack, player);
			}
			items.add(stack);
		}
	}

	public static ItemStack setupDimensions(ItemStack stack, @Nullable EntityPlayer player) {
		if (stack.getTagCompound() == null) {
			stack.setTagCompound(new NBTTagCompound());
		}
		NBTTagCompound nbt = stack.getTagCompound();
		nbt.setDouble("width", 0.75);
		nbt.setDouble("height", 0.75);
		nbt.setDouble("zoom", 1);
		return stack;
	}

	public static double getWidth(ItemStack stack, @Nullable EntityPlayer player) {
		if (stack.getTagCompound() == null) {
			setupDimensions(stack, player);
		}
		return stack.getTagCompound().getDouble("width");
	}

	public static double getHeight(ItemStack stack, @Nullable EntityPlayer player) {
		if (stack.getTagCompound() == null) {
			setupDimensions(stack, player);
		}
		return stack.getTagCompound().getDouble("height");
	}

	public static double getZoom(ItemStack stack, @Nullable EntityPlayer player) {
		if (stack.getTagCompound() == null) {
			setupDimensions(stack, player);
		}
		return stack.getTagCompound().getDouble("zoom");
	}

	public static void setWidth(double width, ItemStack stack, @Nullable EntityPlayer player) {
		if (stack.getTagCompound() == null) {
			setupDimensions(stack, player);
		}
		stack.getTagCompound().setDouble("width", width);
	}

	public static void setHeight(double height, ItemStack stack, @Nullable EntityPlayer player) {
		if (stack.getTagCompound() == null) {
			setupDimensions(stack, player);
		}
		stack.getTagCompound().setDouble("height", height);
	}

	public static void setZoom(double zoom, ItemStack stack, @Nullable EntityPlayer player) {
		if (stack.getTagCompound() == null) {
			setupDimensions(stack, player);
		}
		stack.getTagCompound().setDouble("zoom", zoom);
	}
}
