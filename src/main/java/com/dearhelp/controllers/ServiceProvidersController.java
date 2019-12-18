package com.dearhelp.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.dearhelp.amazons3fileuploader.InvalidFileFormatException;
import com.dearhelp.amazons3fileuploader.InvalidFileSizeException;
import com.dearhelp.amazons3fileuploader.UploadService;
import com.dearhelp.models.Address;
import com.dearhelp.models.DearHelpUsers;
import com.dearhelp.models.Location;
import com.dearhelp.models.Reviews;
import com.dearhelp.repos.DearHelpUsersRepo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.model.GeocodingResult;


@Controller
public class ServiceProvidersController {

	@Autowired
	private DearHelpUsersRepo userRepo;
	
	@Autowired
    private UploadService uploadService;
	
	
	
	@GetMapping("/regfsv")
	public String regSPForm(Model model) {
		model.addAttribute("sp", new DearHelpUsers());
		return "regfsv";
	}
	//registering service provider
	
	@PostMapping("/regServiceProvider")
	public String userRegForm(  @ModelAttribute DearHelpUsers sp,RedirectAttributes redirectAttributes,
			@ModelAttribute("file") MultipartFile file) throws AmazonServiceException, SdkClientException, IOException, InvalidFileFormatException, InvalidFileSizeException {
			   
		 if(file.isEmpty()) {
			   redirectAttributes.addFlashAttribute("message", " Please upload picture along with form");
				return  "redirect:regfsv"; 
			   
		   }
		
		 DearHelpUsers tempUser = userRepo.findByEmail(sp.getEmail());
		if(tempUser!=null) {
			redirectAttributes.addFlashAttribute("message", " Email already exists , try using different email");
			return  "redirect:regfsv";  
		}
	
		sp.setRole("ROLE_SP");
	  
	    //aws file processing
		String imageUrl ="";

      imageUrl = uploadService.uploadImage(file);
		
      GeocodingResult[] results=userRepo.findLatAndLang(sp.getAddress().getStreetName());
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      Address address = sp.getAddress();
      Location location = new Location();
      location.setLattitude(Float.parseFloat(gson.toJson(results[0].geometry.location.lat)));
      location.setLongitude(Float.parseFloat(gson.toJson(results[0].geometry.location.lng)));
      address.setGeoLocation(location);
		
		sp.setImageUrl(imageUrl);
		
		//code for reviews and ratings
		
		 ArrayList<Integer> ratingNumbers = new ArrayList<Integer>();
			for(int i=0;i<5;i++) {
				ratingNumbers.add(0);
			
			}
			sp.setRatingNumbers(ratingNumbers);
			sp.setRating("0");
		
		
		userRepo.saveUser(sp);
		//model.addAttribute("message","Your Profile Saved Successfully");
		redirectAttributes.addFlashAttribute("message", " Your Profile Saved Successfully");
		return  "redirect:regfsv"; 
	}
	

	
	
	//adding reviews to service provider
	@GetMapping("/addReview")
	public String addReview(@RequestParam String email,@RequestParam String review,
			                @RequestParam int ratingValue,@RequestParam String id,Model model) {
		
		DearHelpUsers sp = userRepo.findById(id);
		
		
		ArrayList<Reviews> tempreview = (sp.getReviews()==null ? new ArrayList<Reviews>() : sp.getReviews() );
		Reviews reviews = new Reviews();
		reviews.setEmail(email);
		reviews.setReview(review);
		reviews.setRatingValue(ratingValue);
		reviews.setCreatedDate(new Date());
		tempreview.add(reviews);
	//	System.out.println(tempreview);
		sp.setReviews(tempreview);
		
		
			ArrayList<Integer> ratingNumbers= sp.getRatingNumbers();
			System.out.println(ratingNumbers);
			for(int i=0;i<5;i++) {
				if(ratingValue==(i+1)) {
					ratingNumbers.set(i, ratingNumbers.get(i)+1);
					System.out.println("Rating Number: "+ ratingNumbers.get(i));
				}
			}
			
			//calculating actual rating values
			String rating= userRepo.findRatingvalue(ratingNumbers);
			sp.setRating(rating);    
		
			userRepo.saveUser(sp);
		
		
		
		model.addAttribute("reviews",tempreview);
		System.out.println(tempreview);
		model.addAttribute("sp",sp);
		return"spprofile";
	}
	
	
	
	@PostMapping("/updateSPProfile")
	public String updateSPProfile(  @ModelAttribute DearHelpUsers sp,RedirectAttributes redirectAttributes,
			@ModelAttribute("file") MultipartFile file,Model model) throws AmazonServiceException, SdkClientException, IOException, InvalidFileFormatException, InvalidFileSizeException {
			 
		DearHelpUsers tempSp = userRepo.findById(sp.getId());
		if(!file.isEmpty()) {
			String imageUrl ="";

	        imageUrl = uploadService.uploadImage(file);
	        tempSp.setImageUrl(imageUrl);
	        
		   }
		
		
      GeocodingResult[] results=userRepo.findLatAndLang(sp.getAddress().getStreetName());
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      Address address = sp.getAddress();
      Location location = new Location();
      location.setLattitude(Float.parseFloat(gson.toJson(results[0].geometry.location.lat)));
      location.setLongitude(Float.parseFloat(gson.toJson(results[0].geometry.location.lng)));
	 
      tempSp.setUserName(sp.getUserName());
      tempSp.setBusinessName(sp.getBusinessName());
      tempSp.setServiceType(sp.getServiceType());
      tempSp.setSpecialization(sp.getSpecialization());
      tempSp.setAboutUs(sp.getAboutUs());
      tempSp.setWorkingTime(sp.getWorkingTime());
      tempSp.setClosingDay(sp.getClosingDay());
      tempSp.setPhoneNumber(sp.getPhoneNumber());
	  tempSp.setAddress(sp.getAddress());
	  address.setGeoLocation(location);
		
		userRepo.saveUser(tempSp);
		model.addAttribute("sp",tempSp);
		return  "profilesp"; 
	}
	 
	//Search sp based on StreetName and service Type
	
	@GetMapping("/searchSP")
	public String serachSp(@RequestParam String streetName,@RequestParam String serviceType,
			@RequestParam String specialization,Model model) {

		
		if (StringUtils.isBlank(specialization)) {
			List<DearHelpUsers> sps = userRepo.findByStrtNmAndST(streetName,serviceType);
			model.addAttribute("sps", sps);
			model.addAttribute("streetName", streetName);
			return "randomsplist";	
		}
		
		List<DearHelpUsers> sps = userRepo.findByStrtNmAndSTAndSpclzn(streetName,serviceType,specialization);
		model.addAttribute("sps", sps);
		model.addAttribute("streetName", streetName);
		return "randomsplist";
		
	}

	@GetMapping("/spprofile")
	public String showSP(@RequestParam String email,Model model){
		DearHelpUsers sp = userRepo.findByEmail(email);
		model.addAttribute("email", SecurityContextHolder.getContext().getAuthentication().getName());
		model.addAttribute("reviews", sp.getReviews());   
		model.addAttribute("sp", sp);
		return"spprofile";
	}
	
	//search by categories
	@GetMapping("/{categories}")
	
	public String spByCategories(@PathVariable("categories") String serviceType, Model model) {
		DearHelpUsers user = userRepo.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
		String streetName= user.getAddress().getStreetName()+ " " + user.getAddress().getCity();
		List<DearHelpUsers> sps = userRepo.findByStrtNmAndST(streetName,serviceType);
		model.addAttribute("sps", sps);
		model.addAttribute("streetName", streetName);
		return"randomsplist";
	}
}






