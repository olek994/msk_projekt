package msk.Objects;

import msk.BaseObject;

public class Prom extends BaseObject {
        private int liczbaWolnychMiejsc;
        private int numerStacji;
        private int liczbaZajetychMiejsc;

    public int getLiczbaZajetychMiejsc() {
        return liczbaZajetychMiejsc;
    }

    public void setLiczbaZajetychMiejsc(int liczbaZajetychMiejsc) {
        this.liczbaZajetychMiejsc = liczbaZajetychMiejsc;
    }

    public int getLiczbaWolnychMiejsc() {
        return liczbaWolnychMiejsc;
    }

    public void setLiczbaWolnychMiejsc(int liczbaWolnychMiejsc) {
        this.liczbaWolnychMiejsc = liczbaWolnychMiejsc;
    }

    public int getNumerStacji() {
        return numerStacji;
    }

    public void setNumerStacji(int numerStacji) {
        this.numerStacji = numerStacji;
    }
}
