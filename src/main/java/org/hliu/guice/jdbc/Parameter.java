package org.hliu.guice.jdbc;

public class Parameter {

	public ParameterType getType() {
		return type;
	}

	public void setType(ParameterType type) {
		this.type = type;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	private ParameterType type;
	private Object value;
	
	public Parameter(ParameterType type, Object value) {
		this.type = type;
		this.value = value;
	}
	public String toString() {
		return "Parameter: type=" + type + ", value=" + value;
	}
}
