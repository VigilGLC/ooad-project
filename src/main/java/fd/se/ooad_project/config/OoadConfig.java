package fd.se.ooad_project.config;

import fd.se.ooad_project.interceptor.AuthenticationInterceptor;
import fd.se.ooad_project.interceptor.authorize.AuthorizationInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@AllArgsConstructor
public class OoadConfig implements WebMvcConfigurer {

    final AuthenticationInterceptor authenticationInterceptor;
    final AuthorizationInterceptor authorizationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor).
                excludePathPatterns("/user/signUp", "/user/signIn",
                        "user/date", "/user/productTypes");
        registry.addInterceptor(authorizationInterceptor);
    }
}
