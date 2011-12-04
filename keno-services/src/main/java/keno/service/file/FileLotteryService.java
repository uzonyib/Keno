package keno.service.file;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import keno.model.Draw;
import keno.service.LotteryService;
import keno.util.parser.DrawFileReader;

import org.apache.log4j.Logger;

public class FileLotteryService implements LotteryService {

	private static final Logger LOGGER = Logger.getLogger(FileLotteryService.class);
	
	private File source;

	public FileLotteryService(String sourcePath) {
		this.source = new File(sourcePath);
	}

	@Override
	public Draw getMostRecentDraw() {
		List<Draw> list = listMostRecentDraws(1, null, null);
		if (list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}
	
	@Override
	public List<Draw> listMostRecentDraws(int count) {
		return listMostRecentDraws(count, null, null);
	}

	@Override
	public List<Draw> listMostRecentDraws(int count, Collection<Byte> winnerNumbers) {
		return listMostRecentDraws(count, winnerNumbers, null);
	}
	
	@Override
	public List<Draw> listMostRecentDraws(int count, Collection<Byte> winnerNumbers,
			Collection<Byte> hitCounts) {
		FileReader fr = null;
		try {
			fr = new FileReader(source);
			DrawFileReader reader = new DrawFileReader(fr);
			
			List<Draw> draws = new ArrayList<Draw>();
			
			if (count > 0) {
				for (int i= 0; i < count; ++i) {
					Draw draw = reader.readNext();
					if (draw != null) {
						if (winnerNumbers != null && !winnerNumbers.isEmpty()) {
							if (hitCounts == null) {
								draws.add(draw);
							} else if (hitCounts.contains(draw.getHitCount(winnerNumbers))){
								draws.add(draw);
							}
						} else {
							draws.add(draw);
						}
					} else {
						break;
					}
				}
			} else {
				Draw draw = null;
				while ((draw = reader.readNext()) != null) {
					if (draw != null) {
						if (winnerNumbers != null && !winnerNumbers.isEmpty()) {
							if (hitCounts == null) {
								draws.add(draw);
							} else if (hitCounts.contains(draw.getHitCount(winnerNumbers))){
								draws.add(draw);
							}
						} else {
							draws.add(draw);
						}
					}
				}
			}
			LOGGER.info("Count: " + count + ", required: " + winnerNumbers + ", hits: " + hitCounts
					+ ". Result count: " + draws.size());
			return draws;
		} catch (IOException e) {
			LOGGER.error("I/O error while processing file: " + source.getAbsolutePath());
			return Collections.<Draw>emptyList();
		} finally {
			try {
				if (fr != null) {
					fr.close();
				}
			} catch (IOException e) {
				
			}
		}
	}

	@Override
	public List<Draw> listMostRecentDraws() {
		return listMostRecentDraws(-1, null, null);
	}
	
	@Override
	public List<Draw> listMostRecentDraws(Collection<Byte> winnerNumbers) {
		return listMostRecentDraws(-1, winnerNumbers, null);
	}
	
	@Override
	public List<Draw> listMostRecentDraws(Collection<Byte> winnerNumbers,
			Collection<Byte> hitCounts) {
		return listMostRecentDraws(-1, winnerNumbers, hitCounts);
	}

}
