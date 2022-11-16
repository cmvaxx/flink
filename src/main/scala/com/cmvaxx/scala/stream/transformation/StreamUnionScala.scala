package com.cmvaxx.scala.stream.transformation

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment

/**
 * 合并多个流，多个流的数据类型必须一致
 * 应用场景：多种数据源的数据类型一致，数据处理规则也一致
 */
object StreamUnionScala {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    import org.apache.flink.api.scala._
    //第一份数据流
    val text1 = env.fromCollection(Array(1,2,3,4,5,6))
    //第二份数据流
    val text2 = env.fromCollection(Array(7, 8, 9, 10))

    //合并流
    val unionStream = text1.union(text2)

    //打印流中的数据
    unionStream.print().setParallelism(1)

    env.execute("StreamUnionScala")
  }

}
