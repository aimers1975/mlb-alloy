package scheduler;

import java.sql.*;
import java.util.*;

import com.microsoft.sqlserver.jdbc.*;

//Amy's IP address: 74.193.71.52

public class DB {
	
	static ArrayList<String> buildDBStructure(ArrayList<String> cachedOutput, int soli) {
		ArrayList<String> sql = new ArrayList();
		
		System.out.println("cachedOutput at top of buildDbStructure: " + cachedOutput.toString());
		
			ScheduleOutParser alloyOutput = new ScheduleOutParser(cachedOutput.get(soli)); // parse the schedule
			ArrayList<String> series = alloyOutput.parseSeries(); // parse the series from the schedule
			for (int i = 0; i < series.size(); i++) {
				String sqlTemp = "";
				
				sqlTemp = sqlTemp + String.valueOf(soli) + ", "; //solution number
				
				System.out.println("buildDBStructure: series:" + series.get(i).toString() + "                        +=+=+=+=+=+=+=+==+=+++++++=========+=+=+=+=+=+=+=+=+=+=+=+");
				sqlTemp = sqlTemp + series.get(i).substring(series.get(i).indexOf("$") + 1,series.get(i).length()) + ", "; // series
				System.out.println("sqlTemp after series: " + sqlTemp);
				
				ArrayList<String> temp = alloyOutput.getTeamsForSeries(series.get(i)); // parse the teams
				
				if (temp.size() == 2) {
					sqlTemp = sqlTemp + temp.get(0).substring(temp.get(0).indexOf("$") + 1,temp.get(0).length()) + ", "; // away team
					System.out.println("sqlTemp after away team: " + sqlTemp);
					sqlTemp = sqlTemp + temp.get(1).substring(temp.get(1).indexOf("$") + 1,temp.get(1).length()) + ", "; // home team
					System.out.println("sqlTemp after home team: " + sqlTemp);
				}
				temp = alloyOutput.getGamesForSeries(series.get(i)); // parse the games
				System.out.println("buildDBStructure:   getGamesForSeries: " + temp);
				
				if ((temp.size() == 3) || (temp.size() == 4)) {
					for (int m = 0; m < temp.size(); m++) {
						switch (m) {
						case 0:
							sqlTemp = sqlTemp + temp.get(m).substring(temp.get(m).indexOf("D") + 1, temp.get(m).indexOf("$")) + ", "; // day 1
							break;
						case 1:
							sqlTemp = sqlTemp + temp.get(m).substring(temp.get(m).indexOf("D") + 1, temp.get(m).indexOf("$")) + ", "; // day 2
							break;
						case 2:
							sqlTemp = sqlTemp + temp.get(m).substring(temp.get(m).indexOf("D") + 1, temp.get(m).indexOf("$")) + ", "; // day 3
							break;
						case 3:
							if (!(temp.get(m) == null)) {
								sqlTemp = sqlTemp + temp.get(m).substring(temp.get(m).indexOf("D") + 1, temp.get(m).indexOf("$")); // day 4	
								System.out.println("temp.get(m) == null");
							} else {
								sqlTemp = sqlTemp + ",";
								System.out.println("temp.get(m) != null");
							} // day 4							
						}
					}
				}
				sqlTemp = sqlTemp + ")";
				System.out.println("buildDBStructure built this: " + sqlTemp);
				sql.add(i, sqlTemp);
			}
		return sql;
	}

