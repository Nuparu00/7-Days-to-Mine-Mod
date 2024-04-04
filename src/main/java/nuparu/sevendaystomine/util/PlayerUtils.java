package nuparu.sevendaystomine.util;

import nuparu.sevendaystomine.config.ModConfig;

public class PlayerUtils {
	
	public static int getInfectionStageStart(int stage) {
		if(stage >= ModConfig.players.infectionStagesDuration.length) return Integer.MIN_VALUE;
		
		int start = 0;
		
		if(stage == 0) return start;
		
		for(int i = 0; i < stage; i++) {
			start+=ModConfig.players.infectionStagesDuration[i];
		}
		return start;
	}
	
	// Returns the time until the next infection stage, takes the infection time
	// from IExtendedPlayer
	public static int getCurrentInectionStageRemainingTime(int time) {
		int stage = getInfectionStage(time);
		if(stage >= ModConfig.players.infectionStagesDuration.length) return 24000;
	
		int nextStageStart = getInfectionStageStart(stage+1);
		return nextStageStart-time;

	}

	// Reutns the current infection staged based on the infection time from
	// IExtendedPlayer
	public static int getInfectionStage(int time) {
		if (time < 0)
			return -1;
		for(int i = 0; i < getNumberOfstages()-1;i++) {
			int start = getInfectionStageStart(i);
			int end = getInfectionStageStart(i+1);
			if(start <= time && time < end) {
				
				return i;
			}
			
		}
		return ModConfig.players.infectionStagesDuration.length-1;
	}
	
	public static int getNumberOfstages() {
		return ModConfig.players.infectionStagesDuration.length;
	}
}
