package com.riege.onerecord.carbulator;

import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.iata.onerecord.cargo.codelists.AircraftTypeCode;
import org.iata.onerecord.cargo.model.BookingOption;
import org.iata.onerecord.cargo.model.BookingOptionRequest;
import org.iata.onerecord.cargo.model.BookingSegment;
import org.iata.onerecord.cargo.model.Carrier;
import org.iata.onerecord.cargo.model.CarrierProduct;
import org.iata.onerecord.cargo.model.Company;
import org.iata.onerecord.cargo.model.Location;
import org.iata.onerecord.cargo.model.LogisticsObject;
import org.iata.onerecord.cargo.model.Piece;
import org.iata.onerecord.cargo.model.Price;
import org.iata.onerecord.cargo.model.Ranges;
import org.iata.onerecord.cargo.model.Ratings;
import org.iata.onerecord.cargo.model.Shipment;
import org.iata.onerecord.cargo.model.Value;
import org.iata.onerecord.cargo.util.ONERecordCargoUtil;

public class DemoDataGenerator {

    public static void main(String[] args) throws IOException {
        new DemoDataGenerator().generateAndUpdate();
    }

    public void generateAndUpdate() throws IOException {
        prepareMasterData();

        int pieces = 8;
        double weight = 1400.0d;
        BookingOption bookingOption;
        BookingOptionRequest request =
            new BookingOptionBuilder("BOOK-REQUEST-ID-456", "MUC", "ORD")
                .getBookingRequest();
        bookingOption =
            new BookingOptionBuilder(request)
                .buildBasics("LH", pieces, weight, 450.00, "EUR")
                .addFlight("MUC", "FRA", "LH 8370", AircraftTypeCode.TYPE_RFS)
                .addFlight("FRA", "ORD", "LH 756", AircraftTypeCode.TYPE_342)
                .build();
        bookingOption.setId(bookingOption.getRequestRef().getId() + "_LH-001");
        writeJSONToFile(bookingOption, "engine/src/main/resources/FRA-ORD-LH-001.json");

        bookingOption =
            new BookingOptionBuilder(request)
                .buildBasics("LH", pieces, weight, 485.00, "EUR")
                .addFlight("MUC", "FRA", "LH 056", AircraftTypeCode.TYPE_32Q)
                .addFlight("FRA", "ORD", "LH 756", AircraftTypeCode.TYPE_342)
                .build();
        bookingOption.setId(bookingOption.getRequestRef().getId() + "_LH-002");
        writeJSONToFile(bookingOption, "engine/src/main/resources/FRA-ORD-LH-002.json");

        bookingOption =
            new BookingOptionBuilder(request)
                .buildBasics("LH", pieces, weight, 520.00, "EUR")
                .addFlight("MUC", "ORD", "LH 820", AircraftTypeCode.TYPE_343)
                .build();
        bookingOption.setId(bookingOption.getRequestRef().getId() + "_LH-003");
        writeJSONToFile(bookingOption, "engine/src/main/resources/FRA-ORD-LH-003.json");

        bookingOption =
            new BookingOptionBuilder(request)
                .buildBasics("LX", pieces, weight, 499.95, "CHF")
                .addFlight("MUC", "GVA", "LX 7301", AircraftTypeCode.TYPE_32Q)
                .addFlight("GVA", "ORD", "LX 812", AircraftTypeCode.TYPE_333)
                .build();
        bookingOption.setId(bookingOption.getRequestRef().getId() + "_LX-001");
        writeJSONToFile(bookingOption, "engine/src/main/resources/FRA-ORD-LX-001.json");

        bookingOption =
            new BookingOptionBuilder(request)
                .buildBasics("KL", pieces, weight, 525.32, "EUR")
                .addFlight("MUC", "AMS", "KL 432", AircraftTypeCode.TYPE_32Q)
                .addFlight("AMS", "ORD", "KL 045", AircraftTypeCode.TYPE_332)
                .build();
        bookingOption.setId(bookingOption.getRequestRef().getId() + "_KL-001");
        writeJSONToFile(bookingOption, "engine/src/main/resources/FRA-ORD-KL-001.json");

        /*
Airline	Departure	Routing	Arrival	CO2	CO2costs	Frachtgewicht
Lufthansa	FRA	 	JFK	4210.98	135.97	800
Swiss	    FRA	ZRH	JFK	4882.73	157.85	800
Air Canada	FRA	YYZ	JFK	5148.33	166.78	800
Air France	FRA	CDG	JFK	4748.29	153.44	800
Finnair	    FRA	HEL	JFK	5523.07	178.54	800
         */
        pieces = 1;
        weight = 800.0d;
        bookingOption =
            new BookingOptionBuilder(request)
                .buildBasics("LH", pieces, weight, 3.80 * weight, "EUR")
                .addFlight("FRA", "JFK", "LH 045", AircraftTypeCode.TYPE_332)
                .build();
        bookingOption.setId(bookingOption.getRequestRef().getId() + "_-HACK001");
        writeJSONToFile(bookingOption, "engine/src/main/resources/HACKATHON-001.json");

        bookingOption =
            new BookingOptionBuilder(request)
                .buildBasics("LX", pieces, weight, 3.79 * weight, "EUR")
                .addFlight("FRA", "ZRH", "LX 7301", AircraftTypeCode.TYPE_32Q)
                .addFlight("ZRH", "JFK", "LX 812", AircraftTypeCode.TYPE_333)
                .build();
        bookingOption.setId(bookingOption.getRequestRef().getId() + "_-HACK002");
        writeJSONToFile(bookingOption, "engine/src/main/resources/HACKATHON-002.json");

        bookingOption =
            new BookingOptionBuilder(request)
                .buildBasics("AC", pieces, weight, 3.78 * weight, "EUR")
                .addFlight("FRA", "YYZ", "AC 760", AircraftTypeCode.TYPE_32Q)
                .addFlight("YYZ", "JFK", "AC 4102", AircraftTypeCode.TYPE_333)
                .build();
        bookingOption.setId(bookingOption.getRequestRef().getId() + "_-HACK003");
        writeJSONToFile(bookingOption, "engine/src/main/resources/HACKATHON-003.json");

        bookingOption =
            new BookingOptionBuilder(request)
                .buildBasics("AF", pieces, weight, 3.79 * weight, "EUR")
                .addFlight("FRA", "CDG", "AF 1507", AircraftTypeCode.TYPE_32Q)
                .addFlight("CDG", "JFK", "AF 102", AircraftTypeCode.TYPE_333)
                .build();
        bookingOption.setId(bookingOption.getRequestRef().getId() + "_-HACK004");
        writeJSONToFile(bookingOption, "engine/src/main/resources/HACKATHON-004.json");

        bookingOption =
            new BookingOptionBuilder(request)
                .buildBasics("AY", pieces, weight, 3.80 * weight, "EUR")
                .addFlight("FRA", "HEL", "AY 507", AircraftTypeCode.TYPE_32Q)
                .addFlight("HEL", "JFK", "AY 625", AircraftTypeCode.TYPE_333)
                .build();
        bookingOption.setId(bookingOption.getRequestRef().getId() + "_-HACK005");
        writeJSONToFile(bookingOption, "engine/src/main/resources/HACKATHON-005.json");

    }

