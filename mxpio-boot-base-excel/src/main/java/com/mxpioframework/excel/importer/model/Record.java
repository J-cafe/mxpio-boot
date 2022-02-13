package com.mxpioframework.excel.importer.model;

import java.util.HashMap;
import java.util.Map;

public class Record {
	
	private int row = -1;

	private Map<Integer, Cell> cellMap = new HashMap<>();
	
	public boolean addCellIfNeed(Cell cell) {
		if (row == -1) {
			row = cell.getRow();
			cellMap.put(cell.getCol(), cell);
			return true;
		} else if (row == cell.getRow()) {
			cellMap.put(cell.getCol(), cell);
			return true;
		}
		return false;
	}
	
	public Cell getCell(int col) {
		Cell cell = cellMap.get(col);
		if (cell == null) {
			cell = new Cell(row, col);
		}
		return cell;
	}
	
}
