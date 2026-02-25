package org.example

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.Scanner

@Serializable
data class Juego(
    val id: Int,
    @SerialName("title") val titulo: String,


    @SerialName("short_description")
    val descripcion: String,

    @SerialName("genre") val genero: String,
    @SerialName("platform") val plataforma: String,
    @SerialName("publisher") val editor: String,

    @SerialName("release_date") val fechaLanzamiento: String
)

class ServicioJuegos {
    private val client = HttpClient.newHttpClient()


    private val json = Json { ignoreUnknownKeys = true }

    fun descargarJuegos(): List<Juego> {
        println("Descargando catalogo de juegos...")

        try {
            val request = HttpRequest.newBuilder()
                .uri(URI.create("https://www.freetogame.com/api/games"))
                .GET()
                .build()

            val response = client.send(request, HttpResponse.BodyHandlers.ofString())

            if (response.statusCode() == 200) {
                return json.decodeFromString<List<Juego>>(response.body())
            } else {
                println("Error en la peticion. Codigo: ${response.statusCode()}")
                return emptyList()
            }
        } catch (e: Exception) {
            println("Error de conexion: ${e.message}")
            return emptyList()
        }
    }
}

fun buscarPorPalabraClave(lista: List<Juego>, scanner: Scanner) {
    print("Introduce palabra clave: ")
    val busqueda = scanner.nextLine().trim().lowercase()

    val resultados = lista.filter { juego ->
        juego.titulo.lowercase().contains(busqueda) ||
                juego.descripcion.lowercase().contains(busqueda)
    }

    if (resultados.isEmpty()) {
        println("No hay coincidencias.")
    } else {
        println("\nResultados encontrados (${resultados.size}):")

        resultados.take(5).forEach { juego ->
            println("- ${juego.titulo} [${juego.genero}]")
            println("  Desc: ${juego.descripcion}")
            println("--------------------------------")
        }

        if (resultados.size > 5) {
            println("... y ${resultados.size - 5} mas.")
        }
    }
}

fun menuGeneros(lista: List<Juego>) {

    val conteo = lista.groupingBy { it.genero }.eachCount()

    println("\n--- JUEGOS POR GENERO ---")
    conteo.entries.sortedByDescending { it.value }.forEach { (genero, cantidad) ->
        println("$genero: $cantidad")
    }
}

fun verNovedades(lista: List<Juego>) {
    println("\n--- ULTIMOS LANZAMIENTOS ---")

    lista.sortedByDescending { it.fechaLanzamiento }
        .take(5)
        .forEach { juego ->
            println("[${juego.fechaLanzamiento}] ${juego.titulo}")
        }
}

fun main() {
    val servicio = ServicioJuegos()
    val scanner = Scanner(System.`in`)

    val listaDeJuegos = servicio.descargarJuegos()

    if (listaDeJuegos.isEmpty()) {
        println("No se han podido cargar los datos.")
        return
    }

    println("Carga correcta. Total juegos: ${listaDeJuegos.size}")

    var seguir = true
    while (seguir) {
        println("\n--- MENU PRINCIPAL ---")
        println("1. Buscar juego (Titulo o descripcion)")
        println("2. Ver listado por Generos")
        println("3. Ver ultimos lanzamientos")
        println("4. Salir")
        print("Selecciona opcion: ")

        when (scanner.nextLine().trim()) {
            "1" -> buscarPorPalabraClave(listaDeJuegos, scanner)
            "2" -> menuGeneros(listaDeJuegos)
            "3" -> verNovedades(listaDeJuegos)
            "4" -> {
                println("Saliendo...")
                seguir = false
            }
            else -> println("Opcion no valida.")
        }
    }
}

