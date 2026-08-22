package com.example.nexus.h3;
import com.uber.h3core.H3Core;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

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
    public List<String> getRingCells(String h3Cell, int ring)
    {
        H3Core h3 = null;
        try {
            h3 = H3Core.newInstance();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return h3.gridDisk(h3Cell, ring);
    }
}

/*
Create single isntance, pass resolution 9 to everywhere

 */
