package com.cache.ws.controller;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Controller;

import com.cache.ws.pojo.response.User;

@Controller
@Path("/json/user")
public class TestRestController {
	@GET
	@Path("/get")
	@Produces("application/json")
	public User getUsertInJSON() {

		User user = new User();
		user.setName("苍井空");
		user.setAge(13);

		return user;

	}

	@GET
	@Path("/gets")
	@Produces("application/json")
	public List<User> getUsersInJSON() {
		List<User> users = new ArrayList<User>();
		User user1 = new User();
		user1.setName("苍井空");
		user1.setAge(13);

		users.add(user1);

		User user2 = new User();
		user2.setName("苍井空2");
		user2.setAge(15);
		users.add(user2);
		return users;

	}

	@POST
	@Path("/post")
	@Consumes("application/json")
	public Response createProductInJSON(User user) {

		String result = "User created : " + user;
		return Response.status(201).entity(result).build();

	}

}
