package com.meng.mybatisplus.mybatis.generator.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.meng.mybatisplus.mybatis.generator.pojo.User;
import com.meng.mybatisplus.mybatis.generator.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 查找全部
     *
     * @return
     */
    @GetMapping
    @ResponseBody
    public List<User> getAll() {
        return userService.list(null);
    }

    /**
     * 根据id查询
     */

    @GetMapping("{id}")
    @ResponseBody
    public User getById(@PathVariable String id) {


        return userService.getById(id);

    }

    /**
     * insert
     */
    @PutMapping
    @ResponseBody
    public Boolean saveById(@RequestBody User user) {
        return userService.save(user);

    }

    /**
     * 真实删除
     */

    @DeleteMapping
    @ResponseBody
    public Boolean deletedById(@RequestBody User id) {
        return userService.removeById(id);
    }

    /**
     * 逻辑删除
     */

    @DeleteMapping("/old")
    @ResponseBody
    public Boolean oldLogicDeleteById(@RequestBody User user) {
        UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper.set("deleted", 1);

        return userService.updateById(user);
    }

    @DeleteMapping("/logic")
    @ResponseBody
    public Boolean logicDeleteById(@RequestBody String deleted,
                                   @RequestBody String id) {


        int result = userService.updateDeletedById(Integer.valueOf(deleted), Long.valueOf(id));

        return result > 0;

    }


}
