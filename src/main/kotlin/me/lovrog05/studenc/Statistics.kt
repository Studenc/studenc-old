package me.lovrog05.studenc

class Statistics(kywds: ArrayList<String>){
	var keywoards: ArrayList<String> = ArrayList()
	init {
		setKeywords(kywds)
	}

	private fun setKeywords(kwl: ArrayList<String>) {
		for (s in kwl) {
			keywoards.add(s)
		}
	}

	fun getHitsInString(string: String): HashMap<String, Int?> {
		var hitlist: HashMap<String, Int?> = HashMap()
		for (keywoard in keywoards) {
			if (string.lowercase().contains(keywoard.lowercase(), ignoreCase = true)) {
				hitlist[keywoard] = 1
			}
		}
		return hitlist
	}
}