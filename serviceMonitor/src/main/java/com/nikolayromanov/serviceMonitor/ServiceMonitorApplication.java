package com.nikolayromanov.serviceMonitor;

import com.nikolayromanov.serviceMonitor.handlers.ServiceMonitorHandler;
import com.nikolayromanov.serviceMonitor.models.NatsConnection;
import com.nikolayromanov.serviceMonitor.models.SystemMessageType;
import io.nats.client.Dispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableScheduling
public class ServiceMonitorApplication {
	private final NatsConnection natsConnection;
	private final ServiceMonitorHandler serviceMonitorHandler;

	@Autowired
	public ServiceMonitorApplication(ServiceMonitorHandler serviceMonitorHandler, NatsConnection natsConnection) {
		this.serviceMonitorHandler = serviceMonitorHandler;
		this.natsConnection = natsConnection;
	}

	public static void main(String[] args) {
		SpringApplication.run(ServiceMonitorApplication.class, args);
	}

	@PostConstruct
	void init() {
		for (SystemMessageType systemMessageType : SystemMessageType.values()) {
			Dispatcher dispatcher = natsConnection.getConnection().createDispatcher((message) -> this.serviceMonitorHandler.handleSystemMessage(message, systemMessageType));
			dispatcher.subscribe(systemMessageType.getValue());
		}
	}
}
