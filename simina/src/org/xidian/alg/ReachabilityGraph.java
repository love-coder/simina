package org.xidian.alg;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

import org.jgrapht.DirectedGraph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.AllDirectedPaths;
import org.jgrapht.alg.CycleDetector;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DirectedPseudograph;
import org.xidian.model.GraphModel;
import org.xidian.model.Marking;
import org.xidian.model.Matrix;
import org.xidian.model.PetriModel;
import org.xidian.model.StateNode;
import org.xidian.model.Transition;
import org.xidian.utils.FileUtil;

/**
 * 可达图（广义）数据模型
 * @author HanChun
 * @version 1.0 2016-5-18
 * @version 1.1 2016-6-4 增加环的检测，traverseRG()方法中生成邻接矩阵
 * 在CyclePath类中计算相应的路径，至此，已经有：（1）广度安层遍历，生成可达图；（2）借助jgrapht
 * 计算死锁路径；（3）实现邻接矩阵和邻接图计算环信息；
 * @version 2.0 2016-10-2 (1)自定义方式生成局部可达图;
 */
public class ReachabilityGraph {
	
	private String destPath; //输出路径
	private StateNode rootState; //初始状态
	private static Map<Integer,StateNode> preStatesMap; 
	private DirectedGraph<StateNode, Transition> grapht;	//图论
	private List<StateNode> deadStates;	//死锁状态
	private int statesAmout; //总共多少状态
	private GraphModel graphModel;
	private CyclePath cyclePath;
	
	/**
	 * @param rootState 初始状态
	 * @param model PetriModel
	 */
	public ReachabilityGraph(String destPath) {
		this.destPath = destPath;
		this.rootState = new StateNode(PetriModel.ininmarking.getMarking(), 1, 1); //初始初始状态
		try {
			createReachabilityGraph(null, 0);
		    traverseReachabilityGraph();
			//createCustomizedPartReachabilityGraph(new StateNode(new int[]{5,2,1,0,8,0,0,0,1,0,2}, 1, 1), 2);  //测试局部未删减可达图
		} catch (CloneNotSupportedException e) {
			System.err.println("生成可达图失败！");
			e.printStackTrace();
		}
	}

//	/**  
//	 * 生成可达图(非递归方法), 输出到文件
//	 * @throws CloneNotSupportedException 
//	 */
//	public void createReachabilityGraph() throws CloneNotSupportedException{
//		preStatesMap = new HashMap<Integer,StateNode>(1000); //初始化1000个状态
//		StringBuffer resultStr = new StringBuffer("可达图分析\n");
//		int stateCount = 1;	//状态数
//		Stack<Integer> nextTrans = new Stack<Integer>(); //当前状态下，能够发射的变迁
//		Queue<StateNode> stateQueue = new LinkedList<StateNode>();  //状态队列
//		Marking temState = null;
//		StateNode currentState = rootState;
//		StateNode duringState = null;  //过程中探索到的状态
//		preStatesMap.put(currentState.hashCode(), currentState);
//		stateQueue.add(rootState); //根状态为起始状态
//		boolean[] canFire = null;
//		while(!stateQueue.isEmpty()) {
//			currentState = stateQueue.poll();	//每次取出队列中最前面的状态作为当前状态（注意该状态可能不是新状态）
//			resultStr.append("\nState nr:" + currentState.getStateNo() + "\n"
//					+ printPlaces() + "\n" + "toks: " + currentState + "\n");
//			canFire = getEnabledTrans(currentState);
//			for(int i = 0; i < canFire.length; i++) {
//				if(canFire[i]) nextTrans.push(i);
//			}
//			//死锁状态
//			if(nextTrans.isEmpty()) {
//				currentState.setIfDeadlock(true);
//				resultStr.append("dead state\n");
//			}else{
//				while(!nextTrans.isEmpty()) {
//					resultStr.append("==" + "t" + (nextTrans.peek() + 1) + "==>");
//					temState = fire(currentState, nextTrans.pop());
//					//新状态
//					if(!ifOccured(temState)) {
//						stateCount++;
//						duringState = new StateNode(temState.marking, stateCount);
//						resultStr.append("s"+duringState.getStateNo()+"\n");
//						preStatesMap.put(temState.hashCode(), duringState);
//						stateQueue.add((StateNode) duringState.clone());
//					//旧状态
//					} else {
//						resultStr.append("s"+preStatesMap.get(temState.hashCode()).getStateNo()+"\n");
//					}
//				}
//			}
//		}
//		statesAmout = stateCount;
//		resultStr.append("\n----------end----------");
//		//System.out.println(resultStr.toString());	//for debug
//		FileUtil.write(destPath, resultStr.toString(), false);
//		initGrapht();
//	}
	
