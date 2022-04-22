// Databricks notebook source
// MAGIC %md
// MAGIC ### Used Databricks API to create and submit job

// COMMAND ----------

val json = """{  "name": "Scala-run-ETL",
          "tasks": [
            {
              "task_key": "Test",
              "existing_cluster_id": "0310-084216-le7561h5",
              "notebook_task": {
                "notebook_path": "/Users/c10l8@intra.coop/cm_datagen/scala-ETL"
              },
              "timeout_seconds": 1000
            }
          ],
          "email_notifications": {},
          "max_concurrent_runs": 1
}"""

// COMMAND ----------

import org.apache.spark.sql.{DataFrame, Row}
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.HttpHeaders
import org.apache.http.entity.StringEntity
import org.apache.commons.io.IOUtils

// COMMAND ----------

val client = HttpClientBuilder.create().build()
val post = new HttpPost("https://adb-1916103060728394.14.azuredatabricks.net/api/2.1/jobs/create")

post.addHeader("Authorization", "Bearer " + "dapi541fd3d5e75bd9368a564792a99b6f09");
post.addHeader(HttpHeaders.CONTENT_TYPE, "application/json")

post.setEntity(new StringEntity(json))

val response = client.execute(post)
val entity = response.getEntity()

println(Seq(response.getStatusLine.getStatusCode(), response.getStatusLine.getReasonPhrase()))
println(IOUtils.toString(entity.getContent()))

