package me.lovrog05.studenc

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

class StudencScraper(url: String) {
	private val document: Document = Jsoup.connect(url).get()
	private val prostaDela: Element? = document.getElementById("prostaDela")

	private var results: Elements? = prostaDela?.getElementById("results")?.children()

	val jobsCount: String = results?.get(0)?.text()?: "PO DOGOVORU"

	private fun getJobData(article: Element): HashMap<String, String> {
		val jobId: String = article.attr("data-jobid")
		val jobTitle: String = article.select("h3").text()
		val jobPay: String = article.select("strong").text()
		val description: String = article.select("p").text()

		val data: HashMap<String, String> = HashMap()
		data["jobId"] = jobId
		data["title"] = jobTitle
		data["pay"] = jobPay.split(" ")[0]
		data["description"] = description

		return data
	}

	fun getJobs(): ArrayList<HashMap<String, String>> {
		val jobs: ArrayList<HashMap<String, String>> = ArrayList()
		results?.removeFirst()
		results?.forEach {
			jobs.add(getJobData(it))
		}

		return jobs
	}
}