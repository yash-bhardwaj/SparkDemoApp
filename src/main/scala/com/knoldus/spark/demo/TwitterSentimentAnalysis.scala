package com.knoldus.spark.demo

import java.util.Calendar

import com.knoldus.spark.demo.Sentiment.Sentiment
import org.apache.spark.streaming.twitter.TwitterUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

object TwitterSentimentAnalysis {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf()
      .setMaster(Configurations.MASTER)
      .setAppName(Configurations.APP_NAME)
      .set("spark.executor.memory",Configurations.SPARK_EXECUTOR_MEMORY)

    val sc = new SparkContext(conf)
    val ssc = new StreamingContext(sc, Seconds(5))

    System.setProperty("twitter4j.oauth.consumerKey", Configurations.CONSUMER_KEY)
    System.setProperty("twitter4j.oauth.consumerSecret", Configurations.CONSUMER_SECRET)
    System.setProperty("twitter4j.oauth.accessToken", Configurations.ACCESS_TOKEN)
    System.setProperty("twitter4j.oauth.accessTokenSecret", Configurations.ACCESS_TOKEN_SECRET)

    val stream = TwitterUtils.createStream(ssc, None)

    val tagsWithTweet = stream.map { status =>
      val filterTweet = status.getText.replaceAll("[^a-z A-Z]+", "")
      val tweet = status.getText
      (tweet, filterTweet)
    }.filter(_._2.nonEmpty).flatMap { case (tweet, filterTweet) =>
      val hashTags = tweet.split(" ").filter(_.startsWith("#"))
      hashTags.map { hashTag =>
        (hashTag, filterTweet)
      }
    }

    val finalStream = tagsWithTweet.map { case (hashTag, tweet) =>
      val sentimentScore: Sentiment = SentimentAnalyzer.mainSentiment(tweet)
      hashTag + "::" + tweet + "::" + sentimentScore
    }

    finalStream.foreachRDD { rdd =>
      val now = Calendar.getInstance.getTime
      rdd.saveAsTextFile(s"${Configurations.RESOURCE_STORAGE}/tweets_$now.txt")
    }

    stream.start()
    ssc.start()
    ssc.awaitTermination()
  }
}
