package dev.emircankirez.clean_code_assistant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.shell.command.annotation.CommandScan;

@CommandScan
@SpringBootApplication
public class CleanCodeAssistantApplication {

	public static void main(String[] args) {
		SpringApplication.run(CleanCodeAssistantApplication.class, args);
	}

}
