package com.fortyoneconcepts.valjogen.test.input;

import com.fortyoneconcepts.valjogen.annotations.VALJOGenerate;

@VALJOGenerate
public interface MutableInterface
{
	public int getIntValue();
	public void setIntValue(int v);

	public Object getObjectValue();
	public void setObjectValue(Object v);
}
