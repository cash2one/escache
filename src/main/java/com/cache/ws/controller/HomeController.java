package com.cache.ws.controller;

import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import com.cache.ws.controller.request.Article;
import com.cache.ws.pojo.response.Helloworld;
import org.springframework.stereotype.Controller;

@Controller
@Path("/hello")
public class HomeController {

	@GET
	@Path("/world")
	@Produces("application/json")
	public Helloworld helloworld() throws Exception {
		return new Helloworld("Welcome, HelloWorld");
	}

	@GET
	@Path("/auth")
	@Produces("application/json")
	public Helloworld auth(@Context SecurityContext context) {
		return new Helloworld(context.getUserPrincipal().getName());
	}

	// TODO 测试中，此处报错，可能是参数转换为Article实例出错
	// @POST
	// @Consumes({ MediaType.APPLICATION_JSON })
	// @Produces({ MediaType.APPLICATION_JSON })
	// public Article save(Article article) {
	// return article;
	// }

	/**
	 * 因为自动转换为实例出错，所有直接获取参数，再创建实例
	 * 
	 * @param name
	 * @param id
	 * @return
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Produces({ MediaType.APPLICATION_JSON })
	public Article save(@FormParam("name") String name, @FormParam("id") int id) {
		return new Article(id, name);
	}

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public List<Article> save(@QueryParam("multi") boolean isMulti,
			List<Article> articles) {
		return articles;
	}

}
