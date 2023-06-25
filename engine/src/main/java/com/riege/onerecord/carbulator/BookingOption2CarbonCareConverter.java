package com.riege.onerecord.carbulator;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.iata.onerecord.cargo.model.BookingOption;
import org.iata.onerecord.cargo.model.Location;
import org.iata.onerecord.cargo.model.Piece;
import org.iata.onerecord.cargo.model.Value;

import CarbonCare.AirAddressType;
import CarbonCare.CarbonCareRequestApi;
import CarbonCare.CarbonCareRequestApi.Request.Shipments;
import CarbonCare.CarbonCareRequestApi.Request.Shipments.Shipment;
import CarbonCare.ObjectFactoryRequest;
import CarbonCare.ObjectFactoryResponse;
import sun.util.calendar.CalendarDate;

import static CarbonCare.CarbonCareRequestApi.Request.Shipments.Shipment.*;

public class BookingOption2CarbonCareConverter {

    private ObjectFactoryRequest objectFactory;
    private ObjectFactoryResponse response;


    public BookingOption2CarbonCareConverter() {
        objectFactory = new ObjectFactoryRequest();
    }


    public CarbonCareRequestApi convertToCarbonCareRequest(BookingOption input)
        throws DatatypeConfigurationException
    {
        CarbonCareRequestApi api = objectFactory.createCarbonCareRequestApi();
        CarbonCareRequestApi.Request req = objectFactory.createCarbonCareRequestApiRequest();
        api.setVersion(api.getVersion());
        Shipments shipments =
            objectFactory.createCarbonCareRequestApiRequestShipments();


        input.getShipmentDetails().getContainedPieces()
            .forEach(p -> p.getTransportMovements().forEach(transportMovement -> {
                Shipment shp = objectFactory.createCarbonCareRequestApiRequestShipmentsShipment();
                shp.setId(input.getId());

                Value totalGrossWeight = input.getShipmentDetails().getTotalGrossWeight();
                shp.setWeight(BigDecimal.valueOf(totalGrossWeight.getValue()));
                shp.setWeightUnit("kg");

                Legs legs = objectFactory.createCarbonCareRequestApiRequestShipmentsShipmentLegs();
                Location departure = transportMovement.getDepartureLocation();
                Location arrival = transportMovement.getArrivalLocation();
                Legs.Leg leg =
                    objectFactory.createCarbonCareRequestApiRequestShipmentsShipmentLegsLeg();
                Legs.Leg.Air air =
                    objectFactory.createCarbonCareRequestApiRequestShipmentsShipmentLegsLegAir();
                AirAddressType dep = objectFactory.createAirAddressType();
                dep.setAirPortCode(departure.getCode());
                air.setFrom(dep);
                AirAddressType arr = objectFactory.createAirAddressType();
                arr.setAirPortCode(arrival.getCode());
                air.setTo(arr);
                String vehicleModel = transportMovement.getTransportMeans().getVehicleModel();
                air.setACType(vehicleModel);
                air.setIsFreighter(determineFreighterFlag(vehicleModel));
                leg.setAir(air);
                legs.getLeg().add(leg);
                shp.setLegs(legs);
                shipments.getShipment().add(shp);
            }));
        req.setShipments(shipments);
        api.setRequest(req);

        return api;
    }

    private boolean determineFreighterFlag(String vehicleModel) {
        switch (vehicleModel) {
            case "74T":
            case "73X":
                return true;
            default:
                return false;
        }
    }


}
