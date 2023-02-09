package com.contactManager.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.contactManager.entities.User;
import com.contactManager.helper.Message;
import com.contactManager.repo.UserRepo;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private UserRepo userRepo;

	@GetMapping("/home")
	public String homepage(Model model) {
		model.addAttribute("title", "HOME - SMART CONTACT MANAGER");
		return "home";
	}

	@GetMapping("/about")
	public String about(Model model) {
		model.addAttribute("title", "ABOUT - SMART CONTACT MANAGER");
		return "about";
	}

	@GetMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("title", "REGISTER - SMART CONTACT MANAGER");
		model.addAttribute("user", new User());
		return "signup";
	}

	@GetMapping("/signin")
	public String login(Model model) {
		model.addAttribute("title", "LOGIN - SMART CONTACT MANAGER");
		return "login";
	}

	// handler for user registration.
	@RequestMapping(value = "/do_register", method = RequestMethod.POST)
	public String register(@Valid @ModelAttribute("user") User user, BindingResult result,
			@RequestParam(value = "agreement", defaultValue = "false") boolean agreement, Model model,
			HttpSession session) {
		try {

			if (!agreement) {
				System.out.println("not agreed");
				throw new Exception("Terms and Conditions not agreed");
			}

			if (result.hasErrors()) {
				System.out.println("ERROR" + result.toString());
				model.addAttribute("user", user);
				return "signup";
			}
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageUrl("default.png");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			this.userRepo.save(user);

			session.setAttribute("message", new Message("Successfully Registered, Login Now", "alert-success"));
			model.addAttribute("user", new User());
			return "redirect:/signup";
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			session.setAttribute("message", new Message("Something went wrong" + e.getMessage(), "alert-danger"));
			return "redirect:/Signup";
		}

	}

}
