package com.nuparu.sevendaystomine.network.packets;

import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;

import net.minecraft.world.World;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.block.repair.BreakSavedData;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;

public class BulletImpactHandler implements IMessageHandler<BulletImpactMessage, BulletImpactMessage> {
	@SideOnly(Side.CLIENT)
	public BulletImpactMessage onMessage(BulletImpactMessage message, MessageContext ctx) {
		World world = Minecraft.getMinecraft().world;
		for (int j = 0; j < 20; j++) {
			world.spawnParticle(EnumParticleTypes.BLOCK_CRACK,
					message.posX + message.motionX + world.rand.nextDouble() * 0.2 - 0.1,
					message.posY + message.motionY + world.rand.nextDouble() * 0.2 - 0.1,
					message.posZ + message.motionZ + world.rand.nextDouble() * 0.2 - 0.1,
					(-2.5 + world.rand.nextDouble() * 0.5) * message.motionX,
					(-2.5 + world.rand.nextDouble() * 0.5) * message.motionY,
					(-2.5 + world.rand.nextDouble() * 0.5) * message.motionZ,
					Block.getStateId(world.getBlockState(message.pos)));
		}
		return null;
	}

}