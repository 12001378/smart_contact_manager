package com.contactManager.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.service.annotation.PutExchange;

import com.contactManager.entities.Contact;
import com.contactManager.entities.User;
import com.contactManager.helper.Message;
import com.contactManager.repo.ContactRepo;
import com.contactManager.repo.UserRepo;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class userController {
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private ContactRepo contactRepo;

	@ModelAttribute
	public void addCommonData(Model model, Principal p) {
		String username = p.getName();
		System.out.println(username);
		User user = userRepo.getUserByUsername(username);
		System.out.println(user);
		model.addAttribute("user", user);
	}

	@RequestMapping("/index")
	public String dashboard(Model model, Principal principal) {

		model.addAttribute("title", "Contact_Home");
		return "normal/user_dashboard";
	}

	// add contact form
	@GetMapping("/add_contact")
	public String openAddContact(Model model) {

		model.addAttribute("title", "Add Contact");
		model.addAttribute("contact", new Contact());
		return "normal/add_contact";
	}

	// processing then contact to save.
	@PostMapping("/process_contact")
	public String addcontact(@ModelAttribute Contact contact, HttpSession session,
			@RequestParam("profile") MultipartFile file, Principal p) {
		try {
			System.out.println(contact);
			String name = p.getName();
			User user = this.userRepo.getUserByUsername(name);

			// uploading file

			if (file.isEmpty()) {
				contact.setImage("contact.png");

			} else {
				contact.setImage(file.getOriginalFilename());

				File saveFile = new ClassPathResource("static/img").getFile();

				java.nio.file.Path path = Paths
						.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

				System.out.println("image upladed");
				session.setAttribute("msg", "image uploaded sucessfully");
			}

			contact.setUser(user);
			user.getContacts().add(contact);
			this.userRepo.save(user);
			System.out.println("Added sucessfully");
			session.setAttribute("message", new Message("Contact Added successfully", "alert-success"));
			return "/normal/add_contact";
		} catch (Exception e) {

			e.printStackTrace();
			session.setAttribute("message", new Message("Something went wrong , please try again", "alert-danger"));

		}
		return "/normal/add_contact";

	}
	// show contacts
	// pegination
	// currnt page =0;

	@GetMapping("/show_contacts/{page}")
	public String showContacts(@PathVariable("page") Integer page, Model m, Principal principal) {
		m.addAttribute("title", "show_Contacts");
//		String username = p.getName();
//		User user = this.userRepo.getUserByUsername(username);
//		List<Contact> contact = user.getContacts();
//		System.out.println(contact);
		String name = principal.getName();
		User user = this.userRepo.getUserByUsername(name);
		PageRequest pagabel = PageRequest.of(page, 5);
		Page<Contact> contacts = this.contactRepo.findContactsByUser(user.getId(), pagabel);

		m.addAttribute("contacts", contacts);
		m.addAttribute("currentPage", page);
		m.addAttribute("totalPages", contacts.getTotalPages());

		return "/normal/show_contacts";
	}

	// showing specific contact details
	@GetMapping("/{cid}/contact")
	public String contactDtls(@PathVariable("cid") Integer cid, Model model, Principal p) {

		Optional<Contact> contactOptional = this.contactRepo.findById(cid);
		Contact contact = contactOptional.get();
		String userName = p.getName();

		User user = this.userRepo.getUserByUsername(userName);
		if (user.getId() == contact.getUser().getId()) {
			model.addAttribute("contact", contact);
			model.addAttribute("title", contact.getName());
		}
		System.out.println(cid);
		return "normal/contact_detail";
	}

	@GetMapping("/delete/{cid}")
	public String deleteContact(@PathVariable("cid") int cid, HttpSession session) {
		Optional<Contact> contactOptional = this.contactRepo.findById(cid);
		Contact contact = contactOptional.get();
		contact.setUser(null);
		this.contactRepo.delete(contact);
		session.setAttribute("msg", new Message("Contact deleted Sucessfully", "alert-success"));

		return "redirect:/user/show_contacts/0";
	}

	//open update form handeller
	@PostMapping("/update_contact/{cid}")
	public String updateContact(@PathVariable("cid") int cid,Model m)
	{
		m.addAttribute("title","Update Contact");
		Contact contact = this.contactRepo.findById(cid).get();
		
		m.addAttribute("contact",contact);
		
		return"normal/update_form";
	}
	
	//updating the contact in database
	@PostMapping("/process_update")
	public String updateSave(@ModelAttribute Contact contact,@RequestParam("profile") MultipartFile file, HttpSession session,Principal p)
	{
		try {
			if(!file.isEmpty())
			{
				 
			}
			User user = this.userRepo.getUserByUsername(p.getName());
			contact.setUser(user);
			this.contactRepo.save(contact);
			session.setAttribute("message", new Message("Contact updated Sucessfully","alert-success"));
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return "normal/update_form";
	}

}
