package cn.boykaff.encrypt.model;

import cn.boykaff.encrypt.common.enums.UserType;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @description: 用户对象
 * @author: boykaff
 * @date: 2022-03-24x
 */
@Data
public class User {
    private Integer id;
    private String name;
    private UserType userType = UserType.COMMON;
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH")
    private LocalDateTime registerTime;
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss")
    private Date registerTime1 = new Date();

    public static void main(String[] args) {
        System.out.println(JSON.toJSONString(UserType.COMMON, SerializerFeature.WriteEnumUsingToString));
    }
}
