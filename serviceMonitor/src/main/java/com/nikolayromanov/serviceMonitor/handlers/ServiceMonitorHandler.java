package com.nikolayromanov.serviceMonitor.handlers;

import com.nikolayromanov.platform.models.ServiceData;
import com.nikolayromanov.platform.models.SystemMessageType;
import com.nikolayromanov.serviceMonitor.models.NatsConnection;
import com.nikolayromanov.serviceMonitor.models.ServiceHandlerException;

import com.google.gson.Gson;
import io.nats.client.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceMonitorHandler {
    private static final Logger logger = LoggerFactory.getLogger(ServiceMonitorHandler.class);

    private final NatsConnection natsConnection;
    private List<ServiceData> servicesData = new ArrayList<>();

    @Autowired
    public ServiceMonitorHandler(NatsConnection natsConnection) {
        this.natsConnection = natsConnection;
    }

    public void handleSystemMessage(Message message, SystemMessageType systemMessageType) {
        String strMessage = new String(message.getData(), StandardCharsets.UTF_8);
        ServiceData serviceData = null;
        int hashCode = 0;

        if(systemMessageType == SystemMessageType.MonitorPingReply) {
            hashCode = Integer.parseInt(strMessage);
        }
        else {
            serviceData = new Gson().fromJson(strMessage, ServiceData.class);
        }

        logger.info("Received system message: {} with body: {}", systemMessageType, serviceData == null ? hashCode : serviceData);

        try {
            switch (systemMessageType) {
                case MonitorRegisterService:
                    handleMonitorRegisterService(serviceData);
                    break;
                case MonitorPingReply:
                    handleMonitorPingReply(hashCode);
                    break;
                case MonitorUnregisterService:
                    handleMonitorUnregisterService(serviceData);
                    break;
            }
        } catch (ServiceHandlerException exception) {
            logger.error("Caught: {} with message: {}", exception.getClass().toString(), exception.getMessage());
        }
    }

    private void handleMonitorRegisterService(ServiceData serviceData) throws ServiceHandlerException {
        if(servicesData.contains(serviceData)) {
            throw new ServiceHandlerException(ServiceHandlerException.SERVICE_ALREADY_REGISTERED);
        }

        servicesData.add(serviceData);
        natsConnection.getConnection().publish("monitor.registerService", String.valueOf(serviceData.hashCode()).getBytes(StandardCharsets.UTF_8));
    }

    private void handleMonitorPingReply(int hashCode) {
        Optional<ServiceData> serviceData = servicesData.stream().filter(object-> object.hashCode() == hashCode).findFirst();

        if(serviceData.isEmpty()) {
            logger.error("Received ping for inactive service with hash: {}", hashCode);
        }
        else {
            serviceData.get().setAwaitsPingReply(false);
        }
    }

    private void handleMonitorUnregisterService(ServiceData serviceData) throws ServiceHandlerException {
        if(!servicesData.contains(serviceData)) {
            throw new ServiceHandlerException(ServiceHandlerException.SERVICE_NOT_REGISTERED);
        }

        servicesData.remove(serviceData);
    }

    public List<ServiceData> getServicesData() {
        return servicesData;
    }

    public void setServicesData(List<ServiceData> servicesData) {
        this.servicesData = servicesData;
    }
}
