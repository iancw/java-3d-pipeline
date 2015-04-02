package edu.gwu.graphics2;

import java.io.File;

import edu.gwu.graphics2.ConfigurableShading.ShadingMode;

public class LabSettings {
	private Vector camera;
	private Vector ref;
	private Light light;
	private Vector up;
	private Vector ka;
	private Vector ks;
	private Vector kd;
	private float d;
	private float h;
	private float f;
	private int specularExponent;
	private ShadingMode shader;
	private BufferedImageTexture texture;
	
	/**
	 * 
	 * @param camera
	 * @param ref
	 * @param up
	 * @param d
	 * @param h
	 * @param f
	 * @param light
	 * @param ka
	 * @param kd
	 * @param ks
	 */
	public LabSettings(
			Vector camera, 
			Vector ref, 
			Vector up, 
			float d, float h, float f,
			Light light, 
			Vector ka, Vector kd, Vector ks,
			int specularExponent,
			ShadingMode shader,
			BufferedImageTexture texture)
	{
		this.camera = camera;
		this.ref = ref;
		this.up = up;
		this.light = light;
		this.ka = ka;
		this.kd = kd;
		this.ks = ks;
		this.specularExponent = specularExponent;
		this.d = d;
		this.h=h;
		this.f=f;
		this.shader = shader;
		this.texture = texture;
	}
	public void setTexture(BufferedImageTexture texture){ this.texture = texture; }
	public void setCamera(Vector cam){	this.camera = cam;	}
	public void setReference(Vector ref){	this.ref = ref;	}
	public void setLight(Light lig)	{this.light = lig;}
	public void setKa(Vector ka){	this.ka = ka;	}
	public void setKd(Vector kd){	this.kd = kd;	}
	public void setKs(Vector ks){	this.ks = ks;	}
	public void setSpecularity(int spec){	this.specularExponent = spec;}
	public void setShadingModel(ShadingMode model){this.shader = model;}
	public BufferedImageTexture getTexture(){ return this.texture; }
	public Vector getCamera(){	return camera;	}
	public Light getLight(){	return light;}
	public Vector getReference(){	return ref;}	
	public Vector getUp(){	return up;}
	public Vector getKa(){return ka;}
	public Vector getKs(){return ks;}
	public Vector getKd(){return kd;}
	public float getD()	{	return d;	}
	public float getH(){	return h;	}
	public float getF(){	return f;	}
	public int getSpecularity(){return specularExponent;}
	public ShadingMode getShadingModel(){return shader;}
	

}
