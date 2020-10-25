package com.epam.esm.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
@EnableWebMvc
@ComponentScan("com.epam.esm")
public class DemoAppConfig {

//	@Bean
//    public ViewResolver getViewResolver(){
//        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
//        resolver.setPrefix("/WEB-INF/view/");
//        resolver.setSuffix(".jsp");
//        return resolver;
//    }

	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
	}

	@Bean
	public Jackson2ObjectMapperBuilder objectMapperBuilder() {
		Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
		builder.modules(new JavaTimeModule());

		return builder;
	}

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		return mapper;
	}

}
