package com.cmvaxx.scala.batch.transformation

/**
 * first-n:获得集合的前n个元素
 */

import org.apache.flink.api.common.operators.Order
import org.apache.flink.api.scala.ExecutionEnvironment

import scala.collection.mutable.ListBuffer

object BatchFirstNScala {
  def main(args: Array[String]): Unit = {
    val env = ExecutionEnvironment.getExecutionEnvironment

    val data = ListBuffer[Tuple2[Int,String]]()
    data.append((2,"zs"))
    data.append((4,"ls"))
    data.append((3,"ww"))
    data.append((1,"aw"))
    data.append((1,"xw"))
    data.append((1,"mw"))
    import org.apache.flink.api.scala._
    val text = env.fromCollection(data)

    //获取前三条数据，按照数据插入的顺序
    text.first(3).print()
    println("=========================")

    //根据元素第一列进行分组，获取分组的前两个元素
    text.groupBy(0).first(2).print()

    println("==========================")
    //根据第一列分组，再根据第二列排序【倒序】，获取每组的前2个元素
    //分组排序去TopN
    val res = text.groupBy(0).sortGroup(1,Order.DESCENDING).first(2).print()


  }

}
