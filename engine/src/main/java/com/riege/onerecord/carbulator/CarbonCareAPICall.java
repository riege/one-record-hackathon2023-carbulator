package com.riege.onerecord.carbulator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.iata.onerecord.cargo.model.BookingOption;
import org.iata.onerecord.cargo.model.Piece;
import org.iata.onerecord.cargo.model.TransportMovement;

import CarbonCare.CarbonCareResponseApi;
import CarbonCare.CarbonCareResponseApi.Response.Shipments.Shipment.CompensationCosts;
import CarbonCare.CarbonCareResponseApi.Response.Shipments.Shipment.Emmissions;

public class CarbonCareAPICall {

    /*
     * Flights are:
     *
        String shortInfo = bo.getTransportMovement().stream()
            .sorted(new TransportMovementSequentiator())
            .map(ts -> String.format("%s,%s->%s(%s)",
                ts.getTransportIdentifier(),
                ts.getDepartureLocation().getCode(),
                ts.getArrivalLocation().getCode(),
                ts.getTransportMeans().getVehicleModel()))
            .collect(Collectors.joining(" + "))
            ;
     *
     */
    public static CarbonCareCo2Emmissions calculateCO2(Emmissions emmissions, CompensationCosts costs) {
        CarbonCareCo2Emmissions pojo = new CarbonCareCo2Emmissions();
        pojo.co2e_ttw = emmissions.getOPS().getValue();
        pojo.co2e_wtt = emmissions.getENE().getValue();
        pojo.co2e_wtw = emmissions.getTOT().getValue();
        pojo.compensationCosts = costs.getValue();
        pojo.currency = costs.getCurrency();
        return pojo;
    }

    public static Map<TransportMovement, CarbonCareCo2Emmissions> calculateCO2(List<Emmissions> emmissions, List<CompensationCosts> costs,  BookingOption bo) {
        int size = emmissions.size();
        Map<TransportMovement, CarbonCareCo2Emmissions> result = new HashMap<>();
        Iterator<TransportMovement> tmIter = CarbulatorEngine.transportMovements(bo).iterator();
        for (int i = 0; i < size; i++) {
            ArrayList<Piece> pieces = new ArrayList<>(bo.getShipmentDetails().getContainedPieces());
            result.put(tmIter.next(), calculateCO2(emmissions.get(i), costs.get(i)));
        }
        return result;
    }

    public static Map<BookingOption, CarbonCareCo2Emmissions> calculateCO22(
        Emmissions emmissions, CompensationCosts costs, BookingOption bo) {
        Map<BookingOption, CarbonCareCo2Emmissions> result = new HashMap<>();
        CarbonCareCo2Emmissions co2 = new CarbonCareCo2Emmissions();
        co2.co2e_ttw = emmissions.getOPS().getValue();
        co2.co2e_wtt = emmissions.getENE().getValue();
        co2.co2e_wtw = emmissions.getTOT().getValue();
        co2.compensationCosts = costs.getValue();
        co2.currency = costs.getCurrency();
        result.put(bo, co2);

        return result;
    }

}
