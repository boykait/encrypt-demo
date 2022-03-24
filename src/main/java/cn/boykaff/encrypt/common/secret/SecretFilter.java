package cn.boykaff.encrypt.common.secret;

import cn.boykaff.encrypt.common.constants.SecretConstant;
import cn.boykaff.encrypt.common.exceptions.ResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static cn.boykaff.encrypt.common.base.ResponseCode.SECRET_API_ERROR;

/**
 * @description: 过滤器
 * @author: boykaff
 * @date: 2022-03-25
 */
public class SecretFilter implements Filter {
    public static ThreadLocal<Boolean> secretThreadLocal = new ThreadLocal<>();
    public static ThreadLocal<String> clientSecretKeyThreadLocal = new ThreadLocal<>();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        // 免加密
        if (!request.getRequestURI().startsWith(SecretConstant.PREFIX)) {
            secretThreadLocal.set(Boolean.FALSE);
            filterChain.doFilter(request, response);
        } else {
            // 加密，先只考虑POST情况
            secretThreadLocal.set(Boolean.TRUE);
            filterChain.doFilter(request, response);
        }
    }
}
