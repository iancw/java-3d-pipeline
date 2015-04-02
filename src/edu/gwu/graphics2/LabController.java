/**
 * 
 */
package edu.gwu.graphics2;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * This class is the central control for non-gui related work in lab1.  It 
 * contains the two  main objects in the lab:  the Model, and the GraphicsPipeline.
 * 
 * The updateView method creates a new GraphicsPipeline instance with the supplied
 * view properties.  The getRendererdModel() applies transforms generated in
 * GraphicsPipeline based on camera properties to the current Model and returns
 * the result.
 * 
 * @author ian
 *
 */
public class LabController
{
	private final Logger logger = Logger.getLogger(LabController.class.getName());
	
	private GraphicsPipeline pipeline;
	private List<Model> models;
	private IlluminationModel illum;
	private Dimension dim;
	private List<ChangeListener> listeners;
	private LabSettings settings;

	public LabController(GraphicsPipeline pipeline, List<Model> models, IlluminationModel s, LabSettings settings)
	{
		this.pipeline = pipeline;
		this.models = new ArrayList<Model>();
		this.illum = s;
		this.listeners = new CopyOnWriteArrayList<ChangeListener>();
		this.dim = new Dimension(500, 500);
		
		this.models.addAll(models);
		this.settings = settings;
	}
	
	private void notifyListeners()
	{
		for(ChangeListener l : listeners)
		{
			l.stateChanged(new ChangeEvent(getSettings()));
		}
	}
	
	public LabSettings getSettings()
	{
		return settings;
	}
	
	public void addListener(ChangeListener l)
	{
		this.listeners.add(l);
	}
	
	public void removeListener(ChangeListener l)
	{
		this.listeners.remove(l);
	}
	
	/**
	 * This is used by the Lab1Display to set the title bar text
	 * @return
	 */
	public String getModelName()
	{
		if(models.size() == 0)
		{
			return "";
		}
		return models.get(0).getName();
	}
	
	public void setClockwise(boolean clock)
	{
		for(Model model : models)
		{
			model.setClockwise(clock);
		}
	}
	
	public void loadTexture(File texFile)
	{
		BufferedImage img = null;
		try
		{
			img = ImageIO.read(texFile);
			BufferedImageTexture texture = new BufferedImageTexture(img);
			settings.setTexture(texture);
		}catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * This will replace the current model with a model loaded from the supplied
	 * file
	 */
	public void loadModels(File[] files)
	{
		models.clear();
		DataParser parser = new DataParser();
		int i=0;
		int j=0;
		int k=0;
		for(File file : files)
		{
			if(file == null || !file.exists())
			{
				break;
			}
			try
			{
				Model m = parser.parseData(file);
				m.buildTextureCoords();
				Matrix world = Matrix.fromTranslationVector(new Vector(i, j, k));
				i+=2;
				j+=1;
				models.add(m.multiplyAll(world));
			}catch(Exception e)
			{
				logger.warning("Could not parse "+file.getName()+", "+e.getClass().getName()+": "+e.getMessage());
			}
		}
	}
	
	/**
	 * This is used by the gui to populate text fields with initial values
	 * @return
	 */
	public GraphicsPipeline getPipeline()
	{
		return this.pipeline;
	}
	
	/**
	 * This updates camera properties in response to user input 
	 * 
	 * @param reference
	 * @param camera
	 * @param up
	 * @param h
	 * @param d
	 * @param f
	 */
	public void updateView(LabSettings settings, Dimension dim)
	//Vector reference, Vector camera, Light light, Vector up, float h, float d, float f, Dimension dim)
	{
		this.dim = dim;
		pipeline.updateSettings(settings);
		Material mat = new Material(settings.getKa(),
				settings.getKd(),
				settings.getKs(),
				settings.getTexture(),
				settings.getSpecularity());
				
		for(Model model : models)
		{
			model.setMaterial(mat);
		}
		this.settings = settings;
		notifyListeners();
	}
	
	public void updateCamera(Vector camera)
	{
		settings.setCamera(camera);
		updateView(settings, this.dim);
	}
	
	public void updateLight(Light light)
	{
		for(Model mod : models)
		{
			mod.setLightVector(light.getPosition());
		}
		settings.setLight(light);
		
		updateView(settings, dim);
	}
	
	public void updateReference(Vector reference)
	{
		settings.setReference(reference);
		updateView(settings, dim);
	}
	
	public void moveCamera(float x, float y, float z)
	{
		Vector curCam = pipeline.getCamera();
		updateCamera(new Vector(curCam.getX()+x, curCam.getY()+y, curCam.getZ()+z));
	}
	
	public void moveReference(float x, float y, float z)
	{
		Vector curCam = pipeline.getReference();
		updateReference(new Vector(curCam.getX()+x, curCam.getY()+y, curCam.getZ()+z));
	}
	
	public void moveLight(float x, float y, float z)
	{
		Light curLight = pipeline.getLight();
		Vector curLightPos = curLight.getPosition();
		updateLight(new Light(new Vector(curLightPos.getX()+x, curLightPos.getY()+y, curLightPos.getZ()+z), curLight.getColor()));
	}
	
	public void moveLightPolar(double az, double el)
	{
		Light curLight = pipeline.getLight();
		Vector curLightPos = curLight.getPosition();
		float x = curLightPos.getX();
		float y = curLightPos.getY();
		float z = curLightPos.getZ();
		
		//do polar conversion here
		double theta = Math.atan(y/x);
		double rho = Math.atan(z/x);
		double radius = Math.sqrt(x*x + y*y + z*z);
		
		theta += el;
		rho += az;
		
		while(theta > Math.PI*2)
		{
			theta = theta - Math.PI*2; 
		}
		
		while(rho > Math.PI*2)
		{
			rho = rho - Math.PI*2;
		}
		
		if(radius > 10000)
		{
			radius = 10000;
		}
		
		double newx = radius * Math.cos(theta);
		double newy = radius * Math.sin(theta);
		double newz = radius * Math.sin(rho);
		if(Double.isNaN(newx))
			newx=10;
		if(Double.isNaN(newy))
			newy=0;
		if(Double.isNaN(newz))
			newz=0;
		Vector newPos = new Vector((float)newx, (float)newy, (float)newz);
		updateLight(new Light(newPos, curLight.getColor()));
	}	

	
	public void renderModels(DisplayHandler handler)
	{
		handler.renderModels(
				getRenderedModels(), 
				pipeline.getLight(), 
				pipeline.getCamera(), 
				new ConfigurableShaderFactory(illum, settings.getShadingModel()));
	}
	
	/**
	 * This sends the Model through the GraphicsPipeline and returns the resulting
	 * transformed Model.
	 * @return
	 */
	public List<Model> getRenderedModels()
	{
		List<Model> rendered = new LinkedList<Model>();
		for(Model model : models)
		{
			rendered.add(pipeline.renderModel(model));
		}
		return rendered;
	}
}