	static void writeSeries(ArrayList<String> cachedOutput) {
		System.out.println("Top of writeSeries =======================================================================================================");
		System.out.println("CachedOutput at top of writeSeries: " + cachedOutput.toString());
		
		String connectionUrl = "jdbc:sqlserver://xyha882oo4.database.windows.net:1433;"
				+ "database=MLB Alloy;user=VV@xyha882oo4;password=pass@word1;encrypt=true;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";

		// Declare the JDBC objects.
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;

		
		try {
			// Establish the connection.
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			con = DriverManager.getConnection(connectionUrl);
	
			String SQLprefix = "Insert into dbo.Series (Solution, Series, AwayTeam, HomeTeam, Day1, Day2, Day3, Day4) VALUES(";

			// Create and execute an SQL statement that inserts series data for each series in each solution
			for (int soli = 0; soli < cachedOutput.size(); soli++) {
				ArrayList<String> sqlSeries = buildDBStructure(cachedOutput, soli); //get an arraylist of series for the current solution
				System.out.println("sqlSeries :" + sqlSeries.toString());
				for(int seri = 0; seri < sqlSeries.size(); seri++){
					String SQL = SQLprefix + sqlSeries.get(seri); //add the sql string from the current series to the SQL prefix
					System.out.println("SQL is:" + SQL);
					stmt = con.createStatement();
					stmt.executeUpdate(SQL);
				}
			}
		}

		// Handle any errors that may have occurred.
		catch (Exception e) {
			System.out.println("Exception caught in writeSeries.");
			e.printStackTrace();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (Exception e) {
				}
			if (stmt != null)
				try {
					stmt.close();
				} catch (Exception e) {
				}
			if (con != null)
				try {
					con.close();
				} catch (Exception e) {
				}
		}
	}
}
	/*
	public static void getData(String[] args) {

		// Create a variable for the connection string.
		String connectionUrl = "jdbc:sqlserver://xyha882oo4.database.windows.net:1433;"
				+ "database=MLB Alloy;user=VV@xyha882oo4;password=pass@word1;encrypt=true;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";

		// Declare the JDBC objects.
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			// Establish the connection.
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			con = DriverManager.getConnection(connectionUrl);

			// Create and execute an SQL statement that returns some data.
			String SQL = "SELECT * FROM dbo.Game";
			stmt = con.createStatement();
			rs = stmt.executeQuery(SQL);

			// Iterate through the data in the result set and display it.
			System.out.println("Writing recordset.");
			while (rs.next()) {
				System.out.println(rs.getString(1) + " " + rs.getString(2)
						+ " " + rs.getString(3));
			}
		}

		// Handle any errors that may have occurred.
		catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (Exception e) {
				}
			if (stmt != null)
				try {
					stmt.close();
				} catch (Exception e) {
				}
			if (con != null)
				try {
					con.close();
				} catch (Exception e) {
				}
		}
	}
	
	public class DB {
		static DBSolution[] buildDBStructure(ArrayList<String> cachedOutput) {
			DBSolution[] dbsol = new DBSolution[15]; 
			DBSeries temp = new DBSeries();
			temp.solution = "0";
			temp.series = "0";
			temp.homeTeam = "Yankees";
			temp.awayTeam = "RedSox";
			dbsol[0].series[0] = temp;
			
			System.out.println(dbsol[0].series[0].toString());
			
			/*
			ArrayList<DBSeries> dbs = new ArrayList<>();// array list of game series
			
			// iterate through each of the solutions in cachedOutput and write to the corresponding DBSeries with soli (solution iterator)
			for (int soli = 0; soli < cachedOutput.size(); soli++) {
				ScheduleOutParser alloyOutput = new ScheduleOutParser(cachedOutput.get(soli)); // parse the schedule
				ArrayList<String> series = alloyOutput.parseSeries(); // parse the series from the schedule
				for (int i = 0; i < series.size(); i++) {
					DBSeries tempSeries = new DBSeries();
					tempSeries.solution =  String.valueOf(soli);
					tempSeries.series = series.get(i);
					ArrayList<String> temp = alloyOutput.getTeamsForSeries(series.get(i)); // parse the teams
					if (temp.size() == 2) {
						tempSeries.awayTeam = temp.get(0);
						tempSeries.homeTeam = temp.get(1);
					}
					temp = alloyOutput.getGamesForSeries(series.get(i)); // parse the games
					if ((temp.size() == 3) || (temp.size() == 4)) {
						for (int m = 0; m < temp.size(); m++) {
							switch (m) {
							case 0:
								tempSeries.day1 = temp.get(m);
							case 1:
								tempSeries.day2 = temp.get(m);
							case 2:
								tempSeries.day3 = temp.get(m);
							case 3:
								tempSeries.day4 = temp.get(m);
							}
						}
					}
					dbs.add(i,tempSeries);
					System.out.println("DBS " + i + " : " + dbs.get(i).toString());
				}
				dbsol[soli].series.addAll(dbs);
				System.out.println("DBSolution " + soli + ": " + dbsol[soli].series.get(0).toString());
			}
			return dbsol;
		}
		*/
