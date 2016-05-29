package org.xidian.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import org.jgrapht.DirectedGraph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.AllDirectedPaths;
import org.jgrapht.alg.CycleDetector;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DirectedPseudograph;
import org.xidian.utils.FileUtil;

/**
 * 可达图（广义）数据模型
 * @author HanChun
 * @version 1.0 version 2016-5-18
 */
public class RGDataModel {
	
	public String destPath; //输出路径
	public StateNode rootState; //初始状态
	public static Map<Integer,StateNode> preStatesMap; 
	DirectedGraph<StateNode, Transition> grapht;	//图论
	List<StateNode> deadStates;	//死锁状态
	
	/**
	 * @param rootState 初始状态
	 * @param model PetriModel
	 */
	public RGDataModel(String destPath) {
		this.destPath = destPath;
		this.rootState = new StateNode(PetriModel.ininmarking.getMarking(), 1); //初始初始状态
		try {
			createRG();
		    traverseRG();
		} catch (CloneNotSupportedException e) {
			System.err.println("生成可达图失败！");
			e.printStackTrace();
		}
	}

	/**
	 * 生成可达图(非递归方法), 输出到文件
	 * @throws CloneNotSupportedException 
	 */
	public void createRG() throws CloneNotSupportedException{
		preStatesMap = new HashMap<Integer,StateNode>(1000); //初始化1000个状态
		StringBuffer resultStr = new StringBuffer("可达图分析结果如下：\n");
		int stateCount = 1;	//状态数
		Stack<Integer> nextTrans = new Stack<Integer>(); //当前状态下，能够发射的变迁
		Queue<StateNode> stateQueue = new LinkedList<StateNode>();  //状态队列
		Marking temState = null;
		StateNode currentState = rootState;
		StateNode duringState = null;  //过程中探索到的状态
		preStatesMap.put(currentState.hashCode(), currentState);
		stateQueue.add(rootState); //根状态为起始状态
		boolean[] canFire = null;
		while(!stateQueue.isEmpty()) {
			currentState = stateQueue.poll();	//每次取出队列中最前面的状态作为当前状态（注意该状态可能不是新状态）
			resultStr.append("\nState nr:" + currentState.getStateNo() + "\n"
					+ printPlaces() + "\n" + "toks: " + currentState + "\n");
			canFire = getEnabledTrans(currentState);
			for(int i = 0; i < canFire.length; i++) {
				if(canFire[i]) nextTrans.push(i);
			}
			//死锁状态
			if(nextTrans.isEmpty()) {
				currentState.setIfDeadlock(true);
				resultStr.append("dead state\n");
			}else{
				while(!nextTrans.isEmpty()) {
					resultStr.append("==" + "t" + (nextTrans.peek() + 1) + "==>");
					temState = fire(currentState, nextTrans.pop());
					//新状态
					if(!ifOccured(temState)) {
						stateCount++;
						duringState = new StateNode(temState.marking, stateCount);
						resultStr.append("s"+duringState.getStateNo()+"\n");
						preStatesMap.put(temState.hashCode(), duringState);
						stateQueue.add((StateNode) duringState.clone());
					//旧状态
					}else{
						resultStr.append("s"+preStatesMap.get(temState.hashCode()).getStateNo()+"\n");
					}
				}
			}
		}
		resultStr.append("\n----------end----------");
		//System.out.println(resultStr.toString());	//for debug
		FileUtil.write(destPath, resultStr.toString(), false);
	}
	
