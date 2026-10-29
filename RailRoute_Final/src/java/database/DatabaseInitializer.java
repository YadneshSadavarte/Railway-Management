package database;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

public class DatabaseInitializer {
    private static final String DB_URL = "jdbc:sqlite:data/railroute.db";

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        new File("data").mkdirs();
        return DriverManager.getConnection(DB_URL);
    }

    private static final String[][] STATIONS_DATA = {
        {"MH-001", "NDB",  "Nandurbar", "Nandurbar", "500.0", "-0.496", "0.784"},
        {"MH-002", "DHI",  "Dhule", "Dhule", "500.0", "-0.383", "0.641"},
        {"MH-003", "JL",   "Jalgaon Junction", "Jalgaon", "500.0", "-0.212", "0.679"},
        {"MH-004", "AK",   "Akola Junction", "Akola", "500.0", "0.098", "0.593"},
        {"MH-005", "AMI",  "Amravati", "Amravati", "500.0", "0.260", "0.654"},
        {"MH-006", "NGP",  "Nagpur Junction", "Nagpur", "310.0", "0.546", "0.719"},
        {"MH-007", "BRD",  "Bhandara Road", "Bhandara", "500.0", "0.667", "0.744"},
        {"MH-008", "G",    "Gondia Junction", "Gondia", "500.0", "0.783", "0.810"},
        {"MH-009", "NK",   "Nashik Road", "Nashik", "565.0", "-0.583", "0.366"},
        {"MH-010", "PLG",  "Palghar", "Palghar", "500.0", "-0.813", "0.293"},
        {"MH-011", "TNA",  "Thane", "Thane", "9.0", "-0.769", "0.142"},
        {"MH-012", "BA",   "Bandra Terminus", "Mumbai", "12.0", "-0.798", "0.104"},
        {"MH-013", "CSMT", "Mumbai CSMT", "Mumbai", "8.0", "-0.799", "0.070"},
        {"MH-014", "PUNE", "Pune Junction", "Pune", "559.0", "-0.576", "-0.050"},
        {"MH-015", "ANG",  "Ahilyanagar", "Ahilyanagar", "500.0", "-0.393", "0.110"},
        {"MH-016", "BEED", "Beed", "Beed", "500.0", "-0.176", "0.080"},
        {"MH-017", "AWB",  "Chhatrapati Sambhajinagar (Aurangabad)", "Aurangabad", "500.0", "-0.267", "0.340"},
        {"MH-018", "J",    "Jalna", "Jalna", "500.0", "-0.141", "0.331"},
        {"MH-019", "WHM",  "Washim", "Washim", "500.0", "0.129", "0.411"},
        {"MH-020", "HNL",  "Hingoli Deccan", "Hingoli", "500.0", "0.128", "0.301"},
        {"MH-021", "WR",   "Wardha Junction", "Wardha", "500.0", "0.440", "0.596"},
        {"MH-022", "CD",   "Chandrapur", "Chandrapur", "500.0", "0.592", "0.369"},
        {"MH-023", "PBN",  "Parbhani Junction", "Parbhani", "500.0", "0.048", "0.163"},
        {"MH-024", "NED",  "Huzur Sahib Nanded", "Nanded", "500.0", "0.164", "0.135"},
        {"MH-025", "LUR",  "Latur", "Latur", "500.0", "0.001", "-0.079"},
        {"MH-026", "UMD",  "Dharashiv (Usmanabad)", "Dharashiv", "500.0", "-0.113", "-0.132"},
        {"MH-027", "SUR",  "Solapur Junction", "Solapur", "457.0", "-0.141", "-0.304"},
        {"MH-028", "STR",  "Satara", "Satara", "500.0", "-0.535", "-0.297"},
        {"MH-029", "RN",   "Ratnagiri", "Ratnagiri", "500.0", "-0.687", "-0.497"},
        {"MH-030", "SLI",  "Sangli", "Sangli", "500.0", "-0.422", "-0.540"},
        {"MH-031", "KOP",  "Kolhapur SCSMT", "Kolhapur", "500.0", "-0.498", "-0.585"},
        {"MH-032", "KUDL", "Kudal", "Kudal", "500.0", "-0.618", "-0.786"}
    };

    public static final String[][] TRAINS_DATA = {
        {"TRN101", "22225", "CSMT Solapur Vande Bharat", "MH-013", "MH-027", "5", "5", "6.05", "2.40", "MH-013|MH-011|MH-014|MH-027", "CSMT|TNA|PUNE|SUR", "0|33|192|455", "0.0|0.4|3.0|6.5"},
        {"TRN102", "22226", "Solapur CSMT Vande Bharat", "MH-027", "MH-013", "5", "5", "6.05", "2.40", "MH-027|MH-014|MH-011|MH-013", "SUR|PUNE|TNA|CSMT", "0|263|422|455", "0.0|3.5|6.1|6.5"},
        {"TRN103", "11303", "Mumbai Latur Solapur Exp", "MH-013", "MH-027", "5", "5", "20.50", "1.80", "MH-013|MH-011|MH-014|MH-026|MH-025|MH-027", "CSMT|TNA|PUNE|UMD|LUR|SUR", "0|33|192|450|539|729", "0.0|0.5|3.4|7.8|9.3|12.5"},
        {"TRN104", "11304", "Solapur Latur Mumbai Exp", "MH-027", "MH-013", "5", "5", "7.15", "1.80", "MH-027|MH-025|MH-026|MH-014|MH-011|MH-013", "SUR|LUR|UMD|PUNE|TNA|CSMT", "0|190|279|537|696|729", "0.0|3.2|4.7|9.1|12.0|12.5"},
        {"TRN105", "11039", "Maharashtra Express", "MH-031", "MH-008", "5", "5", "14.45", "1.80", "MH-031|MH-030|MH-028|MH-014|MH-015|MH-003|MH-004|MH-005|MH-021|MH-006|MH-007|MH-008", "KOP|SLI|STR|PUNE|ANG|JL|AK|AMI|WR|NGP|BRD|G", "0|54|181|326|480|793|957|1045|1131|1210|1273|1340", "0.0|1.1|3.5|6.8|9.8|15.2|18.0|19.5|21.0|22.5|23.6|25.0"},
        {"TRN106", "11040", "Maharashtra Express Return", "MH-008", "MH-031", "5", "5", "10.45", "1.80", "MH-008|MH-007|MH-006|MH-021|MH-005|MH-004|MH-003|MH-015|MH-014|MH-028|MH-030|MH-031", "G|BRD|NGP|WR|AMI|AK|JL|ANG|PUNE|STR|SLI|KOP", "0|67|130|209|295|383|547|860|1014|1159|1286|1340", "0.0|1.2|2.5|4.0|5.5|7.0|9.8|15.2|18.2|21.5|23.9|25.0"},
        {"TRN107", "11404", "Kolhapur Nagpur Express", "MH-031", "MH-006", "5", "5", "12.45", "1.80", "MH-031|MH-030|MH-027|MH-026|MH-025|MH-023|MH-020|MH-019|MH-004|MH-021|MH-006", "KOP|SLI|SUR|UMD|LUR|PBN|HNL|WHM|AK|WR|NGP", "0|54|242|310|399|559|668|715|796|971|1049", "0.0|1.0|4.5|5.8|7.4|10.5|12.4|13.3|15.0|18.0|19.5"},
        {"TRN108", "11403", "Nagpur Kolhapur Express", "MH-006", "MH-031", "5", "5", "15.00", "1.80", "MH-006|MH-021|MH-004|MH-019|MH-020|MH-023|MH-025|MH-026|MH-027|MH-030|MH-031", "NGP|WR|AK|WHM|HNL|PBN|LUR|UMD|SUR|SLI|KOP", "0|78|253|334|381|490|650|739|807|995|1049", "0.0|1.3|4.5|6.1|7.0|9.0|12.1|13.7|15.0|18.5|19.5"},
        {"TRN109", "11025", "Pune Amravati Express (Via ANG)", "MH-014", "MH-005", "5", "5", "11.05", "1.80", "MH-014|MH-015|MH-003|MH-004|MH-005", "PUNE|ANG|JL|AK|AMI", "0|154|466|631|719", "0.0|2.8|8.2|11.0|12.8"},
        {"TRN110", "11026", "Amravati Pune Express (Via ANG)", "MH-005", "MH-014", "5", "5", "21.50", "1.80", "MH-005|MH-004|MH-003|MH-015|MH-014", "AMI|AK|JL|ANG|PUNE", "0|88|253|565|719", "0.0|1.6|4.5|10.0|12.8"},
        {"TRN111", "11405", "Pune Amravati Marathwada Exp", "MH-014", "MH-006", "5", "5", "22.50", "1.80", "MH-014|MH-026|MH-025|MH-023|MH-020|MH-019|MH-004|MH-005|MH-021|MH-006", "PUNE|UMD|LUR|PBN|HNL|WHM|AK|AMI|WR|NGP", "0|258|347|506|615|663|744|832|918|997", "0.0|4.6|6.2|9.2|11.2|12.1|13.8|15.5|17.0|18.5"},
        {"TRN112", "11406", "Amravati Pune Marathwada Exp", "MH-006", "MH-014", "5", "5", "18.30", "1.80", "MH-006|MH-021|MH-005|MH-004|MH-019|MH-020|MH-023|MH-025|MH-026|MH-014", "NGP|WR|AMI|AK|WHM|HNL|PBN|LUR|UMD|PUNE", "0|79|165|253|334|382|491|650|739|997", "0.0|1.4|3.0|4.7|6.3|7.2|9.3|12.3|13.9|18.5"},
        {"TRN113", "17613", "Pune Nanded Express (Via Latur)", "MH-014", "MH-024", "5", "5", "19.35", "1.80", "MH-014|MH-026|MH-025|MH-023|MH-024", "PUNE|UMD|LUR|PBN|NED", "0|258|347|506|565", "0.0|4.5|6.0|9.0|10.2"},
        {"TRN114", "17614", "Nanded Pune Express (Via Latur)", "MH-024", "MH-014", "5", "5", "17.30", "1.80", "MH-024|MH-023|MH-025|MH-026|MH-014", "NED|PBN|LUR|UMD|PUNE", "0|59|218|307|565", "0.0|1.1|4.1|5.7|10.2"},
        {"TRN115", "11409", "Pune Nanded Express (Via AWB)", "MH-014", "MH-024", "5", "5", "15.10", "1.80", "MH-014|MH-015|MH-017|MH-018|MH-023|MH-024", "PUNE|ANG|AWB|J|PBN|NED", "0|154|420|483|597|661", "0.0|2.8|7.5|8.6|10.6|11.8"},
        {"TRN116", "11410", "Nanded Pune Express (Via AWB)", "MH-024", "MH-014", "5", "5", "6.15", "1.80", "MH-024|MH-023|MH-018|MH-017|MH-015|MH-014", "NED|PBN|J|AWB|ANG|PUNE", "0|64|178|241|507|661", "0.0|1.2|3.2|4.3|9.0|11.8"},
        {"TRN117", "12105", "Vidarbha SF Express", "MH-013", "MH-008", "5", "5", "19.05", "1.95", "MH-013|MH-011|MH-009|MH-003|MH-004|MH-005|MH-021|MH-006|MH-007|MH-008", "CSMT|TNA|NK|JL|AK|AMI|WR|NGP|BRD|G", "0|33|187|420|584|672|759|837|900|967", "0.0|0.5|3.1|6.5|9.0|10.4|11.8|13.2|14.2|15.5"},
        {"TRN118", "12106", "Vidarbha SF Express Return", "MH-008", "MH-013", "5", "5", "14.40", "1.95", "MH-008|MH-007|MH-006|MH-021|MH-005|MH-004|MH-003|MH-009|MH-011|MH-013", "G|BRD|NGP|WR|AMI|AK|JL|NK|TNA|CSMT", "0|67|130|208|295|383|547|780|934|967", "0.0|1.1|2.3|3.6|5.0|6.4|9.0|12.4|15.0|15.5"},
        {"TRN119", "19003", "Bandra Khandesh Tapti Exp", "MH-013", "MH-006", "5", "5", "0.15", "1.80", "MH-013|MH-012|MH-010|MH-001|MH-002|MH-003|MH-004|MH-021|MH-006", "CSMT|BA|PLG|NDB|DHI|JL|AK|WR|NGP", "0|15|87|413|505|564|728|902|981", "0.0|0.3|1.4|6.8|8.4|9.5|12.2|15.0|16.4"},
        {"TRN120", "19004", "Khandesh Bandra Tapti Exp", "MH-006", "MH-013", "5", "5", "6.30", "1.80", "MH-006|MH-021|MH-004|MH-003|MH-002|MH-001|MH-010|MH-012|MH-013", "NGP|WR|AK|JL|DHI|NDB|PLG|BA|CSMT", "0|79|253|417|476|568|894|966|981", "0.0|1.3|4.2|6.9|8.0|9.6|15.0|16.1|16.4"},
        {"TRN121", "11001", "Nandigram Express (Via AWB-NED)", "MH-013", "MH-006", "5", "5", "16.35", "1.80", "MH-013|MH-011|MH-009|MH-017|MH-018|MH-023|MH-024|MH-022|MH-021|MH-006", "CSMT|TNA|NK|AWB|J|PBN|NED|CD|WR|NGP", "0|33|187|373|436|551|610|938|1072|1151", "0.0|0.5|3.2|6.5|7.6|9.6|10.8|16.5|18.8|20.2"},
        {"TRN122", "11002", "Nandigram Express Return", "MH-006", "MH-013", "5", "5", "6.00", "1.80", "MH-006|MH-021|MH-022|MH-024|MH-023|MH-018|MH-017|MH-009|MH-011|MH-013", "NGP|WR|CD|NED|PBN|J|AWB|NK|TNA|CSMT", "0|79|213|541|600|715|778|964|1118|1151", "0.0|1.3|3.6|9.4|10.5|12.5|13.6|17.0|19.7|20.2"},
        {"TRN123", "22109", "Mumbai Chandrapur SF Exp (Via AK)", "MH-013", "MH-022", "5", "5", "14.55", "1.95", "MH-013|MH-011|MH-009|MH-003|MH-004|MH-021|MH-022", "CSMT|TNA|NK|JL|AK|WR|CD", "0|33|187|420|584|759|877", "0.0|0.5|3.1|6.4|8.9|11.6|13.5"},
        {"TRN124", "22110", "Chandrapur Mumbai SF Exp (Via AK)", "MH-022", "MH-013", "5", "5", "18.20", "1.95", "MH-022|MH-021|MH-004|MH-003|MH-009|MH-011|MH-013", "CD|WR|AK|JL|NK|TNA|CSMT", "0|118|293|457|690|844|877", "0.0|1.9|4.6|7.1|10.4|13.0|13.5"},
        {"TRN125", "17666", "Mumbai Nanded Exp (Via Washim)", "MH-013", "MH-024", "5", "5", "16.55", "1.80", "MH-013|MH-011|MH-009|MH-003|MH-004|MH-019|MH-020|MH-023|MH-024", "CSMT|TNA|NK|JL|AK|WHM|HNL|PBN|NED", "0|33|187|420|584|663|713|821|880", "0.0|0.5|3.1|6.6|9.2|10.6|11.5|13.3|14.4"},
        {"TRN126", "17665", "Nanded Mumbai Exp (Via Washim)", "MH-024", "MH-013", "5", "5", "8.30", "1.80", "MH-024|MH-023|MH-020|MH-019|MH-004|MH-003|MH-009|MH-011|MH-013", "NED|PBN|HNL|WHM|AK|JL|NK|TNA|CSMT", "0|59|167|217|296|460|693|847|880", "0.0|1.0|2.9|3.8|5.2|7.8|11.3|13.9|14.4"},
        {"TRN127", "16734", "Khandesh Marathwada Exp (Via AWB)", "MH-001", "MH-024", "5", "5", "9.15", "1.80", "MH-001|MH-003|MH-017|MH-018|MH-023|MH-024", "NDB|JL|AWB|J|PBN|NED", "0|151|335|398|512|658", "0.0|2.6|6.0|7.1|9.1|11.5"},
        {"TRN128", "16733", "Marathwada Khandesh Exp (Via AWB)", "MH-024", "MH-001", "5", "5", "11.20", "1.80", "MH-024|MH-023|MH-018|MH-017|MH-003|MH-001", "NED|PBN|J|AWB|JL|NDB", "0|146|260|323|507|658", "0.0|2.4|4.4|5.5|8.9|11.5"},
        {"TRN129", "17296", "Tapi Washim Nanded SF Exp", "MH-001", "MH-024", "5", "5", "13.10", "1.95", "MH-001|MH-003|MH-004|MH-019|MH-020|MH-024", "NDB|JL|AK|WHM|HNL|NED", "0|151|315|394|444|554", "0.0|2.5|5.1|6.5|7.4|9.3"},
        {"TRN130", "17295", "Nanded Washim Tapi SF Exp", "MH-024", "MH-001", "5", "5", "15.40", "1.95", "MH-024|MH-020|MH-019|MH-004|MH-003|MH-001", "NED|HNL|WHM|AK|JL|NDB", "0|110|160|239|403|554", "0.0|1.9|2.8|4.2|6.8|9.3"},
        {"TRN131", "10103", "Mandovi Konkan Express", "MH-013", "MH-032", "5", "5", "7.10", "1.80", "MH-013|MH-011|MH-029|MH-032", "CSMT|TNA|RN|KUDL", "0|33|349|488", "0.0|0.5|6.2|8.8"},
        {"TRN132", "10104", "Mandovi Konkan Express Return", "MH-032", "MH-013", "5", "5", "10.30", "1.80", "MH-032|MH-029|MH-011|MH-013", "KUDL|RN|TNA|CSMT", "0|139|455|488", "0.0|2.5|8.2|8.8"},
        {"TRN133", "11011", "CSMT Dhule Express (Via NK)", "MH-013", "MH-002", "5", "5", "12.00", "1.80", "MH-013|MH-011|MH-009|MH-002", "CSMT|TNA|NK|DHI", "0|33|187|384", "0.0|0.5|3.2|6.8"},
        {"TRN134", "11012", "Dhule CSMT Express (Via NK)", "MH-002", "MH-013", "5", "5", "6.30", "1.80", "MH-002|MH-009|MH-011|MH-013", "DHI|NK|TNA|CSMT", "0|197|351|384", "0.0|3.5|6.2|6.8"},
        {"TRN135", "11421", "Pune Ahilyanagar Beed Exp", "MH-014", "MH-016", "5", "5", "7.25", "1.80", "MH-014|MH-015|MH-016", "PUNE|ANG|BEED", "0|154|323", "0.0|2.7|5.8"},
        {"TRN136", "11422", "Beed Ahilyanagar Pune Exp", "MH-016", "MH-014", "5", "5", "14.30", "1.80", "MH-016|MH-015|MH-014", "BEED|ANG|PUNE", "0|169|323", "0.0|3.1|5.8"},
        {"TRN137", "11029", "Koyna Express", "MH-013", "MH-031", "5", "5", "8.40", "1.80", "MH-013|MH-011|MH-014|MH-028|MH-030|MH-031", "CSMT|TNA|PUNE|STR|SLI|KOP", "0|33|192|337|463|518", "0.0|0.5|3.4|6.2|8.6|9.8"},
        {"TRN138", "11030", "Koyna Express Return", "MH-031", "MH-013", "5", "5", "8.15", "1.80", "MH-031|MH-030|MH-028|MH-014|MH-011|MH-013", "KOP|SLI|STR|PUNE|TNA|CSMT", "0|55|181|326|485|518", "0.0|1.1|3.5|6.4|9.2|9.8"},
        {"TRN139", "11027", "Mumbai Solapur Sangli Satara Exp", "MH-013", "MH-028", "5", "5", "23.55", "1.80", "MH-013|MH-011|MH-014|MH-027|MH-030|MH-028", "CSMT|TNA|PUNE|SUR|SLI|STR", "0|33|192|455|643|770", "0.0|0.5|3.4|8.0|11.5|13.8"},
        {"TRN140", "11028", "Satara Sangli Solapur Mumbai Exp", "MH-028", "MH-013", "5", "5", "15.30", "1.80", "MH-028|MH-030|MH-027|MH-014|MH-011|MH-013", "STR|SLI|SUR|PUNE|TNA|CSMT", "0|127|315|578|737|770", "0.0|2.3|5.8|10.4|13.3|13.8"}
    };

    public static void initializeDatabase() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            boolean needRecreate = false;
            try (ResultSet rs = stmt.executeQuery("PRAGMA table_info(stations)")) {
                boolean hasElevM = false;
                while (rs.next()) {
                    if ("elevation_m".equalsIgnoreCase(rs.getString("name"))) {
                        hasElevM = true;
                        break;
                    }
                }
                if (!hasElevM) {
                    needRecreate = true;
                }
            } catch (Exception e) {
                needRecreate = true;
            }

            try (ResultSet rs = stmt.executeQuery("PRAGMA table_info(trains)")) {
                boolean hasTrainNum = false;
                while (rs.next()) {
                    if ("train_number".equalsIgnoreCase(rs.getString("name"))) {
                        hasTrainNum = true;
                        break;
                    }
                }
                if (!hasTrainNum) {
                    needRecreate = true;
                }
            } catch (Exception e) {
                needRecreate = true;
            }

            try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM trains")) {
                if (rs.next() && rs.getInt(1) < 40) {
                    needRecreate = true;
                }
            } catch (Exception e) {
                needRecreate = true;
            }

            if (needRecreate) {
                stmt.execute("DROP TABLE IF EXISTS stations");
                stmt.execute("DROP TABLE IF EXISTS track_edges");
                stmt.execute("DROP TABLE IF EXISTS trains");
                stmt.execute("DROP TABLE IF EXISTS tickets");
                stmt.execute("DROP TABLE IF EXISTS passengers");
            }

            stmt.execute("CREATE TABLE IF NOT EXISTS stations ("
                    + "station_id TEXT PRIMARY KEY, "
                    + "irctc_code TEXT NOT NULL, "
                    + "station_name TEXT NOT NULL, "
                    + "city TEXT NOT NULL, "
                    + "elevation_m REAL NOT NULL, "
                    + "opengl_x REAL NOT NULL, "
                    + "opengl_y REAL NOT NULL)");

            stmt.execute("CREATE TABLE IF NOT EXISTS track_edges ("
                    + "edge_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "from_station_id TEXT NOT NULL, "
                    + "to_station_id TEXT NOT NULL, "
                    + "distance_km INTEGER NOT NULL, "
                    + "travel_time_hr REAL NOT NULL)");

            stmt.execute("CREATE TABLE IF NOT EXISTS trains ("
                    + "train_id TEXT PRIMARY KEY, "
                    + "train_number TEXT NOT NULL, "
                    + "train_name TEXT NOT NULL, "
                    + "source_station_id TEXT NOT NULL, "
                    + "dest_station_id TEXT NOT NULL, "
                    + "total_seats INTEGER NOT NULL, "
                    + "available_seats INTEGER NOT NULL, "
                    + "departure_time REAL NOT NULL, "
                    + "base_fare REAL NOT NULL, "
                    + "route_station_ids TEXT NOT NULL, "
                    + "route_station_codes TEXT NOT NULL, "
                    + "cumulative_distances_km TEXT NOT NULL, "
                    + "cumulative_times_hr TEXT NOT NULL)");

            stmt.execute("CREATE TABLE IF NOT EXISTS passengers ("
                    + "passenger_id TEXT PRIMARY KEY, "
                    + "name TEXT NOT NULL, "
                    + "age INTEGER NOT NULL, "
                    + "gender TEXT NOT NULL, "
                    + "contact_number TEXT NOT NULL)");

            stmt.execute("CREATE TABLE IF NOT EXISTS tickets ("
                    + "ticket_id TEXT PRIMARY KEY, "
                    + "passenger_id TEXT NOT NULL, "
                    + "train_id TEXT NOT NULL, "
                    + "from_station TEXT NOT NULL, "
                    + "to_station TEXT NOT NULL, "
                    + "route_choice TEXT NOT NULL, "
                    + "seat_number TEXT NOT NULL, "
                    + "status TEXT NOT NULL, "
                    + "fare REAL NOT NULL)");

            seedStations(conn);
            seedTrains(conn);
            seedTrackEdgesFromTrains(conn);
            writeTrainRoutesCsv();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void seedStations(Connection conn) throws SQLException {
        try (ResultSet rs = conn.createStatement().executeQuery("SELECT COUNT(*) FROM stations")) {
            if (rs.next() && rs.getInt(1) >= 32) {
                return;
            }
        }
        String sql = "INSERT OR REPLACE INTO stations (station_id, irctc_code, station_name, city, elevation_m, opengl_x, opengl_y) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (String[] st : STATIONS_DATA) {
                pstmt.setString(1, st[0]);
                pstmt.setString(2, st[1]);
                pstmt.setString(3, st[2]);
                pstmt.setString(4, st[3]);
                pstmt.setDouble(5, Double.parseDouble(st[4]));
                pstmt.setDouble(6, Double.parseDouble(st[5]));
                pstmt.setDouble(7, Double.parseDouble(st[6]));
                pstmt.executeUpdate();
            }
        }
    }

    private static void seedTrains(Connection conn) throws SQLException {
        try (ResultSet rs = conn.createStatement().executeQuery("SELECT COUNT(*) FROM trains")) {
            if (rs.next() && rs.getInt(1) >= 40) {
                return;
            }
        }
        String sql = "INSERT OR REPLACE INTO trains (train_id, train_number, train_name, source_station_id, dest_station_id, total_seats, available_seats, departure_time, base_fare, route_station_ids, route_station_codes, cumulative_distances_km, cumulative_times_hr) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (String[] trn : TRAINS_DATA) {
                pstmt.setString(1, trn[0]);
                pstmt.setString(2, trn[1]);
                pstmt.setString(3, trn[2]);
                pstmt.setString(4, trn[3]);
                pstmt.setString(5, trn[4]);
                pstmt.setInt(6, Integer.parseInt(trn[5]));
                pstmt.setInt(7, Integer.parseInt(trn[6]));
                pstmt.setDouble(8, Double.parseDouble(trn[7]));
                pstmt.setDouble(9, Double.parseDouble(trn[8]));
                pstmt.setString(10, trn[9]);
                pstmt.setString(11, trn[10]);
                pstmt.setString(12, trn[11]);
                pstmt.setString(13, trn[12]);
                pstmt.executeUpdate();
            }
        }
    }

    private static void seedTrackEdgesFromTrains(Connection conn) throws SQLException {
        try (ResultSet rs = conn.createStatement().executeQuery("SELECT COUNT(*) FROM track_edges")) {
            if (rs.next() && rs.getInt(1) >= 40) {
                return;
            }
        }
        Set<String> insertedEdges = new HashSet<>();
        String sql = "INSERT INTO track_edges (from_station_id, to_station_id, distance_km, travel_time_hr) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (String[] trn : TRAINS_DATA) {
                String[] ids = trn[9].split("\\|");
                String[] dists = trn[11].split("\\|");
                String[] times = trn[12].split("\\|");
                for (int i = 0; i < ids.length - 1; i++) {
                    String from = ids[i];
                    String to = ids[i + 1];
                    int dist = Math.abs(Integer.parseInt(dists[i + 1]) - Integer.parseInt(dists[i]));
                    double time = Math.abs(Double.parseDouble(times[i + 1]) - Double.parseDouble(times[i]));
                    if (dist == 0) dist = 50;
                    if (time <= 0.0) time = 1.0;

                    String key1 = from + "->" + to;
                    if (!insertedEdges.contains(key1)) {
                        pstmt.setString(1, from);
                        pstmt.setString(2, to);
                        pstmt.setInt(3, dist);
                        pstmt.setDouble(4, time);
                        pstmt.executeUpdate();
                        insertedEdges.add(key1);
                    }

                    String key2 = to + "->" + from;
                    if (!insertedEdges.contains(key2)) {
                        pstmt.setString(1, to);
                        pstmt.setString(2, from);
                        pstmt.setInt(3, dist);
                        pstmt.setDouble(4, time);
                        pstmt.executeUpdate();
                        insertedEdges.add(key2);
                    }
                }
            }
        }
    }

    private static void writeTrainRoutesCsv() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("data/maharashtra_train_routes.csv"))) {
            pw.println("train_id,train_number,train_name,source_station_id,dest_station_id,total_seats,available_seats,departure_time,base_fare,route_station_ids,route_station_codes,cumulative_distances_km,cumulative_times_hr");
            for (String[] trn : TRAINS_DATA) {
                pw.println(String.join(",", trn));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
