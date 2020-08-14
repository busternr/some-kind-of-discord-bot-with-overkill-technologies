package com.nikolayromanov.serviceMonitor.controllers;

import com.nikolayromanov.platform.models.ServiceData;
import com.nikolayromanov.serviceMonitor.handlers.ServiceMonitorHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RestController
public class ServiceMonitorController {
    private final ServiceMonitorHandler serviceMonitorHandler;

    @Autowired
    public ServiceMonitorController(ServiceMonitorHandler serviceMonitorHandler) {
        this.serviceMonitorHandler = serviceMonitorHandler;
    }

    @GetMapping("/services")
    public List<ServiceData> getServices() {
        return serviceMonitorHandler.getServicesData();
    }
}