	/**
	 * @see createRG(),该方法在createRG()上面做了优化（之所以不在上面直接改，是为了保证基础版的足够简单）
	 * 使用jgrapht开源包，实现遍历可达图，生成图数据结构，然后借助相应的办法
	 * @param destPath 输出目录
	 * @throws CloneNotSupportedException 
	 */
	public void traverseRG() throws CloneNotSupportedException{
		preStatesMap = new HashMap<Integer,StateNode>(1000); //初始化1000个状态
		deadStates = new LinkedList<StateNode>();
		grapht = new DirectedPseudograph<StateNode, Transition>(Transition.class);
		
		int stateCount = 1;	//状态数
		Stack<Integer> nextTrans = new Stack<Integer>(); //当前状态下，能够发射的变迁
		Queue<StateNode> stateQueue = new LinkedList<StateNode>();  //状态队列
		Marking temState = null;
		Transition currentTran = null;
		String currentTranName = null;
		StateNode currentState = rootState;
		StateNode duringState = null;  //过程中探索到的状态
		preStatesMap.put(currentState.hashCode(), currentState);//根节点加入
		stateQueue.add(rootState); //根状态为起始状态
		grapht.addVertex(rootState);
		boolean[] canFire = null;
		while(!stateQueue.isEmpty()) {
			currentState = stateQueue.poll();//每次取出队列中最前面的状态作为当前状态（注意该状态可能不是新状态）
			canFire = getEnabledTrans(currentState);
			for(int i = 0; i < canFire.length; i++) {
				if(canFire[i]) nextTrans.push(i);
			}
			//死锁状态
			if(nextTrans.isEmpty()) {
				deadStates.add(preStatesMap.get(currentState.hashCode()));
				currentState.setIfDeadlock(true);
			}else{
				while(!nextTrans.isEmpty()) {
					//构建图的数据结构
					currentTranName = "t" + (nextTrans.peek() + 1);
					//transition 命名
					currentTran = new Transition();
					currentTran.setTranName("t"+(nextTrans.peek()+1));
					temState = fire(currentState, nextTrans.pop());
					//新状态
					if(!ifOccured(temState)) {
						stateCount++;
						//变迁信息
						duringState = new StateNode(temState.marking, stateCount);//新节点，之前没有遇到过
						grapht.addVertex(duringState);
						currentState.getChildNodes().add(duringState);
						currentTran = grapht.addEdge(preStatesMap.get(currentState.hashCode()), duringState);
						currentTran.setTranName(currentTranName);
						preStatesMap.put(temState.hashCode(), duringState);
						stateQueue.add((StateNode) duringState.clone());
					//旧状态
					}else{
						currentState.getChildNodes().add(preStatesMap.get(temState.hashCode()));
						currentTran = grapht.addEdge(preStatesMap.get(currentState.hashCode()), preStatesMap.get(temState.hashCode()));
						currentTran.setTranName(currentTranName);
					}
				}
			}
		}
		
		printDeadlockPath();
		
	}
	
