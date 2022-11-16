package com.cmvaxx.scala.batch.transformation

import org.apache.flink.api.scala.ExecutionEnvironment

import scala.collection.mutable.ListBuffer

/**
 * MapPartition的使用：一次处理一个分区的数据
 */
object BatchMapPartitonScala {
  def main(args: Array[String]): Unit = {

    val env = ExecutionEnvironment.getExecutionEnvironment

    import org.apache.flink.api.scala._
    val text = env.fromCollection(Array("hello you","hello me"))


    //每次处理一个分区的数据
    text.mapPartition(it=>{
      //可以在此出创建数据库连接，建议把这块代码放到try-catch代码块中
      //注意：此时是每个分区获取一次数据库连接，不需要每处理一条数据就获取一次连接，性能较高

      val res = ListBuffer[String]()
      it.foreach(line=>{
        val words = line.split(" ")
        for(word<-words){
          res.append(word)
        }
      })
      res
      //关闭数据库连接
    }).print()

    //针对批处理代码，执行print后就会执行了，不需要下面的这些代码了，不然报错。还有count,collect等
//    env.execute("BatchMapPartitonScala")

  }

}
