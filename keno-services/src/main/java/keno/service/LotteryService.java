package keno.service;

import java.util.List;
import java.util.Set;

import keno.model.Draw;
import keno.model.NumberState;

public interface LotteryService {
	
	Draw getMostRecentDraw();
	
	List<Draw> listMostRecentDraws(int count);
	
	List<Draw> listMostRecentDraws(int count, List<NumberState> filter);
	
	List<Draw> listMostRecentDraws(int count, List<NumberState> filter,
			Set<Integer> hitCounts);
	
	List<Draw> listMostRecentDraws();
	
	List<Draw> listMostRecentDraws(List<NumberState> filter);
	
	List<Draw> listMostRecentDraws(List<NumberState> filter,
			Set<Integer> hitCounts);

}
