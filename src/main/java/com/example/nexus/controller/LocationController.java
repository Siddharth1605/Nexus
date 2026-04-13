package com.example.nexus.controller;

import com.example.nexus.service.H3Service;
import com.example.nexus.service.RiderStateStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class LocationController {
    @Autowired
    private H3Service h3service;
    private final RiderStateStore riderStateStore;
    public LocationController(RiderStateStore riderStateStore) {
        this.riderStateStore = riderStateStore;
    }
    @GetMapping("/location")
    public void getH3Id(@RequestParam String riderId, @RequestParam double latitude, @RequestParam double longitude) {
        String h3CellId = h3service.getH3Id(latitude, longitude);
        riderStateStore.updateCell(riderId, h3CellId);
        riderStateStore.setIdle(riderId);
    }

    @GetMapping("/claimRider")
    public String getRider(@RequestParam String riderId) {
        return riderStateStore.claimRider(riderId);
    }
}