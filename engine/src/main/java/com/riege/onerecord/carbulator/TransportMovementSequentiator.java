package com.riege.onerecord.carbulator;

import java.util.Comparator;

import org.iata.onerecord.cargo.model.TransportMovement;

public class TransportMovementSequentiator implements Comparator<TransportMovement> {

    @Override
    public int compare(TransportMovement o1, TransportMovement o2) {
        if (o1.getDepartureLocation().getCode().equals(o2.getArrivalLocation().getCode())) {
            return 1;
        } else if (o1.getArrivalLocation().getCode().equals(o2.getDepartureLocation().getCode())) {
            return -1;
        }
        return 0;
    }

}
