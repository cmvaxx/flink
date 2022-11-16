package com.cmvaxx.scala.stream.transformation

import org.apache.flink.streaming.api.functions.ProcessFunction
import org.apache.flink.streaming.api.scala.{OutputTag, StreamExecutionEnvironment}
import org.apache.flink.util.Collector

object StreamSideOutputScala {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    import org.apache.flink.api.scala._
    val text = env.fromCollection(Array(1,2,3,4,5,6,7,8,9,10))

    //按照奇偶性对数据进行分流
    //首先定义两个sideoutput来准备保存切分出来的数据
    val outputTag1 = new OutputTag[Int]("even") //保存偶数
    val outputTag2 = new OutputTag[Int]("odd")  //保存奇数


    //注意，process为Flink的低级api
    val outputStream = text.process(new ProcessFunction[Int,Int] {
      override def processElement(value: Int, context: ProcessFunction[Int, Int]#Context, collector: Collector[Int]): Unit = {
        if(value%2==0) {
          context.output(outputTag1,value)
        }else{
          context.output(outputTag2,value)
        }
      }
    })

    //获取偶数数据流
    val eventStream = outputStream.getSideOutput(outputTag1)
    //获取奇数数据流
    val oddStream = outputStream.getSideOutput(outputTag2)

//    eventStream.print().setParallelism(1)

    //对evenStream进行二次切分
    val outputTag11 = new OutputTag[Int]("low") //保存小于等于5的数字
    val outputTag12 = new OutputTag[Int]("hight")  //保存大于5的数字
    val subOutputStream = eventStream.process(new ProcessFunction[Int,Int] {
      override def processElement(i: Int, context: ProcessFunction[Int, Int]#Context, collector: Collector[Int]): Unit = {
        if (i<=5){
          context.output(outputTag11,i)
        }else{
          context.output(outputTag12,i)
        }
      }
    })

    //获取小于等于5的数据流
    val lowStream =subOutputStream.getSideOutput(outputTag11)
    //获取大于5数据流
    val hightStream = subOutputStream.getSideOutput(outputTag12)

    lowStream.print().setParallelism(1)



    env.execute("StreamSideOutputScala")
  }

}
