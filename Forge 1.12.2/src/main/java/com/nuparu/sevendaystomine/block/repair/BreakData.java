package com.nuparu.sevendaystomine.block.repair;

import java.io.Serializable;

public class BreakData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	long pos;
	int dim;
	float state;

	public BreakData(long pos, int dim, float state) {
		this.pos = pos;
		this.dim = dim;
		this.state = state;
	}

     public long getPos(){
          return this.pos;
     }
     public float getState(){
          return this.state;
     }
     public int getDim(){
          return this.dim;
     }
}