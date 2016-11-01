package com.sksamuel.elastic4s2.get

import com.sksamuel.elastic4s2.Executable
import org.elasticsearch.action.get._
import org.elasticsearch.client.Client

import scala.concurrent.Future

trait MultiGetApi extends GetDsl {

  def multiget(gets: Iterable[GetDefinition]): MultiGetDefinition = MultiGetDefinition(gets.toSeq)
  def multiget(gets: GetDefinition*): MultiGetDefinition = MultiGetDefinition(gets)

  implicit object MultiGetDefinitionExecutable
    extends Executable[MultiGetDefinition, MultiGetResponse, MultiGetResult] {
    override def apply(c: Client, t: MultiGetDefinition): Future[MultiGetResult] = {
      injectFutureAndMap(c.multiGet(t.build, _))(MultiGetResult.apply)
    }
  }
}

case class MultiGetResult(original: MultiGetResponse) {

  import scala.collection.JavaConverters._

  @deprecated("use .responses for a scala friendly Seq, or use .original to access the java result", "2.0")
  def getResponses = original.getResponses

  def responses: Seq[MultiGetItemResult] = original.iterator.asScala.map(MultiGetItemResult.apply).toList
}

case class MultiGetItemResult(original: MultiGetItemResponse) {

  @deprecated("use failure for a scala friendly Option, or use .original to access the java result", "2.0")
  def getFailure = original.getFailure

  @deprecated("use response for a scala friendly Option, or use .original to access the java result", "2.0")
  def getResponse = original.getResponse

  def getId = original.getId
  def getIndex = original.getIndex

  def getType = original.getType
  def isFailed = original.isFailed

  def failure: Option[MultiGetResponse.Failure] = Option(original.getFailure)
  def id = original.getId
  def index = original.getIndex
  def response: Option[GetResponse] = Option(original.getResponse)
  def `type`: String = original.getType
  def failed: Boolean = original.isFailed
}