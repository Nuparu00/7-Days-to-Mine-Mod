package nuparu.ni.methods;

public class Methods {

	public static final Method SEND_PACKET = new MethodSendPacket();
	public static final Method RANDOM = new MethodRandom();
	public static final Method PRINT = new MethodPrint();
	public static final Method SAVE = new MethodSave();
	public static final Method POW = new MethodPow();
	public static final Method SQRT = new MethodSqrt();
	public static final Method GET_WORLD_TICKS = new MethodGetWorldTicks();
	public static final Method SEND_REDSTONE_SIGNAL = new MethodSendRedstoneSignal();
	
	public static final Method[] METHODS = new Method[] {
			SEND_PACKET, RANDOM, PRINT, SAVE, POW, SQRT, GET_WORLD_TICKS, SEND_REDSTONE_SIGNAL
	};
	
	
	public static Method getByName(String name) {
		for(Method method : METHODS) {
			if(method.name.equals(name)) {
				return method;
			}
		}
		return null;
	}
}
