package com.lsx

import scala.beans.BeanProperty

/**
  * Location in the package "com.lsx" of this project "slick-basecrud"
  *
  * Created by 梁圣贤GetOUO on 2018/9/14 - 14:08
  */
trait BaseBean[ID]{val id: ID}

class IDObj(@BeanProperty var num: Int, @BeanProperty var re: String){
  def this() { this(0, "") }
}

case class OfStringIDBean(id: String, field1: Int) extends BaseBean[String]
case class OfIntIDBean(id: Int, field1: Double) extends BaseBean[Int]
case class OfObjIDBean(id: IDObj, field1: Long) extends BaseBean[IDObj]

object Beans {

}
