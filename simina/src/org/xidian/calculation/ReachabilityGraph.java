package org.xidian.calculation;

import java.util.ArrayList;
import java.util.List;

import org.xidian.model.Marking;
import org.xidian.model.PetriModel;

/**
 * 计算可达图(进行广度计算)
 * @author HanChun
 * @version 1.0 2016-5-16
 */
public class ReachabilityGraph {
	
	PetriModel model; 
	Marking currentMarking; 
	
	/**
	 * 得到当前状态下，能够发射的变迁序列
	 * @param currentState 当前状态
	 * @return 
	 */
	public boolean[] getFireTrans(int currentState) {
		
		
		
		return null;
	}
	
   
	
	
	
	
	/**
	 * 是否能发射
	 * @return
	 */
	public boolean canFire() {
		
		return false;
	}

}
