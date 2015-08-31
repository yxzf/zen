/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.cloudml.zen.ml.clustering

import java.util.Random

import com.github.cloudml.zen.ml.partitioner._
import com.github.cloudml.zen.ml.util.{FTree, AliasTable, XORShiftRandom}
import breeze.linalg.{SparseVector => BSV, DenseVector => BDV}
import org.apache.spark.SparkConf
import org.apache.spark.graphx._


object LDADefines {
  type DocId = VertexId
  type WordId = VertexId
  type Count = Int
  type ED = Array[Int]
  type VD = BSV[Count]
  type BOW = (Long, BSV[Count])

  val sv_formatVersionV1_0 = "1.0"
  val sv_classNameV1_0 = "com.github.cloudml.zen.ml.clustering.DistributedLDAModel"
  val cs_sampleRate = "zen.lda.sampleRate"
  val cs_LDAAlgorithm = "zen.lda.LDAAlgorithm"
  val cs_accelMethod = "zen.lda.accelMethod"
  val cs_storageLevel = "zen.lda.storageLevel"
  val cs_partStrategy = "zen.lda.partStrategy"
  val cs_chkptInterval = "zen.lda.chkptInterval"
  val cs_calcPerplexity = "zen.lda.calcPerplexity"
  val cs_saveInterval = "zen.lda.saveInterval"
  val cs_inputPath = "zen.lda.inputPath"
  val cs_outputpath = "zen.lda.outputPath"
  val cs_saveAsSolid = "zen.lda.saveAsSolid"
  val cs_numThreads = "zen.lda.numThreads"

  def uniformDistSampler(gen: Random,
    tokens: Array[Int],
    topics: Array[Int],
    numTopics: Int): BSV[Count] = {
    val docTopicCounter = BSV.zeros[Count](numTopics)
    for (i <- tokens.indices) {
      val topic = gen.nextInt(numTopics)
      topics(i) = topic
      docTopicCounter(topic) += 1
    }
    docTopicCounter
  }

  def registerKryoClasses(conf: SparkConf): Unit = {
    conf.registerKryoClasses(Array(classOf[VD], classOf[ED],
      classOf[BSV[Double]], classOf[BDV[Count]], classOf[BDV[Double]],
      classOf[BOW], classOf[Random], classOf[XORShiftRandom],
      classOf[LDA], classOf[LocalLDAModel], classOf[DistributedLDAModel],
      classOf[LDAAlgorithm], classOf[FastLDA], classOf[LightLDA],
      classOf[DBHPartitioner], classOf[VSDLPPartitioner], classOf[BBRPartitioner],
      classOf[AliasTable[Double]], classOf[AliasTable[Object]], classOf[FTree[Double]], classOf[FTree[Object]]
    ))
  }
}
