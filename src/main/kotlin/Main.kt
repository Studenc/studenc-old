import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import me.lovrog05.studenc.Studenc


fun main(args: Array<String>) {
	val url: String = "https://www.studentski-servis.com/studenti/prosta-dela?kljb=&page=1&isci=1&sort=&dm1=1&skD%5B%5D=004&skD%5B%5D=A832&skD%5B%5D=A210&skD%5B%5D=A055&skD%5B%5D=A078&skD%5B%5D=A090&skD%5B%5D=A095&regija%5B%5D=ljubljana-z-okolico&regija%5B%5D=vrhnika-z-okolico&hourly_rate=4.98%3B21"
	val dfk: ArrayList<String> = ArrayList()
	dfk.add("PHP")
	dfk.add("JavaScript")
	dfk.add("Python")
	dfk.add("Java")
	dfk.add("Kotlin")

	val studenc: Studenc = Studenc(url, dfk)
	studenc.requestAndStoreJobs()
	embeddedServer(Netty, port=6969) {
		routing {
			route("/") {
				get() {
					call.respondText("Welcome to StudencProgrammerAPI!")
				}
			}
			route("/keywordhits") {
				get {
					call.respondText { "ERROR: no word" }
				}
				get("{word}") {
					val response = call.parameters["word"]?.let { it1 -> studenc.getKeywordHits(it1) }
				}
			}

		}
	}.start(wait = true)
}