package com.nikolayromanov.serviceMonitor;

import com.nikolayromanov.platform.models.SystemMessageType;
import com.nikolayromanov.serviceMonitor.config.Context;
import com.nikolayromanov.serviceMonitor.handlers.ServiceMonitorHandler;

import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableScheduling
public class ServiceMonitorApplication {
	private final Connection connection;
	private final ServiceMonitorHandler serviceMonitorHandler;

	@Autowired
	public ServiceMonitorApplication(ServiceMonitorHandler serviceMonitorHandler, Context context) {
		this.serviceMonitorHandler = serviceMonitorHandler;
		this.connection = context.getNatsConnection().getConnection();
	}

	public static void main(String[] args) {
		SpringApplication.run(ServiceMonitorApplication.class, args);
	}

	@PostConstruct
	void init() {
		for (SystemMessageType systemMessageType : SystemMessageType.values()) {
			Dispatcher dispatcher = connection.createDispatcher((message) -> this.serviceMonitorHandler.handleSystemMessage(message, systemMessageType));
			dispatcher.subscribe(systemMessageType.getValue());
		}
	}
}
