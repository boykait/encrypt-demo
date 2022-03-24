package cn.boykaff.encrypt.common.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @description: 返回异常
 * @author: boykaff
 * @date: 2022-03-24-0024
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultException extends RuntimeException implements Serializable {
    private Integer code;
    private String msg;
}
