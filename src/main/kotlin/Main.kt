import com.google.gson.Gson
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import me.lovrog05.studenc.Studenc


fun main(args: Array<String>) {
	val url: String = "https://www.studentski-servis.com/studenti/prosta-dela/"
	val studenc: Studenc = Studenc(url)
	studenc.requestAndStoreJobs()
	var lastUpdated: Long = System.currentTimeMillis()
	embeddedServer(Netty, port=6969) {
		install(ContentNegotiation)
		routing {
			route("/") {
				get {
					call.respondRedirect("http://127.0.0.1:5500/index.html")
				}
			}
			route("/keywordhits") {
				get {
					val response = call.request.queryParameters["word"]?.let { it1 -> studenc.getKeywordHits(it1) }
					val gson = Gson()
					call.respondText(gson.toJson(response).toString(), ContentType.Application.Json, status=HttpStatusCode.OK)
				}
			}
			route("/jobs") {
				get {
					val response: HashMap<String, ArrayList<HashMap<String, String>>> = HashMap()
					response["jobs"] = studenc.getStoredJobs()
					val gson = Gson()
					call.respondText(gson.toJson(response).toString(), ContentType.Application.Json, status=HttpStatusCode.OK)
				}
				get("{id}") {
					val response = call.parameters["id"]?.let { it1 -> studenc.getJobById(it1) }
					if (response?.isEmpty() == false) {
						val gson = Gson()
						call.respondText(gson.toJson(response).toString(), ContentType.Application.Json)
					} else {
						call.respondText("404", status=HttpStatusCode.NotFound)
					}
				}
			}
			route("/jobstats") {
				get {
					val response = studenc.getJobStats()
					val gson = Gson()
					call.respondText(gson.toJson(response).toString(), ContentType.Application.Json, status=HttpStatusCode.OK)
				}
			}
			route("/requestupdate") {
				post {
					if ((System.currentTimeMillis() - lastUpdated)/1000 >= 86400) {
						studenc.requestAndStoreJobs()
						call.respondText("UPDATE APPROVED", status=HttpStatusCode.OK)
					} else {
						call.respondText("UPDATE DENIED - TOO EARLY", status=HttpStatusCode.OK) //too early
					}
				}
			}

		}
	}.start(wait = true)
}