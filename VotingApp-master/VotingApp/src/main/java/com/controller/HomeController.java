package com.controller;



import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.model.Role;
import com.model.User;
import com.service.RoleService;
import com.service.UserService;



@Controller
public class HomeController {
	
	
	@Autowired
	private UserService userserv;
	
	@Autowired
	private RoleService roleserv;

	@GetMapping("/")
	public String index(@RequestParam(value = "error", required = false) String error,Model m)
	{
		if (error !=null) {
            m.addAttribute("errorMessage", "Invalid username or password. Please try again.");
        }
		
		m.addAttribute("title","Home");
		return "home";
	}
	
	
	
	@GetMapping("/signin")
	public String signin(Model m)
	{
		m.addAttribute("title","Signin");
		return "signin";
	}
	
	
	
	
	@GetMapping("/register")
	public String register(Model m)
	{
		m.addAttribute("title","Registration");
		return "register";
	}
	
	
	@GetMapping("/about")
	public String about(Model m)
	{
		m.addAttribute("title","About");
		return "about";
	}

	
	@PostMapping("/createuser")
	public String createuser(@RequestParam("photo") MultipartFile photo,
            @RequestParam("name") String name,
            @RequestParam("phone") String phone,
            @RequestParam("password") String password,
            @RequestParam("confirmpass") String confirmPassword,
            @RequestParam("email") String email,
            
            HttpSession session)
	{		
		
		
		try {
	        if (!photo.isEmpty()) {
	            byte[] photoBytes = photo.getBytes();
	            List<Role> roles = new ArrayList<>();
	            roles.add(roleserv.getRoleByName("ROLE_USER"));

	            if (userserv.getUserByEmail(email) != null) {
	                session.setAttribute("msg", "Registration Failed, Please try a different email id");
	                return "redirect:/register";
	            } else {
	                // Create the User object
	                User user = new User(name, password, email, phone, roles, photoBytes);
	                userserv.addUser(user);

	                session.setAttribute("msg", "Registration Successful...");
	                return "redirect:/";
	            }
	        }
	    } catch (IOException e) {
	        System.out.println("Error while processing the photo: " + e.getMessage());
	    }

	    session.setAttribute("msg", "Error during registration. Please try again.");
	    return "redirect:/register";
		
		
		
	}
	
	
}

