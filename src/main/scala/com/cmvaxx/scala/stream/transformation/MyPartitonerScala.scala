package com.cmvaxx.scala.stream.transformation

import org.apache.flink.api.common.functions.Partitioner

/**
 * 自定义分区规则：按照数字的奇偶性进行分区
 */
class MyPartitonerScala extends Partitioner[Int]{

  override def partition(k: Int, i: Int): Int = {
    println("分区总数:"+i)
    if(k%2==0) {//偶数分到0号分区
      0
    }else{//奇数分到1号分区
      1
    }
  }
}
