package com.timeworx.controller;

import com.timeworx.model.Person;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author ryzhang
 * @Package com.timeworx.controller
 * @date 2023/02/01 00:17
 * @Copyright
 */
@Controller
public class TestController {


    @RequestMapping("/test")
    @ResponseBody
    public Person test(){
        Person person = new Person("小明",18);

        return  person;
    }
}
