name := "SparkDemoApp"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies += "org.apache.spark" % "spark-core_2.11" % "1.5.2"
libraryDependencies += "org.apache.spark" % "spark-streaming_2.11" % "1.5.2"
libraryDependencies += "org.apache.spark" % "spark-streaming-twitter_2.11" % "1.5.2"
libraryDependencies += "edu.stanford.nlp" % "stanford-corenlp" % "3.5.1"
libraryDependencies += "edu.stanford.nlp" % "stanford-corenlp" % "3.5.1" classifier "models"
//addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.8.6")
libraryDependencies += "com.eed3si9n" % "sbt-assembly_2.8.1" % "sbt0.10.1_0.5"
