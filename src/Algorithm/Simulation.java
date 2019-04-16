/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithm;

import Tree.DecisionTree;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import stocktraderga.Candle;
import stocktraderga.Decision;
import stocktraderga.Genome;

/**
 *
 * @author Sameer Pawar (slp3706@rit.edu)
 */
public class Simulation extends Thread{
    
    private final Genome gene;
    private final Map<Integer,Double> initialCapital = new HashMap<>();
    private final Map<Integer,Double> buyingPower = new HashMap<>();
    private final CountDownLatch c;
    // Stock Index , Number of shares
    //private final Map<Integer,Integer> holding = new HashMap<>();
    private final List<List<Candle>> stockdata;
    
    public Simulation(Genome gene, List<List<Candle>> stockdata, CountDownLatch c){
        this.gene = gene;
        this.stockdata = stockdata;
        this.c = c;
        for(int i = 0; i < stockdata.size(); i++){
            double initCap = stockdata.get(i).get(0).getClose()*100;
            initialCapital.put(i, initCap);
            buyingPower.put(i, initCap);
        }
    }

    @Override
    public void run() {
        DecisionTree tree = gene.getDecisionTree();
        for(int i = 0; i < stockdata.size(); i++){
            int shares = 0;
            List<Candle> stock = stockdata.get(i);
            for(int j = 0; j < stock.size(); j++){
                Candle c = stock.get(j);
                Decision d = tree.evaluate(c);
                double bp ;
                if(d == Decision.BUY){
                    // BUY only if NOT already bought
                    if(shares == 0){
                        bp = buyingPower.get(i);
                        shares = (int)(bp/c.getClose());
                        //System.out.println("BUY "+i+" "+shares);
                        buyingPower.put(i,bp-c.getClose()*shares);
                    }
                // SELL if decision is sell or Last Candle
                }else if (d == Decision.SELL || j == stock.size()-1){
                    // SELL only if already bought
                    if(shares > 0){
                        //System.out.println("SELL "+i+" "+shares);
                        bp = buyingPower.get(i);
                        buyingPower.put(i,bp+shares*c.getClose());
                        shares = 0;
                    }
                }
            }
        }
        double initCapital = 0;
        for(Double d : initialCapital.values()){
            initCapital += d;
        }
        double finalCapital = 0;
        for(Double d : buyingPower.values()){
            finalCapital += d;
        }
        double plRatio = ((finalCapital - initCapital)/initCapital)*100;
        //System.out.println(plRatio);
        gene.setFitness(plRatio);
        c.countDown();
    }
}
