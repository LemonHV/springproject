package myproject.springproject.config;

import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import myproject.springproject.mapper.UserMapper;

@Configuration
public class MapperConfig {
    @Bean
    public UserMapper userMapper() {
        return Mappers.getMapper(UserMapper.class);
    }
}

