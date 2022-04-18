package com.michelle.bookclub.controllers;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.michelle.bookclub.models.Book;
import com.michelle.bookclub.services.BookService;
import com.michelle.bookclub.services.UserService;

@Controller
public class BookController {
	@Autowired
	private BookService bookService;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/home")
	public String home(Model model,HttpSession session) {
		if(session.getAttribute("user_id")== null) {
			return "redirect:/";
		}
		model.addAttribute("user", userService.getUser((Long) session.getAttribute("user_id")));
		model.addAttribute("books", bookService.allBooks());
		return "home.jsp";
	}
	
	@GetMapping("/books/{id}")
	public String getOneBook(@PathVariable("id") Long id,Model model, HttpSession session) {
		if(session.getAttribute("user_id")== null) {
			return "redirect:/";
		}
		model.addAttribute("user_id",session.getAttribute("user_id"));
		model.addAttribute("book", bookService.getOneBook(id));
		return "oneBook.jsp";
	}
	
	//get form and post
	@GetMapping("/books/new")
	public String newBook(@ModelAttribute("book") Book book, Model model,HttpSession session) {
		model.addAttribute("user", userService.getUser((Long) session.getAttribute("user_id")));
		return "newBook.jsp";
	}
	
	@PostMapping("/books/new")
	public String makeNewBook(@Valid @ModelAttribute("book") Book book, BindingResult result, Model model,HttpSession session) {
		if(result.hasErrors()) {
			model.addAttribute("user", userService.getUser((Long) session.getAttribute("user_id")));
			return "newBook.jsp";
		}else {
			bookService.createBook(book);
			return "redirect:/home";
		}
	}
	
	//delete book
	@DeleteMapping("/books/delete/{id}")
	public String deleteGift(@PathVariable("id") Long id) {
		bookService.deleteBook(id);
		return "redirect:/home";
	}
	
	//edit book
	@GetMapping("/books/edit/{id}")
	public String editBookForm(Model model,@PathVariable("id")Long id,HttpSession session) {
		if(session.getAttribute("user_id")== null) {
			return "redirect:/";
		}
		model.addAttribute("book", bookService.getOneBook(id));
		return "editBook.jsp";
	}
	
	@PutMapping("/books/edit/{id}")
	public String updateBook(@Valid @ModelAttribute("book") Book book, BindingResult result) {
		if(result.hasErrors()) {
			return "editBook.jsp";
		}else {
			bookService.updateBook(book);
			return "redirect:/home";
		}
	}
}
