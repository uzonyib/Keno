package keno.service;

import java.util.List;

import keno.model.Draw;

public interface LotteryService {
	
	Draw getMostRecentDraw();
	
	List<Draw> listMostRecentDraws(int count);
	
	List<Draw> listDraws();

}
