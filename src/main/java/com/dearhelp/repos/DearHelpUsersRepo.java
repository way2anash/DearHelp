package com.dearhelp.repos;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.dearhelp.models.DearHelpUsers;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;



@Component
public class DearHelpUsersRepo {

	@Autowired
    private MongoTemplate mongoTemplate;
	
	public void saveUser(DearHelpUsers user) {
		mongoTemplate.save(user);
		
	}
	
	public DearHelpUsers findByEmail(String email) {
		 Query query = new Query();
	        query.addCriteria(Criteria.where("email").is(email));
	        return mongoTemplate.findOne(query, DearHelpUsers.class);
	}
	
	public DearHelpUsers findByResetToken(String token) {
		Query query = new Query();
        query.addCriteria(Criteria.where("resetToken").is(token));
        return mongoTemplate.findOne(query, DearHelpUsers.class);
	}

	public DearHelpUsers findByPassword(String password) {
		Query query = new Query();
        query.addCriteria(Criteria.where("password").is(password));
		return mongoTemplate.findOne(query, DearHelpUsers.class);
	}

	
	//calculating lattitude and logitude
	public GeocodingResult[] findLatAndLang(String streetName) {
		
		GeoApiContext context = new GeoApiContext.Builder()
			    .apiKey("Enter your google api key")
			    .build();
			GeocodingResult[] results=null;
			try {
				results = GeocodingApi.geocode(context,
				    streetName).await();
			} catch (ApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		return results;
	}

	

	
	//calculating rating value
		public String findRatingvalue(ArrayList<Integer> ratingNumbers) {
			int maxRatings ,avgRatings=0,totalRatings=0;
			
			for (int rating : ratingNumbers) {
				totalRatings= totalRatings + rating;
			
			}
			
			maxRatings = totalRatings*5;
			
			for(int i=0;i<5;i++) {
				avgRatings = avgRatings + (ratingNumbers.get(i) * (i+1));
				
			}
			float rating = (float) (avgRatings *5)/ maxRatings;
			DecimalFormat df = new DecimalFormat(".##");
			return df.format(rating);
		}

		
		// finding sp by streetname and servicetype
		
		public List<DearHelpUsers> findByStrtNmAndST(String streetName, String serviceType) {
		
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			GeocodingResult[] results = findLatAndLang( streetName);
			double longitude = Double.parseDouble(gson.toJson(results[0].geometry.location.lng));
			double lattitude = Double.parseDouble(gson.toJson(results[0].geometry.location.lat));
			System.out.println("lattitude : "+lattitude);
			System.out.println("longitude : "+longitude);              
			int distance = 3;
			Point basePoint = new Point(longitude,lattitude);
			Distance radius = new Distance(distance,Metrics.MILES);
			Circle area = new Circle(basePoint,radius);
			Query query = new Query();
			query.addCriteria(Criteria.where("address.geoLocation").withinSphere(area)
			           .andOperator( Criteria.where("serviceType").is(serviceType)));
			
			  List<DearHelpUsers> sps=  mongoTemplate.find(query, DearHelpUsers.class); 
			//List<DearHelpUsers> sps= mongoTemplate.findAll(DearHelpUsers.class);
			   System.out.println(sps);
			   
			return sps;
		}

		//post login landing page index1
		public List<DearHelpUsers> findByStreetName(String streetName) {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			GeocodingResult[] results = findLatAndLang( streetName);
			double longitude = Double.parseDouble(gson.toJson(results[0].geometry.location.lng));
			double lattitude = Double.parseDouble(gson.toJson(results[0].geometry.location.lat));
			System.out.println("lattitude : "+lattitude);
			System.out.println("longitude : "+longitude);              
			int distance = 3;
			Point basePoint = new Point(longitude,lattitude);
			Distance radius = new Distance(distance,Metrics.MILES);
			Circle area = new Circle(basePoint,radius);
			Query query = new Query();
			query.addCriteria(Criteria.where("address.geoLocation").withinSphere(area)
			           .andOperator( Criteria.where("role").is("ROLE_SP")));         
			
			  List<DearHelpUsers> sps=  mongoTemplate.find(query, DearHelpUsers.class); 
			
			   System.out.println(sps);
			   
			return sps;
		}

		//find by streetName, serviceType and specialization
		public List<DearHelpUsers> findByStrtNmAndSTAndSpclzn(String streetName, String serviceType,
				String specialization) {
			
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			GeocodingResult[] results = findLatAndLang( streetName);
			double longitude = Double.parseDouble(gson.toJson(results[0].geometry.location.lng));
			double lattitude = Double.parseDouble(gson.toJson(results[0].geometry.location.lat));
			System.out.println("lattitude : "+lattitude);
			System.out.println("longitude : "+longitude);              
			int distance = 3;
			Point basePoint = new Point(longitude,lattitude);
			Distance radius = new Distance(distance,Metrics.MILES);
			Circle area = new Circle(basePoint,radius);
			Query query = new Query();
			query.addCriteria(Criteria.where("address.geoLocation").withinSphere(area)
			           .andOperator( Criteria.where("serviceType").is(serviceType),
			        		   Criteria.where("specialization").is(specialization)));
			
			  List<DearHelpUsers> sps=  mongoTemplate.find(query, DearHelpUsers.class); 
			
			   System.out.println(sps);
			   
			return sps;
		}

		
		//Admin controller
		
		public List<DearHelpUsers> findAllUsers() {
			Query query = new Query();
			  query.addCriteria(Criteria.where("role").is("ROLE_USER"));
			  List<DearHelpUsers> users=  mongoTemplate.find(query, DearHelpUsers.class); 
			
			return users;
		}
		//finding All service providers
		public List<DearHelpUsers> findAllSPs() {
			Query query = new Query();
			  query.addCriteria(Criteria.where("role").is("ROLE_SP"));
			  List<DearHelpUsers> sps=  mongoTemplate.find(query, DearHelpUsers.class); 
			
			return sps;
		}

		public DearHelpUsers findById(String id) {
			Query query = new Query();
	        query.addCriteria(Criteria.where("id").is(id));
	        return mongoTemplate.findOne(query, DearHelpUsers.class);
		}

		public void deletUserById(String id) {
			Query query = new Query();
	        query.addCriteria(Criteria.where("id").is(id));
	        mongoTemplate.findAndRemove(query, DearHelpUsers.class);
			
		}

		public void deletUserByEmail(String email) {
			Query query = new Query();
	        query.addCriteria(Criteria.where("email").is(email));
	        mongoTemplate.findAndRemove(query, DearHelpUsers.class);
			
		}
		
		
}
