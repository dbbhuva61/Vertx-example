package com.vertx.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;

import io.vertx.core.Vertx;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;


@ComponentScan("com.vertx")
public class VertxCrudApplication {
	
	@Resource
	private Vartical vartical;
	
	
	@PostConstruct
	public void deployVertical() {
		Vertx.vertx().deployVerticle(vartical);
	}
	
	public static void main(String[] args) {
        SpringApplication.run(VertxCrudApplication.class, args);

	}

}
