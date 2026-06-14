package com.example.nexus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PositionProcessor {
    @Autowired
    private H3Service h3service;
    private RiderStateStore riderStateStore;
    public void updateRiderId(RiderCoordinatesDTO riderCoordinatesDTO) {
        String h3CellId = h3service.getH3Id(riderCoordinatesDTO.getLatitude(), riderCoordinatesDTO.getLongitude());
        riderStateStore.updateCell(riderCoordinatesDTO.getRiderId(), h3CellId);
        riderStateStore.setIdle(riderCoordinatesDTO.getRiderId());
    }
}
