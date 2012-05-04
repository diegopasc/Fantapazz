package it.fantapazz.utility;

import java.util.List;

public interface TurnSelector<T> {
	
	public void init(List<T> list);
	
	public T next();

}
