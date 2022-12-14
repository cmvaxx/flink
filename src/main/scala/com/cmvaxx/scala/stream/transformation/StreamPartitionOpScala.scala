package com.cmvaxx.scala.stream.transformation

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.api.scala._
/**
 * 分区规则的使用
 */
object StreamPartitionOpScala {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    //注意：在这里建议将这个隐式转换代码放到类上面
    //因为默认它只在main函数生效，针对下面提取的shuffleOp是无效的，否则也需要在shuffleOp添加这行代码
    //import org.apache.flink.api.scala._


    //默认情况下Flink任务中的算子并行度会读取当前机器的CPU个数，并行度默认为cpu个数。一个并行度对应一个分区
    //fromCollection的并行度默认为1
    val text = env.fromCollection(Array(1,2,3,4,5,6,7,8,9,10))

    //使用shuffle分区规则
    //shuffleOp(text)

    //使用rebalance分区规则
    //rebalanceOp(text)


    //使用rescale分区规则
    // rescaleOp(text)


    //使用broadcast分区规则
    //broadcast(text)

    //自定义分区规则：根据数据的奇偶性进行分区
    //注意：此时虽然print算子的并行度是4，但是自定义的分区规则只会把数据分发给2个并行度，所以有两个是不干活的
    custormPartitionOp(text)

    env.execute("StreamPartitionOpScala")
  }

  private def custormPartitionOp(text: DataStream[Int]) = {
    text.map(num => num)
      .setParallelism(2) //设置map算子的并行度为2
      .partitionCustom(new MyPartitonerScala,num=>num) //建议使用keySelector
      .print()
      .setParallelism(4) //设置print算子的并行度为4
  }

  private def rebalanceOp(text: DataStream[Int]) = {
    text.map(num => num)
      .setParallelism(2) //设置map算子的并行度为2
      .rebalance
      .print()
      .setParallelism(4) //设置print算子的并行度为4
  }

  private def shuffleOp(text: DataStream[Int]) = {
    //由于fromCollection已经设置了并行度为1，所以需要再接一个算子才能修改并行度，在这使用map算子
    text.map(num => num)
      .setParallelism(2) //设置map算子的并行度为2
      .rebalance
      .print()
      .setParallelism(4) //设置print算子的并行度为4
  }

  private def rescaleOp(text: DataStream[Int]) = {
    //由于fromCollection已经设置了并行度为1，所以需要再接一个算子才能修改并行度，在这使用map算子
    text.map(num => num)
      .setParallelism(2) //设置map算子的并行度为2
      .rescale
      .print()
      .setParallelism(4) //设置print算子的并行度为4
  }

  private def broadcast(text: DataStream[Int]) = {
    //由于fromCollection已经设置了并行度为1，所以需要再接一个算子才能修改并行度，在这使用map算子
    text.map(num => num)
      .setParallelism(2) //设置map算子的并行度为2
      .broadcast
      .print()
      .setParallelism(4) //设置print算子的并行度为4
  }
}
