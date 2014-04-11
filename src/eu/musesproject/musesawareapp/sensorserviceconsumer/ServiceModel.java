/*
 * MUSES High-Level Object Oriented Model
 * Copyright MUSES project (European Commission FP7) - 2013 
 */

package eu.musesproject.musesawareapp.sensorserviceconsumer;


public class ServiceModel {

	private static ServiceModel instance;
	MusesServiceConsumer service;
	
	
	public ServiceModel() {
	
	}
	
	public static ServiceModel getInstance() {
		if (instance == null)
			instance = new ServiceModel();
		return instance;

	}
	
	/**
	 * Starts Muses Service
	 * @param service
	 */
	
	public void setService(MusesServiceConsumer service){
		this.service = service;
	}
	
	/**
	 * Return Muses Service instance
	 * @return
	 */
	
	public MusesServiceConsumer getService(){
		return this.service;
	}
}
