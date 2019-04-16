/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tree;

import java.io.Serializable;
import stocktraderga.Decision;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import stocktraderga.Candle;

/**
 *
 * @author the1s
 */
public final class DecisionTree implements Serializable{
    private int nodeCount;
    private Node root;
    
    public DecisionTree(){
        this(15);
    }
    
    public DecisionTree(int nodeCount){
        this.nodeCount = nodeCount;
        root = IndicatorNode.getRandomNode();
        for(int i=0;i<nodeCount-1;i++){
            insertRandomIndicatorNode();
        }
    }
    
    private DecisionTree(int nodeCount, Node root){
        this.nodeCount=nodeCount;
        this.root=root;
    }
    
    public void insertRandomIndicatorNode(){
        Node randomLeaf = getRandomLeafNode();
        IndicatorNode inode = IndicatorNode.getRandomNode();
        if(randomLeaf.parent == null){
            if(Math.random()<0.5)
                root.left = inode;
            else
                root.right = inode;
            inode.parent = root;
        }else{
            if(randomLeaf.parent.left == randomLeaf)
                randomLeaf.parent.left = inode;
            else
                randomLeaf.parent.right = inode;
            inode.parent = randomLeaf.parent;
        }
    }
    
    public void removeRandomIndicatorNode(){
        List<Node> nonleafList = new ArrayList<>();
        getAllNonleafNodes(root, nonleafList);
        nonleafList = nonleafList.stream()
                .filter(s-> s.left instanceof DecisionNode && s.right instanceof DecisionNode)
                .collect(Collectors.toList());
        int index = new Random().nextInt(nonleafList.size());
        Node randomIndicator = nonleafList.get(index);
        if(randomIndicator != null){
            if(randomIndicator.parent.left == randomIndicator)
                randomIndicator.parent.left = DecisionNode.getRandomNode();
            else
                randomIndicator.parent.right = DecisionNode.getRandomNode();
        }
    }
    
    public Node getRandomLeafNode(){
        Node node = root;
        if(node.isLeaf()){
            return node;
        }
        Node parent;
        while(!node.isLeaf()){
            parent = node;
            node = Math.random()<0.5 ? node.left : node.right;
            node.parent = parent;
        }
        return node;
    }
    
    public Node getRandomNonleafNode(){
        List<Node> nonleafList = new ArrayList<>();
        getAllNonleafNodes(root, nonleafList);
        
        if(nonleafList.isEmpty())
            return null;
        int randomIndex = new Random().nextInt(nonleafList.size());
        // Avoid sending root node
        if(randomIndex == 0)
            randomIndex ++;
        return nonleafList.get(randomIndex);
    }
    
    private void getAllNonleafNodes(Node root, List<Node> nonleafList){
        if(!root.isLeaf()){
            nonleafList.add(root);
            getAllNonleafNodes(root.left, nonleafList);
            getAllNonleafNodes(root.right, nonleafList);
        }
    }
    
    public Decision evaluate(Candle c) {
        return root.evaluate(c);
    }
    
    public void mutateRandomDecisionNode(){
        Node decision = getRandomLeafNode();
        ((DecisionNode) decision).mutateDecision();
    }
    
    public void mutateRandomDIndicatorNode(){
        Node indicator = getRandomNonleafNode();
        ((IndicatorNode)indicator).mutateIndicator();
    }
    
    public void mutateRandomDIndicatorNodeValue(){
        Node indicator = getRandomNonleafNode();
        ((IndicatorNode)indicator).mutateIndicatorValue();
    }
    
    public Node getRoot(){
        return this.root;
    }
    
    @Override
    public DecisionTree clone(){
        return new DecisionTree(this.nodeCount,this.root);
    }
    
    @Override
    public String toString(){
        String tree = "";
        
        return tree;
    }
}
