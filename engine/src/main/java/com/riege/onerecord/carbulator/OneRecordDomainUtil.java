package com.riege.onerecord.carbulator;

import java.io.IOException;
import java.io.InputStream;

import org.iata.onerecord.cargo.codelists.AircraftTypeCode;
import org.iata.onerecord.cargo.model.BookingOption;
import org.iata.onerecord.cargo.model.Carrier;
import org.iata.onerecord.cargo.model.Company;
import org.iata.onerecord.cargo.model.Location;
import org.iata.onerecord.cargo.model.Piece;
import org.iata.onerecord.cargo.model.TransportMeans;
import org.iata.onerecord.cargo.model.TransportMovement;
import org.iata.onerecord.cargo.model.Value;
import org.iata.onerecord.cargo.util.ONERecordCargoUtil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.riege.onerecord.jsonutils.JacksonObjectMapper;

public class OneRecordDomainUtil {

    public static final String buildJSON(Object logisticObject) throws
        JsonProcessingException
    {
        ObjectMapper mapper = JacksonObjectMapper.buildMapperWithoutTimezone();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(logisticObject);
    }

    public static final BookingOption readJsonBookingOption(InputStream is) throws
        IOException
    {
        ObjectMapper mapper = JacksonObjectMapper.buildMapperWithoutTimezone();
        return (BookingOption) mapper.readValue(is, BookingOption.class);
    }

    // *************************************************************************

    public static final Location generateLocation(String code, String name) {
        Location model = ONERecordCargoUtil.create(Location.class);
        model.setCode(code);
        model.setLocationName(name);
        return model;
    }

    public static Company generateCompany(String shortCode, String awbPrefix, String name) {
        Company model = ONERecordCargoUtil.create(Company.class);
        model.setId(shortCode);
        model.setCompanyName(name);
        return model;
    }

    public static Carrier generateCarrier(String shortCode, String awbPrefix, String name) {
        Carrier model = ONERecordCargoUtil.create(Carrier.class);
        model.setAirlineCode(shortCode);
        model.setAirlinePrefix(awbPrefix);
        model.setCarrierName(name);
        return model;
    }

    public static final TransportMovement generateFlight(int pieces, Value grossWeight, Location dep, Location des, String fullFlightNumber, AircraftTypeCode flightEquipment) {
        TransportMovement result = ONERecordCargoUtil.create(TransportMovement.class);
        Piece mainPiece;
        mainPiece = ONERecordCargoUtil.create(Piece.class);
        mainPiece.setGrossWeight(grossWeight);
        result.setTransportedPieces(ONERecordCargoUtil.buildSet(mainPiece));
        result.setDepartureLocation(dep);
        result.setArrivalLocation(des);
        result.setTransportIdentifier(fullFlightNumber);
        if (flightEquipment != null) {
            result.setTransportMeans(ONERecordCargoUtil.create(TransportMeans.class));
            result.getTransportMeans().setVehicleModel(flightEquipment.code());
        }
        result.setCompanyIdentifier(fullFlightNumber.substring(0, 2));
        return result;
    }

}
