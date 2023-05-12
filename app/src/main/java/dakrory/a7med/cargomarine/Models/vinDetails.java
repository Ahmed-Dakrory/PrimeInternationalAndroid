package dakrory.a7med.cargomarine.Models;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class vinDetails {

	
	  @SerializedName("Count")
	    public Integer Count;
	    
	    @SerializedName("Message")
	    public String Message;
	    
	    @SerializedName("SearchCriteria")
	    public String SearchCriteria;
	    
	    
	    @SerializedName("Results")
	    public List<Results> Results;
	    
	    
	    public class Results{
	    	 @SerializedName("BodyClass")
	 	    public String BodyClass;
	    	 
	    	 @SerializedName("BrakeSystemType")
	 	    public String BrakeSystemType;
	    	 
	    	 @SerializedName("DisplacementCC")
	 	    public String DisplacementCC;
	    	 
	    	 @SerializedName("DisplacementCI")
	 	    public String DisplacementCI;
	    	 
	    	 @SerializedName("DisplacementL")
	 	    public String DisplacementL;
	    	 
	    	 @SerializedName("DriveType")
	 	    public String DriveType;
	    	 
	    	 @SerializedName("EngineConfiguration")
	 	    public String EngineConfiguration;

			@SerializedName("FuelTypePrimary")
			public String FuelTypePrimary;


			@SerializedName("FuelTypeSecondary")
			public String FuelTypeSecondary;
	    	 

	    	 @SerializedName("GVWR")
		 	 public String GVWR;
		    	 
		    @SerializedName("Make")
		 	public String Make;
		    	 
		    	 
		    	 
		    @SerializedName("Manufacturer")
			public String Manufacturer;
			    	 
		    @SerializedName("ManufacturerId")
			public String ManufacturerId;
		    

		    @SerializedName("Model")
			public String Model;

		    @SerializedName("ModelYear")
			public String ModelYear;


		    
		    @SerializedName("EngineCylinders")
			public String EngineCylinders;
		    

		    
		    @SerializedName("NCSAModel")
			public String NCSAModel;
		    
		    
		    @SerializedName("PlantCity")
			public String PlantCity;
		    
		    @SerializedName("PlantCompanyName")
			public String PlantCompanyName;
		    
		    
		    @SerializedName("PlantCountry")
			public String PlantCountry;
		    
		    
		    
		    @SerializedName("PlantState")
			public String PlantState;
		    
		    
		    @SerializedName("Series")
			public String Series;
		    
		    
		    @SerializedName("Series2")
			public String Series2;
		    
		    
		    @SerializedName("SteeringLocation")
			public String SteeringLocation;
		    
		    
		    @SerializedName("Trim")
			public String Trim;
		    
		    
		    
		    @SerializedName("VIN")
			public String VIN;
		    
		    
		    @SerializedName("VehicleType")
			public String VehicleType;
	    	 
	    	 
	    	
	    }
}
