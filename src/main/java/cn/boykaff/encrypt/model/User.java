package cn.boykaff.encrypt.model;

import cn.boykaff.encrypt.common.enums.UserType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime registerTime;
}
