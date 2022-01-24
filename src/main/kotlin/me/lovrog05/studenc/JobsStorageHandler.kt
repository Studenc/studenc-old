package me.lovrog05.studenc

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException
import java.time.LocalDate

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
						"description TEXT, pay TEXT, date TEXT, workingday TEXT, duration TEXT, spots TEXT, UNIQUE(jobid))")
				st.close()
			}
		} catch (e: SQLException) {
			println(e.message)
		}
	}

	fun insertJob(title: String, jobid: String, description: String, pay: String, wd: String, dur: String, spots: String) {
		try {
			val st = conn?.createStatement()
			if (st != null) {
				st.executeUpdate("INSERT OR IGNORE INTO jobs (title, jobid, description, pay, date, workingday, " +
						"duration, spots) VALUES " +
						"('${title}', '${jobid}', '${description}', '${pay}', '${LocalDate.now()}', '$wd', '$dur', '$spots')")
				st.close()
			}
		} catch (e: SQLException) {
			println("insertjob: $e.message")
		}
	}

	fun getAllJobs(): ArrayList<HashMap<String, String>> {
		var jobs: ArrayList<HashMap<String, String>> = ArrayList()
		try {
			var rs: ResultSet? = null
			val st = conn?.createStatement()
			if (st != null) {
				rs = st.executeQuery("SELECT * FROM jobs")
				while (rs.next()) {
					var hmp: HashMap<String, String> = HashMap()
					hmp["jobId"] = rs.getString("jobid")
					hmp["title"] = rs.getString("title")
					hmp["pay"] = rs.getString("pay")
					hmp["description"] = rs.getString("description")
					hmp["dateSpotted"] = rs.getString("date")
					hmp["workingDay"] = rs.getString("workingday")
					hmp["duration"] = rs.getString("duration")
					hmp["spots"] = rs.getString("spots")
					jobs.add(hmp)
				}
				st.close()
			}
		} catch (e: SQLException) {
			println("queryJobById: $e.message")
		}
		return jobs
	}

	fun getJobById(jobid: String): HashMap<String, String> {
		var job: HashMap<String, String> = HashMap()

		try {
			var rs: ResultSet? = null
			val st = conn?.createStatement()
			if (st != null) {
				rs = st.executeQuery("SELECT * FROM jobs WHERE jobid = '$jobid'")
				while (rs.next()) {
					job["jobId"] = rs.getString("jobid")
					job["title"] = rs.getString("title")
					job["pay"] = rs.getString("pay")
					job["description"] = rs.getString("description")
					job["dateSpotted"] = rs.getString("date")
					job["workingDay"] = rs.getString("workingday")
					job["duration"] = rs.getString("duration")
					job["spots"] = rs.getString("spots")
				}
				st.close()
			}
		} catch (e: SQLException) {
			println("queryJobById: $e.message")
		}
		return job
	}

	fun isJobIdInDB(jobid: String): Boolean {
		var isin: Boolean = false

		try {
			var rs: ResultSet? = null
			val st = conn?.createStatement()
			if (st != null) {
				rs = st.executeQuery("SELECT COUNT(*) FROM jobs WHERE jobid = '$jobid'")
				while (rs.next()) {
					if (rs.getInt("COUNT(*)") >= 1) {
						isin = true
					}
				}
				st.close()
			}
		} catch (e: SQLException) {
			println("queryJobById: $e.message")
		}
		return isin
	}

	fun close() {
		conn?.close()
	}
}