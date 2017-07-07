package cn.com.bonc.main;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import cn.com.bonc.Indexer;
import cn.com.bonc.KafkaIndexer;
import cn.com.bonc.config.Conf;
import cn.com.bonc.config.ConfigHandle;

public class Starter {

	public static void main(String args[]) {

		ConfigHandle handle = new ConfigHandle(
				"E:/Files/eclipse/workspace/CommonESIndexer/src/conf.txt");
		Conf conf = handle.getConfigs();
		String sourceFileType = conf.sourceFileType.toLowerCase();

		String indexerClass = "cn.com.bonc."
				+ sourceFileType.substring(0, 1).toUpperCase()
				+ sourceFileType.substring(1) + "Indexer";

		Indexer indexer = null;
		try {
			indexer = (Indexer) Class.forName(indexerClass).newInstance();

			@SuppressWarnings("unchecked")
			Class<Indexer> clazz = (Class<Indexer>) Class.forName(indexerClass);
			Constructor<Indexer> c = clazz.getConstructor(Conf.class);// 获取有参构造

			indexer = (Indexer) c.newInstance(conf);
			// 通过有参构造创建对象

		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (indexer == null) {
			System.out.println("source file type is kafka or txt ");
			System.exit(1);
		}
		indexer.init();
		Thread thread = new Thread(indexer);
		thread.start();
	}

}
