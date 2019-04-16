/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stocktraderga;

import java.util.Random;
/**
 *
 * @author the1s
 */
public enum Decision {
    BUY, SELL;
    
    public static Decision getRandomDecision(){
        Random rand = new Random();
        if(rand.nextBoolean())
                return Decision.BUY;
        else
            return Decision.SELL;
    }
}
