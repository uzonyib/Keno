package keno.service.file;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import keno.model.Draw;
import keno.model.NumberState;
import keno.service.LotteryService;
import keno.util.parser.DrawFileReader;

import org.apache.log4j.Logger;

// TODO unit test
public class FileLotteryService implements LotteryService {

	private static final Logger LOGGER = Logger.getLogger(FileLotteryService.class);
	
	private File source;

	public FileLotteryService(String sourcePath) {
		this.source = new File(sourcePath);
	}

	@Override
	public Draw getMostRecentDraw() {
		List<Draw> list = listMostRecentDraws(1);
		if (list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}
	
	@Override
	public List<Draw> listMostRecentDraws(int count) {
		return listMostRecentDraws(count, null);
	}

	@Override
	public List<Draw> listMostRecentDraws(int count, List<NumberState> filter) {
		FileReader fr = null;
		try {
			fr = new FileReader(source);
			DrawFileReader reader = new DrawFileReader(fr);
			
			List<Draw> draws = new ArrayList<Draw>();
			
			if (count > 0) {
				for (int i= 0; i < count; ++i) {
					Draw draw = reader.readNext();
					if (draw != null) {
						if (filter(draw, filter)) {
							draws.add(draw);
						}
					} else {
						break;
					}
				}
			} else {
				Draw draw = null;
				while ((draw = reader.readNext()) != null) {
					if (filter(draw, filter)) {
						draws.add(draw);
					}
				}
			}
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
	
	private static boolean filter(Draw draw, List<NumberState> filter) {
		if (filter == null) {
			return true;
		}
		
		for (int i = 0; i < filter.size(); ++i) {
			if (filter.get(i) == NumberState.SELECTED
					&& !draw.hasNumber((byte) (i + 1))) {
				return false;
			} else if (filter.get(i) == NumberState.DESELECTED
					&& draw.hasNumber((byte) (i + 1))) {
				return false;
			}
		}
		return true;
	}

	@Override
	public List<Draw> listDraws() {
		return listMostRecentDraws(-1);
	}
	
	@Override
	public List<Draw> listDraws(List<NumberState> filter) {
		return listMostRecentDraws(-1, filter);
	}

}
