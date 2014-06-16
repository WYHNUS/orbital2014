package edu.nus.comp.dotagridandroid.logic;

import java.util.Arrays;

public final class GridPointIndex {
	private final int[] idx;

	public GridPointIndex (int[] idx) {
		if (idx == null || idx.length != 2)
			throw new RuntimeException("Wrong grid point index");
		this.idx = idx.clone();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof GridPointIndex))
			return false;
		return ((GridPointIndex) o).idx.equals(idx);
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode(idx);
	}
}
