package com.lsx

import com.util.JsonUtil
import slick.dbio.Effect
import slick.driver.MySQLDriver.api._
import slick.lifted.TableQuery
import slick.sql.FixedSqlAction

/**
  * Location in the package "com.lsx" of this project "slick-basecrud"
  *
  * Created by 梁圣贤GetOUO on 2018/9/14 - 14:09
  */

abstract class BaseTableSchema[BID, TID, B <: BaseBean[BID]](tag: Tag, name: String) extends Table[B](tag, name) {
  def id: Rep[BID]

  def toTID(bID: BID): TID
  def fromTID(tID: TID): BID
  implicit val customIDColumn: BaseColumnType[BID] = MappedColumnType.base[BID,TID](toTID, fromTID)
}

class OfStringIDTableSchema(tag: Tag) extends BaseTableSchema[String, String, OfStringIDBean](tag, "TABLE_OF_STRING") {

  override def id = column[String]("S_ID", O.PrimaryKey)
  override def toTID(id: String) = id
  override def fromTID(id: String) = id

  def field1 = column[Int]("FIELD")

  def * = (id, field1)<>(OfStringIDBean.tupled, OfStringIDBean.unapply)
}

class OfIntIDTableSchema(tag: Tag) extends BaseTableSchema[Int, Int, OfIntIDBean](tag, "TABLE_OF_INT") {
  override def id = column[Int]("I_ID", O.PrimaryKey)
  override def toTID(id: Int) = id
  override def fromTID(id: Int) = id

  def field1 = column[Double]("FIELD")

  def * = (id, field1) <> (OfIntIDBean.tupled, OfIntIDBean.unapply)
}

class OfObjIDTableSchema(tag: Tag) extends BaseTableSchema[IDObj, String, OfObjIDBean](tag, "TABLE_OF_INT") {
  override def id = column[IDObj]("O_ID", O.PrimaryKey)
  override def toTID(id: IDObj) = JsonUtil.getJson(id)
  override def fromTID(id: String) = JsonUtil.getObject(id, classOf[IDObj])

  def field1 = column[Long]("FIELD")

  def * = (id, field1) <> (OfObjIDBean.tupled, OfObjIDBean.unapply)
}

class Tables[TID, BID, B <: BaseBean[BID], TBS <: BaseTableSchema[BID, TID, B]]{}

object Tables {

  val db = Database.forConfig("mysql")

  def tableOfStringID = TableQuery[OfStringIDTableSchema]
  val tableOfIntID = TableQuery[OfIntIDTableSchema]
  val tableOfObjID = TableQuery[OfObjIDTableSchema]

  def tables[B <: BaseBean[_], TBS <: BaseTableSchema[_, _, B]]() = {
    Map[Class[B], TableQuery[TBS]](
      OfStringIDBean.getClass -> tableOfStringID,
      OfIntIDBean.getClass -> tableOfIntID,
      OfObjIDBean.getClass -> tableOfObjID
    )
  }
//
//  def tables[TID, BID, B <: BaseBean[BID], TBS <: BaseTableSchema[BID, TID, B]]() = {
//    Map[Class[B], TableQuery[TBS]](
//      classOf[OfStringIDBean] -> tableOfStringID,
//      OfIntIDBean.getClass -> tableOfIntID,
//      OfObjIDBean.getClass -> tableOfObjID
//    )
//  }

  def baseUpdate1[ID, B <: BaseBean[ID]](anyBean: B): Unit = {
    val tableByBeanOption = tables.get(anyBean.getClass) //???
    val updateAction = tableByBeanOption.headOption.get.filter(_.id === anyBean.id).update(anyBean)
    db.run(updateAction)
  }

  def baseUpdate2[ID, B <: BaseBean[ID]](table: TableQuery[BaseTableSchema[ID, _, B]], bean: B): Unit = {
    table.filter(_.id === bean.id).update(bean)
  }
}
