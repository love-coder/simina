package org.xidian.alg;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.plaf.synth.SynthScrollBarUI;

import org.xidian.model.GraphModel;
import org.xidian.model.Node;
import org.xidian.model.Stacks;
import org.xidian.test.TestCycle;

/**
 * 活锁检测及活锁步长分析
 * @author HanChun
 * @version 1.0
 * @since 2016-6-4 
 */
public final class CyclePath2{
	
	GraphModel graphModel; 
	int destinalionId = 75;   //终节点
	boolean[] isVisted; //是否被遍历过
	Map<Integer,Integer> justPushNode;  //每个节点刚弹出来的节点（说明不能再加入栈中）
	Stacks[] arrNodes;
	Set<Integer> previousInstance = new LinkedHashSet<Integer>(16); //记录遍历过程中的点
	
	List<Integer> trace;  //实时路径
	
	int cycleCounts = 0;
	
	/**
	 * @param graphModel
	 */
	public CyclePath2(GraphModel graphModel) {
		this.graphModel = graphModel;
		trace = new LinkedList<Integer>();   
		isVisted = new boolean[graphModel.getStatesAmount()];
		justPushNode = new HashMap<Integer,Integer>(graphModel.getStatesAmount()); //初始化，刚在栈顶压进的元素
		//初始化，用于判断过程中是否有从某点重复出来情况
		for(int i = 0; i < graphModel.getStatesAmount(); i++){
			justPushNode.put(i,-1);
		}
		//初始化
		arrNodes = new Stacks[graphModel.getStatesAmount()];
		for(int i = 0; i < graphModel.getStatesAmount(); i++){
			arrNodes[i] = new Stacks(null);
		}
		//邻接矩阵初始化邻接表
		for(int i = 0; i < graphModel.getStatesAmount(); i++){
			for(int j = 0; j < graphModel.getStatesAmount(); j++){
				if(graphModel.getCostMatrix().getMatrix()[i][j] != 0){
					arrNodes[i].push(j);
				}
			}
		}
		
		//graphModel.costMatrix.printMatrix(graphModel.costMatrix.getMatrix());
		TestCycle t = new TestCycle();
		t.testCycle(graphModel.getCostMatrix().getMatrix(), graphModel.getStatesAmount());
		//deepSearch();
		
	}

	//深度优先  
	public void deepSearch(){
		//程序中下标从0开始
		Stacks stack = new Stacks(null);
		int currentNodeId = 0;
		stack.push(0);
		isVisted[0] = true;
		trace.add(0);  //加入初始状态
		
		Node temNode;
		
		while(stack.top != null){
			currentNodeId = stack.top.nodeId;
			//得到下一波能够到到的状态
			temNode = getEnableNextNode(currentNodeId);
			if(temNode != null){
				justPushNode.put(currentNodeId, temNode.nodeId);
				currentNodeId = temNode.nodeId;
				stack.push(currentNodeId);
				trace.add(currentNodeId);
				//printPath("往前走",trace);
				isVisted[currentNodeId] = true;
			}else{
				//回溯到上一个状态
				stack.pop();
				trace.remove((Integer)currentNodeId);
				//printPath("往后退",trace);
				isVisted[currentNodeId] = false;
				justPushNode.put(currentNodeId, -1);
			}
		}
		
		System.out.println("-------------" + cycleCounts + "--------------");
	
	} 
	
	//得到满足要求的下一个节点
	public Node getEnableNextNode(int nodeId){
		Node tem = arrNodes[nodeId].top;
		while(tem != null){
			//已经访问过,即产生环！？？？
			if(isVisted[tem.nodeId]){
				cycleCounts++;
//printPath(String.valueOf(tem.nodeId) + "规律：",trace);
				tem = tem.nextNode;
			//没有访问过
			}else{
				//1.前面路径没有经过该点
				if(justPushNode.get(nodeId)==-1){
					while(tem != null && isVisted[tem.nodeId]){
						tem = tem.nextNode;
					}
					return tem;
			    //2.当前路径经过该点
				}else if(tem.nodeId == justPushNode.get(nodeId) ){
					tem = tem.nextNode;
					while(tem != null && isVisted[tem.nodeId]){
						cycleCounts++;
						tem = tem.nextNode;
					}
					return tem;
			    //3. 
				}else{
					tem = tem.nextNode;
				}
			}
		}
		return null;
	}
	
	//这个i状态下,能够到达下面的哪个状态，没有状态到达，返回-1 
	public int nextNode(int i) {
		for (int j = graphModel.getStatesAmount() - 1; j >= 0; j--) {
			if (graphModel.getCostMatrix().getMatrix()[i][j] > 0)
			return j;
		}
		return -1;
	}

	//这个i状态下,能够到达下面的哪个状态（缩小了范围）
	public int nextNode(int i, int k) {
		for (int j = k - 1; j >= 0; j--) {
			if (graphModel.getCostMatrix().getMatrix()[i][j] > 0)
			return j;
		}
		return -1;
	}
	
	
	
	//输出成功路径
	public void printSuccessPath(int[] tem){
	    
	    StringBuilder sb = new StringBuilder();
	   
	    //打印路径和对应的cost值
	    for(int i = 0; i < tem.length - 1; i++){
	    	sb.append(graphModel.getCostMatrix().getStrMatrix()[tem[i]][tem[i+1]]);
		    System.out.print(graphModel.getCostMatrix().getStrMatrix()[tem[i]][tem[i+1]]);  //for debug
		    if(i != (tem.length - 2)){
		    	sb.append("|");
			    System.out.print("|");   //for debug
		    }
		 }
	}
	
	//for debug
	public void printPath(String pathFlag, List<Integer> path) {
		System.out.print(pathFlag + " ");
		for(Integer p : path) {
			System.out.print(p + " ");
		}
		System.out.println();
	}
	
}