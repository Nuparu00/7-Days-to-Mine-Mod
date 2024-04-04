package nuparu.sevendaystomine.block.repair;

import java.io.Serializable;

public class BreakData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	long pos;
	long lastChange;
	float state;

	public BreakData(long pos, long dim, float state) {
		this.pos = pos;
		this.lastChange = dim;
		this.state = state;
	}

     public long getPos(){
          return this.pos;
     }
     public float getState(){
          return this.state;
     }
     public long getLastChange(){
          return this.lastChange;
     }
}