package com.nuix.nx;

/**
 * Interface for classes providing data to {@link NuixDataBridge}
 * @author JasonWells
 *
 * @param <T>
 */
public interface DataProvider<T> {
	T getData();
}
