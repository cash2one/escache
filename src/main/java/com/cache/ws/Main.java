package com.cache.ws;

import com.cache.ws.server.NettyServer;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
	public static void main(String[] args) throws Exception {

		ApplicationContext ac = new ClassPathXmlApplicationContext(
				"root-context.xml");

		NettyServer netty = ac.getBean(NettyServer.class);

		netty.start();

	}
}
