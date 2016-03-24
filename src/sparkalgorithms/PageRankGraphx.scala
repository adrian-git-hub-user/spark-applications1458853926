package sparkalgorithms

import org.apache.spark.Logging
import org.apache.spark.graphx._
import org.apache.spark.{ SparkConf, SparkContext }
import org.apache.spark.SparkContext._

object PageRankGraphx extends App {

  val sc = new SparkConf().setMaster("local[2]")
             .setAppName("CountingSheep")
             
  val ctx = new SparkContext(sc)

  val graph = GraphLoader.edgeListFile(ctx, "C:\\Users\\adrian.ronayne\\Google Drive\\workspaces\\data-science\\everything-and-anything\\followers.txt")
  // Run PageRank
  val ranks = graph.pageRank(0.0001).vertices
  // Join the ranks with the usernames
  val users = ctx.textFile("C:\\Users\\adrian.ronayne\\Google Drive\\workspaces\\data-science\\everything-and-anything\\users.txt").map { line =>
    val fields = line.split(",")
    (fields(0).toLong, fields(1))
  }
  val ranksByUsername = users.join(ranks).map {
    case (id, (username, rank)) => (username, rank)
  }
  // Print the result
  println(ranksByUsername.collect().mkString("\n"))

}