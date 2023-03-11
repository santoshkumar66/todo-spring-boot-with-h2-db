/*
 * You can use the following import statements
 *
 * import org.springframework.beans.factory.annotation.Autowired;
 * import org.springframework.http.HttpStatus;
 * import org.springframework.jdbc.core.JdbcTemplate;
 * import org.springframework.stereotype.Service;
 * import org.springframework.web.server.ResponseStatusException;
 * import java.util.*;
 *
 */

// Write your code here
package com.example.todo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;

import com.example.todo.model.Todo;
import com.example.todo.repository.TodoRepository;
import com.example.todo.model.TodoRowMapper;

@Service 
public class TodoH2Service implements TodoRepository{
    @Autowired 
    private JdbcTemplate db;
    @Override 
    public ArrayList<Todo> getTodos(){
        List<Todo> todoList = db.query("select * from todolist", new TodoRowMapper());

        ArrayList<Todo> todos = new ArrayList<>(todoList);
        return todos;
    }

    @Override 
    public Todo getTodoById(int id){
        try {
            Todo todo = db.queryForObject("select * from todolist where id = ?", new TodoRowMapper(), id);
            return todo;

        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override 

    public Todo addTodo(Todo todo){
        db.update("insert into todolist(todo, priority, status) values(?,?,?)", todo.getTodo(), todo.getPriority(), todo.getStatus());
        Todo savedTodo = db.queryForObject("select * from todolist where todo=? and priority=? and status=?", new TodoRowMapper(), todo.getTodo(), todo.getPriority(), todo.getStatus());
        return savedTodo;
    }
    @Override 

    public Todo updateTodo(int id, Todo todo){
        if(todo.getTodo() != null){
            db.update("Update todolist SET todo=? WHERE id=?", todo.getTodo(), id);
        }
        if(todo.getPriority() != null){
            db.update("Update todolist SET priority=? WHERE id=?", todo.getPriority(), id);
        }
        if(todo.getStatus() != null){
            db.update("Update todolist SET status=? WHERE id=?", todo.getStatus(), id);
        }
        return getTodoById(id);
    }

    @Override 

    public void deleteTodo(int id){
        db.update("Delete from todolist where id=?", id);
    }


}
