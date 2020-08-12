package com.nikolayromanov.backend;

import com.nikolayromanov.platform.models.ServiceData;
import com.nikolayromanov.platform.models.ServiceStatus;
import com.nikolayromanov.platform.models.ServiceType;
import com.nikolayromanov.platform.models.SystemMessageType;

import com.google.gson.Gson;
import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.Nats;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@SpringBootApplication
public class BackendApplication {
	private final Connection natsConnection = Nats.connect("nats://localhost:4222");
	private ServiceData serviceData;

	@Value("${properties.serviceName}")
	private String serviceName;

	public BackendApplication() throws IOException, InterruptedException {
	}

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@PostConstruct
	void init() throws IOException, XmlPullParserException {
		MavenXpp3Reader reader = new MavenXpp3Reader();
		Model model = reader.read(new FileReader("pom.xml"));
		serviceData = new ServiceData(ServiceType.BackendCore, serviceName, ServiceStatus.Ready, model.getVersion());

		Dispatcher dispatcher = natsConnection.createDispatcher((message) -> {
			natsConnection.publish(SystemMessageType.MonitorPingReply.getValue(), message.getData());
		});
		dispatcher.subscribe(SystemMessageType.MonitorPing.getValue());

		natsConnection.publish("monitor.registerService", new Gson().toJson(serviceData).getBytes(StandardCharsets.UTF_8));
	}

	@PreDestroy
	public void destroy() {
		natsConnection.publish("monitor.unregisterService", new Gson().toJson(serviceData).getBytes(StandardCharsets.UTF_8));
	}
}
