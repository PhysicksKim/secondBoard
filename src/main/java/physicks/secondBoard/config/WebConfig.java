package physicks.secondBoard.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import physicks.secondBoard.interceptor.UserSessionInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private UserSessionInterceptor userSessionInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userSessionInterceptor)
                .addPathPatterns("/**");
                // .excludePathPatterns("/excludePath1/**", "/excludePath2/**");  // 제외할 경로
    }
}
