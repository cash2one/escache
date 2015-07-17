package com.cache.ws.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import javax.ws.rs.core.Response;

@Path("/message")
public class MessageRestService {
	public MessageRestService() {
	}

	@GET
	@Path("/{ }")
	public Response printMessage(@PathParam("param") String msg) {

		String result = "Restful example : " + msg;

		return Response.status(200).entity(result).build();

	}

	@GET
	@Path("/get/{param}")
	public Response getMessage(@PathParam("param") String msg) {

		String result = "Restful example12321321 : " + msg;

		return Response.status(200).entity(result).build();

	}

}