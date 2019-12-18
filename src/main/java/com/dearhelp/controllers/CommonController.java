package com.dearhelp.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dearhelp.models.DearHelpUsers;
import com.dearhelp.models.Messages;

import com.dearhelp.repos.DearHelpUsersRepo;

@Controller
public class CommonController {

	@Autowired
	private DearHelpUsersRepo userRepo;
	
	
	//will be converting GET into post Mapping
	@GetMapping("/profile")
	public String getProfile(Model model) {
		
		DearHelpUsers user = userRepo.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
		System.out.println(user);
		if(user.getRole().equals("ROLE_USER")){
			model.addAttribute("user", user);
			return"profileuser";	
		}
		
		
		model.addAttribute("sp",user);
		return"profilesp";
	}
	
	//editing profile
	@GetMapping("/editProfile")
	public String editProfile(Model model) {
		DearHelpUsers user = userRepo.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
		if(user.getRole().equals("ROLE_USER")) {
			
			model.addAttribute("user", user);
			return"edituserprofile";
		}
		model.addAttribute("sp",user);
		return "editspprofile";
	}
	
	@GetMapping("/changePwd")
	public String changePwd() {
		
		return "changePwd";
	}
	
	@PostMapping("/changePwd")
	public String changePwdProcessing( @RequestParam String currentPassword,@RequestParam String newPassword,
			@RequestParam String cnfPassword,RedirectAttributes redirectAttributes,Model model) {
		
		redirectAttributes.addFlashAttribute("message", "New Password and Confirm Password should be same");
		   
		    if(!newPassword.equals(cnfPassword)) {
		    	return "redirect:/changePwd";
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
		    
			return "redirect:/changePwd";
	}
	
	// for anonymous users
	@PostMapping("/contactform")
	public String contactForm( @RequestParam String name,@RequestParam String emailId,
			 @RequestParam String queries, Principal userCheck) {
		DearHelpUsers user = userRepo.findByEmail("way2dearhelp@gmail.com");
		
		ArrayList<Messages> tempMessages = (user.getMessages()==null ? new ArrayList<Messages>() : user.getMessages());
		
		Messages message = new Messages();
		message.setName(name);
		message.setEmailId(emailId);
		message.setQueries(queries);
		message.setCreatedDate(new Date());
		
		tempMessages.add(message);
		user.setMessages(tempMessages);
	    userRepo.saveUser(user);
	    
	    if(userCheck==null) {
	    	
	    	return"contactinfo";	
	    }
	    return"contactinfo1";
	}
		
	@GetMapping("/messages")
	public String showMessages(Model model) {
		
		DearHelpUsers user = userRepo.findByEmail("way2dearhelp@gmail.com");
		ArrayList<Messages> m = user.getMessages();
		ArrayList<Messages> messages = new ArrayList<Messages>();
		int length= user.getMessages().size()-1;
		for(int i=length; i>=0 ;i--) {
			messages.add(m.get(i));
		}
		model.addAttribute("messages", messages);
		return"adminMessages";
	}
	
}






