package ca.java.spring.cache.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CacheController {
	@RequestMapping("/index")
	public String index() {
		return "index";
	}
}
