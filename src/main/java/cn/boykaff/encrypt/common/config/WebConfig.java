package cn.boykaff.encrypt.common.config;

import cn.boykaff.encrypt.common.secret.SecretFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.Filter;

import static cn.boykaff.encrypt.common.constants.SecretConstant.PREFIX;

/**
 * @description:
 * @author: boykaff
 * @date: 2022-03-26-0026
 */
@Configuration
public class WebConfig {
    @Bean
    public Filter secretFilter() {
        return new SecretFilter();
    }


    @Bean
    public FilterRegistrationBean filterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new DelegatingFilterProxy("secretFilter"));
        registration.setName("secretFilter");
        registration.addUrlPatterns(PREFIX + "/*");
        registration.setOrder(1);
        return registration;
    }
}