    private static void writeJSONToFile(LogisticsObject logisticObject, String path)
        throws IOException
    {
        OutputStreamWriter osw = new FileWriter(path);
        osw.write(OneRecordDomainUtil.buildJSON(logisticObject));
        osw.close();
    }

    // *************************************************************************

    public void prepareMasterData() {
        addLocation("DUS", "Dusseldorf");
        addLocation("MUC", "Munich");
        addLocation("FRA", "Frankfurt International");
        addLocation("ORD", "Chicago O'Hare International");
        addLocation("YTO", "Totonto");
        addLocation("AMS", "Schiphol Airport");
        addCompany("LH", "020", "Lufthansa Cargo");
        addCompany("LX", "724", "Swiss WorldCargo");
        addCompany("KL", "074", "KLM Royal Dutch Airlines");
        addCompany("AF", "057", "Air France");
        addCompany("AC", "014", "Air Canada");
        addCompany("AY", "105", "Finnair Oy");
    }

    // *************************************************************************

    private Map<String, Location> locationMap = new HashMap<>();
    private Map<String, Carrier> carrierMap = new HashMap<>();
    private Map<String, Company> companyMap = new HashMap<>();

    private void addLocation(String code, String name) {
        locationMap.put(code, OneRecordDomainUtil.generateLocation(code, name));
    }

    private Location findLocation(String code) {
        if (locationMap.containsKey(code)) {
            return locationMap.get(code);
        }
        return OneRecordDomainUtil.generateLocation(code, null);
    }

    private void addCarrier(String shortCode, String awbPrefix, String name) {
        carrierMap.put(shortCode, OneRecordDomainUtil.generateCarrier(shortCode, awbPrefix, name));
    }

    private Carrier findCarrier(String shortCode) {
        if (carrierMap.containsKey(shortCode)) {
            return carrierMap.get(shortCode);
        }
        return OneRecordDomainUtil.generateCarrier(shortCode, null, null);
    }

    private void addCompany(String shortCode, String awbPrefix, String name) {
        companyMap.put(shortCode, OneRecordDomainUtil.generateCompany(shortCode, awbPrefix, name));
    }

