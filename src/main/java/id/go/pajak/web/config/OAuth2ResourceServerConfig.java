package id.go.pajak.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.web.AuthenticationEntryPoint;

@Configuration
@EnableResourceServer
public class OAuth2ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Value("${auth.ResourceId}")
    private static String resourceId;

    private static final String RESOURCE_ID = resourceId;


    @Autowired
    @Qualifier("cTokenService")
    CustomRemoteTokenService remoteTokenService;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources
                .resourceId(RESOURCE_ID)
                .tokenServices(remoteTokenService);
    }

    @Bean
    public AuthenticationEntryPoint oauthAuthenticationEntryPoint() {
        return new OAuth2AuthenticationEntryPoint();
    }

    @Bean
    public AuthenticationEntryPoint clientAuthenticationEntryPoint() {
        OAuth2AuthenticationEntryPoint a = new OAuth2AuthenticationEntryPoint();
        a.setRealmName("springsec/client");
        a.setTypeName("Basic");
        return a;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/assets/**").permitAll()
                .antMatchers("/err/**").permitAll()
                .antMatchers("/").permitAll()
                .and().exceptionHandling();

        http.headers().frameOptions().sameOrigin();
        http.sessionManagement()
                .sessionFixation().none();
    }
}

