package com.knoldus.spark.demo

/**
  * Created by knoldus on 7/10/17.
  */
class Main {

  def apply: Main = new Main(){
    println("Welcome to Spark")
  }
}

object Main{
  def main(args: Array[String]): Unit = {
    new Main().apply
  }
}
