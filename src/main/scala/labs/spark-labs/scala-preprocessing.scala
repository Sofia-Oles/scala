// Databricks notebook source
// MAGIC %md
// MAGIC ***Analysis part***
// MAGIC 
// MAGIC I loaded “movies_metadata.csv” and ”ratings.csv” (or ratings_small.csv) as spark dataframes https://www.kaggle.com/rounakbanik/the-movies-dataset/data
// MAGIC 
// MAGIC Performed basic investigation and printed out summary (are there any missing values; How many records; How many unique users are there)
// MAGIC 
// MAGIC 
// MAGIC ---
// MAGIC 
// MAGIC 
// MAGIC ***Preprocessing part***
// MAGIC 
// MAGIC Using spark DataFrame API transform data loaded on a previous step to create MovieProfile and UserProfile entities with following schemas: 
// MAGIC 
// MAGIC 
// MAGIC ---
// MAGIC 
// MAGIC 
// MAGIC **MovieProfile entity schema**:
// MAGIC 
// MAGIC •	MovieId - id of the movie
// MAGIC 
// MAGIC •	MovieName - name of the movie
// MAGIC 
// MAGIC •	MovieRating - average from all the user ratings for the movie
// MAGIC 
// MAGIC •	Genres - an array with the comma separated genres for the movie

// COMMAND ----------

import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._

var movie_df = spark
  .read
  .format("csv")
  .options(Map("inferSchema"->"true", "header"->"true"))
  .load("/mnt/raw/TestSO/movies_metadata.csv")

var rate_df = spark
  .read
  .format("csv")
  .options(Map("inferSchema"->"true", "header"->"true"))
  .load("/mnt/raw/TestSO/ratings_small.csv")

// COMMAND ----------

//  schema in tree format

movie_df.printSchema()

// COMMAND ----------

(movie_df.count(), rate_df.count())

// COMMAND ----------

//  check missing values in movie_df in each column

display(movie_df.select(movie_df.columns.map(c => sum(col(c).isNull.cast("int")).alias(c)): _*))

// COMMAND ----------

//  MovieProfile creation

// Cast the most important fields from raw movie_df

val mv_id_name = movie_df
    .select(
      col("id").cast("int").alias("movieId"),
      col("title").cast("string").alias("movieName")
    )
display(mv_id_name)

// COMMAND ----------

//  basic preprocessing with regex, null values and valid movieName
import org.apache.spark.sql.DataFrame

def filter_name_rex(input_df: DataFrame): DataFrame = {
    // one or more alpha symbols to clean numbers
    input_df
      .filter(col("movieName")
      .rlike("[a-zA-Z]+"))
}

def filter_name_id(input_df: DataFrame): DataFrame = { 
    input_df
      .filter( 
        (col("movieName").startsWith("[") === false) 
        and   
        (col("movieId").isNotNull) 
        and 
        (col("movieName").isNotNull))
}

val movie_df_valid = mv_id_name.transform(filter_name_rex).transform(filter_name_id)
display(movie_df_valid)

// COMMAND ----------

// raw rate_df :  select, cast, validate on NotNull values      

val movie_id_rate = rate_df
    .select(
      col("movieId").cast("int").alias("movieId"),
      col("rating").cast("double").alias("movieRating"))
    .filter((col("movieId").isNotNull) and (col("movieRating").isNotNull))

display(movie_id_rate)

// COMMAND ----------

display(movie_id_rate)

// COMMAND ----------

//  df[movie_id, movie_name, avg_rate] = movie_id_name[id, name] **inner join** movie_id_rating[id, rate]

val mv_id_name_rate = movie_df_valid
  .join(movie_id_rate, movie_df_valid("movieId") === movie_id_rate("movieId"), "inner")
  .select(movie_df_valid("movieId").alias("movieId"), 
          movie_df_valid("movieName").alias("movieName"), 
          movie_id_rate("movieRating"))
  .groupBy("movieId", "movieName")
  .agg(avg(movie_id_rate("movieRating")).alias("movieRating"))

display(mv_id_name_rate)

// COMMAND ----------

// mv_id_name_rate[id, name, rate]: validation step

display(movie_df.select(col("id").cast("int").alias("movieId"), col("genres"))
        .filter(col("movieId").isNotNull and col("genres").isNotNull)
        .select(col("movieId"), get_json_object(col("genres"), "$[*].name").alias("genres"))
)

// COMMAND ----------

val final_movie_entity = movie_df
        .select(col("id").cast("int").alias("movieId"), col("genres"))
        .filter(col("movieId").isNotNull)
        .select(col("movieId").alias("movieId"), get_json_object(col("genres"), "$[*].name").alias("genres"))
        
display(final_movie_entity
        .join(mv_id_name_rate, final_movie_entity("movieId") === mv_id_name_rate("movieId"))
        .select(mv_id_name_rate("movieId"), mv_id_name_rate("movieName"), mv_id_name_rate("movieRating"), final_movie_entity("genres")))

// COMMAND ----------

display(final_movie_entity
        .join(mv_id_name_rate, final_movie_entity("movieId") === mv_id_name_rate("movieId"))
        .select(mv_id_name_rate("movieId"), mv_id_name_rate("movieName"), mv_id_name_rate("movieRating"), final_movie_entity("genres")))

// COMMAND ----------


