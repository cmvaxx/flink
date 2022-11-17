package com.cmvaxx.scala.tablesql

import org.apache.flink.table.api.{EnvironmentSettings, FieldExpression, TableEnvironment, WithOperations}

/**
 * TableAPI和 SQL的使用
 */
object TableAPIAndSQLOpScala {

  def main(args: Array[String]): Unit = {
    //获取TableEnvironment
    val sSettings = EnvironmentSettings.newInstance().useBlinkPlanner().inStreamingMode().build()
    val sTableEnv = TableEnvironment.create(sSettings)

    //创建输入表（创建数据源，获取数据）
    sTableEnv.executeSql(""+
    "create table myTable(\n"+
    "id int,\n"+
    "name string\n"+
    ") with (\n"+
    "'connector.type'='filesystem',\n"+  //书写格式去官网看文档，不同版本的flink有的不一样
    "'connector.path' = 'D:\\data\\source',\n"+
    "'format.type' = 'csv'\n"+
    ")")

    //使用TableAPI实现数据查询和过滤等操作
    import org.apache.flink.table.api._
    val result = sTableEnv.from("myTable")
      .select($"id",$"name")
      .filter($"id"> 1)

    //输出结果到控制台
    result.execute().print()
  }

}
