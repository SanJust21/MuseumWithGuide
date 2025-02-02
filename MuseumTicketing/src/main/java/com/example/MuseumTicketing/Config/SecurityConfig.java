package com.example.MuseumTicketing.Config;

//import com.example.MuseumTicketing.Repo.AdminRepo;
//import com.example.MuseumTicketing.Service.Admin.AdminService;
import com.example.MuseumTicketing.Model.Role;
import com.example.MuseumTicketing.Service.Jwt.UsersService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.filters.CorsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@SuppressWarnings("ALL")
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {


    @Autowired
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private final UsersService usersService;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/api/2factor/**").permitAll()
                        .requestMatchers("/api/payment/**").permitAll()
                        .requestMatchers("/api/qr/**").permitAll()
                        .requestMatchers("/api/details/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/stime/getSlot").permitAll()
                        .requestMatchers("/api/booking/**").permitAll()
                        .requestMatchers("/api/calEve/eventCal").permitAll()
                        .requestMatchers("/api/holidays/getDayList").permitAll()
                        .requestMatchers("/api/admin").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/api/admin/addEmployee").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/api/admin/employees").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/api/admin/employees-and-scanners").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/api/admin/scanners").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/api/admin/allTickets").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/api/admin/uploadImg/{employeeId}").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/api/admin/downloadImg/{employeeId}").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/api/admin/update/{employeeId}").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/api/admin/delete/{employeeId}").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/api/admin/delete-by-name/{name}").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/api/admin/updateRole/{employeeId}").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/api/admin/addPrice").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/api/admin/deletePrice/{id}").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/api/admin/updatePrice/{id}").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/api/dashboard/**").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/api/stime/updateShow/{id}").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/api/stime/addstime").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/api/holidays/addDayData").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/api/holidays/updateDate/{id}").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/api/holidays/deleteDate/{id}").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/api/calEve/distinctDate").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/api/calEve/dateData").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/api/calEve/capacity/{id}").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/api/scanner/**").hasAnyAuthority(Role.SCANNER.name())
                        .requestMatchers("/dataType1/**").hasAnyAuthority(Role.EMPLOYEE.name())
                        .anyRequest().authenticated())
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider()).addFilterBefore(
                        jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class
                );

        return http.build();


    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(usersService.userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
        throws Exception {
        return config.getAuthenticationManager();
    }

}
