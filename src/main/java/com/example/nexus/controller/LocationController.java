package com.example.nexus.controller;

import com.uber.h3core.H3Core;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class LocationController {

    @GetMapping("/location")
    public String getH3Id(@RequestParam int riderId, @RequestParam double latitude, @RequestParam double longitude) {
        try {
            H3Core h3 = H3Core.newInstance();
            int resolution = 9;
            return h3.latLngToCellAddress(latitude, longitude, resolution);
        } catch(Exception e) {

        }
        return "failed";
    }
}