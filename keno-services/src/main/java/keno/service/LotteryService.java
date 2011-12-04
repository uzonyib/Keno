package keno.service;

import java.util.Collection;
import java.util.List;

import keno.model.Draw;

public interface LotteryService {
	
	Draw getMostRecentDraw();
	
	List<Draw> listMostRecentDraws(int count);
	
	List<Draw> listMostRecentDraws(int count, Collection<Byte> winnerNumbers);
	
	List<Draw> listMostRecentDraws(int count, Collection<Byte> winnerNumbers,
			Collection<Byte> hitCounts);
	
	List<Draw> listMostRecentDraws();
	
	List<Draw> listMostRecentDraws(Collection<Byte> winnerNumbers);
	
	List<Draw> listMostRecentDraws(Collection<Byte> winnerNumbers,
			Collection<Byte> hitCounts);

}
