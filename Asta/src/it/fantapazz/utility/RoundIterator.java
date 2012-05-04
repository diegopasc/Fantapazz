package it.fantapazz.utility;


import java.util.Iterator;
import java.util.List;

/**
 * Implementation of a round iteration: given a list of
 * objects, this class will return the list in the given
 * order and at the end of the list, it will restart 
 * from the beginning.
 * 
 * Be sure that list is not changed during iteration
 * 
 * @author Michele Mastrogiovanni
 *
 * @param <T> Type of list objects
 */
public class RoundIterator<T> implements TurnSelector<T> {
	
	private List<T> list;
	
	private Iterator<T> iterator;
			
	public T next() {
		if ( iterator == null )
			iterator = list.iterator();
		if ( !iterator.hasNext() ) {
			iterator = null;
			return null;
		}
		T player = iterator.next();
		if ( ! iterator.hasNext() ) {
			iterator = null;
		}
		return player;
	}

	public void init(List<T> list) {
		this.list = list;
	}

}