	/**
	 * 1.计算出起始节点到各个死锁节点的全部(或部分)路径
	 * 2.计算出各个路径中的最短路径（路径长度）
	 * 3.计算步长
	 * 4.计算环个数
	 */
	public void printDeadlockPath() {
		
		StringBuffer resultStr = new StringBuffer("动态路径规划分析如下：\n");
		resultStr.append("完全许可性为："+grapht.vertexSet().size()+"\n");
		resultStr.append("deadlock states 共  "+deadStates.size()+" 个"+"分别如下：" + "\n");
		
//		System.out.println("完全许可性为："+grapht.vertexSet().size()+"\n");			//for debug
//		System.out.println("deadlock states 共  "+deadStates.size()+" 个"+"分别如下：" + "\n");
		
		GraphPath<StateNode, Transition> deadShortPaths = null;
		
		//计算出最短路径
		for(StateNode e: deadStates) {
			DijkstraShortestPath<StateNode, Transition> dpath = new DijkstraShortestPath<StateNode, Transition>(grapht,rootState, e);
			resultStr.append("["+e.getStateNo()+"] "+e.toString()+"\n");
			//System.out.print("["+e.getStateNo()+"] "+e.toString()+"\n");
			deadShortPaths = dpath.getPath();
			for(Transition  t : deadShortPaths.getEdgeList()){
				resultStr.append(t.getTranName()+"-->");
				//System.out.print(t.getTranName()+"-->");
			}
			resultStr.append("\n["+deadShortPaths.getEndVertex().getStateNo()+"] (最短路径)"+"\n");
			//System.out.println("["+deadShortPaths.getEndVertex().getStateNo()+"] (最短路径)"+"\n");
		}
		resultStr.append("\n其余死锁路径参考（若状态数大于40，则显示其中部分路径）：\n");
		//System.out.println("其余死锁路径参考（若状态数大于40，则显示其中部分路径）：\n");
		//显示更多的路径
		for(StateNode e: deadStates) {
			Integer calNum = null; //计算路径步长
			if(grapht.vertexSet().size() > 40) calNum = PetriModel.transCount;
			AllDirectedPaths<StateNode, Transition> allPath = new AllDirectedPaths<StateNode, Transition>((DirectedGraph<StateNode, Transition>) grapht);
			List<GraphPath<StateNode, Transition>> paths = allPath.getAllPaths(rootState, e, true, calNum);
			resultStr.append("\n死锁状态  [" + e.getStateNo() +"]" + "其余参考路径如下：\n");
			//System.out.println("\n死锁状态  [" + e.getStateNo() +"]" + "其余参考路径如下：");
			for(GraphPath<StateNode, Transition> g : paths) {
	    		for(Transition t: g.getEdgeList()) {
	    			resultStr.append(t.getTranName()+"-->");
	    			//System.out.print(t.getTranName()+"-->");
	    		}
	    		resultStr.append("\n");
	    		//System.out.println();
	    	}
		}
		resultStr.append("\n全局可达图环路信息分析如下：\n");
		//System.out.println("\n全局可达图环路信息分析如下：");
		CycleDetector<StateNode, Transition> cdetector =  new CycleDetector<StateNode, Transition>(grapht);
		//System.out.println("共   "+cdetector.findCycles().size() + " 个环，其中经过节点（即可逆）的环共   " + cdetector.findCyclesContainingVertex(rootState).size() + " 个环");
		resultStr.append("共   "+cdetector.findCycles().size() + " 个环，其中经过节点（即可逆）的环共   " + cdetector.findCyclesContainingVertex(rootState).size() + " 个环/n");		
		resultStr.append("\n最优步长信息分析如下：\n");
		//System.out.println("\n最优步长信息分析如下：");
		//下面为计算步长:
		//需要说明的是：这里的计算只是简单的计算步长，算法如下：
		//死锁节点开始，找到前一个状态，如果前一状态只要存在一个变迁发射到达不死锁状态，则标记为临界状态，
		//否则标记为死锁状态，重复上面的定义，直到标记出所有的临界状态.
		
		Set<StateNode> preDeadlockSet  = new HashSet<StateNode>();
		//寻找步长时的中间状态
		Queue<StateNode> tem = new LinkedList<StateNode>();
		//临界状态
		Set<StateNode> criticalStates  = new HashSet<StateNode>();
		
		for(StateNode e: deadStates) {
			preDeadlockSet.add(e);
		}
		
		//初始化tem集合（死锁节点上一层节点）
		for(StateNode s: deadStates) {
			Set<Transition> set = grapht.incomingEdgesOf(s);
			for(Transition t : set) {
				grapht.getEdgeSource(t).setToDeadLength(1);  //初始距离为1
				tem.add(grapht.getEdgeSource(t));
			}
		}
		//System.out.println("##死锁节点上一层节点共"+tem.size()+"个，分别为");  //for debug
		//		for(StateNode node : tem) {
		//System.out.print(node.getStateNo()+" ");
		//		}
		StateNode currentState = null;
		
		while(!tem.isEmpty()) {
			currentState = tem.poll();
			boolean flag = false;
			for(StateNode child : currentState.getChildNodes()) {
				//只要有一个孩子节点，不属于死锁相关，则标记为临界节点
				if(!preDeadlockSet.contains(child)){
					criticalStates.add(currentState);
					flag = true;
					break;
				}
			}
			if(!flag) {
				preDeadlockSet.add(currentState);
				//System.out.println("\n"+currentState.getStateNo()+"不是临界节点往上接着找");
				Set<Transition> set = grapht.incomingEdgesOf(currentState);
				for(Transition t : set) {
					//System.out.println(currentState.getStateNo()+"新增加的上游节点"+grapht.getEdgeSource(t).getStateNo());
					if(!grapht.getEdgeSource(t).isChange()) {
						grapht.getEdgeSource(t).addPathLength();
						grapht.getEdgeSource(t).setChange(true);
					}
					tem.add(grapht.getEdgeSource(t));
				}
			}
			//System.out.println(tem);
		}
		
		resultStr.append("\n临界节点为：\n");
		//System.out.println("\n临界节点为：");
		for(StateNode node : criticalStates) {
			resultStr.append(node.getStateNo() + " ");
			//System.out.print(node.getStateNo() + " ");
		}
		resultStr.append("\n删除的坏节点为：\n");
		//System.out.println("\n删除的坏节点为：");
		int pathLength = 0;
		for(StateNode node : preDeadlockSet) {
			resultStr.append(node.getStateNo() + " ");
			if(node.getToDeadLength() > pathLength) pathLength = node.getToDeadLength();
			//System.out.print(node.getStateNo() + " ");
		}
		resultStr.append("\n综上，最优步长为："+pathLength+"\n删除"+preDeadlockSet.size()+"个坏状态," + 
				"占全部状态的"+preDeadlockSet.size()+"/"+grapht.vertexSet().size());
//		System.out.println("\n综上，最优步长为："+pathLength+"\n删除"+preDeadlockSet.size()+"个坏状态," + 
//				"占全部状态的"+preDeadlockSet.size()+"/"+grapht.vertexSet().size());
		FileUtil.write(destPath, resultStr.toString(), true);
	}
	