    private Company findCompany(String shortCode) {
        if (companyMap.containsKey(shortCode)) {
            return companyMap.get(shortCode);
        }
        return OneRecordDomainUtil.generateCompany(shortCode, null, null);
    }

    // *************************************************************************

    private class BookingOptionBuilder {

        private final BookingOption mainBooking;

        public BookingOptionBuilder(String requestID, String dep, String des) {
            mainBooking = ONERecordCargoUtil.create(BookingOption.class);
            BookingOptionRequest bookingRequest =
                ONERecordCargoUtil.create(BookingOptionRequest.class);
            bookingRequest.setId(requestID);
            bookingRequest.setBookingSegment(ONERecordCargoUtil.create(BookingSegment.class));
            bookingRequest.getBookingSegment().setDepartureLocation(findLocation(dep));
            bookingRequest.getBookingSegment().setArrivalLocation(findLocation(des));
            mainBooking.setRequestRef(bookingRequest);

        }

        public BookingOptionBuilder(BookingOptionRequest bookingRequest) {
            mainBooking = ONERecordCargoUtil.create(BookingOption.class);
            mainBooking.setRequestRef(bookingRequest);
        }

        public BookingOptionRequest getBookingRequest() {
            return mainBooking.getRequestRef();
        }

        public BookingOptionBuilder buildBasics(String carrierCode, int totalPieces, double weightInKg,
            double priceAmount, String currency)
        {
            mainBooking.setCarrier(findCarrier(carrierCode));
            // CarrierProduct for LH:
            if ("LH".equals(carrierCode)) {
                CarrierProduct carrierProduct = ONERecordCargoUtil.create(CarrierProduct.class);
                mainBooking.setCarrierProductInfo(carrierProduct);
                carrierProduct.setProductCode("R21");
                carrierProduct.setProductDescription("GENERAL");
                /* CarrierProduct does not sport fields like
                 * ServiceLevelCode (=STANDARD)
                 * ServiceLevelCodeName (=td.Pro)
                 */
            }

            /*
             * TODO: Big problem here with currency exchange rates:
             *       Riege internal: We could use the https://github.com/riege/exchange-rate-service
             *                       to convert cost into the same currency for comparison.
             *
             *       rating.setSubTotal(..) is also a bit difficult
             */
            Price bookingCost = ONERecordCargoUtil.create(Price.class);
            mainBooking.setPrice(bookingCost);
            bookingCost.setRatings(ONERecordCargoUtil.buildSet());
            Ratings rating = ONERecordCargoUtil.create(Ratings.class);
            rating.setRanges(ONERecordCargoUtil.buildSet());
            // rating.setSubTotal(price);
            Ranges range = ONERecordCargoUtil.create(Ranges.class);
            range.setAmount(priceAmount);
            range.setUnitBasis(currency);
            rating.getRanges().add(range);
            bookingCost.getRatings().add(rating);

            Shipment mainShipment = ONERecordCargoUtil.create(Shipment.class);
            mainBooking.setShipmentDetails(mainShipment);
            Value grossWeight = ONERecordCargoUtil.create(Value.class);
            grossWeight.setValue(weightInKg);
            grossWeight.setUnit("KGM");
            mainShipment.setTotalGrossWeight(grossWeight);
            mainShipment.setTotalPieceCount(totalPieces);
            Piece mainPiece = ONERecordCargoUtil.create(Piece.class);
            mainShipment.setContainedPieces(ONERecordCargoUtil.buildSet(mainPiece));
            mainPiece.setTransportMovements(ONERecordCargoUtil.buildSet());
            return this;
        }

        private BookingOptionBuilder addFlight(String dep, String des, String fullFlightNumber, AircraftTypeCode flightEquipment) {
            Piece mainPiece = (Piece) mainBooking.getShipmentDetails().getContainedPieces().toArray()[0];
            mainPiece.getTransportMovements().add(OneRecordDomainUtil.generateFlight(
                mainBooking.getShipmentDetails().getTotalPieceCount(),
                mainBooking.getShipmentDetails().getTotalGrossWeight(),
                findLocation(dep),
                findLocation(des),
                fullFlightNumber,
                flightEquipment
            ));
            return this;
        }

        private BookingOption build() {
            return mainBooking;
        }
    }

    private static final LocalDate BASE_DATE = LocalDate.of(2023, 6, 20);

    private final Date date(int dayOffset, int hour, int minute) {
        LocalDateTime localDateTime =
            LocalDateTime.of(BASE_DATE.plus(dayOffset, ChronoUnit.DAYS), LocalTime.of(hour, minute, 0));
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

}
