package UI

import clearConsole
import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.ServerApi
import com.mongodb.ServerApiVersion
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import models.MarkerR
import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.pojo.PojoCodecProvider
import java.util.*

class AdminConsole() {

    private val scanner = Scanner(System.`in`)
    private lateinit var collection : MongoCollection<MarkerR>
    private val user = "admin"
    private val password = "q5u02rLYRnS5yfPb"

    fun start()
    {
        login()
        mainMenu()
    }

    private fun mainMenu()
    {
        val metricas = Consultas(collection, scanner)
        val crud = CRUD(collection, scanner)

        while (true)
        {
            clearConsole()
            println("== Menu principal == ")
            println("1.- Consultas")
            println("2.- CRUD")
            println("3.- Logout")
            print("> ")
            when(scanner.nextLine().toInt())
            {
                1 -> metricas.run()
                2 -> crud.run()
                3 -> return
            }
            println("Bye")
        }
    }

    private fun login()
    {
        val pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build()
        val pojoCodecRegistry = CodecRegistries.fromRegistries(
            MongoClientSettings.getDefaultCodecRegistry(),
            CodecRegistries.fromProviders(pojoCodecProvider)
        )
        val connectionString = ConnectionString("mongodb+srv://$user:$password@slcluster.fvkcvnl.mongodb.net/?retryWrites=true&w=majority")

        val settings: MongoClientSettings = MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .serverApi(ServerApi.builder().version(ServerApiVersion.V1).build()).build()

        val mongoClient: MongoClient = MongoClients.create(settings)
        val database: MongoDatabase = mongoClient.getDatabase("todo").withCodecRegistry(pojoCodecRegistry);
        collection = database.getCollection("MarkerR", MarkerR::class.java)


        /*val db: MongoDatabase = mongoClient.getDatabase("system").withCodecRegistry(pojoCodecRegistry);
        val users = db.getCollection("users", MarkerR::class.java)
        val allMarkers = users.find().toList()
        println(allMarkers)
        scanner.nextLine()*/
    }
}

