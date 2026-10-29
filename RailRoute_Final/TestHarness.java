import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import DS.core.COAProcessorEngine;
import DS.core.RouteManager;
import DS.data_structures.*;
import database.DatabaseInitializer;
import database.DatabaseManager;

public class TestHarness {

    public static void main(String[] args) throws Exception {
        DatabaseInitializer.initializeDatabase();

        RouteManager rm = new RouteManager();
        rm.loadDefaultNetwork();

        saveFile("baseline_outputs/coa_16bit.txt", COAProcessorEngine.run16BitTask1Trace());
        saveFile("baseline_outputs/coa_64bit_pune_csmt.txt", COAProcessorEngine.run64BitLiveRouteTrace(rm, "PUNE", "CSMT"));
        saveFile("baseline_outputs/coa_64bit_pune_ami.txt", COAProcessorEngine.run64BitLiveRouteTrace(rm, "PUNE", "AMI"));
        saveFile("baseline_outputs/coa_co3_trace.txt", COAProcessorEngine.runCo3AddShiftStringTrace(rm, "PUNE", "AMI"));
        saveFile("baseline_outputs/dsa_boundary_tests.txt", WaitingListQueue.runBoundaryCaseTests());
        saveFile("baseline_outputs/rm_distance_matrix.txt", rm.getDistanceMatrixAsString());
        saveFile("baseline_outputs/rm_compare_pune_csmt.txt", rm.compareRoutesAndFares("PUNE", "CSMT", "SL (Sleeper)"));
        saveFile("baseline_outputs/rm_compare_pune_ami.txt", rm.compareRoutesAndFares("PUNE", "AMI", "CC (AC Chair Car)"));

        rm.exportSelectedRouteForOpenGL("PUNE", "CSMT", "SL (Sleeper)", false);
        copyActiveRoute("baseline_outputs/active_route_sample1.txt");

        rm.exportSelectedRouteForOpenGL("PUNE", "AMI", "3A (AC 3-Tier)", true);
        copyActiveRoute("baseline_outputs/active_route_sample2.txt");

        rm.exportSelectedRouteForOpenGL("NGP", "PUNE", "SL (Sleeper)", false);
        copyActiveRoute("baseline_outputs/active_route_sample3.txt");

        saveFile("baseline_outputs/db_summary.txt", DatabaseManager.getDatabaseSummary());

        StringBuilder dsSb = new StringBuilder();
        TrainArray ta = new TrainArray();
        TrainLinkedList tl = new TrainLinkedList();
        DatabaseManager.syncTrainsToDS(ta, tl);

        dsSb.append("TRAIN_ARRAY_DUMP:\n").append(ta.getAllArrayTrainsAsString()).append("\n");
        dsSb.append("QUICKSORT_DUMP:\n").append(ta.getQuickSortedByFareAsString()).append("\n");
        dsSb.append("TRAIN_LL_DUMP:\n").append(tl.displayAllTrainsAsString()).append("\n");
        dsSb.append("SEARCH_DUMP:\n").append(tl.searchTrainAsString("TR01")).append("\n");

        TicketHashTable th = new TicketHashTable();
        th.put("TKT001", "PAX001 | Aarav | PUNE -> CSMT | Rs. 150.0");
        dsSb.append("HASH_TABLE_DUMP:\n").append(th.displayHashTableAsString()).append("\n");
        dsSb.append("HASH_GET_DUMP:\n").append(th.get("TKT001")).append("\n");

        WaitingListQueue wq = new WaitingListQueue();
        wq.enqueue("P01", "Rohan", "TR01", 150.0);
        dsSb.append("QUEUE_DUMP:\n").append(wq.displayQueueAsString()).append("\n");

        CancellationStack cs = new CancellationStack();
        cs.push("TKT001", "P01", "TR01", 150.0);
        dsSb.append("STACK_DUMP:\n").append(cs.displayStackAsString()).append("\n");

        saveFile("baseline_outputs/ds_dump.txt", dsSb.toString());

        System.out.println("ALL_BASELINES_CAPTURED_SUCCESSFULLY");
    }

    private static void saveFile(String path, String content) throws IOException {
        try (FileWriter fw = new FileWriter(path)) {
            fw.write(content);
        }
    }

    private static void copyActiveRoute(String dest) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get("data/active_route.txt"));
        Files.write(Paths.get(dest), bytes);
    }
}
