package com.riege.onerecord.carbulator;

import java.util.Iterator;
import java.util.Set;

import org.iata.onerecord.cargo.model.BookingOption;
import org.iata.onerecord.cargo.model.CO2Emissions;
import org.iata.onerecord.cargo.model.Ranges;
import org.iata.onerecord.cargo.model.Ratings;
import org.iata.onerecord.cargo.model.TransportMovement;

// Exchange Rates as per https://www.finanzen.net/rohstoffe/goldpreis
public class PriceCalculatorUtils {

    public static double calculateAsTroyOunce(String currency, double amount) {
        if (currency == null || currency.isEmpty()) {
            // without currency, we simply do no conversion ;-)
            return amount;
        } else if ("EUR".equals(currency)) {
            return amount / 1965.50;
        } else if ("USD".equals(currency)) {
            return amount / 1813.37;
        } else if ("CHF".equals(currency)) {
            return amount / 1804.82;
        } else {
            throw new IllegalArgumentException("Can't calculate Troy Ounce equivalent for currency '" + currency + "'.");
        }
    }

    private static final String FORMATTED_WITHOUT_OZ_TR = "%.03f";
    private static final String FORMATTED_OZ_TR = "%.03f oz.tr.";

    public static String calculateAsTroyOunceFormatted(String currency, double amount) {
        return (currency == null || currency.isEmpty())
                    ? String.format(FORMATTED_WITHOUT_OZ_TR, amount)
                    : String.format(FORMATTED_OZ_TR, calculateAsTroyOunce(currency, amount));
    }

    public static String calculateAsTroyOunceFormatted(BookingOption bo) {
        return ratingCurrency(bo) == null
            ? String.format(FORMATTED_WITHOUT_OZ_TR, ratingPrice(bo))
            : String.format(FORMATTED_OZ_TR, ratingPrice(bo));
    }

    public static String ratingCurrency(BookingOption bo) {
        String currency = null;
        Iterator<Ratings> ratingsIterator = bo.getPrice().getRatings().iterator();
        while (ratingsIterator.hasNext()) {
            Iterator<Ranges> rangesIterator = ratingsIterator.next().getRanges().iterator();
            while (rangesIterator.hasNext()) {
                Ranges range = rangesIterator.next();
                if (currency == null) {
                    currency = range.getUnitBasis();
                } else if (!currency.equals(range.getUnitBasis())) {
                    throw new IllegalArgumentException("Determine currency uniquely.");
                }
            }
        }
        return currency;
    }

    public static double ratingPrice(BookingOption bo) {
        double totalPrice = 0;
        Iterator<Ratings> ratingsIterator = bo.getPrice().getRatings().iterator();
        while (ratingsIterator.hasNext()) {
            Iterator<Ranges> rangesIterator = ratingsIterator.next().getRanges().iterator();
            while (rangesIterator.hasNext()) {
                Ranges range = rangesIterator.next();
                totalPrice += calculateAsTroyOunce(
                    range.getUnitBasis(),
                    range.getAmount()
                );
            }
        }
        return totalPrice;
    }

    public static double ratingPriceWithCompensation(BookingOption bo) {
        Iterator<TransportMovement> iter = CarbulatorEngine.transportMovements(bo).iterator();
        double compensationInEUR = 0;
        while (iter.hasNext()) {
            Set<CO2Emissions> missions = iter.next().getCo2Emissions();
            compensationInEUR += missions.stream()
                .filter(entry -> { return CarbonCareCo2Emmissions.COMPENSATION.equals(entry.getMethodName()); } )
                .mapToDouble(entry -> entry.getCalculatedEmissions().getValue())
                .sum();
        }
        // if we have no currency on the BookingOption then we only add the amount
        // else we recalculate to a money base
        double totalCompensation = ratingCurrency(bo) == null
            ? compensationInEUR
            : PriceCalculatorUtils.calculateAsTroyOunce("EUR", compensationInEUR);
        double totalPrice = ratingPrice(bo) + totalCompensation;
        return Math.round(1000.0d * totalPrice) / 1000.0d;
    }

    public static String ratingPriceWithCompensationFormatted(BookingOption bo) {
        return String.format(FORMATTED_OZ_TR, ratingPriceWithCompensation(bo));
    }

}