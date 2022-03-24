package cn.boykaff.encrypt.common.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

/**
 * @description: 用户类型枚举类
 * @author: boykaff
 * @date: 2022-03-24
 * @time: 23:11:12
 */
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum UserType {
    VIP(0, "VIP用户"),
    COMMON(1, "普通用户");
    private Integer code;
    private String type;

    UserType(Integer code, String type) {
        this.code = code;
        this.type = type;
    }
}
