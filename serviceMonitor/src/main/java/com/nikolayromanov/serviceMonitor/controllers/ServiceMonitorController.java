package com.nikolayromanov.serviceMonitor.controllers;

import com.nikolayromanov.platform.models.ServiceData;
import com.nikolayromanov.serviceMonitor.handlers.ServiceMonitorHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class ServiceMonitorController {
    private final ServiceMonitorHandler serviceMonitorHandler;

    @Autowired
    public ServiceMonitorController(ServiceMonitorHandler serviceMonitorHandler) {
        this.serviceMonitorHandler = serviceMonitorHandler;
    }

    @GetMapping("/services")
    public ResponseEntity<List<ServiceData>> getServices() {
        return new ResponseEntity<>(serviceMonitorHandler.getServicesData(), HttpStatus.OK);
    }
}
