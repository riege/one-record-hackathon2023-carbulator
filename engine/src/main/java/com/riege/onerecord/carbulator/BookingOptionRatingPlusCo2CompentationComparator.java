package com.riege.onerecord.carbulator;

import java.util.Comparator;

import org.iata.onerecord.cargo.model.BookingOption;

public class BookingOptionRatingPlusCo2CompentationComparator implements Comparator<BookingOption> {

    @Override
    public int compare(BookingOption o1, BookingOption o2) {
        double rate1 = PriceCalculatorUtils.ratingPriceWithCompensation(o1);
        double rate2 = PriceCalculatorUtils.ratingPriceWithCompensation(o2);
        if (rate1 < rate2) {
            return -1;
        } else if (rate1 > rate2) {
            return 1;
        }
        return 0;
    }

}
