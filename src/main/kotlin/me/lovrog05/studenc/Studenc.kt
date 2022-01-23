package me.lovrog05.studenc

class Studenc(private val url: String, defaultKeywords: ArrayList<String>) {
	private val scraper: StudencScraper = StudencScraper(url)
	private val jobsStorageHandler: JobsStorageHandler = JobsStorageHandler()
	init {
		for (kwd in defaultKeywords) {
			jobsStorageHandler.insertKeywords(kwd, 0)
		}

	}
	private val keywordsFull: HashMap<String, Int> = jobsStorageHandler.getKeywoardsFull()
	private val keywords: ArrayList<String> = ArrayList(keywordsFull.keys)

	private val statistic: Statistics = Statistics(keywords)

	fun requestAndStoreJobs(updateKeywordHits: Boolean = true) {
		var jobsArray: ArrayList<HashMap<String, String>> = scraper.getJobs()
		for (job in jobsArray) {
			if (job["jobId"]!! != "" && job["title"]!! != "" && job["description"]!! != "" && job["pay"]!! != "") {
				if (jobsStorageHandler.isJobIdInDB(job["jobId"]!!)) {
					jobsStorageHandler.insertJob(job["title"]!!, job["jobId"]!!, job["description"]!!, job["pay"]!!)
				}
			}
		}
		if (updateKeywordHits) {
			updateKeywordHits(jobsArray)
		}
	}

	fun getStoredJobs(): ArrayList<HashMap<String, String>> {
		return jobsStorageHandler.getAllJobs()
	}

	fun getJobById(id: String): HashMap<String, String> {
		return jobsStorageHandler.getJobById(id)
	}

	fun getKeywords(): HashMap<String, Int> {
		return jobsStorageHandler.getKeywoardsFull()
	}

	fun getKeywordHits(word: String): HashMap<String, Int> {
		var hmp: HashMap<String, Int> = HashMap()
		hmp[word] = jobsStorageHandler.getWordHits(word)
		return hmp
	}

	fun updateKeywordHits(jobsArray: ArrayList<HashMap<String, String>>) {
		val recordDate: String = jobsStorageHandler.makeKeywordRecordColumn()

		for (job in jobsArray) {
			if (!jobsStorageHandler.isJobIdInDB(job["jobId"]!!)) {
				for ((word, hits) in statistic.getHitsInString(job["description"]!!)) {
					if (hits != null) {
						jobsStorageHandler.updateKeyword(word, hits, recordDate)
					} else {println("null")}
				}
			}
		}
	}

	fun close() {
		jobsStorageHandler.close()
	}

}