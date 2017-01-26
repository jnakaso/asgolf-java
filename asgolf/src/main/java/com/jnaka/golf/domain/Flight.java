/**
 * 
 */
package com.jnaka.golf.domain;

public enum Flight {
	A, B, GUEST;

	@Override
	public String toString() {
		return this.name();
	}

}