	/**
	 * @param transIndex 发射变迁编号
	 * @param currentState 待发射状态
	 * @return  
	 */
	public Marking fire(StateNode currentState, int transIndex) {
	    Marking newMarking = new Marking(PetriModel.placesCount);;  //发射之后的marking
	    for (int i = 0; i < currentState.getState().length; i++) {
	    	newMarking.marking[i] = currentState.getState()[i] + PetriModel.preMatrix.getValue(i, transIndex) - 
	    			PetriModel.posMatrix.getValue(i, transIndex);	
	    }
	    return newMarking;
	}
		   
	 /**
	  * 得到当前状态下能够发射的变迁
	  * @return boolean[] 
	  */
	 public boolean[] getEnabledTrans(StateNode currentState) {
	      //记录变迁是否能发射结果
	      boolean[] result = new boolean[PetriModel.transCount];  
	      for(int i = 0; i < result.length; i++) {
	    	  result[i] = true;
	      }
	      for (int i = 0; i < PetriModel.transCount ;i++) {
	         for (int j = 0; j < PetriModel.placesCount; j++) {
	        	 //必须指定变迁全部满足
	            if ((currentState.getState()[j] < PetriModel.posMatrix.getValue(j, i))) {  
	               result[i] = false;
	               break;
	            }
	         }
	      }
	      return result;
	   }  
	
	/**
	 * 检查当前状态是否已经发生过
	 * @param node 待检查状态
	 * @return boolean true:发生过
	 */
	public boolean ifOccured(Marking node) {
		if(preStatesMap.containsKey(node.hashCode())){
			return true;
		}else{
			return false;
		}
	}
	
	
	/**
	 * 检查当前状态是否已经发生过
	 * @param node 待检查状态
	 * @return boolean true:发生过
	 */
	public boolean ifOccured2(StateNode node) {
		if(preStatesMap.containsKey(node.hashCode())){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * p.nr: 1 2 3 4 5 6 7 8
	 * @return
	 */
	public String printPlaces() {
		StringBuffer sb = new StringBuffer();
		sb.append("p.nr: ");
		for(int i = 0; i < PetriModel.placesCount; i++) {
			sb.append((i+1)+" ");
		}
		return sb.toString();
	}

}
