package com.dearhelp.controllers;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dearhelp.models.DearHelpUsers;
import com.dearhelp.repos.DearHelpUsersRepo;
import com.dearhelp.services.EmailService;


@Controller
public class DearHelpFrogetPwdController {
	private String resetToken;
	@Autowired 
	private EmailService emailService;
	@Autowired
	private DearHelpUsersRepo userRepo;
	
	@GetMapping("/forgetPwd")
    public String userForgetPwd() {
        
        return "forgetPwd";
    }
	
	@PostMapping("/forgetPassword")
	public String emailForgetPwd(HttpServletRequest request,@RequestParam("email") String email,Model model) {
		DearHelpUsers user = userRepo.findByEmail(email);
		user.setResetToken(UUID.randomUUID().toString());
		
		userRepo.saveUser(user);
		String appUrl = request.getScheme() + "://" + request.getServerName()+":" +request.getServerPort();
		
		SimpleMailMessage resetEmail = new SimpleMailMessage();
		resetEmail.setTo(user.getEmail());
		resetEmail.setSubject("Password Reset");
		resetEmail.setText("To reset your password , please click the link below:\n"
				+ appUrl + "/resetPwd?token=" + user.getResetToken());
		resetEmail.setFrom("noreply@domain.com");
		
		emailService.sendEmail(resetEmail);
		model.addAttribute("message","A password reset link has been sent to "+user.getEmail());
		return"resetToken";
	}
	
	@GetMapping("/resetPwd")
	public String resetPwdToken(Model model,@RequestParam("token") String token) {
		
		DearHelpUsers user = userRepo.findByResetToken(token);
		if(user!=null)
		{
			resetToken=token;
		}
		else {
			model.addAttribute("errorMessage", "Oops!  This is an invalid password reset link.");
			return"forgetPwdForm";
		}
		return"resetPwd";
		
	}
	
	
	
	@PostMapping("/resetPwd")
	public String resetPassword(@RequestParam("password") String password) {
		
		DearHelpUsers user = userRepo.findByResetToken(resetToken);
		user.setPassword(password);
		user.setCnfPassword(password);
		user.setResetToken(null);
		userRepo.saveUser(user);
		
		return"resetPwdSuccess";
	}
}
