/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithm;

import Tree.Node;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import stocktraderga.Genome;
import stocktraderga.StockTraderGA;

/**
 *
 * @author Sameer Pawar (slp3706@rit.edu)
 */
public class Algorithms {
    
    // CrossOver Probabilities
    private static final double INITIAL_PROBABILITY = 0.75;
    private static final double FINAL_PROBABILITY = 0.3;
    
    // Selection replace Probabilities
    private static final double REPLACE_INIT = 0.6;
    private static final double REPLACE_FINAL = 0.2;
    
    // Mutation Probabilities
    private static final double GROW_RATE_INIT = 0.9;
    private static final double GROW_RATE_FINAL = 0.4;
    private static final double DELETE_RATE_INIT = 0.0;
    private static final double DELETE_RATE_FINAL = 0.3;
    private static final double INDICATOR_RATE_INIT = 0.6;
    private static final double INDICATOR_RATE_FINAL = 0.1;
    private static final double INEQ_RATE_INIT = 0.6;
    private static final double INEQ_RATE_FINAL = 0.1;
    private static final double DECISION_RATE_INIT = 0.5;
    private static final double DECISION_RATE_FINAL = 0.1;
    
    static int compare(Genome a,Genome b){
        Double x = a.getFitness();
        Double y = b.getFitness();
        return y.compareTo(x);
    }
    
    public static void initialize(List<Genome> population ,int size, int treesize){
        for(int i = 0; i < size; i++){
            population.add(new Genome(treesize));
        }
    }
    
    public static List<Genome> select(List<Genome> population, int size, int generation){
        List<Genome> selected = new ArrayList();
        List<Genome> replacePool = new ArrayList<>();
        Collections.sort(population, Algorithms::compare);
        for(int i = 0; i < population.size(); i++){
            if(i<size)
                selected.add(population.get(i).clone());
            else
                replacePool.add(population.get(i).clone());
        }
        // Replace some genomes in selected with genomes in remaining pool
        // to avoid overfitting
        double progress = ((double) generation / (double) StockTraderGA.GENERATIONS);
        int numReplace = (int)((REPLACE_INIT*(1.0-progress)+REPLACE_FINAL*progress)*selected.size());
        double sum = 0.0;
        double[] replace_prob = new double[selected.size()];
        for(int i=1;i<=selected.size(); i++){
            sum += i;
        }
        // Compute Normalized probabilites
        for(int i=0;i<selected.size(); i++){
            replace_prob[i]=(double) i / sum;
        }
        while(numReplace>0 && replacePool.size()>1){
            Random random= new Random();
            sum = 0.0;
            double prob = random.nextDouble();
            for (int j = 0; j < selected.size(); j++) {
                sum += replace_prob[j];
                if (sum > prob) {
                    int index = random.nextInt(replacePool.size());
                    selected.set(j,replacePool.get(index).clone());
                    break; // get another random value for next candidate selection
                }
            }
            numReplace--;
        }
        Collections.sort(selected, Algorithms::compare);
        return selected;
    }
    
    public static List<Genome> crossover(List<Genome> population, int generation){
        List<Genome> offspring = new ArrayList();
        double progress = ((double) generation / (double) StockTraderGA.GENERATIONS);
        double crossover_probability = (1.0-progress)*INITIAL_PROBABILITY + progress * FINAL_PROBABILITY;
        Random random = new Random();
        double random_probability;
        for(Genome g : population){
            random_probability = random.nextDouble();
            if(random_probability < crossover_probability){
                Genome genomeA = g.clone();
                Genome genomeB = population.get(random.nextInt(population.size())).clone();
                Node nodeA = genomeA.getDecisionTree().getRandomNonleafNode();
                Node nodeB = genomeB.getDecisionTree().getRandomNonleafNode();
                if(nodeA == null || nodeB == null)
                    continue;
                // Swap nodes
                if(nodeA.parent.left == nodeA)
                    nodeA.parent.left = nodeB;
                else
                    nodeA.parent.right = nodeB;
                if(nodeB.parent.left == nodeB)
                    nodeB.parent.left = nodeA;
                else
                    nodeB.parent.right = nodeA;
                // Swap parents
                Node temp = nodeB.parent;
                nodeB.parent = nodeA.parent;
                nodeA.parent = temp;
                // Add new genomes to offspring
                offspring.add(genomeA);
                offspring.add(genomeB);
            }
        }
        return offspring;
    }
    
    public static List<Genome> mutate(List<Genome> population, int generation){
        List<Genome> offspring = new ArrayList<>();
        double grow_probability = getMutationProbability(generation, GROW_RATE_INIT, GROW_RATE_FINAL);
        double delete_probability = getMutationProbability(generation, DELETE_RATE_INIT, DELETE_RATE_FINAL);
        double indicator_probability = getMutationProbability(generation, INDICATOR_RATE_INIT, INDICATOR_RATE_FINAL);
        double ineq_probability = getMutationProbability(generation, INEQ_RATE_INIT, INEQ_RATE_FINAL);
        double decision_probability = getMutationProbability(generation, DECISION_RATE_INIT, DECISION_RATE_FINAL);
        Random random = new Random();
        double mutation_probability;
        for(Genome g: population){
            mutation_probability = random.nextDouble();
            Genome gene = g.clone();
            if(mutation_probability < grow_probability)
                gene.getDecisionTree().insertRandomIndicatorNode();
            mutation_probability = random.nextDouble();
            if(mutation_probability < delete_probability)
                gene.getDecisionTree().removeRandomIndicatorNode();
            mutation_probability = random.nextDouble();
            if(mutation_probability < indicator_probability)
                gene.getDecisionTree().mutateRandomDIndicatorNode();
            mutation_probability = random.nextDouble();
            if(mutation_probability < ineq_probability)
                gene.getDecisionTree().mutateRandomDIndicatorNodeValue();
//            mutation_probability = random.nextDouble();
//            if(mutation_probability < decision_probability)
//                gene.getDecisionTree().mutateRandomDecisionNode();
            offspring.add(gene);
        }
        return offspring;
    }
    
    private static double getMutationProbability(int generation, double init, double fin){
        double progress = ((double) generation / (double) StockTraderGA.GENERATIONS);
        return (1.0-progress)*init + progress * fin;
    }
}
