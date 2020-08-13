package com.nikolayromanov.serviceMonitor.services;

import com.nikolayromanov.platform.models.ServiceData;
import com.nikolayromanov.platform.models.SystemMessageType;
import com.nikolayromanov.serviceMonitor.config.Context;
import com.nikolayromanov.serviceMonitor.handlers.ServiceMonitorHandler;

import io.nats.client.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;

@Component
public class ServiceStatusCheckerService {
    private static final Logger logger = LoggerFactory.getLogger(ServiceMonitorHandler.class);

    private final Connection connection;
    private final ServiceMonitorHandler serviceMonitorHandler;

    @Autowired
    public ServiceStatusCheckerService(ServiceMonitorHandler serviceMonitorHandler, Context context) {
        this.serviceMonitorHandler = serviceMonitorHandler;
        this.connection = context.getNatsConnection().getConnection();
    }

    @Scheduled(fixedRateString = "${properties.serviceCheckerInterval:60000}")
    void checkAllServices() {
        List<ServiceData> servicesData = serviceMonitorHandler.getServicesData();

        for (Iterator<ServiceData> iterator = servicesData.iterator(); iterator.hasNext();) {
            ServiceData  serviceData = iterator.next();

            if(serviceData.isAwaitsPingReply()) {
                logger.info("Removing inactive service: {} with hashcode: {}", serviceData, serviceData.hashCode());
                iterator.remove();
            }
            else {
                String hashCode = Integer.toString(serviceData.hashCode()); // TODO: Fix this garbage transformation please.
                connection.publish(SystemMessageType.MonitorPing.getValue(), hashCode.getBytes(StandardCharsets.UTF_8));
                serviceData.setAwaitsPingReply(true);
            }
        }

        serviceMonitorHandler.setServicesData(servicesData);
    }
}
