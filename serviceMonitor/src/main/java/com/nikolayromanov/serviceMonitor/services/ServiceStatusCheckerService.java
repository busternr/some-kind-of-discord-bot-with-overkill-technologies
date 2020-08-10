package com.nikolayromanov.serviceMonitor.services;

import com.nikolayromanov.serviceMonitor.handlers.ServiceMonitorHandler;
import com.nikolayromanov.serviceMonitor.models.NatsConnection;
import com.nikolayromanov.serviceMonitor.models.ServiceData;
import com.nikolayromanov.serviceMonitor.models.SystemMessageType;
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

    private final ServiceMonitorHandler serviceMonitorHandler;
    private final NatsConnection natsConnection;

    @Autowired
    public ServiceStatusCheckerService(ServiceMonitorHandler serviceMonitorHandler, NatsConnection natsConnection) {
        this.serviceMonitorHandler = serviceMonitorHandler;
        this.natsConnection = natsConnection;
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
                natsConnection.getConnection().publish(SystemMessageType.MonitorPing.getValue(), hashCode.getBytes(StandardCharsets.UTF_8));
                serviceData.setAwaitsPingReply(true);
            }
        }

        serviceMonitorHandler.setServicesData(servicesData);
    }
}
