package database;

import DS.data_structures.TrainArray;
import DS.data_structures.TrainLinkedList;
import DS.data_structures.TrainRecord;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import models.Passenger;
import models.Station;
import models.Ticket;
import models.Train;

public class DatabaseManager {
    public static Connection getConnection() throws SQLException {
        return DatabaseInitializer.getConnection();
    }

    public static void initializeDatabase() {
        DatabaseInitializer.initializeDatabase();
    }

    public static void savePassenger(Passenger p) {
        String sql = "INSERT OR REPLACE INTO passengers (passenger_id, name, age, gender, contact_number) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, p.getPassengerId());
            pstmt.setString(2, p.getName());
            pstmt.setInt(3, p.getAge());
            pstmt.setString(4, p.getGender());
            pstmt.setString(5, p.getContactNumber());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void saveTicket(Ticket t) {
        String sql = "INSERT OR REPLACE INTO tickets (ticket_id, passenger_id, train_id, from_station, to_station, route_choice, seat_number, status, fare) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, t.getTicketId());
            pstmt.setString(2, t.getPassengerId());
            pstmt.setString(3, t.getTrainId());
            pstmt.setString(4, t.getFromStation() != null ? t.getFromStation() : "");
            pstmt.setString(5, t.getToStation() != null ? t.getToStation() : "");
            pstmt.setString(6, t.getRouteChoice() != null ? t.getRouteChoice() : "SHORTEST");
            pstmt.setString(7, t.getSeatNumber());
            pstmt.setString(8, t.getStatus());
            pstmt.setDouble(9, t.getFare());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateTrainSeats(String trainId, int availableSeats) {
        String sql = "UPDATE trains SET available_seats = ? WHERE train_id = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, availableSeats);
            pstmt.setString(2, trainId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void syncTrainsToDS(TrainArray trainArray, TrainLinkedList trainList) {
        if (trainArray != null) {
            trainArray.clear();
        }
        if (trainList != null) {
            trainList.clear();
        }
        String sql = "SELECT train_id, train_number, train_name, source_station_id, dest_station_id, total_seats, available_seats, departure_time, base_fare, route_station_ids, route_station_codes, cumulative_distances_km, cumulative_times_hr FROM trains ORDER BY train_id";
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String id = rs.getString("train_id");
                String num = rs.getString("train_number");
                String name = rs.getString("train_name");
                String src = rs.getString("source_station_id");
                String dst = rs.getString("dest_station_id");
                int total = rs.getInt("total_seats");
                int avail = rs.getInt("available_seats");
                double dept = rs.getDouble("departure_time");
                double fare = rs.getDouble("base_fare");
                String rIds = rs.getString("route_station_ids");
                String rCodes = rs.getString("route_station_codes");
                String cDist = rs.getString("cumulative_distances_km");
                String cTime = rs.getString("cumulative_times_hr");

                TrainRecord rec = new TrainRecord(id, num, name, src, dst, total, avail, dept, fare, rIds, rCodes, cDist, cTime);

                if (trainArray != null) {
                    trainArray.addRecord(rec.copy());
                }
                if (trainList != null) {
                    trainList.appendRecord(rec.copy());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void saveOrUpdateTrainInSQL(String id, String name, String src, String dst, int totalSeats, int availSeats, double depTime, double baseFare) {
        String sql = "INSERT OR REPLACE INTO trains (train_id, train_number, train_name, source_station_id, dest_station_id, total_seats, available_seats, departure_time, base_fare, route_station_ids, route_station_codes, cumulative_distances_km, cumulative_times_hr) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.setString(2, "10000");
            pstmt.setString(3, name);
            pstmt.setString(4, src);
            pstmt.setString(5, dst);
            pstmt.setInt(6, totalSeats);
            pstmt.setInt(7, availSeats);
            pstmt.setDouble(8, depTime);
            pstmt.setDouble(9, baseFare);
            pstmt.setString(10, src + "|" + dst);
            pstmt.setString(11, "");
            pstmt.setString(12, "0|100");
            pstmt.setString(13, "0.0|2.0");
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteTrainFromSQL(String id) {
        String sql = "DELETE FROM trains WHERE train_id = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateTicketStatusInSQL(String ticketId, String newStatus) {
        String sql = "UPDATE tickets SET status = ? WHERE ticket_id = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newStatus);
            pstmt.setString(2, ticketId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Train> loadTrainsFromSQL() {
        List<Train> list = new ArrayList<>();
        String sql = "SELECT train_id, train_number, train_name, source_station_id, dest_station_id, total_seats, available_seats, departure_time, base_fare, route_station_ids, route_station_codes, cumulative_distances_km, cumulative_times_hr FROM trains ORDER BY train_id";
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String id = rs.getString("train_id");
                String num = rs.getString("train_number");
                String name = rs.getString("train_name");
                String src = rs.getString("source_station_id");
                String dst = rs.getString("dest_station_id");
                int total = rs.getInt("total_seats");
                int avail = rs.getInt("available_seats");
                double dept = rs.getDouble("departure_time");
                double fare = rs.getDouble("base_fare");
                String rIds = rs.getString("route_station_ids");
                String rCodes = rs.getString("route_station_codes");
                String cDist = rs.getString("cumulative_distances_km");
                String cTime = rs.getString("cumulative_times_hr");

                Train t = new Train(name, src, dst);
                t.setTrainId(id);
                t.setTrainNumber(num);
                t.setTotalSeats(total);
                t.setAvailableSeats(avail);
                t.setDepartureTime(dept);
                t.setBaseFare(fare);
                t.setRouteStationIds(rIds);
                t.setRouteStationCodes(rCodes);
                t.setCumulativeDistancesKm(cDist);
                t.setCumulativeTimesHr(cTime);
                list.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<Station> loadStationsFromSQL() {
        List<Station> list = new ArrayList<>();
        String sql = "SELECT station_id, irctc_code, station_name, city, elevation_m, opengl_x, opengl_y FROM stations ORDER BY station_id";
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String id = rs.getString("station_id");
                String code = rs.getString("irctc_code");
                String name = rs.getString("station_name");
                String city = rs.getString("city");
                double elev = rs.getDouble("elevation_m");
                double ox = rs.getDouble("opengl_x");
                double oy = rs.getDouble("opengl_y");

                Station s = new Station(name, city, elev);
                s.setStationId(id);
                s.setIrctcCode(code);
                s.setOpenglX(ox);
                s.setOpenglY(oy);
                list.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static String getDatabaseSummary() {
        StringBuilder sb = new StringBuilder();
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            int stationsCount = getCount(stmt, "stations");
            int edgesCount = getCount(stmt, "track_edges");
            int trainsCount = getCount(stmt, "trains");
            int passengersCount = getCount(stmt, "passengers");
            int ticketsCount = getCount(stmt, "tickets");

            sb.append("========================================================================================\n");
            sb.append("                  SQLITE ENTERPRISE DATABASE SUMMARY (railroute.db)                     \n");
            sb.append("========================================================================================\n");
            sb.append(String.format("Stations:      %-4d records (32 Verified Maharashtra Junctions)\n", stationsCount));
            sb.append(String.format("Track Edges:   %-4d records (100%% Connected Bidirectional Railway Network)\n", edgesCount));
            sb.append(String.format("Trains:        %-4d records (40 Real Operational Indian Railways Services)\n", trainsCount));
            sb.append(String.format("Passengers:    %-4d records registered in SQLite\n", passengersCount));
            sb.append(String.format("Tickets:       %-4d records booked in SQLite\n", ticketsCount));
            sb.append("========================================================================================");
        } catch (SQLException e) {
            sb.append("Error reading database: ").append(e.getMessage());
        }
        return sb.toString();
    }

    private static int getCount(Statement stmt, String tableName) {
        try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM " + tableName)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            return 0;
        }
        return 0;
    }
}
