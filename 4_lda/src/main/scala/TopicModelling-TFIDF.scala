import scala.collection.mutable
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import org.apache.spark.mllib.clustering.LDA
import org.apache.spark.mllib.linalg.{Vector, Vectors}
import org.apache.spark.rdd.RDD
import org.apache.spark.mllib.feature.HashingTF
import org.apache.spark.mllib.feature.IDF

object TopicModel {
    def main(args: Array[String]) {
        val hdfshost = args(0)
        val hdfsroot = args(1)

        val sparkConf = new SparkConf().setAppName("TopicModel")
        val sc = new SparkContext(sparkConf)

        // Load documents from text files, 1 document per file
        //val corpus: RDD[String] = sc.wholeTextFiles("hdfs://%s:54310/%s/*".format(hdfshost, hdfsroot)).map(_._2)
        val corpus: RDD[String] = sc.wholeTextFiles("hdfs://%s:9000/%s/*".format(hdfshost, hdfsroot)).map(_._2).flatMap(_.split('\n'))
        val num_corp = corpus.count.toDouble
        println(num_corp)

        // Split each document into a sequence of terms (words)
        val tokenized: RDD[Seq[String]] =
            corpus.map(_.toLowerCase.split("\\s")).map(_.filter(_.length > 3).filter(_.forall(java.lang.Character.isLetter)))

        // Choose the vocabulary.
        //   termCounts: Sorted list of (term, termCount) pairs
        val termCounts: Array[(String, Long)] =
            tokenized.flatMap(_.map(_ -> 1L)).reduceByKey(_ + _).collect().sortBy(-_._2)

        val idfs: Map[String, Double] = tokenized.flatMap(_.map(term => (term, 1.0))).distinct.collect.toMap

        //for (elem <- termCounts) {
        //    println("%s, %d".format(elem._1, elem._2))
        //}

        //   vocabArray: Chosen vocab (removing common terms)
        //val numStopwords = 200
        //val vocabArray: Array[String] =
        //    termCounts.takeRight(termCounts.size - numStopwords).map(_._1)
        val vocabArray: Array[String] = termCounts.map(_._1)
        //   vocab: Map term -> term index
        val vocab: Map[String, Int] = vocabArray.zipWithIndex.toMap

        // Convert documents into term count vectors
        val documents: RDD[(Long, Vector)] =
            tokenized.zipWithIndex.map { case (tokens, id) =>
                val counts = new mutable.HashMap[Int, Double]()
                tokens.foreach { term =>
                    if (vocab.contains(term)) {
                        val idx = vocab(term)
                        counts(idx) = counts.getOrElse(idx, 0.0) + (1.0 / scala.math.log(num_corp / idfs(term)))
                      }
                }
            (id, Vectors.sparse(vocab.size, counts.toSeq))
            }

        // Set LDA parameters
        val numTopics = 20
        val lda = new LDA().setK(numTopics).setMaxIterations(50)

        val ldaModel = lda.run(documents)
        val avgLogLikelihood = ldaModel.logLikelihood / documents.count()

        // Print topics, showing top-weighted 10 terms for each topic.
        val topicIndices = ldaModel.describeTopics(maxTermsPerTopic = 20)
        topicIndices.foreach { case (terms, termWeights) =>
            println("TOPIC:")
            terms.zip(termWeights).foreach { case (term, weight) =>
                println(s"${vocabArray(term.toInt)}\t$weight")
            }
            println()
        }
    }
}
