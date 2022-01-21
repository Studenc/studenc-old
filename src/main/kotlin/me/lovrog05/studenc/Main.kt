import org.jsoup.Jsoup
import org.jsoup.nodes.Document

fun main(args: Array<String>) {
	val page: Document = Jsoup.connect("https://www.studentski-servis.com/studenti/prosta-dela?kljb=&page=1&isci=1&sort=&dm1=1&skD%5B%5D=004&skD%5B%5D=A832&skD%5B%5D=A210&skD%5B%5D=A055&skD%5B%5D=A078&skD%5B%5D=A090&skD%5B%5D=A095&regija%5B%5D=ljubljana-z-okolico&regija%5B%5D=vrhnika-z-okolico&hourly_rate=4.98%3B21").get()

	println(page.body().allElements)

}