package com.dearhelp.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
import com.dearhelp.repos.DearHelpUsersRepo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.model.GeocodingResult;



@Controller
public class DearHelpUsersController {

	
	
	@Autowired
	private DearHelpUsersRepo userRepo;
	
	@Autowired
    private UploadService uploadService;
	
	
	@GetMapping("/index")
    public String getIndex() {
        
        return "index";
    }
	
	@GetMapping("/index1")
    public String getIndex1(Model model) {
        DearHelpUsers user = userRepo.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
		String streetName = user.getAddress().getStreetName()+" ,"+user.getAddress().getCity();
        
        List<DearHelpUsers> sps = userRepo.findByStreetName(streetName);
        model.addAttribute("email",user.getEmail());
        System.out.println("Sending user email in index1 for seeing profile: "+ user.getEmail());
        model.addAttribute("streetName", streetName);
        model.addAttribute("sps", sps);
        return "index1";
    }
	
	//Anonymous about
	@GetMapping("/about")
    public String getAboutUs() {
        
        return "about";
    }
	
	//User about
	@GetMapping("/about1")
    public String getAboutUsUser() {
        
        return "about1";
    }
	
	//Anonymous contact
	@GetMapping("/contactinfo")
    public String getContact() {
        
        return "contactinfo";
    }
	
	@GetMapping("/contactinfo1")
    public String getContactUser(Model model) {
        
		model.addAttribute("email", SecurityContextHolder.getContext().getAuthentication().getName());
        return "contactinfo1";
    }
	
		
	//user login page
	@GetMapping("/login")
    public String userLogin() {
        
        return "login";
    }
	
	// user registration get method
		@GetMapping("/regfuser")
	    public String regUserForm(Model model) {
	        
			model.addAttribute("user", new DearHelpUsers());
	        return "regfuser";
	    }
	
	
	//user registration processing
	@PostMapping("/regForm")
	public String userRegForm( @ModelAttribute DearHelpUsers user,RedirectAttributes redirectAttributes,
			@ModelAttribute("file") MultipartFile file) throws AmazonServiceException, SdkClientException, IOException, InvalidFileFormatException, InvalidFileSizeException {
			   
		DearHelpUsers tempUser = userRepo.findByEmail(user.getEmail());
		if(tempUser!=null) {
			redirectAttributes.addFlashAttribute("message", " Email already exists , try using different email");
			return  "redirect:regfuser";  
		}
		
		user.setRole("ROLE_USER");
	   
	    
		
	    //checking file size
	   
	   
	    
	   if(file.isEmpty()) {
		   redirectAttributes.addFlashAttribute("message", " Please upload picture along with form");
			return  "redirect:regfuser"; 
		   
	   }
	    //aws file processing
		String imageUrl ="";

        imageUrl = uploadService.uploadImage(file);
		
        GeocodingResult[] results=userRepo.findLatAndLang(user.getAddress().getStreetName());
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Address address = user.getAddress();
        Location location = new Location();
        location.setLattitude(Float.parseFloat(gson.toJson(results[0].geometry.location.lat)));
        location.setLongitude(Float.parseFloat(gson.toJson(results[0].geometry.location.lng)));
        address.setGeoLocation(location);
        //user.setAddress(address);
     //   user.setLattitude(Float.parseFloat(gson.toJson(results[0].geometry.location.lat)));
     //   user.setLongitude(Float.parseFloat(gson.toJson(results[0].geometry.location.lng)));
		
		user.setImageUrl(imageUrl);
		
		userRepo.saveUser(user);
		//model.addAttribute("message","Your Profile Saved Successfully");
		redirectAttributes.addFlashAttribute("message", " Your Profile Saved Successfully");
		return  "redirect:regfuser"; 
		
	}
	
	
	//updating user profile
	
	
	@PostMapping("/updateUserProfile")
	public String updateProfile(@ModelAttribute DearHelpUsers user,RedirectAttributes redirectAttributes,
			@ModelAttribute("file") MultipartFile file,Model model) throws AmazonServiceException, SdkClientException, IOException, InvalidFileFormatException, InvalidFileSizeException {
		
		DearHelpUsers tempUser = userRepo.findById(user.getId());
		
		if(!file.isEmpty()) {
			String imageUrl ="";

	        imageUrl = uploadService.uploadImage(file);
	        tempUser.setImageUrl(imageUrl);
	        
		   }
		 GeocodingResult[] results=userRepo.findLatAndLang(user.getAddress().getStreetName());
	        Gson gson = new GsonBuilder().setPrettyPrinting().create();
	        Address address = tempUser.getAddress();
	        Location location = new Location();
	        location.setLattitude(Float.parseFloat(gson.toJson(results[0].geometry.location.lat)));
	        location.setLongitude(Float.parseFloat(gson.toJson(results[0].geometry.location.lng)));
	        tempUser.setAddress(user.getAddress());
	        address.setGeoLocation(location);
	        //address.setStreetName(user.getAddress().getStreetName());
	       // address.setCity(user.getAddress().getCity());
	        tempUser.setUserName(user.getUserName());
	        tempUser.setPhoneNumber(user.getPhoneNumber());
	        userRepo.saveUser(tempUser);
	        model.addAttribute("user",tempUser);
		return "profileuser";
	}
	
	
}