	/** 
	 * 自定义方式生成局部可达图 (未删减)
	 * @param initNode 自定义初始marking
	 * @param step 步长, 为0表示不限制步长
	 * @throws CloneNotSupportedException 
	 */
	public void createReachabilityGraph(StateNode initNode, int step) throws CloneNotSupportedException{
		if(initNode == null) {
			initNode = rootState;
		}
		String str = "初始marking[" + initNode.toString().trim() + "],探测步长为" + step + "的局部可达图(未删减)\n";
		if(step == 0) {
			step = Integer.MAX_VALUE - 2;
			str = "初始marking[" + initNode.toString().trim() + "],可达图(未删减)\n";
		}
		preStatesMap = new HashMap<Integer,StateNode>(200); //初始化1000个状态
		StringBuffer resultStr = new StringBuffer(str);
		int stateCount = 1;	//状态数
		Stack<Integer> nextTrans = new Stack<Integer>(); //当前状态下，能够发射的变迁
		Queue<StateNode> stateQueue = new LinkedList<StateNode>();  //状态队列
		Marking temState = null;
		StateNode currentState = initNode;
		StateNode duringState = null;  //过程中探索到的状态
		preStatesMap.put(currentState.hashCode(), currentState);
		stateQueue.add(initNode); //根状态为起始状态
		boolean[] canFire = null;
		while(!stateQueue.isEmpty()) {
			//超过步长,跳出循环
			if(currentState.getDepth() > (step + 1)) {
				break;
			}
			currentState = stateQueue.poll();	//每次取出队列中最前面的状态作为当前状态（注意该状态可能不是新状态）
			resultStr.append("\nState nr:" + currentState.getStateNo() + "\n"
					+ printPlaces() + "\n" + "toks: " + currentState + "\n");
			canFire = getEnabledTrans(currentState);
			for(int i = 0; i < canFire.length; i++) {
				if(canFire[i]) {
					nextTrans.push(i);
				}
			}
			//超过步长,不再计算可发射变迁
			if(currentState.getDepth() > step) {
				continue;
			}
			//死锁状态
			if(nextTrans.isEmpty()) {
				currentState.setIfDeadlock(true);
				resultStr.append("dead state\n");
			} else {
				while(!nextTrans.isEmpty()) {
					if(currentState.getDepth() > (step + 1)) {}
					resultStr.append("==" + "t" + (nextTrans.peek() + 1));
					temState = fire(currentState, nextTrans.pop());
					//新状态
					if(!ifOccured(temState)) {
						stateCount++;
						duringState = new StateNode(temState.marking, stateCount, currentState.getDepth() + 1);
						resultStr.append("==>" + "s" + duringState.getStateNo() + "\n");
						preStatesMap.put(temState.hashCode(), duringState);
						stateQueue.add((StateNode) duringState.clone());
					//旧状态
					}else{
						resultStr.append("==>" + "s" + preStatesMap.get(temState.hashCode()).getStateNo() + "\n");
					}
				}
			}
		}
		statesAmout = stateCount;
		resultStr.append("\n----------end----------");
		//System.out.println(resultStr.toString());	//for debug
		FileUtil.write(destPath, resultStr.toString(), false);
		initGrapht();
	}
	
