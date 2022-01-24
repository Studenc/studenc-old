package me.lovrog05.studenc

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

class StudencScraper(private val url: String) {
	private val document: Document = Jsoup.connect(url).get()
	private val pageCount: Int = document.getElementsByClass("page-link").secondToLast()?.text()?.toInt() ?: 1


	private fun getJobData(article: Element): HashMap<String, String> {
		val jobId: String = article.attr("data-jobid")
		val jobTitle: String = article.select("h3").text()
		val jobPay: String = article.select("strong").text()
		val description: String = article.select("p").text()
		var trajanje: String = ""
		var delovnik: String = ""
		var stprostih: String = ""
		val attr: Elements = article.getElementsByClass("job-attributes")
		/*println(attr)
		println("*******************************************************")*/
		if (attr.isNotEmpty()) {
			stprostih = attr[1].select("strong")[0].text()
			if (stprostih.toIntOrNull() != null) {
				trajanje = article.getElementsByClass("job-attributes")[1].select("strong")[1].text()
				delovnik = article.getElementsByClass("job-attributes")[1].select("strong")[2].text()
			} else {
				stprostih = "1"
				trajanje = article.getElementsByClass("job-attributes")[1].select("strong")[0].text()
				delovnik = article.getElementsByClass("job-attributes")[1].select("strong")[1].text()
			}
		}



		val data: HashMap<String, String> = HashMap()
		data["jobId"] = jobId
		data["title"] = jobTitle
		data["pay"] = jobPay.split(" ")[0]
		data["description"] = description
		data["workingDay"] = delovnik
		data["duration"] = trajanje
		data["spots"] = stprostih

		return data
	}

	fun getJobs(): ArrayList<HashMap<String, String>> {
		val jobs: ArrayList<HashMap<String, String>> = ArrayList()
		for (pagenum in 1..pageCount) {
			println("page: $pagenum of $pageCount")
			val doc: Document = Jsoup.connect("$url?page=$pagenum").get()
			val prostaDela: Element? = doc.getElementById("prostaDela")
			var results: Elements? = prostaDela?.getElementById("results")?.children()
			results?.removeFirst()
			results?.forEach {
				jobs.add(getJobData(it))
			}
		}

		return jobs
	}
}

private fun <E> List<E>.secondToLast(): E {
	if (size < 2)
		throw NoSuchElementException("List has less than two elements")
	return this[size - 2]
}
