package nuparu.sevendaystomine.item;

import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemQualityAxe extends ItemQualityTool {

	private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(Blocks.PLANKS, Blocks.BOOKSHELF, Blocks.LOG,
			Blocks.LOG2, Blocks.CHEST, Blocks.PUMPKIN, Blocks.LIT_PUMPKIN, Blocks.MELON_BLOCK, Blocks.LADDER,
			Blocks.WOODEN_BUTTON, Blocks.WOODEN_PRESSURE_PLATE, Blocks.LEAVES, Blocks.LEAVES2);

	public ItemQualityAxe(ToolMaterial materialIn, double speed) {
		super(materialIn, EFFECTIVE_ON);
		this.setAttackDamage(materialIn.getAttackDamage());
		this.setAttackSpeed(speed);
	}

	protected ItemQualityAxe(Item.ToolMaterial material, float damage, double speed) {
		super(material, EFFECTIVE_ON);
		this.setAttackDamage(damage);
		this.setAttackSpeed(speed);
	}

	public float getDestroySpeed(ItemStack stack, IBlockState state) {
		Material material = state.getMaterial();
		float f = material != Material.WOOD && material != Material.PLANTS && material != Material.VINE
				? super.getDestroySpeed(stack, state)
				: this.efficiency;
		return f;
	}
	@Override
	public Set<String> getToolClasses(ItemStack stack) {
	    return ImmutableSet.of("axe");
	}
}
