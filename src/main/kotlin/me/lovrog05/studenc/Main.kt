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

	var keywordsFull: HashMap<String, Int> = jobsStorageHandler.getKeywoards()
	var keywords: ArrayList<String> = ArrayList(keywordsFull.keys)

	var statistic: Statistics = Statistics(keywords)

	var hits: ArrayList<HashMap<String, Int?>> = ArrayList()
	for (job in jobsArray) {
		hits.add(job["description"]?.let { statistic.getHitsInString(it) }!!)
	}

	keywords.forEach {
		for (hit in hits) {
			if (hit.containsKey(it)) {
				keywordsFull[it] = keywordsFull[it]?.plus(hit[it]!!) ?: 0
			}
		}
	}

	for ((word, hits) in keywordsFull) {
		jobsStorageHandler.updateKeyword(word, hits)
	}

	jobsStorageHandler.close()
}