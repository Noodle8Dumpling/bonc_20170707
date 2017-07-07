package cn.com.bonc.dataprocess;

public class OrignalProcessor<T> extends Processor<T> {

	
	@Override
	public void init()
	{
		//do nothing 
	}

	@Override
	public T process(T input) {
		// do nothing, just return the input 
		return input;
	}
}
