package me.lovrog05.studenc


fun main(args: Array<String>) {
	val url: String = "https://www.studentski-servis.com/studenti/prosta-dela?kljb=&page=1&isci=1&sort=&dm1=1&skD%5B%5D=004&skD%5B%5D=A832&skD%5B%5D=A210&skD%5B%5D=A055&skD%5B%5D=A078&skD%5B%5D=A090&skD%5B%5D=A095&regija%5B%5D=ljubljana-z-okolico&regija%5B%5D=vrhnika-z-okolico&hourly_rate=4.98%3B21"
	val scraper: StudencScraper = StudencScraper(url)
	var jobsArray: ArrayList<HashMap<String, String>> = scraper.getJobs()

	val jobsStorageHandler: JobsStorageHandler = JobsStorageHandler()
	for (job in jobsArray) {
		if (job["jobId"]!! != "" && job["title"]!! != "" && job["description"]!! != "" && job["pay"]!! != "") {
			if (jobsStorageHandler.queryByJobId(job["jobId"]!!)) {
				jobsStorageHandler.insertJob(job["title"]!!, job["jobId"]!!, job["description"]!!, job["pay"]!!)
			}
		}
	}

	val keywordsFull: HashMap<String, Int> = jobsStorageHandler.getKeywoardsFull()
	val keywords: ArrayList<String> = ArrayList(keywordsFull.keys)

	val statistic: Statistics = Statistics(keywords)
	val recordDate: String = jobsStorageHandler.makeKeywordRecordColumn()

	for (job in jobsArray) {
		if (!jobsStorageHandler.queryByJobId(job["jobId"]!!)) {
			for ((word, hits) in statistic.getHitsInString(job["description"]!!)) {
				if (hits != null) {
					jobsStorageHandler.updateKeyword(word, hits, recordDate)
				} else {println("null")}
			}
		}
	}

	jobsStorageHandler.close()
}