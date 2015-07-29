package com.cache.ws;

import com.cache.ws.controller.HomeController;
import com.cache.ws.server.NettyServer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Assert;

public class Main {
	public static void main(String[] args) throws Exception {

		ApplicationContext ac = new ClassPathXmlApplicationContext(
				"root-context.xml");
		Assert.notNull(ac);
		Assert.notNull(ac.getBean(HomeController.class));

		NettyServer netty = ac.getBean(NettyServer.class);

		netty.start();

	}
}
