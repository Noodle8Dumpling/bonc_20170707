package cn.com.bonc.dataprocess;

public abstract class Processor<T> {
	
	public abstract void init();
	
	public abstract T process(T input);
	

}
