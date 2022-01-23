package me.lovrog05.studenc

class Studenc(private val url: String) {
	private val scraper: StudencScraper = StudencScraper(url)
	private val jobsStorageHandler: JobsStorageHandler = JobsStorageHandler()

	fun requestAndStoreJobs() {
		var jobsArray: ArrayList<HashMap<String, String>> = scraper.getJobs()
		for (job in jobsArray) {
			if (job["jobId"]!! != "" && job["title"]!! != "" && job["description"]!! != "" && job["pay"]!! != "") {
				if (!jobsStorageHandler.isJobIdInDB(job["jobId"]!!)) {
					jobsStorageHandler.insertJob(job["title"]!!, job["jobId"]!!, job["description"]!!, job["pay"]!!)
				}
			}
		}
	}

	fun getStoredJobs(): ArrayList<HashMap<String, String>> {
		return jobsStorageHandler.getAllJobs()
	}

	fun getJobById(id: String): HashMap<String, String> {
		return jobsStorageHandler.getJobById(id)
	}

	fun getKeywordHits(word: String): HashMap<String, Int?> {
		var hits: HashMap<String, Int?> = HashMap()
		for (job in getStoredJobs()) {
			hits[job["dateSpotted"]!!] = hits[job["dateSpotted"]!!]?.plus(Statistics.getHitsInString(job["description"]!!, word))
		}
		return hits
	}

	fun close() {
		jobsStorageHandler.close()
	}
}