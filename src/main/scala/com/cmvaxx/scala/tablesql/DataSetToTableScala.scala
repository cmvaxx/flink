package com.cmvaxx.scala.tablesql

import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.table.api.bridge.scala.BatchTableEnvironment

/**
 * 将DataSet转化为表。
 * 此时只能使用旧的执行引擎。
 * 新的Blink引擎不支持和DataSet转换
 */
object DataSetToTableScala {
  def main(args: Array[String]): Unit = {
    //创建BatchTableEnvironment
    val bbEnv = ExecutionEnvironment.getExecutionEnvironment
    val bbTableEnv = BatchTableEnvironment.create(bbEnv)

    //获取DataSet
    import org.apache.flink.api.scala._
    val set = bbEnv.fromCollection(Array((1,"jack"),(2,"tom"),(3,"mack")))

    //第一种：将DataSet转化为view视图
    import org.apache.flink.table.api._
    bbTableEnv.createTemporaryView("myTable",set, 'id, 'name)
    bbTableEnv.sqlQuery("select * from myTable where id > 1")

    //第二种：将DataSet对象转化为table对象
    val table = bbTableEnv.fromDataSet(set, 'id, 'name)
    table.select($"id",$"name")
      .filter($"id">1)
      .execute()
      .print()

  }

}
