package com.dearhelp.controllers;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dearhelp.models.DearHelpUsers;
import com.dearhelp.repos.DearHelpUsersRepo;

@Controller
public class AdminController {
	@Autowired
	private DearHelpUsersRepo userRepo;
	
	
	@GetMapping("/admin")
	public String adminDashboard() {
		
		return "adminDashboard";
	}
	
	//list all users
@GetMapping("/listusers")
	public String listAllUsers(Model model) {
	List<DearHelpUsers>  users = userRepo.findAllUsers();
	model.addAttribute("users",users);
	System.out.println(users);
	return "listAllUsers";
}

//list all service providers
@GetMapping("/listsps")
public String listAllSPs(Model model) {
List<DearHelpUsers>  sps = userRepo.findAllSPs();
model.addAttribute("sps",sps);
return "listAllSPs";
}

//admin change password 
@GetMapping("/adminChangePwd")
public String changePwd() {
	
	return "adminChangePwd";
}

@PostMapping("/adminChangePwd")
public String changePwdProcessing( @RequestParam String currentPassword,@RequestParam String newPassword,
		@RequestParam String cnfPassword,RedirectAttributes redirectAttributes,Model model) {
	
	redirectAttributes.addFlashAttribute("message", "New Password and Confirm Password should be same");
	   
	    if(!newPassword.equals(cnfPassword)) {
	    	return "redirect:/adminChangePwd";
	    }
	
	DearHelpUsers user = userRepo.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
	  if(user.getPassword().equals(currentPassword)) {
		  user.setPassword(newPassword);
		  user.setCnfPassword(cnfPassword);
	    	//redirectAttributes.addFlashAttribute("message", "Password changed Successfully");
	    	userRepo.saveUser(user);
	    	 
	        return "redirect:/logout";
	    }
	  redirectAttributes.addFlashAttribute("message", "Please enter correct current password");
	    
		return "redirect:/adminChangePwd";
}



//delete user by id
@GetMapping("/deleteuser1")
public String deleteUserById(@RequestParam String id,RedirectAttributes redirectAttributes,Model model) {
	
	DearHelpUsers user = userRepo.findById(id);
	if(user==null) {
		String message = "No user with id: " + id +" is found.";
		redirectAttributes.addFlashAttribute("message",message);
		return "redirect:/adminDashboard";
	}
	userRepo.deletUserById(id);
	String message2 = "User with id: " + id +" is deleted Successfully.";
	redirectAttributes.addFlashAttribute("message",message2);
	return "redirect:/admin";
}

//delete user by email
@GetMapping("/deleteuser2")
public String deleteUserByEmail(@RequestParam String email,RedirectAttributes redirectAttributes) {
	
	DearHelpUsers user = userRepo.findByEmail(email);
	if(user==null) {
		String message = "No user with Email: " + email +" is found.";
		redirectAttributes.addFlashAttribute("message",message);
		return "redirect:/admin";
	}
	userRepo.deletUserByEmail(email);
	String message2 = "User with Email: " + email +" is deleted Successfully.";
	redirectAttributes.addFlashAttribute("message",message2);
	return "redirect:/admin";
}

//search user by id
@GetMapping("/searchuser1")
public String searchUserById(@RequestParam String id,RedirectAttributes redirectAttributes,Model model) {
	DearHelpUsers user = userRepo.findById(id);
	if(user==null) {
		String message = "No user with id: " + id +" is found.";
		redirectAttributes.addFlashAttribute("message",message);
		return "redirect:/admin";
	}
	if(user.getRole().equals("ROLE_USER")) {
		model.addAttribute("users", user);
		return"listAllUsers";
	}
	model.addAttribute("sps", user);
	return"listAllSPsId";
}

//search user by email
@GetMapping("/searchuser2")
public String searchUserByEmail(@RequestParam String email,RedirectAttributes redirectAttributes,Model model) {
	DearHelpUsers user = userRepo.findByEmail(email);
	if(user==null) {
		String message = "No user with Email: " + email +" is found.";
		redirectAttributes.addFlashAttribute("message",message);
		return "redirect:/admin";
	}
	if(user.getRole().equals("ROLE_USER")) {
		model.addAttribute("users", user);
		return"listAllUsers";
	}
	model.addAttribute("sps", user);
	return"listAllSPsId";
}

// admin top search bar

@GetMapping("/searchSP2")
public String serachSp(@RequestParam String streetName,@RequestParam String serviceType,
		@RequestParam String specialization,Model model) {

	
	if (StringUtils.isBlank(specialization)) {
		List<DearHelpUsers> sps = userRepo.findByStrtNmAndST(streetName,serviceType);
		model.addAttribute("sps", sps);
		return "listAllSPsId";	
	}
	
	List<DearHelpUsers> sps = userRepo.findByStrtNmAndSTAndSpclzn(streetName,serviceType,specialization);
	model.addAttribute("sps", sps);
	return "listAllSPsId";
	
}
	
}







