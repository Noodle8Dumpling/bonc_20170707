package cn.com.bonc.config;

public class Conf {
	
	
	
	/*
	 * index infomation 
	 * */
	public String sourceFileType="KAFKA";
	
	public String processor="cn.com.bonc.dataprocess.OrignalProcessor";
	
	/*
	 * ElasticSearch's configuration 	  
	 * */
	
	public String esClusterName=null;
	public String esIndexName=null;
	public String esTypeName=null;
	public String esHost=null;
	public String esPort=null;
	public String esFileds=null;
	public String esMainKey=null;
	public String esFieldSeperator=null;
	
		
	
	/*
	 * Kafka's configuration  
	 * */
	
	public String kafkaTopic=null;
	public String kafkaZookeeperHosts=null;
	public String kafkaZookeeperPort=null;
	public String kafkaGroupID=null;
	public Integer    kafkaTopicPartitions=0;
	
	
	
	
	/*
	 * txt file's configration 
	 * */
	
	public String filePath=null;
	
	
	public String getSourceFileType() {
		return sourceFileType;
	}




	public void setSourceFileType(String sourceFileType) {
		this.sourceFileType = sourceFileType;
	}




	public String getProcessor() {
		return processor;
	}




	public void setProcessor(String processor) {
		this.processor = processor;
	}




	public String getEsClusterName() {
		return esClusterName;
	}




	public void setEsClusterName(String esClusterName) {
		this.esClusterName = esClusterName;
	}




	public String getEsIndexName() {
		return esIndexName;
	}




	public void setEsIndexName(String esIndexName) {
		this.esIndexName = esIndexName;
	}




	public String getEsTypeName() {
		return esTypeName;
	}




	public void setEsTypeName(String esTypeName) {
		this.esTypeName = esTypeName;
	}




	public String getEsHost() {
		return esHost;
	}




	public void setEsHost(String esHost) {
		this.esHost = esHost;
	}




	public String getEsPort() {
		return esPort;
	}




	public void setEsPort(String esPort) {
		this.esPort = esPort;
	}




	public String getEsFileds() {
		return esFileds;
	}




	public void setEsFileds(String esFileds) {
		this.esFileds = esFileds;
	}




	public String getEsMainKey() {
		return esMainKey;
	}




	public void setEsMainKey(String esMainKey) {
		this.esMainKey = esMainKey;
	}




	public String getEsFieldSeperator() {
		return esFieldSeperator;
	}




	public void setEsFieldSeperator(String esFieldSeperator) {
		this.esFieldSeperator = esFieldSeperator;
	}




	public String getKafkaTopic() {
		return kafkaTopic;
	}




	public void setKafkaTopic(String kafkaTopic) {
		this.kafkaTopic = kafkaTopic;
	}




	public String getKafkaZookeeperHosts() {
		return kafkaZookeeperHosts;
	}




	public void setKafkaZookeeperHosts(String kafkaZookeeperHosts) {
		this.kafkaZookeeperHosts = kafkaZookeeperHosts;
	}




	public String getKafkaZookeeperPort() {
		return kafkaZookeeperPort;
	}




	public void setKafkaZookeeperPort(String kafkaZookeeperPort) {
		this.kafkaZookeeperPort = kafkaZookeeperPort;
	}




	public String getKafkaGroupID() {
		return kafkaGroupID;
	}




	public void setKafkaGroupID(String kafkaGroupID) {
		this.kafkaGroupID = kafkaGroupID;
	}




	public int getKafkaTopicPartitions() {
		return kafkaTopicPartitions;
	}




	public void setKafkaTopicPartitions(int kafkaTopicPartitions) {
		this.kafkaTopicPartitions = kafkaTopicPartitions;
	}




	public String getFilePath() {
		return filePath;
	}




	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}




	
}
