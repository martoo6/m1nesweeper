object Mongo {
  import org.mongodb.scala._
  val mongoClient: MongoClient = MongoClient(sys.env.getOrElse("MONGODB", "mongodb://localhost"))
  val database: MongoDatabase = mongoClient.getDatabase("m1nesweeper")
}
