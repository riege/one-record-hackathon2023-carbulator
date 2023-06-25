package com.riege.onerecord.carbulator;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

import org.iata.onerecord.cargo.model.CO2Emissions;
import org.iata.onerecord.cargo.model.Value;
import org.iata.onerecord.cargo.util.ONERecordCargoUtil;

public class CarbonCareCo2Emmissions {

    public static final String CO2E_TTW = "Co2e, Tank-to-Wheel";
    public static final String CO2E_WTT = "Co2e, Well-to-Tank";
    public static final String CO2E_WTW = "Co2e, Well-to-Wheel";
    public static final String COMPENSATION = "Compensation";

    public BigDecimal co2e_ttw;
    public BigDecimal co2e_wtt;
    public BigDecimal co2e_wtw;
    public double compensation;
    public BigDecimal compensationCosts;
    public String currency;

    public void addValues(CarbonCareCo2Emmissions pojo) {
        co2e_ttw = pojo.co2e_ttw;
        co2e_wtt = pojo.co2e_wtt;
        co2e_wtw = pojo.co2e_wtw;
        compensationCosts = pojo.compensationCosts;
        currency = pojo.currency;
    }

    public Set<CO2Emissions> convertToOneRecord() {
        Set<CO2Emissions> result = new LinkedHashSet<>();
        CO2Emissions entry;

        entry = ONERecordCargoUtil.create(CO2Emissions.class);
        entry.setMethodName(CO2E_TTW);
        entry.setCalculatedEmissions(ONERecordCargoUtil.create(Value.class));
        entry.getCalculatedEmissions().setValue(co2e_ttw.doubleValue());
        entry.getCalculatedEmissions().setUnit("KGM");
        result.add(entry);

        entry = ONERecordCargoUtil.create(CO2Emissions.class);
        entry.setMethodName(CO2E_WTT);
        entry.setCalculatedEmissions(ONERecordCargoUtil.create(Value.class));
        entry.getCalculatedEmissions().setValue(co2e_wtt.doubleValue());
        entry.getCalculatedEmissions().setUnit("KGM");
        result.add(entry);

        entry = ONERecordCargoUtil.create(CO2Emissions.class);
        entry.setMethodName(CO2E_WTW);
        entry.setCalculatedEmissions(ONERecordCargoUtil.create(Value.class));
        entry.getCalculatedEmissions().setValue(co2e_wtw.doubleValue());
        entry.getCalculatedEmissions().setUnit("KGM");
        result.add(entry);

        entry = ONERecordCargoUtil.create(CO2Emissions.class);
        entry.setMethodName(COMPENSATION);
        entry.setCalculatedEmissions(ONERecordCargoUtil.create(Value.class));
        entry.getCalculatedEmissions().setValue(compensationCosts.doubleValue());
        entry.getCalculatedEmissions().setUnit(currency);
        result.add(entry);

        return result;
    }

}
