package dangqu.powertrade.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.stereotype.Component;

import dangqu.powertrade.security.conf.config.ServiceWebSecurityConfigurer;

@Component
public class ServiceWebSecurityConfigurerImpl implements ServiceWebSecurityConfigurer {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // Here add your security config 
        // http
        //     .authorizeRequests()
        //     .antMatchers("/swagger**/**").permitAll();

    }
    
}
