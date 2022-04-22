// Databricks notebook source
// MAGIC %md
// MAGIC ***Spark ETL***
// MAGIC 
// MAGIC Working with Spark Structured Streaming and batch processing
// MAGIC 
// MAGIC ---

// COMMAND ----------

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.Column
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.types._
import org.apache.spark.sql.functions._

// COMMAND ----------

case class DataLoader() {
  
   def read(path: String, use_stream: Boolean): DataFrame = use_stream match {
     
      case x if use_stream => spark
        .readStream
        .format("delta")
        .load(path)
  
      case _ => spark
        .read
        .format("delta")
        .load(path)
  } 
  
  def write(df: DataFrame, path: String, use_stream: Boolean): Unit = use_stream match {
    
      case x if use_stream => df.writeStream.foreachBatch { (batchDF: DataFrame, batchId: Long) =>
        
        batchDF
        .withColumn("UID", expr("uuid()"))
        .write
        .mode("append")
        .save()}
        .format("delta")
        .option("checkpointLocation", path + "/checkpoints")
        .option("mergeSchema", "true")
        .option("path", path)
        .start()
  
      case _ => df.write.format("delta").save(path)
  }
  
}

case class DataTransformer() {
  
   def transformations(df: DataFrame): DataFrame = {
    val colsToRemove = Seq("CountryPhoneCode", "UpdatedAt", "DeletedAt", "IsDeleted", "EffectiveTime", "uuid") 
    
    df.select(df.columns.filter(colName => !colsToRemove.contains(colName)).map(colName => new Column(colName)): _*)
  }
  
}

// COMMAND ----------

val loader = new DataLoader
val transformer = new DataTransformer

val extract = loader.read(path="/mnt/raw/TestSO/Phones/Data", use_stream=true)
val transformed = transformer.transformations(df=extract)
val load = loader.write(df=transformed, path="/mnt/raw/TestSO/Done", use_stream=true)

// COMMAND ----------

// spark.read.format("delta").load("/mnt/raw/TestSO/NOW/Phones").count()

// COMMAND ----------


