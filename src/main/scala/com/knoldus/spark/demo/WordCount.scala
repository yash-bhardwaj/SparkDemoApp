package com.knoldus.spark.demo

import java.util.Calendar

import org.apache.spark.streaming.twitter.TwitterUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}


/**
  * Created by knoldus on 7/10/17.
  */
object WordCount {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setMaster("local[*]")
      .setAppName("Test Spark")
      .set("spark.executor.memory", "2g")
    val sc = new SparkContext(conf)

    sc.setLogLevel("WARN")
    val ssc = new StreamingContext(sc, Seconds(5))
    System.setProperty("twitter4j.oauth.consumerKey", "zARETSDdNqGUVSJ6VhBVydkKw")
    System.setProperty("twitter4j.oauth.consumerSecret", "xf3bkFvhZfRT0PgkzNCJEhk8Xqe4LKrJxiZpTKpchqhaYy10uK")
    System.setProperty("twitter4j.oauth.accessToken", "917008140809388038-sDU6J1Ihc0F4r6wGVcjY0us4N2HXOHJ")
    System.setProperty("twitter4j.oauth.accessTokenSecret", "6wQxK1BD1Sn4v6xjuHfUNXp3j4H6SoAVcaKnhIrFi2b12")

    val stream = TwitterUtils.createStream(ssc, None)


    /*val tags = stream.flatMap { status => {
      if(status.getText.contains("#bigdata"))
        println(s"status -> ${status.getText}")
      status.getHashtagEntities.map(_.getText) }
    }
    tags.countByValue().foreachRDD { rdd =>

      val now = Calendar.getInstance.getTime
      rdd.sortBy(_._2).map{x =>
        if(x.toString().contains("#bigdata"))
        println(s"x -> ${x}")
        (x, now)}.saveAsTextFile(s"src/main/resources/twitter/tags_$now")
    }
    stream.filter { t =>
      println(s"t -> ${t}")
      val tags = t.getText.split(" ")
        .filter(_.startsWith("#")).map(_.toLowerCase)
      tags.contains("#bigdata") && tags.contains("#food")
    }*/
    /* stream.map { status => status.getText
     }.print()*/
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
      val sentimentScore: Int = 6
      hashTag + "::" + tweet + "::" + sentimentScore
    }

    finalStream.foreachRDD { rdd =>
      val now = Calendar.getInstance.getTime
      rdd.saveAsTextFile(s"src/main/resources/twitter/tweets_$now.txt")
    }
    stream.start()
    ssc.start()
    ssc.awaitTermination()
  }
}
