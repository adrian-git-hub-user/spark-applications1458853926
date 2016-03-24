package sparkalgorithms

import org.apache.spark.sql.functions.col
import org.apache.spark.Logging
import org.apache.spark.graphx._
import org.apache.spark.{ SparkConf, SparkContext }
import org.apache.spark.SparkContext._
import org.apache.spark.sql.SQLContext._

//http://stackoverflow.com/questions/33844591/prepare-data-for-multilayerperceptronclassifier-in-scala

object NeuralNetwork extends App {

  import org.apache.log4j.Logger
  import org.apache.log4j.Level

  Logger.getLogger("org").setLevel(Level.OFF)
  Logger.getLogger("akka").setLevel(Level.OFF)

  val sc = new SparkContext(new SparkConf().setMaster("local[2]")
    .setAppName("cs"))

  val sqlContext = new org.apache.spark.sql.SQLContext(sc)
  import sqlContext.implicits._

  // val seq = Seq(("0", "1", "1"), ("0", "1", "1"))
  //val p = sc.parallelize(seq);

  import org.apache.spark.sql.functions.col

  val df = sc.parallelize(Seq(

    ("1", "1", "1"),

    ("2", "1", "1"),

    ("2", "3", "3"),

    ("3", "3", "3"),

    ("0", "1", "0")))

    .toDF("label", "feature1", "feature2")

  val numeric = df
    .select(df.columns.map(c => col(c).cast("double").alias(c)): _*)

  import org.apache.spark.ml.feature.VectorAssembler

  val assembler = new VectorAssembler()
    .setInputCols(Array("feature1", "feature2"))
    .setOutputCol("features")

  val data = assembler.transform(numeric)
  
  import org.apache.spark.ml.classification.MultilayerPerceptronClassifier

  val layers = Array[Int](2, 3, 5, 4) // Note 2 neurons in the input layer
  val trainer = new MultilayerPerceptronClassifier()
    .setLayers(layers)
    .setBlockSize(128)
    .setSeed(1234L)
    .setMaxIter(100)

  val model = trainer.fit(data)
  model.transform(data).show

  val result: org.apache.spark.sql.DataFrame = model.transform(data)

  val predictionAndLabels = result.select("prediction", "label")
  println(predictionAndLabels);

  val evaluator = new org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator()
    .setMetricName("precision")
  println("Precision:" + evaluator.evaluate(predictionAndLabels))

  //http://spark.apache.org/docs/latest/sql-programming-guide.html
  //df.filter(df("age") > 21).show()

  //s.filter(s("label").equals("newlabel")).show()

  //df.filter(df("label").equals("newlabel").show()

}