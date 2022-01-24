package me.lovrog05.studenc


class Studenc(private val url: String) {
	private val scraper: StudencScraper = StudencScraper(url)
	private val jobsStorageHandler: JobsStorageHandler = JobsStorageHandler()
	private var currentAvailableJobs: ArrayList<HashMap<String, String>> = ArrayList()
	private var currentAverage: Double = 0.0
	private var currentHighest: Float = 0F

	fun requestAndStoreJobs() {
		var jobsArray: ArrayList<HashMap<String, String>> = scraper.getJobs()
		for (job in jobsArray) {
			if (job["jobId"]!! != "" && job["title"]!! != "" && job["description"]!! != "" && job["pay"]!! != "") {
				if (!jobsStorageHandler.isJobIdInDB(job["jobId"]!!)) {
					jobsStorageHandler.insertJob(job["title"]!!, job["jobId"]!!, job["description"]!!, job["pay"]!!, job["workingDay"]!!, job["duration"]!!, job["spots"]!!)
				}
			}
		}
		currentAvailableJobs = jobsArray
		currentAverage = calculateCurrentAverage()
		currentHighest = getCurrentHighest()
	}

	private fun calculateCurrentAverage(): Double {
		var sumPay: ArrayList<Float> = ArrayList()
		currentAvailableJobs.forEach {
			if (it["pay"]?.replace(",", ".")?.toFloatOrNull() != null) {
				it["pay"]?.let { it1 -> sumPay.add(it1.replace(",", ".").toFloat()) }
			}
		}
		return sumPay.average()
	}

	fun getCurrentHighest(): Float {
		var highestYet: Float = 0F
		for (curJob in currentAvailableJobs) {
			if (curJob["pay"]?.replace(",", ".")?.toFloatOrNull() != null) {
				if (curJob["pay"]?.replace(",", ".")?.toFloat() ?: 0F > highestYet) {
					highestYet = curJob["pay"]?.replace(",", ".")?.toFloat()!!
				}
			}
		}
		return highestYet
	}

	fun getStoredJobs(): ArrayList<HashMap<String, String>> {
		return jobsStorageHandler.getAllJobs()
	}

	fun getJobById(id: String): HashMap<String, String> {
		return jobsStorageHandler.getJobById(id)
	}

	fun getKeywordHits(word: String): HashMap<String, Int?> {
		var hits: HashMap<String, Int?> = HashMap()
		var totalHits: Int = 0
		for (job in getStoredJobs()) {
			var currentHits: Int = Statistics.getHitsInString(job["description"]!!, word)
			hits[job["dateSpotted"]!!] = hits[job["dateSpotted"]!!]?.plus(currentHits) ?: currentHits
			totalHits += currentHits
		}
		hits["total"] = totalHits
		return hits
	}

	fun getJobStats(): HashMap<String, String> {
		var data: HashMap<String, String> = HashMap()
		val curJobs: ArrayList<HashMap<String, String>> = getStoredJobs()
		data["numberOfJobsAT"] = curJobs.size.toString()
		var sumPay: ArrayList<Float> = ArrayList()
		curJobs.forEach {
			if (it["pay"]?.replace(",", ".")?.toFloatOrNull() != null) {
				it["pay"]?.let { it1 -> sumPay.add(it1.replace(",", ".").toFloat()) }
			}
		}
		data["averagePayAT"] = sumPay.average().toString()
		var highestYet: Float = 0F
		for (curJob in curJobs) {
			if (curJob["pay"]?.replace(",", ".")?.toFloatOrNull() != null) {
				if (curJob["pay"]?.replace(",", ".")?.toFloat() ?: 0F > highestYet) {
					highestYet = curJob["pay"]?.replace(",", ".")?.toFloat()!!
				}
			}
		}
		data["highestPayAT"] = highestYet.toString()
		data["currentAveragePay"] = currentAverage.toString()
		data["currentHighestPay"] = currentHighest.toString()
		data["currentNumberOfJobs"] = currentAvailableJobs.size.toString()
		return data
	}

	fun close() {
		jobsStorageHandler.close()
	}
}