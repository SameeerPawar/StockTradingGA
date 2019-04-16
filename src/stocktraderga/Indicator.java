/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stocktraderga;

import java.util.Random;

/**
 *
 * @author Sameer Pawar (slp3706@rit.edu)
 */
public enum Indicator {
    SMA5 ("SP>SMA5", 0),
    SMA7 ("SP>SMA7", 1),
    SMA14 ("SP>SMA14", 2),
    SMA5_SMA14 ("SMA5>SMA14", 3),
    EMA5("SP>EMA5",4),
    EMA7("SP>EMA7",5),
    EMA9("SP>EMA9",6),
    EMA13("SP>EMA13",7),
    EMA20("SP>EMA20",8),
    EMA9_EMA20("EMA9>EMA20",9),
    MACD("MACD>MACD_SIGNAL",10),
    RSI_OVERSOLD("RSI<30",11),
    RSI_OVERBOUGHT("RSI>70",12),
    UBB("SP>UBB",13),
    LBB("SP<LBB",14),
    MBB("SP>MBB",15),
    AVG_VOL10("VOL>AVG_VOL10",16),
    INC_VOL("INC_VOL",17);
    
    private final String name;
    private final int index;

    public String getName() {
        return name;
    }
    public int getIndex(){
        return index;
    }
    
    private Indicator(String name, int index){
        this.name = name;
        this.index = index;
    }
    
    public static Indicator getRandomIndicator(){
        Indicator[] indicators = Indicator.values();
        Random rand = new Random();
        return indicators[rand.nextInt(indicators.length)];
    }
}
