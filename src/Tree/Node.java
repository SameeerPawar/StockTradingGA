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
public class Node implements Serializable{
    
    public Node left;
    public Node right;
    public Node parent = null;
    
    public Node(){
        left = null;
        right = null;
    }
    public Node(Node left, Node right) {
        this.left = left;
        this.right = right;
    }
    public boolean isLeaf(){
        return false;
    }
    public Decision evaluate(Candle c){
        return null;
    }
    
    @Override
    public Node clone(){
        return new Node(this.left.clone(), this.right.clone());
    }

}
