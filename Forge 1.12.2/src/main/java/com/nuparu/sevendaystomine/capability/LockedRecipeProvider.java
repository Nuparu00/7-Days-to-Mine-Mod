package com.nuparu.sevendaystomine.capability;

import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.Capability;

import net.minecraft.util.EnumFacing;
import net.minecraft.nbt.NBTBase;


public class LockedRecipeProvider implements ICapabilitySerializable<NBTBase> {
     @CapabilityInject(ILockedRecipe.class)
     public static final Capability<ILockedRecipe> LOCKED_RECIPE_CAP = null;
     private ILockedRecipe instance = LOCKED_RECIPE_CAP.getDefaultInstance();

     @Override
     public boolean hasCapability(Capability<?> capability, EnumFacing facing)

     {

          return capability == LOCKED_RECIPE_CAP;

     }

     @Override
     public <T> T getCapability(Capability<T> capability, EnumFacing facing)

     {

          return capability == LOCKED_RECIPE_CAP ? LOCKED_RECIPE_CAP.<T> cast(this.instance) : null;

     }

     @Override
     public NBTBase serializeNBT()

     {

          return LOCKED_RECIPE_CAP.getStorage().writeNBT(LOCKED_RECIPE_CAP, this.instance, null);

     }

     @Override
     public void deserializeNBT(NBTBase nbt)

     {

          LOCKED_RECIPE_CAP.getStorage().readNBT(LOCKED_RECIPE_CAP, this.instance, null, nbt);

     }

}