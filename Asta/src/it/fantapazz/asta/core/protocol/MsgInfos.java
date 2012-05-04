package it.fantapazz.asta.core.protocol;

import it.fantapazz.chat.Message;

public class MsgInfos extends Message {
	
	private static final long serialVersionUID = 1L;
	
	private int typeInfo;
	
	private Object value;
	
	public MsgInfos() {}

	public MsgInfos(String source, int typeInfo, Object value) {
		super(source);
		this.typeInfo = typeInfo;
		this.value = value;
	}

	public int getTypeInfo() {
		return typeInfo;
	}

	public void setTypeInfo(int typeInfo) {
		this.typeInfo = typeInfo;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "MsgInfos [typeInfo=" + typeInfo + ", value=" + value + "]";
	}
	
}
