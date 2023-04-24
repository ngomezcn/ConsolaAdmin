package UI

import clearConsole
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Sorts
import enter
import models.MarkerR
import java.util.*

class Consultas(private val collection : MongoCollection<MarkerR>, private val scanner: Scanner) {

    fun run()
    {
        while (true)
        {
            clearConsole()
            println("== Consultas == ")
            println("1.- Cantidad de usuarios")
            println("2.- Marcadores por categoría Lift Off")
            println("3.- Marcadores por categoría Primary Stage")
            println("4.- Marcadores por categoría Secondary Stage")
            println("5.- Marcadores por usuario")
            println("6.- Volver")
            print("> ")
            when(scanner.nextLine().toInt())
            {
                1 -> { cantidadDeUsuarios(); enter() }
                2 -> { marcadoresPorCategoría("Lift Off"); enter() }
                3 -> { marcadoresPorCategoría("Primary Stage"); enter() }
                4 -> { marcadoresPorCategoría("Secondary Stage"); enter() }
                5 -> { marcadoresPorUsuario(); enter() }
                6 -> return
            }
        }
    }

    private fun marcadoresPorUsuario() {
        val allMarkers = collection.find().toList()

        val markerCountsByUser = mutableMapOf<String, Int>()

        allMarkers.forEach { marker ->
            val userId = marker.owner_id
            val count = markerCountsByUser.getOrDefault(userId, 0)
            markerCountsByUser[userId] = count + 1
        }

        markerCountsByUser.forEach { (userId, count) ->
            println("ID de usuario: $userId, Cantidad de marcadores: $count")
        }
    }

    private fun cantidadDeUsuarios()
    {
        val distinctOwners = collection.distinct("owner_id", String::class.java)
        val numOwners = distinctOwners.count()

        println(numOwners)
        println("Hay $numOwners usuarios en el sistema")
    }

    private fun marcadoresPorCategoría(categoryToSearch : String)
    {
        val filter = Filters.eq("category", categoryToSearch)
        val sort = Sorts.descending("date")

        val markers = collection.find(filter).sort(sort).toList()

        println("La categoría $categoryToSearch tiene ${markers.size} marcadores")

       /* markers.forEach { marker ->
            println("ID: ${marker._id}, Título: ${marker.title}, Descripción: ${marker.description}, Fecha: ${marker.date}, Categoría: ${marker.category}")
        }*/
    }
}