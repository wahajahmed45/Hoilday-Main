package org.helmo.HolyD.domains.models;

import java.util.Collection;


public class NbrUserAndNbrUserInHoliday {

    private int nbrUserTotal;
    private Collection<NbrUserInHolidayByCountry> nbrUserInHolidayByCountry;

    public NbrUserAndNbrUserInHoliday(int nbrUserTotal, Collection<NbrUserInHolidayByCountry> nbrUserInHolidayByCountry) {
        this.nbrUserTotal = nbrUserTotal;
        this.nbrUserInHolidayByCountry = nbrUserInHolidayByCountry;
    }

    public int getNbrUserTotal() {
        return nbrUserTotal;
    }

    public void setNbrUserTotal(int nbrUserTotal) {
        this.nbrUserTotal = nbrUserTotal;
    }

    public Collection<NbrUserInHolidayByCountry> getNbrUserInHolidayByCountry() {
        return nbrUserInHolidayByCountry;
    }

    public void setNbrUserInHolidayByCountryMap(Collection<NbrUserInHolidayByCountry> nbrUserInHolidayByCountry) {
        this.nbrUserInHolidayByCountry = nbrUserInHolidayByCountry;
    }

    @Override
    public String toString() {
        return "NbrUserAndNbrUserInHoliday{" +
                "nbrUserTotal=" + nbrUserTotal +
                ", nbrUserInHolidayByCountry=" + nbrUserInHolidayByCountry +
                '}';
    }
}
