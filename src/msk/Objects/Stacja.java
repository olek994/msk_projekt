package msk.Objects;

import msk.BaseObject;

/**
 * Created by Aleksander Małkowicz, Date: 19.09.2018
 * Copyright by RelayOnIT, Warszawa 2018
 */
public class Stacja extends BaseObject {
    private int numer;
    private int maxDlugoscKolejki;
    private int numerKolejnejStacji;
    private int liczbaPasazerow;
    private int liczbaSamochodow;
    private int promNaStacji;

    public int getPromNaStacji() {
        return promNaStacji;
    }

    public void setPromNaStacji(int promNaStacji) {
        this.promNaStacji = promNaStacji;
    }

    public int getLiczbaPasazerow() {
        return liczbaPasazerow;
    }

    public void setLiczbaPasazerow(int liczbaPasazerow) {
        this.liczbaPasazerow = liczbaPasazerow;
    }

    public int getLiczbaSamochodow() {
        return liczbaSamochodow;
    }

    public void setLiczbaSamochodow(int liczbaSamochodow) {
        this.liczbaSamochodow = liczbaSamochodow;
    }

    public int getNumer() {
        return numer;
    }

    public void setNumer(int numer) {
        this.numer = numer;
    }

    public int getMaxDlugoscKolejki() {
        return maxDlugoscKolejki;
    }

    public void setMaxDlugoscKolejki(int maxDlugoscKolejki) {
        this.maxDlugoscKolejki = maxDlugoscKolejki;
    }

    public int getNumerKolejnejStacji() {
        return numerKolejnejStacji;
    }

    public void setNumerKolejnejStacji(int numerKolejnejStacji) {
        this.numerKolejnejStacji = numerKolejnejStacji;
    }
}
