package com.chj.gr.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import de.codecentric.boot.admin.server.notify.LoggingNotifier;
import de.codecentric.boot.admin.server.notify.Notifier;

@Configuration
public class SbaNotificationConfig {
	@Bean
	public Notifier loggingNotifier(InstanceRepository repository) {
		return new LoggingNotifier(repository);
	}
    
}