	/**
	 * @see createReachabilityGraph(),该方法在createReachabilityGraph()上面做了优化（之所以不在上面直接改，是为了保证基础版的足够简单）
	 * 使用jgrapht开源包，实现遍历可达图，生成图数据结构，然后借助相应的办法
	 * @param destPath 输出目录
	 * @throws CloneNotSupportedException 
	 */
	public void traverseReachabilityGraph() throws CloneNotSupportedException{
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
						
						//下面转为graph model
						graphModel.getCostMatrix().getMatrix()[preStatesMap.get(currentState.hashCode()).getStateNo()-1][duringState.getStateNo()-1] = 1;
						graphModel.getArcMatrix().getStrMatrix()[preStatesMap.get(currentState.hashCode()).getStateNo()-1][duringState.getStateNo()-1] = currentTranName;
						
						preStatesMap.put(temState.hashCode(), duringState);
						stateQueue.add((StateNode) duringState.clone());
					//旧状态
					}else{
						currentState.getChildNodes().add(preStatesMap.get(temState.hashCode()));
						currentTran = grapht.addEdge(preStatesMap.get(currentState.hashCode()), preStatesMap.get(temState.hashCode()));
						currentTran.setTranName(currentTranName);
						//下面转为graph model
						graphModel.getCostMatrix().getMatrix()[preStatesMap.get(currentState.hashCode()).getStateNo()-1][preStatesMap.get(temState.hashCode()).getStateNo()-1] = 1;
						graphModel.getArcMatrix().getStrMatrix()[preStatesMap.get(currentState.hashCode()).getStateNo()-1][preStatesMap.get(temState.hashCode()).getStateNo()-1] = currentTranName; 
						
					}
				}
			}
		}
		//Matrix.printMatrix(graphModel.getCostMatrix().getMatrix());
		calculateDeadlockPath();
	}
	
	/**
	 * 初始化graph model
	 */
	public void initGrapht() {
		graphModel = new GraphModel(statesAmout);
	}
	
	/**
	 * 1.计算出起始节点到各个死锁节点的全部(或部分)路径
	 * 2.计算出各个路径中的最短路径（路径长度）
	 * 3.计算步长
	 * 4.计算环个数
	 */
	public void calculateDeadlockPath() {
		StringBuffer resultStr = new StringBuffer("完全许可性为：" + grapht.vertexSet().size() 
				+ "\ndeadlock states 共  "+deadStates.size()+" 个分别如下\n");
		GraphPath<StateNode, Transition> deadShortPaths = null;
		//计算出最短路径
		for(StateNode el : deadStates) {
			DijkstraShortestPath<StateNode, Transition> dpath = new DijkstraShortestPath<StateNode, Transition>(grapht,rootState, el);
			resultStr.append("["+el.getStateNo()+"] "+el.toString()+"\n");
			deadShortPaths = dpath.getPath();
			for(Transition tran : deadShortPaths.getEdgeList()){
				resultStr.append(tran.getTranName()+"=>");
			}
			resultStr.append("dead state["+deadShortPaths.getEndVertex().getStateNo()+"](最短路径)"+"\n");
		}
		resultStr.append("\n其余死锁路径参考(若状态数大于40，则只显示其中部分路径)\n");
		//显示更多的路径
		for(StateNode el: deadStates) {
			Integer calNum = null; //计算路径步长
			if(grapht.vertexSet().size() > 40) calNum = PetriModel.transCount;
			AllDirectedPaths<StateNode, Transition> allPath = new AllDirectedPaths<StateNode, Transition>((DirectedGraph<StateNode, Transition>) grapht);
			List<GraphPath<StateNode, Transition>> paths = allPath.getAllPaths(rootState, el, true, calNum);
			resultStr.append("\ndead state[" + el.getStateNo() +"]" + "其余参考路径：\n");
			for(GraphPath<StateNode, Transition> g : paths) {
	    		for(Transition t : g.getEdgeList()) {
	    			resultStr.append(t.getTranName() + "=>");
	    		}
	    		resultStr.append("dead state[" + el.getStateNo() +"]\n");
	    	}
		}
		
		//暂时考虑是：状态数低于100计算环情况
//		if(grapht.vertexSet().size() < 100) {
//			resultStr.append("\n全局可达图环路信息分析如下：\n");
//			//System.out.println("\n全局可达图环路信息分析如下：");
//			CycleDetector<StateNode, Transition> cdetector =  new CycleDetector<StateNode, Transition>(grapht);
//			System.out.println("共   "+cdetector.findCycles().size() + " 个环，其中经过节点（即可逆）的环共   " + cdetector.findCyclesContainingVertex(rootState).size() + " 个环");
//			resultStr.append("共   "+cdetector.findCycles().size() + " 个环，其中经过节点（即可逆）的环共   " + cdetector.findCyclesContainingVertex(rootState).size() + " 个环");		
//		}
		//添加步长计算
		resultStr.append(getDeadlockPathLength());
		//String path = destPath;
		FileUtil.write(destPath.substring(0, destPath.length()-3) + "path", resultStr.toString(), true);
		//路径探索
		//cyclePath = new CyclePath(graphModel);
	}
	
	/**
	 * 改进版的DFS,计算最优步长
	 */
	public String getDeadlockPathLength() {
		StringBuffer resultStr = new StringBuffer("\n最优步长分析结果：\n\n");
		TreeSet<Integer> preDeadlockNodes =  new TreeSet<Integer>();
		TreeSet<Integer> criticalNodes = new TreeSet<Integer>(); //临界节点
		Queue<Integer> stateQueue = new LinkedList<Integer>();  //状态队列
		//下标0开始
		for(StateNode node : deadStates) {
			stateQueue.add(node.getStateNo() - 1);
			preDeadlockNodes.add(node.getStateNo() - 1);
			node.setDepth(1); //初始化全部死锁节点高度为1
		}
		int currentNode =  -1;
		List<Integer> upNodes = null;
		while(!stateQueue.isEmpty()) {
			currentNode = stateQueue.poll();//每次取出队列中最前面的状态作为当前状态（注意该状态可能不是新状态）
			upNodes = getUpNodes(currentNode); //得到上一层节点
			if(upNodes!=null) {
				for(Integer i : upNodes) {
					//该节点是deadlock节点, getDownNodes(i) i节点的全部下层节点
					if(ifOccuredInDeadlock(getDownNodes(i), preDeadlockNodes)){
						preDeadlockNodes.add(i);
						stateQueue.add(i);
					//该节点为临界节点
					} else {
						criticalNodes.add(i);
					}
				}
			}
		}
		resultStr.append("临界状态：" + printSet(preDeadlockNodes) + "\n");
		resultStr.append("dead state：" + printSet(criticalNodes));
		resultStr.append("\n\n关系如下,s(临界)==>s(dead state)\n");
		//下面开始打印关系
		List<Integer> tempDownNodes = null;
		for(Integer cri : criticalNodes) {
			tempDownNodes = getDownNodes(cri);
			for(Integer i : tempDownNodes) {
				if(preDeadlockNodes.contains((Integer)i)) {
					resultStr.append("s" + (cri + 1) + "==>" + "s" + (i + 1) + "\n");
				}
			}
		}
		
		System.out.println(resultStr);
		return resultStr.toString();
	}
	
	/**
	 * @param transIndex 发射变迁编号
	 * @param currentState 待发射状态
	 * @return  
	 */
	public Marking fire(StateNode currentState, int transIndex) {
	    Marking newMarking = new Marking(PetriModel.placesCount);;  //发射之后的marking
	    for (int i = 0; i < currentState.getState().length; i++) {
	    	newMarking.marking[i] = currentState.getState()[i] + PetriModel.preMatrix.getValue(i, transIndex)  
	    			- PetriModel.posMatrix.getValue(i, transIndex);	
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
	  * 由GraphModel 得到上一层节点（后往前推）
	  * 对应就是邻接矩阵的列向量
	  * @return boolean[] 
	  */
	 public List<Integer> getUpNodes(int currentNode) {
	      //记录变迁是否能发射结果
	      List<Integer>  tem = new LinkedList<Integer>();
	      int[] temArr = Matrix.getMatrixCol(currentNode, graphModel.costMatrix.getMatrix());
	      for(int i = 0; i < temArr.length; i++){
	    	  if(temArr[i] != 0){
	    		  tem.add(i);
	    	  }
	      }
	      return tem;
	   } 
	 
	 /**
	  * 由GraphModel 得到上一层节点（后往前推）
	  * 对应就是邻接矩阵的列向量
	  * @return boolean[] 
	  */
	 public List<Integer> getDownNodes(int currentNode) {
	      //记录变迁是否能发射结果
	      List<Integer>  tem = new LinkedList<Integer>();
	      int[] temArr = Matrix.getMatrixRow(currentNode, graphModel.costMatrix.getMatrix());
	      for(int i = 0; i < temArr.length; i++){
	    	  if(temArr[i] != 0){
	    		  tem.add(i);
	    	  }
	      }
	      return tem;
	   }  
	 
	/**
	 * 检查当前状态是否已经在deadlock 状态里面
	 * @param i
	 * @param set
	 * @return boolean true:deadlock false:邻界节点
	 */
	public boolean ifOccuredInDeadlock(List<Integer> list, Set<Integer> set) {
		//只要有一个节点不在死锁节点集合里面，则为临界节点
		for(Integer i : list) {
			if(!set.contains(i)){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 检查当前状态是否已经发生过
	 * @param node 待检查状态
	 * @return boolean true:发生过
	 */
	public boolean ifOccured(Marking node) {
		return preStatesMap.containsKey(node.hashCode());
	}
	
	/**
	 * 检查当前状态是否已经发生过
	 * @param node 待检查状态
	 * @return boolean true:发生过
	 */
	public boolean ifOccured2(StateNode node) {
		return preStatesMap.containsKey(node.hashCode());
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
		return sb.toString().trim();
	}
	
	//for debug
	public String printSet(Set<Integer> s) {
		StringBuffer tem = new StringBuffer();
		tem.append("总共：" + s.size() + "个\n");
		//System.out.println("总共：" + s.size() + "个");
		for(Integer i : s) {
			tem.append((i+1)+" ");
			//System.out.print((i+1) + " ");
		}
		return tem.toString();
	}

}
