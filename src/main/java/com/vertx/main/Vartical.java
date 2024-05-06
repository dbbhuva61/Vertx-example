package com.vertx.main;
import java.util.HashSet;
import java.util.Set;

import javax.management.loading.PrivateClassLoader;

import com.vertx.main.entity.User;
import com.vertx.main.service.UserService;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;

public class Vartical extends AbstractVerticle{
	
	public void start(Future<Void> db){
	
		Router router = Router.router(vertx);
		
        Set<String> allowHeaders = new HashSet<>();
        allowHeaders.add("x-requested-with");
        allowHeaders.add("Access-Control-Allow-Origin");
        allowHeaders.add("origin");
        allowHeaders.add("Content-Type");
        allowHeaders.add("accept");
        Set<HttpMethod> allowMethods = new HashSet<>();
        allowMethods.add(HttpMethod.GET);
        allowMethods.add(HttpMethod.POST);
        allowMethods.add(HttpMethod.DELETE);
        allowMethods.add(HttpMethod.PUT);

        router.route().handler(CorsHandler.create("*")
                .allowedHeaders(allowHeaders)
                .allowedMethods(allowMethods));
        router.route().handler(BodyHandler.create()); 
		
		
        router.get("/user").handler(this::getUsers);
        router.get("/user/:id").handler(this::getById);
        router.post("/user").handler(this::save);
        router.put("/user").handler(this::update);
        router.delete("/user/:id").handler(this::remove);
		 		 
		 vertx.createHttpServer()
		 		.requestHandler(router::accept)
		 		.listen(8080 , result -> {
                    if (result.succeeded())
                        db.complete();
                    else
                        db.fail(result.cause());
                });
	}


	 UserService userService = new UserService();

	 private void getUsers(RoutingContext context) {
	        userService.list(ar -> {
	            if (ar.succeeded()) {
	                sendSuccess(Json.encodePrettily(ar.result()), context.response());
	            } else {
	                sendError(ar.cause().getMessage(), context.response());
	            }
	        });
	    }
	 
	 private void getById(RoutingContext context) {
		 userService.getById(context.request().getParam("id"), ar -> {
	            if (ar.succeeded()) {
	                if (ar.result() != null){
	                    sendSuccess(Json.encodePrettily(ar.result()), context.response());
	                } else {
	                    sendSuccess(context.response());
	                }
	            } else {
	                sendError(ar.cause().getMessage(), context.response());
	            }
	        });
	 }
	 
	 private void save(RoutingContext context) {
		 userService.save(Json.decodeValue(context.getBodyAsString(), User.class), ar->{
			 if(ar.succeeded()) {
				 sendSuccess(context.response());
			 }
			 else {
	             sendError(ar.cause().getMessage(), context.response());
			 }
		 });
	 }
 
	 private void update(RoutingContext context) {
		 userService.update(Json.decodeValue(context.getBodyAsString(), User.class), ar->{
			 if(ar.succeeded()) {
				 sendSuccess(context.response());
			 }
			 else {
	             sendError(ar.cause().getMessage(), context.response());
			 }
		 });
	 }
	 
	 private void remove(RoutingContext context) {
		 userService.remove(context.request().getParam("id"), ar -> {
	            if (ar.succeeded()) {
	                sendSuccess(context.response());
	            } else {
	                sendError(ar.cause().getMessage(), context.response());
	            }
	        });
	 }
	 
	 
	    private void sendError(String errorMessage, HttpServerResponse response) {
	        JsonObject jo = new JsonObject();
	        jo.put("errorMessage", errorMessage);

	        response
	                .setStatusCode(500)
	                .putHeader("content-type", "application/json; charset=utf-8")
	                .end(Json.encodePrettily(jo));
	    }

	    private void sendSuccess(HttpServerResponse response) {
	        response
	                .setStatusCode(200)
	                .putHeader("content-type", "application/json; charset=utf-8")
	                .end();
	    }

	    private void sendSuccess(String responseBody, HttpServerResponse response) {
	        response
	                .setStatusCode(200)
	                .putHeader("content-type", "application/json; charset=utf-8")
	                .end(responseBody);
	    }
}
