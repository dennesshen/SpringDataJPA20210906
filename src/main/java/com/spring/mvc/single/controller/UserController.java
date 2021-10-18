package com.spring.mvc.single.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	
	//查詢範例資料3
	@GetMapping("/test/findall_ids")
	@ResponseBody
	public List<User> testFindAllIDs(){
		Iterable<Long> ids = Arrays.asList(1L, 3L, 5L );
		List<User> users = userRepository.findAll(ids);
		return users;
	}
	
	//查詢範例資料4
	@GetMapping("/test/findall_example")
	@ResponseBody
	public List<User> testFindAllExample(){
		User user = new User();
		System.out.println(user);
		user.setId(2L);
		//user.setPassword("3386");
		Example<User> example = Example.of(user);
		List<User> users = userRepository.findAll(example);
		return users;
	}
	
	//查詢範例資料5
	@GetMapping("/test/findall_example2")
	@ResponseBody
	public List<User> testFindAllExample2(){
		User user = new User();
		user.setName("a");
		//欄位name內容有包含a的
		//建立ExampleMatcher 比對器
		ExampleMatcher matcher = ExampleMatcher.matching()
											   .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains());
		Example<User> example = Example.of(user, matcher);
		List<User> users = userRepository.findAll(example);
		return users;
	}
	
	//查詢範例資料6 - 單筆查詢
	@GetMapping("/test/find_one")
	@ResponseBody
	public User findOne() {
		return userRepository.findOne(3L);
	}
		
	@GetMapping("/test/page/{no}")
	@ResponseBody
	public List<User> testPage(@PathVariable("no") Integer no) {
		int pageNo = no - 1 ;
		int pageSize = 10; 
		//排序
		Sort.Order order1 = new Sort.Order(Sort.Direction.ASC, "name");
		Sort.Order order2 = new Sort.Order(Sort.Direction.DESC, "id");
		Sort sort = new Sort(order1, order2);
		//分頁請求
		PageRequest pageRequest = new PageRequest(pageNo, pageSize, sort);
		Page<User> page = userRepository.findAll(pageRequest) ; 
		return page.getContent() ;
	}
	
	//JPQL自定義方法 用name來查詢
	@GetMapping("/test/name")
	@ResponseBody
	public List<User> getByName(@RequestParam("name") String name) {
		return userRepository.getByName(name);
	}
	
	@GetMapping("/test/name/id/{name}/{id}")
	@ResponseBody
	public List<User> getByNameAndId(@PathVariable("name") String name,
									 @PathVariable("id") Long id) {
		return userRepository.getByNameStartingWithAndIdGreaterThanEqual(name, id);
	}
	
	@GetMapping("/test/ids")
	@ResponseBody
	public List<User> getByIds(@RequestParam("ids") List<Long> ids) {
		return userRepository.getByIdIn(ids);
	}
	
	
	@GetMapping("/test/birth")
	@ResponseBody
	public List<User> getByBirthLessThan(@RequestParam("birth") @DateTimeFormat(iso = ISO.DATE) Date birth) {
		return userRepository.getByBirthLessThan(birth);
	}
	@GetMapping("/test/birth_between")
	@ResponseBody
	public List<User> getByBirthBetween(@RequestParam("begin") @DateTimeFormat(iso = ISO.DATE) Date begin,
										@RequestParam("end") @DateTimeFormat(iso = ISO.DATE) Date end) {
		return userRepository.getByBirthBetween(begin, end);
	}
}
	
	
	

