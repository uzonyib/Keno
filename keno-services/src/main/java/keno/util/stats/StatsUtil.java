package keno.util.stats;

import java.util.List;

import keno.model.Draw;

public class StatsUtil {
	
	private StatsUtil() {
		
	}
	
	public static int[] getCounts(List<Draw> draws) {
		if (draws == null) {
			throw new IllegalArgumentException("Parameter is null!");
		}
		
		int[] counts = new int[80];
		for (Draw draw : draws) {
			for (byte number : draw.getNumbers()) {
				++counts[number - 1];
			}
		}
		return counts;
	}

}
