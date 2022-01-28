package me.lovrog05.studenc

class Statistics(){
	companion object {
		fun getHitsInString(description: String, keyword: String): Int {
			var hits = 0
			val list = description.split(" ")
			for (word in list) {
				if (keyword.lowercase() in word.lowercase()) {
					hits += 1
				}
			}

			return hits
		}
	}
}