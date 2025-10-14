package com.graduation.common;// You would create this file in your project, for example in a 'com.partapp.partappchain.common' package
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * A generic base controller providing CRUD endpoints.
 * @param <S> The service class for the entity
 * @param <T> The entity class
 */
public class BaseController<S extends IService<T>, T> {

    @Autowired
    protected S service;

    @GetMapping("/{id}")
    public T getById(@PathVariable("id") Long id) {
        return service.getById(id);
    }

    @GetMapping("/list")
    public List<T> list() {
        return service.list();
    }

    @PostMapping
    public boolean save(@RequestBody T entity) {
        return service.save(entity);
    }

    @PutMapping
    public boolean update(@RequestBody T entity) {
        return service.updateById(entity);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable("id") Long id) {
        return service.removeById(id);
    }
}