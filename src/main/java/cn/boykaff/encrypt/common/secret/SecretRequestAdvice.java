package cn.boykaff.encrypt.common.secret;

import cn.boykaff.encrypt.common.exceptions.ResultException;
import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.io.IOException;
import java.lang.reflect.Type;

import static cn.boykaff.encrypt.common.base.ResponseCode.SECRET_API_ERROR;

/**
 * @description:
 * @author: boykaff
 * @date: 2022-03-25-0025
 */
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SecretRequestAdvice extends RequestBodyAdviceAdapter {


    public static ThreadLocal<Boolean> clientTypeThreadLocal = new ThreadLocal<>();

    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        // 验签过程
        HttpHeaders headers = inputMessage.getHeaders();
        if (CollectionUtils.isEmpty(headers.get("client"))
                || CollectionUtils.isEmpty(headers.get("timestamp"))
                || CollectionUtils.isEmpty(headers.get("salt"))
                || CollectionUtils.isEmpty(headers.get("signature"))) {
            throw new ResultException(SECRET_API_ERROR, "请求解密参数错误，请确认请求头中client、timestamp、salt、signature等参数传递是否正确传递");
        }


        return super.beforeBodyRead(inputMessage, parameter, targetType, converterType);
    }
}
