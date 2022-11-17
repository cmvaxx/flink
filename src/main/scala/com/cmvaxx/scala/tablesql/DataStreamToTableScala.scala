package com.cmvaxx.scala.tablesql

/**
 *将DataSet转换为表
 */

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.table.api.bridge.scala.StreamTableEnvironment
import org.apache.flink.table.api.EnvironmentSettings

/**
 * 创建TableEnvironment对象
 */
object DataStreamToTableScala {
  def main(args: Array[String]): Unit = {

    //创建StreamTableEnvironment
    val ssEnv = StreamExecutionEnvironment.getExecutionEnvironment
    val ssSettings = EnvironmentSettings.newInstance().useBlinkPlanner().inStreamingMode().build()
    val ssTableEnv = StreamTableEnvironment.create(ssEnv,ssSettings)

    //获取DataStream
    import org.apache.flink.api.scala._
    val stream = ssEnv.fromCollection(Array((1,"jack"),(2,"tom"),(3,"mack")))

    //第一种：将DataStream转化为view视图
    import org.apache.flink.table.api._
    ssTableEnv.createTemporaryView("myTable",stream,'id,'name) //和用$效果一样，可以互换

    ssTableEnv.sqlQuery("select * from myTable where id > 1").execute().print()


    //第二种：将DataStream转化为Table对象
    val table = ssTableEnv.fromDataStream(stream,$"id",$"name")
    table.select($"id",$"name")
      .filter($"id">1)
      .execute()
      .print()

  }
}
