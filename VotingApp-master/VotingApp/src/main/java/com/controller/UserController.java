package com.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.Base64;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.model.OTPGenerator;
import com.model.User;
import com.service.CandidateService;
import com.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userServ;
	
	@Autowired
	private CandidateService cndServ;
	@PersistenceContext
    private EntityManager entityManager;
	
	@RequestMapping("")
	public String dashboard(Model m,Principal p)
	{
		String userName = p.getName(); 		

		User user = userServ.getUserByEmail(userName);
		
		
		String status = "Not Voted";
		if (cndServ.getCandByUser(userName) != null) {
			
			status = "Voted";
		}
		
		m.addAttribute("status",status);
		
		m.addAttribute("user",user);
		
		
		m.addAttribute("title","Voter Home");
					
		/*
		 * if (user.getPhoto() != null) {
		 * 
		 * byte[] photoBytes = user.getPhoto(); InputStream inputStream = new
		 * ByteArrayInputStream(photoBytes); try { String image =
		 * byteArrayToBase64(inputStream); m.addAttribute("image", image); } catch
		 * (IOException e) { e.printStackTrace(); }}
		 */
			
		if (user.getPhoto() != null && user.getPhoto().length > 0) {
	        String base64Photo = Base64.getEncoder().encodeToString(user.getPhoto());
	        m.addAttribute("base64Photo", base64Photo);
	    }
		
		
		return "user/dashboard";
	}
	
	
	
	
	
	
	
	
	

	@PostMapping("/generate-otp")
    @ResponseBody
    public String generateOTP(HttpSession session) {
        String otp = OTPGenerator.generateOTP();
        session.setAttribute("otp", otp);
        return otp;
    }

    @PostMapping("/verify-otp")
    @ResponseBody
    public String verifyOTP(@RequestParam String enteredOTP, HttpSession session) {
        String storedOTP = (String) session.getAttribute("otp");
        if (storedOTP != null && storedOTP.equals(enteredOTP)) {
            session.removeAttribute("otp");
            return "OTP verified successfully";
        } else {
            return "Invalid OTP";
        }
    }

}
