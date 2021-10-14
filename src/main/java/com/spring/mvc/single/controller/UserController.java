package com.spring.mvc.single.controller;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.javafaker.Faker;
import com.spring.mvc.single.entity.User;
import com.spring.mvc.single.repository.UserRepository;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/test/create_sample_data")
	@ResponseBody
	public String testCreateSampleData() {
		Random r = new Random();
		Faker faker = new Faker();
		int count = 150;
		for(int i = 0 ; i < count ; i++) {
			User user = new User();
			user.setName(faker.name().lastName());
			user.setPassword(String.format("%04d", r.nextInt(10000)));
			user.setBirth( faker.date().birthday() );
			userRepository.saveAndFlush(user);
		}
		return "Create sample data ok";
	}
	
	//查詢範例資料1
	@GetMapping("/test/findall")
	@ResponseBody
	public List<User> findAll() {
		List<User> users = userRepository.findAll();
		return users;
	}
	
	//查詢範例資料2
	@GetMapping("/test/findall_sort")
	@ResponseBody
	public List<User> testFindAllSort(){
		Sort sort = new Sort(Sort.Direction.ASC, "name");
		List<User> users = userRepository.findAll(sort);
		return users;
	}
	
	
	
}
