/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stocktraderga;

import Tree.DecisionTree;

/**
 *
 * @author Sameer Pawar (slp3706@rit.edu)
 */
public class Genome {
    private DecisionTree tree;
    private double fitness = 0.0;
    
    public Genome(int treesize){
        this.tree = new DecisionTree(treesize);
    }
    
    private Genome(DecisionTree tree, double fitness){
        this.tree = tree;
        this.fitness = fitness;
    }
    
    @Override
    public Genome clone(){
        return new Genome(this.tree.clone(),this.fitness);
    }
    
    public DecisionTree getDecisionTree(){
        return tree;
    }
    
    public double getFitness(){
        return fitness;
    }
    
    public void setFitness(double fitness){
        this.fitness = fitness;
    }
    
}
