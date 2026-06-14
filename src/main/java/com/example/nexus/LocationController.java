package com.example.nexus;

import com.example.nexus.H3Service;
import com.example.nexus.RiderStateStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class LocationController {
    @Autowired
    private H3Service h3service;
    private LocationProducer locationProducer;

    private final RiderStateStore riderStateStore;
    public LocationController(RiderStateStore riderStateStore, LocationProducer locationProducer) {
        this.riderStateStore = riderStateStore;
        this.locationProducer = locationProducer;
    }
    @GetMapping("/location")
    public String updateLocation(@RequestParam String riderId, @RequestParam double latitude, @RequestParam double longitude) {
        locationProducer.publishRiderId(new RiderCoordinatesDTO(riderId, latitude, longitude));
        return "OK";
    }

    @GetMapping("/claimRider")
    public String getRider(@RequestParam String riderId) {
        return riderStateStore.claimRider(riderId);
    }
}