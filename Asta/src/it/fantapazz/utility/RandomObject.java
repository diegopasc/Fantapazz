package it.fantapazz.utility;

import java.util.List;
import java.util.Random;

public class RandomObject {

	public static <T> T randomObject(List<T> list) {
		int index = new Random().nextInt(list.size());
		return list.get(index);
	}
	
}
