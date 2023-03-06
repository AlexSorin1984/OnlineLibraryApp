package com.alexsorin;

import connection.DataStaxAstraProperties;
import connection.SpringDataCassandraProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@RestController
@EnableConfigurationProperties({DataStaxAstraProperties.class, SpringDataCassandraProperties.class})
public class OnlineLibraryApp {

	public static void main(String[] args) {
		SpringApplication.run(OnlineLibraryApp.class, args);
	}

	@RequestMapping("/user")
	public String user(@AuthenticationPrincipal OAuth2User principal) {
		System.out.println(principal);
		String login = (String) principal.getAttributes().get("login");
		return login;
	}

	@Bean
	public CqlSessionBuilderCustomizer sessionBuilderCustomizers(DataStaxAstraProperties astraProperties, SpringDataCassandraProperties cassandraProperties) {
		return builder -> builder.withCloudSecureConnectBundle(astraProperties.getSecureConnectBundle().toPath())
				.withKeyspace("main")
				.withAuthCredentials(cassandraProperties.getUsername(), cassandraProperties.getPassword());


	}
}
