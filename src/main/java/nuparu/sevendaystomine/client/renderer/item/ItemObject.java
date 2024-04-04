package nuparu.sevendaystomine.client.renderer.item;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ItemObject {

	public ItemObject parent = null;;
	public Transform localTransform = Transform.ZERO;
	public List<ItemObject> children = new ArrayList<ItemObject>();
	
	public ResourceLocation res = null;
	public IModel model = null;
	public IBakedModel baked = null;
	
	public ItemObject(ResourceLocation res) {
		this.res = res;
		try {
			model = OBJLoader.INSTANCE.loadModel(res);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(model != null) {
			baked = model.bake(TRSRTransformation.identity(), DefaultVertexFormats.ITEM,
                    ModelLoader.defaultTextureGetter());
		}
		
	}
	
	public void doRender(double x, double y, double z, int step) {
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);
		Transform transform = localTransform;
		Vec3d pos = transform.getPostion();
		Vec3d rot = transform.getRotation();
		Vec3d scale = transform.getScale();
				
		GL11.glTranslated(pos.x, pos.y, pos.z);
		GL11.glRotated(rot.x, 1, 0, 0);
		GL11.glRotated(rot.y, 0, 1, 0);
		GL11.glRotated(rot.z, 0, 0, 1);
		GL11.glScaled(scale.x, scale.y, scale.z);
		//RENDER MODEL
		for(ItemObject child : children) {
			child.doRender(0, 0, 0, step);
		}
		
		GL11.glPopMatrix();
	}
	
	public void doAnimationStep() {
		
	}
	
	public int getStepsCount() {
		return 0;
	}
	
	public int getTickPerStep() {
		return 20;
	}
	
	public Transform getGlobalTransform() {
		if(parent==null) return localTransform;
		return localTransform.combineReverse(parent.getGlobalTransform());
	}
	
	
	public static class Transform{
		private double x;
		private double y;
		private double z;
		
		private double xRot;
		private double yRot;
		private double zRot;
		
		private double xScale;
		private double yScale;
		private double zScale;
		
		public static final Transform ZERO = new Transform();
		
		public Transform(double x, double y, double z, double xRot, double yRot, double zRot, double xScale, double yScale, double zScale) {
			this.x = x;
			this.y = y;
			this.z = z;
			this.xRot = xRot;
			this.yRot = yRot;
			this.zRot = zRot;
			this.xScale = xScale;
			this.yScale = yScale;
			this.zScale = zScale;
		}
		
		public Transform(double x, double y, double z) {
			this(x,y,z,0,0,0,0,0,0);
		}
		
		public Transform() {
			this(0,0,0,0,0,0,0,0,0);
		}
		
		public void setPosition(double x, double y, double z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
		
		public void setRotation(double xRot, double yRot, double zRot) {
			this.xRot = xRot;
			this.yRot = yRot;
			this.zRot = zRot;
		}
		
		public void setScale(double xScale, double yScale, double zScale) {
			this.xScale = xScale;
			this.yScale = yScale;
			this.zScale = zScale;
		}
		
		public void translate(double x, double y, double z) {
			this.x += x;
			this.y += y;
			this.z += z;
		}
		
		public void rotate(double xRot, double yRot, double zRot) {
			this.xRot += xRot;
			this.yRot += yRot;
			this.zRot += zRot;
		}
		
		public void scale(double xScale, double yScale, double zScale) {
			this.xScale *= xScale;
			this.yScale *= yScale;
			this.zScale *= zScale;
		}
		
		public void setPosition(Vec3d vec) {
			this.x = vec.x;
			this.y = vec.y;
			this.z = vec.z;
		}
		
		public void setRotation(Vec3d vec) {
			this.xRot = vec.x;
			this.yRot = vec.y;
			this.zRot = vec.z;
		}
		
		public void setScale(Vec3d vec) {
			this.xScale = vec.x;
			this.yScale = vec.y;
			this.zScale = vec.z;
		}
		
		public void translate(Vec3d vec) {
			this.x += vec.x;
			this.y += vec.y;
			this.z += vec.z;
		}
		
		public void rotate(Vec3d vec) {
			this.xRot += vec.x;
			this.yRot += vec.y;
			this.zRot += vec.y;
		}
		
		public void scale(Vec3d vec) {
			this.xScale *= vec.x;
			this.yScale *= vec.y;
			this.zScale *= vec.z;
		}
		
		public Vec3d getPostion() {
			return new Vec3d(x,y,z);
		}
		
		public Vec3d getRotation() {
			return new Vec3d(xRot,yRot,zRot);
		}
		
		public Vec3d getScale() {
			return new Vec3d(xScale,yScale,zScale);
		}
		
		public Transform copy() {
			Transform transform = new Transform();
			transform.setPosition(x, y, z);
			transform.setRotation(xRot, yRot, zRot);
			transform.setScale(xScale, yScale, zScale);
			
			return transform;
		}
		
		public Transform combine(Transform other) {
			Transform transform = this.copy();
			transform.translate(other.getPostion());
			transform.rotate(other.getRotation());
			transform.scale(other.getScale());
			return transform;
		}
		
		public Transform combineReverse(Transform other) {
			Transform transform = other.copy();
			transform.translate(getPostion());
			transform.rotate(getRotation());
			transform.scale(getScale());
			return transform;
		}
	}
}
