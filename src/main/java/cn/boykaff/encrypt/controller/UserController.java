package cn.boykaff.encrypt.controller;

import cn.boykaff.encrypt.common.base.ResponseBasic;
import cn.boykaff.encrypt.common.enums.UserType;
import cn.boykaff.encrypt.model.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static cn.boykaff.encrypt.common.constants.SecretConstant.PREFIX;

/**
 * @description: 用户控制器类
 * @author: boykaff
 * @date: 2022-03-24
 */
@RestController
@RequestMapping(value = {"/user", PREFIX + "/user"})
public class UserController {
    @RequestMapping("/list")
    ResponseBasic<List<User>> listUser() {
        List<User> users = new ArrayList<>();
        User u = new User();
        u.setId(1);
        u.setName("boyka");
        u.setRegisterTime(LocalDateTime.now());
        u.setUserType(UserType.COMMON);
        users.add(u);
        ResponseBasic<List<User>> response = new ResponseBasic<>();
        response.setCode(200);
        response.setData(users);
        response.setMsg("用户列表查询成功");
        return response;
    }
}
