package edu.ucsd.som.vchs.converter.helper;

import java.util.Collection;

public interface ViewCachedAutoCompleteHelper {
	/**
	 * Converts an object to its value by using the toString method on the object.
	 * Be sure the object has a toString method which returns the ID.
	 * 
	 * @param value
	 * @param values
	 * @return the converted value of the object
	 */
	<T> T convertToObjectValue(String value, Collection<T> values);
}
