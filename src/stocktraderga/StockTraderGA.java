/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stocktraderga;

import Algorithm.Algorithms;
import Algorithm.Simulation;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 *
 * @author the1s
 */
public class StockTraderGA {
    public static final int GENERATIONS = 2000;
    public static final int POPULATION_SIZE = 50;
    public static final int INIT_TREE_SIZE = 20;
    /**
     * @param args the command line arguments
     */
    private static Candle getCandle(String line){
        Candle c = new Candle();
        String[] fields = line.split(",");
        c.setOpen(Double.parseDouble(fields[1]));
        c.setHigh(Double.parseDouble(fields[2]));
        c.setLow(Double.parseDouble(fields[3]));
        c.setClose(Double.parseDouble(fields[4]));
        c.setVolume((int)Double.parseDouble(fields[5]));
        boolean [] features = new boolean[18];
        for(int i = 6, j = 0; i < fields.length; i++,j++){
            features[j] = Boolean.parseBoolean(fields[i]);
        }
        c.indicatorList = features;
        return c;
    }
    public static void main(String[] args){
        // TODO code application logic here
        List<List<Candle>> stockdata = new ArrayList<>();
        String folderpath = "C:\\Users\\the1s\\OneDrive\\Documents\\NetBeansProjects\\StockTraderGA\\candledata\\";
        File folder = new File(folderpath);
        File[] list = folder.listFiles();
        for(File file: list){
            String symbol = file.getName().replace(".csv","");
            //System.out.println(symbol);
            ArrayList<Candle> stock = new ArrayList<>();
            try (Stream<String> lines = Files.lines(Paths.get(folderpath+file.getName()), Charset.defaultCharset()).skip(1)) {
                lines.forEach(line->{
                    Candle c = getCandle(line);
                    stock.add(c);
                });
            } catch (IOException ex) {
                Logger.getLogger(StockTraderGA.class.getName()).log(Level.SEVERE, null, ex);
            }
            stockdata.add(stock);
        }
        //stockdata.forEach(stock->System.out.println(stock.size()));
        List<Double> bestFitness = new ArrayList<>();
        List<Double> bestFitness2 = new ArrayList<>();
        List<Double> bestFitness3 = new ArrayList<>();
        List<Genome> population = new ArrayList<>();
        Algorithms.initialize(population, POPULATION_SIZE, INIT_TREE_SIZE);
        
        // SIMULATE
        simulate(population, stockdata);
        
        int generation = 1;
        while(generation <= GENERATIONS){
            //System.out.println("++++ Genereation "+ (generation));
            // SELECTION
            List<Genome> select = Algorithms.select(population, POPULATION_SIZE/2, generation);
            //select.forEach(g -> System.out.println(g.getFitness()));
            // CROSSOVER
            List<Genome> crossoverOffspring = Algorithms.crossover(select, generation);
            // SIMULATE CROSSOVER OFFSPRING
            simulate(crossoverOffspring, stockdata);
            //System.out.println("CROSSOVER");
            //crossoverOffspring.forEach(g -> System.out.println(g.getFitness()));
            
            // MUTATION
            List<Genome> mutationOffspring = Algorithms.mutate(crossoverOffspring, generation);
            // SIMULATE MUTATION OFFSPRING
            simulate(mutationOffspring, stockdata);
            //System.out.println("MUTATION");
            //mutationOffspring.forEach(g -> System.out.println(g.getFitness()));
            
            List<Genome> union = new ArrayList<>();
            union.addAll(population);
            union.addAll(crossoverOffspring);
            union.addAll(mutationOffspring);
            
            population = Algorithms.select(union, POPULATION_SIZE, generation);
            bestFitness.add(population.get(0).getFitness());
            bestFitness2.add(population.get(1).getFitness());
            bestFitness3.add(population.get(2).getFitness());
            System.out.println("#### GENERATION "+(generation)+" BEST FITNESS = "+ population.get(0).getFitness());
            generation++;
        }
        System.out.println(bestFitness);
        System.out.println(bestFitness2);
        System.out.println(bestFitness3);
    }
    
    private static void simulate(List<Genome> population, List<List<Candle>> stockdata){
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(population.size());      
        //Simulation [] sims = new Simulation[population.size()];
        for(Genome g : population){
            Simulation sim=new Simulation(g, stockdata, latch);
            //sims[counter].start();
            executor.execute(sim);
//            try {
//                sims[counter].join();
//            } catch (InterruptedException ex) {
//                Logger.getLogger(StockTraderGA.class.getName()).log(Level.SEVERE, null, ex);
//            }
        }
        try {
            latch.await();
        } catch (InterruptedException ex) {
            Logger.getLogger(StockTraderGA.class.getName()).log(Level.SEVERE, null, ex);
        }
        executor.shutdown();
//        for(Simulation s: sims){
//            try {
//                s.join();
//            } catch (InterruptedException ex) {
//                Logger.getLogger(StockTraderGA.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
    }
}
