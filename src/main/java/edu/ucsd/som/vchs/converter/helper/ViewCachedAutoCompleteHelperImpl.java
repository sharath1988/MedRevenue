package edu.ucsd.som.vchs.converter.helper;

import java.util.Collection;

public class ViewCachedAutoCompleteHelperImpl implements ViewCachedAutoCompleteHelper {
	@Override
	public <T> T convertToObjectValue(String value, Collection<T> values) {
		if (value == null || value.isEmpty()) {
			return null;
		}
		for (T currentValue: values) {
			if (value.equals(currentValue.toString())) {
				return currentValue;
			}
		}
		return null;
	}
}
