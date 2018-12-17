import models._
import org.bson.json.{JsonMode, JsonWriterSettings}
import org.bson.types.ObjectId
import org.mongodb.scala.bson.collection.immutable.Document
import spray.json._
import org.mongodb.scala.model.Filters._

object GameStorage extends JsonSupport with ExecutionContextProvider {
  private val games = Mongo.database.getCollection("games")
  private val jsonWriterConfig = JsonWriterSettings.builder.outputMode(JsonMode.RELAXED).build()

  def getAllGames = {
    games.find.toFuture().map(x => x.map(y => y.toJson(jsonWriterConfig).parseJson.convertTo[Game]))
  }

  def getGame(id: String) = {
    games.find(equal("id", id))
      .toFuture()
      .map(x => x.headOption.map(_.toJson(jsonWriterConfig).parseJson.convertTo[Game]))
  }

  def updateGame(game: Game) = {
    val doc = Document(game.toJson.compactPrint) + ("_id" -> new ObjectId(game.id))
    games.replaceOne(equal("id", game.id), doc).toFuture()
  }

  def createGame(game: Game) = {
    //TODO: Uggly implementation with repeated _id and id
    val doc = Document(game.toJson.compactPrint) + ("_id" -> new ObjectId(game.id))
    games.insertOne(doc).toFuture()
  }
}
