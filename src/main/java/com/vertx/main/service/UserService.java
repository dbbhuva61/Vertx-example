package com.vertx.main.service;

import java.util.List;

import com.vertx.main.entity.User;
import com.vertx.main.repo.UserDao;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;

public class UserService {

	
	private UserDao userDao = UserDao.getInstance();
	
	public void list(Handler<AsyncResult<List<User>>> handler) {
		
		Future<List<User>> future = Future.future();
		
        future.setHandler(handler);
        try {
            List<User> result = userDao.findall();
            future.complete(result);
        } catch (Throwable ex) {
            future.fail(ex);
        }
	}
	
	public void getById(String id, Handler<AsyncResult<User>> handler){
		Future<User> future = Future.future();
		future.setHandler(handler);
		try {
            User result = userDao.getById(id);
            future.complete(result);
        } catch (Throwable ex) {
            future.fail(ex);
        }
	}
	
    public void save(User newUser, Handler<AsyncResult<User>> handler) {
        Future<User> future = Future.future();
        future.setHandler(handler);

        try {
            User user = userDao.getById(newUser.getId());

            if (user != null) {
                future.fail("Enter Data!!");
                return;
            }
            userDao.presist(newUser);
            future.complete();
        } catch (Throwable ex) {
            future.fail(ex);
        }
    }
    
    public void update(User user, Handler<AsyncResult<User>> handler) {
        Future<User> future = Future.future();
        future.setHandler(handler);

        try {
            userDao.merge(user);
            future.complete();
        } catch (Throwable ex) {
            future.fail(ex);
        }
    }

    public void remove(String id, Handler<AsyncResult<User>> handler) {
        Future<User> future = Future.future();
        future.setHandler(handler);

        try {
            userDao.removeById(id);
            future.complete();
        } catch (Throwable ex) {
            future.fail(ex);
        }
    }
}


	