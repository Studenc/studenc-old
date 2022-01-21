package me.lovrog05.studenc

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException
import java.time.LocalDateTime


class JobsStorageHandler {
	var conn: Connection? = null

	init {
		try {
			val url = "jdbc:sqlite:/home/lovro/Documents/jobs.db"
			// create a connection to the database
			conn = DriverManager.getConnection(url)
			println("Connection to SQLite has been established.")
		} catch (e: SQLException) {
			e.printStackTrace()
		} catch (e: ClassNotFoundException) {
			e.printStackTrace()
		}

		createTable()
	}

	private fun createTable() {
		try {
			val st = conn?.createStatement()
			if (st != null) {
				st.executeUpdate("CREATE TABLE IF NOT EXISTS jobs (id INTEGER PRIMARY KEY, title TEXT, jobid TEXT," +
						"description TEXT, pay TEXT, date TEXT)")
				st.executeUpdate("CREATE TABLE IF NOT EXISTS keywords (id INTEGER PRIMARY KEY, word TEXT, hits INTEGER)")
				st.close()
			}
		} catch (e: SQLException) {
			println(e.message)
		}
	}

	fun insertJob(title: String, jobid: String, description: String, pay: String) {
		try {
			val st = conn?.createStatement()
			if (st != null) {
				st.executeUpdate("INSERT INTO jobs (title, jobid, description, pay, date) VALUES ('${title}', '${jobid}', '${description}', '${pay}', '${LocalDateTime.now()}')")
				st.close()
			}
		} catch (e: SQLException) {
			println(e.message)
		}
	}

	fun queryByJobId(jobid: String): Boolean {
		var isin: Boolean = false

		try {
			var rs: ResultSet? = null
			val st = conn?.createStatement()
			if (st != null) {
				rs = st.executeQuery("SELECT * FROM jobs WHERE jobid = $jobid")
				isin = !rs.next()
				st.close()
			}
		} catch (e: SQLException) {
			println(e.message)
		}
		return isin
	}

	fun getKeywoards(): HashMap<String, Int> {
		var keywords: HashMap<String, Int> = HashMap()
		try {
			var rs: ResultSet? = null
			val st = conn?.createStatement()
			if (st != null) {
				rs = st.executeQuery("SELECT * FROM keywords")
				while (rs.next()) {
					keywords[rs.getString("word")] = rs.getInt("hits")
				}
				st.close()
			}
		} catch (e: SQLException) {
			println(e.message)
		}
		return keywords
	}

	fun updateKeyword(word: String, hits: Int) {
		try {
			val st = conn?.createStatement()
			if (st != null) {
				st.executeUpdate("UPDATE keywords SET hits = '$hits' WHERE word = '$word'")
				st.close()
			}
		} catch (e: SQLException) {
			println(e.message)
		}
	}

	fun insertKeyboards(word: String, hits: Int) {
		try {
			val st = conn?.createStatement()
			if (st != null) {
				st.executeUpdate("INSERT INTO keywords (word, hits) VALUES ($word, $hits)")
				st.close()
			}
		} catch (e: SQLException) {
			println(e.message)
		}
	}

	fun close() {
		conn?.close()
	}
}