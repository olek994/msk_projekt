package msk.Objects;

import msk.BaseObject;

/**
 * Created by Aleksander on 19.09.2018.
 * Wojskowa Akademia Techniczna im. Jarosława Dąbrowskiego, Warszawa 2017r.
 */
public class Pasazer extends BaseObject {

    private int id;
    private int typ;
    private int numerStacji;
    private int stacjaDocelowa;
    private int naPromie;
    private int wysiada;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTyp() {
        return typ;
    }

    public void setTyp(int typ) {
        this.typ = typ;
    }

    public int getNumerStacji() {
        return numerStacji;
    }

    public void setNumerStacji(int numerStacji) {
        this.numerStacji = numerStacji;
    }

    public int getStacjaDocelowa() {
        return stacjaDocelowa;
    }

    public void setStacjaDocelowa(int stacjaDocelowa) {
        this.stacjaDocelowa = stacjaDocelowa;
    }

    public int getNaPromie() {
        return naPromie;
    }

    public void setNaPromie(int naPromie) {
        this.naPromie = naPromie;
    }

    public int getWysiada() {
        return wysiada;
    }

    public void setWysiada(int wysiada) {
        this.wysiada = wysiada;
    }
}
