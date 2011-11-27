package keno.service;

import java.util.List;

import keno.model.Draw;
import keno.model.NumberState;

public interface LotteryService {
	
	Draw getMostRecentDraw();
	
	List<Draw> listMostRecentDraws(int count);
	
	List<Draw> listMostRecentDraws(int count, List<NumberState> filter);
	
	List<Draw> listDraws();
	
	List<Draw> listDraws(List<NumberState> filter);

}
