package com.knoldus.spark.demo

import com.typesafe.config.ConfigFactory

/**
  * Created by knoldus on 12/10/17.
  */
object Configurations {
  lazy val APP_NAME = config.getString("appName")
  lazy val SPARK_EXECUTOR_MEMORY = config.getString("spark.executor.memory")
  lazy val CONSUMER_KEY = config.getString("twitter4j.oauth.consumerKey")
  lazy val CONSUMER_SECRET = config.getString("twitter4j.oauth.consumerSecret")
  lazy val ACCESS_TOKEN = config.getString("twitter4j.oauth.accessToken")
  lazy val ACCESS_TOKEN_SECRET = config.getString("twitter4j.oauth.accessTokenSecret")
  lazy val RESOURCE_STORAGE = config.getString("resourcestorage")
  lazy val MASTER = config.getString("master")
  private val config = ConfigFactory.load
}
