/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stocktraderga;
import java.time.LocalDateTime;

/**
 *
 * @author Sameer Pawar (slp3706@rit.edu)
 */
public class Candle {
    private LocalDateTime datetime;
    private double open;
    private double close;
    private double high;
    private double low;
    private int volume;
    public boolean [] indicatorList;

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }
            
}
