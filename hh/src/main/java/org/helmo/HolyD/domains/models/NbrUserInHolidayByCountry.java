package org.helmo.HolyD.domains.models;


public class NbrUserInHolidayByCountry {

    private String pays;
    private int nbrUserInHoliday;

    public NbrUserInHolidayByCountry() {
    }

    public NbrUserInHolidayByCountry(String pays, int nbrUserInHoliday) {
        this.pays = pays;
        this.nbrUserInHoliday = nbrUserInHoliday;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public int getNbrUserInHoliday() {
        return nbrUserInHoliday;
    }

    public void setNbrUserInHoliday(int nbrUserInHoliday) {
        this.nbrUserInHoliday = nbrUserInHoliday;
    }

    @Override
    public String toString() {
        return "NbrUserInHolidayByCountry{" +
                "pays='" + pays + '\'' +
                ", nbrUserInHoliday=" + nbrUserInHoliday +
                '}';
    }
}
