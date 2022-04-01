package cn.boykaff.encrypt.common.secret;

import cn.boykaff.encrypt.common.base.ResponseBasic;
import cn.boykaff.encrypt.common.base.SecretResponseBasic;
import cn.boykaff.encrypt.common.utils.EncryptUtils;
import cn.boykaff.encrypt.common.utils.Md5Utils;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static cn.boykaff.encrypt.common.base.ResponseCode.SECRET_API_ERROR;

/**
 * @description:
 * @author: boykaff
 * @date: 2022-03-25-0025
 */
@ControllerAdvice
public class SecretResponseAdvice implements ResponseBodyAdvice {
    private Logger logger = LoggerFactory.getLogger(SecretResponseAdvice.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        // 判断是否需要加密
        Boolean respSecret = SecretFilter.secretThreadLocal.get();
        String secretKey = SecretFilter.clientPrivateKeyThreadLocal.get();
        // 清理本地缓存
        SecretFilter.secretThreadLocal.remove();
        SecretFilter.clientPrivateKeyThreadLocal.remove();
        if (null != respSecret && respSecret) {
            if (o instanceof ResponseBasic) {
                // 外层加密级异常
                if (SECRET_API_ERROR == ((ResponseBasic) o).getCode()) {
                    return SecretResponseBasic.fail(((ResponseBasic) o).getCode(), ((ResponseBasic) o).getData(), ((ResponseBasic) o).getMsg());
                }
                // 业务逻辑
                try {
                    String dataStr = objectMapper.writeValueAsString(o);
                    // 使用FastJson序列号会导致和之前的接口响应参数不一致，后面会重点讲到
                    String data = EncryptUtils.aesEncrypt(JSON.toJSONString(o), secretKey);
                    // 增加签名
                    long timestamp = System.currentTimeMillis() / 1000;
                    int salt = EncryptUtils.genSalt();
                    String dataNew = timestamp + "" + salt + "" + data + secretKey;
                    String newSignature = Md5Utils.genSignature(dataNew);
                    return SecretResponseBasic.success(data, timestamp, salt, newSignature);
                } catch (Exception e) {
                    logger.error("beforeBodyWrite error:", e);
                    return SecretResponseBasic.fail(SECRET_API_ERROR, "", "服务端处理结果数据异常");
                }
            }
        }
        return o;
    }
}
