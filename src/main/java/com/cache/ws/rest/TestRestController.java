package com.cache.ws.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Controller;

import com.cache.ws.rest.dto.User;

@Controller
@Path("/json/user")
public class TestRestController {
	@GET
	@Path("/get")
	@Produces("application/json")
	public User getProductInJSON() {

		User user = new User();
		user.setName("苍井空");
		user.setAge(13);

		return user;

	}

	@POST
	@Path("/post")
	@Consumes("application/json")
	public Response createProductInJSON(User user) {

		String result = "User created : " + user;
		return Response.status(201).entity(result).build();

	}

}
