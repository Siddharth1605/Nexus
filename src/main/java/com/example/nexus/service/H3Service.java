package com.example.nexus.service;
import com.uber.h3core.H3Core;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
@Service
public class H3Service {
    public String getH3Id(double latitude, double longitude) {
        try {
            H3Core h3 = H3Core.newInstance();
            int resolution = 9;
            return h3.latLngToCellAddress(latitude, longitude, resolution);
        } catch(Exception e) {

        }
        return "";
    }
}
