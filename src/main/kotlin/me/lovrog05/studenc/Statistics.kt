package me.lovrog05.studenc

class Statistics {
	var keywoards: ArrayList<String> = ArrayList()

	fun setKeywords(kwl: ArrayList<String>) {
		for (s in kwl) {
			keywoards.add(s)
		}
	}

	fun getHitsInString(string: String): HashMap<String, Int?> {
		var hitlist: HashMap<String, Int?> = HashMap()
		for (keywoard in keywoards) {
			if (string.contains(keywoard, ignoreCase = true)) {
				hitlist[keywoard] = hitlist[keywoard]?.plus(1)
			}
		}
		return hitlist
	}
}