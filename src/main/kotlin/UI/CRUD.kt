package UI

import clearConsole
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Accumulators
import com.mongodb.client.model.Aggregates
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Sorts
import enter
import models.MarkerR
import org.bson.Document
import org.bson.conversions.Bson
import org.litote.kmongo.MongoOperator
import java.lang.Math.sqrt
import java.util.*

class CRUD(private val collection : MongoCollection<MarkerR>, private val scanner: Scanner) {

    fun run()
    {
        while (true)
        {
            clearConsole()
            println("== CRUD == ")
            println("1.- Listar marcadores")
            println("2.- Listar usuarios")
            println("==================")
            println("3.- Crear marcador")
            println("4.- Edit marcador")
            println("5.- Eliminar marcador")
            println("6.- Volver")
            print("> ")
            when(scanner.nextLine().toInt())
            {
                1 -> { listarMarcadores(); enter() }
                2 -> { listarUsuarios(); enter() }
                3 -> { crearMarcador(); enter() }
                4 -> { editMarker(); enter() }
                5 -> { eliminarMarker(); enter() }
                6 -> return
            }
        }
    }

    private fun eliminarMarker() {
        print("[ID Marcador] > ")
        val markerId = scanner.nextLine()
        val filter = Document("_id", org.bson.types.ObjectId(markerId))

        val result = collection.deleteOne(filter)

        if (result.deletedCount > 0) {
            println("El marcador con el ID $markerId ha sido eliminado.")
        } else {
            println("No se encontró ningún marcador con el ID $markerId.")
        }
    }

    private fun editMarker() {

        print("[ID Marcador] > ")
        val markerId = scanner.nextLine()
        val filter = Document("_id", org.bson.types.ObjectId(markerId))
        val existingMarker = collection.find(filter).first()

        if (existingMarker == null) {
            println("No se encontró ningún marcador con el ID $markerId.")
            return
        } else {
            print("[Latitude] > ")
            val latitude = scanner.nextLine().toDouble()

            print("[Longitude] > ")
            val longitude = scanner.nextLine().toDouble()

            print("[Title] > ")
            val title = scanner.nextLine()

            print("[Description] > ")
            val description = scanner.nextLine()

            val date = Calendar.getInstance().time.toString()

            print("[Category] > ")
            val category = scanner.nextLine()

            print("[Owner ID] > ")
            val owner_id = scanner.nextLine()

            val update = Document("\$set", Document()
                .append("latitude", latitude)
                .append("longitude", longitude)
                .append("title", title)
                .append("description", description)
                .append("date", date)
                .append("category", category)
                .append("owner_id", owner_id)
            )

            val result = collection.updateOne(filter, update)

            if (result.modifiedCount > 0) {
                println("El marcador con el ID $markerId ha sido actualizado.")
            } else {
                println("El marcador no ha sido actualizado debido a un error interno.")
            }
        }
    }

    private fun listarUsuarios() {
        val marcadores: List<MarkerR> = collection.find().toList()

        val userIds = marcadores.map { it.owner_id }.distinct()

        userIds.forEach { userId ->
            println("ID de usuario: $userId")
        }
    }

    private fun listarMarcadores() {
        val allMarkers = collection.find().toList()

        allMarkers.forEach { marker ->
            println("ID: ${marker._id}, Título: ${marker.title}, Descripción: ${marker.description}, Latitud: ${marker.latitude}, Longitud: ${marker.longitude}, Fecha: ${marker.date}, Categoría: ${marker.category}, ID del propietario: ${marker.owner_id}")
        }
    }

    private fun crearMarcador() {
        val newMarker = MarkerR()

        print("[Latitude] > ")
        newMarker.latitude = scanner.nextLine().toDouble()

        print("[Longitude] > ")
        newMarker.longitude = scanner.nextLine().toDouble()

        print("[Title] > ")
        newMarker.title = scanner.nextLine()

        print("[Description] > ")
        newMarker.description = scanner.nextLine()

        newMarker.date = Calendar.getInstance().time.toString()

        print("[Category] > ")
        newMarker.category = scanner.nextLine()

        print("[Owner ID] > ")
        newMarker.owner_id = scanner.nextLine()

        collection.insertOne(newMarker)

        println("Se ha creado un nuevo marcador con el ID: ${newMarker._id}")
    }
}