package me.lovrog05.studenc

class Statistics(){
	companion object {
		fun getHitsInString(description: String, keyword: String): Int {
			var hits: Int = 0
			if (keyword.lowercase() in description.lowercase()) {
				hits += 1
			}

			return hits
		}
	}
}