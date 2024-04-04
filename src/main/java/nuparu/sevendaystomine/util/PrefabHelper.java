package nuparu.sevendaystomine.util;

import net.minecraft.util.math.BlockPos;

public class PrefabHelper {


	public static PrefabHelper INSTANCE;
	
	//Positions selected by the reality wand
	public BlockPos posA;
	public BlockPos posB;
	
	
	public static PrefabHelper getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new PrefabHelper();
		}
		return INSTANCE;
	}
	
	public void setPosA(BlockPos pos) {
		this.posA = pos;
	}
	
	public void setPosB(BlockPos pos) {
		this.posB = pos;
	}
}
