package nuparu.sevendaystomine.util;

import net.minecraft.util.math.Vec3d;

public class Wec2f {
	/** An immutable vector with {@code 0.0F} as the x and y components. */
	public static final Wec2f ZERO = new Wec2f(0.0F, 0.0F);
	/** An immutable vector with {@code 1.0F} as the x and y components. */
	public static final Wec2f ONE = new Wec2f(1.0F, 1.0F);
	/** An immutable vector with {@code 1.0F} as the x component. */
	public static final Wec2f UNIT_X = new Wec2f(1.0F, 0.0F);
	/** An immutable vector with {@code -1.0F} as the x component. */
	public static final Wec2f NEGATIVE_UNIT_X = new Wec2f(-1.0F, 0.0F);
	/** An immutable vector with {@code 1.0F} as the y component. */
	public static final Wec2f UNIT_Y = new Wec2f(0.0F, 1.0F);
	/** An immutable vector with {@code -1.0F} as the y component. */
	public static final Wec2f NEGATIVE_UNIT_Y = new Wec2f(0.0F, -1.0F);
	/**
	 * An immutable vector with {@link Float#MAX_VALUE} as the x and y components.
	 */
	public static final Wec2f MAX = new Wec2f(Float.MAX_VALUE, Float.MAX_VALUE);
	/**
	 * An immutable vector with {@link Float#MIN_VALUE} as the x and y components.
	 */
	public static final Wec2f MIN = new Wec2f(Float.MIN_VALUE, Float.MIN_VALUE);
	/** The x component of this vector. */
	public final float x;
	/** The y component of this vector. */
	public final float y;

	public Wec2f(float xIn, float yIn) {
		this.x = xIn;
		this.y = yIn;
	}
	
    public static Vec3d fromPitchYawVector(Wec2f p_189984_0_)
    {
        return Vec3d.fromPitchYaw(p_189984_0_.x, p_189984_0_.y);
    }
}