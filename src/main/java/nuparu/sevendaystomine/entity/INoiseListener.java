package nuparu.sevendaystomine.entity;

public interface INoiseListener {

	public void addNoise(Noise noise);
	public Noise getCurrentNoise();
	public void reset();
	
}
