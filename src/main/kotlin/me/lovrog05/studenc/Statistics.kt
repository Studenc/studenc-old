package me.lovrog05.studenc

class Statistics(kywds: ArrayList<String>){
	private var keywords: ArrayList<String> = ArrayList()
	init {
		setKeywords(kywds)
	}

	private fun setKeywords(kwl: ArrayList<String>) {
		for (s in kwl) {
			keywords.add(s)
		}
	}

	fun getHitsInString(description: String): HashMap<String, Int?> {
		var hitlist: HashMap<String, Int?> = HashMap()
		for (keyword in keywords) {
			if (keyword.lowercase() in description.lowercase()) {
				hitlist[keyword] = 1
			}
		}
		return hitlist
	}
}