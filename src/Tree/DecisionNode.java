/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tree;

import java.io.Serializable;
import stocktraderga.Decision;
import stocktraderga.Candle;

/**
 *
 * @author the1s
 */
public class DecisionNode extends Node implements Serializable{
    public Decision decision;
    
    public DecisionNode(){
        this.decision = Decision.BUY;
    }
    public DecisionNode(Decision decision){
        this.decision = decision;
    }
    
    public static DecisionNode getRandomNode(){
        return new DecisionNode(Decision.getRandomDecision());
    }
    
    public void mutateDecision(){
        if (decision == Decision.BUY){
            decision = Decision.SELL;
        } else {
            decision = Decision.BUY;
        }
    }
    
    @Override
    public Decision evaluate(Candle c){
        return decision;
    }
    
    @Override
    public boolean isLeaf(){
        return true;
    }
        
    @Override
    public String toString(){
        return null;
    }
    
    @Override
    public DecisionNode clone(){
        return new DecisionNode(this.decision);
    }
}
