import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HTMLReportGenerator {
    public static void main(String[] args){
        // Can parse commadling arguments for the Product type and the version

        // Reading the Result File
        String jmterLogLocation = "target/logs/result.log";
        String htmlTemplateLocation = "";
        String htmlReportLocation = "target/reports/html/index.html";
        String finalTableString = "";
        String dashboardJSLocation = "target/reports/html/content/js/dashboard.js";

        // Counter to count success counts
        int successCount = 0;
        int FailureCount = 0;

        String [] resultString1 = null;
        String [] samplerString = null;
        try {
            resultString1 = new String(Files.readAllBytes(Paths.get(jmterLogLocation))).split("####");
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Creating the table String
        finalTableString = "<table border=\"1\" style=\"table-layout: fixed; width:100%\" class=\"table table-bordered table-condensed tablesorter \"> " +
                "<col width=\"300px\">\n" +
                "<col width=\"300px\">\n" +
                "<col width=\"300px\">\n" +
                "<col width=\"300px\">\n" +
                "<col width=\"1000px\">\n" +
                "<col width=\"500px\"><tr>\n" +
                "<th>Thread Group Name</th>\n" +
                "<th>Sampler Name</th>\n" +
                "<th>Sampler Status</th>\n" +
                "<th>Response Code</th>\n" +
                "<th>Response Message</th>\n" +
                "<th>Request Details</th>\n" +
                "  </tr>\n";
        for (int i = 0; i < resultString1.length; i++) {

            if(resultString1[i] == null || resultString1[i].equals("")){
                System.out.println("Result Set is Empty, Hence Continuing!!");
                continue;
            }
            samplerString = resultString1[i].split(",");

            if (samplerString[2].equals("Successful")){
                successCount++;
                // We are not including successes in the report
            } else {
                FailureCount++;
                finalTableString=finalTableString+"<tr>";
                for(int j =0; j < samplerString.length; j++){
                    if (j==2 ){
                            finalTableString=finalTableString+"<td bgcolor=\"#FF0000\">" +samplerString[j]+"</td>";
                            continue;
                    }
                    if (j==5){
                            finalTableString=finalTableString+"<td>" +samplerString[j]+"</td>";
                            continue;
                    }
                    finalTableString=finalTableString+"<td>" +samplerString[j]+"</td>";
                    //print(samplerRersults[j]);
                }
                finalTableString=finalTableString+"</tr>";
            }
        }
        finalTableString=finalTableString+"</table>";

        // Creaing the Summary Table
        String summaryTable = "<table class=\"table table-bordered table-condensed tablesorter \">\n" +
                "  <tr>\n" +
                "    <th>Result</th>\n" +
                "    <th>Values</th>\n" +
                "<th>Percentage</th>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td>No of Successful Requests</td>\n" +
                "    <td>"+successCount+"</td>\n" +
                "    <td>"+(Math.round((double)successCount / (successCount+FailureCount)*100))+"%</td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td>No of Failures</td>\n" +
                "    <td>"+FailureCount+"</td>\n" +
                "<td>"+(Math.round((double)FailureCount / (successCount+FailureCount)*100))+"%</td>\n" +
                "  </tr>\n" +
                "<tr>\n" +
                "    <th>Total Requests </th>\n" +
                "    <td>"+(successCount+FailureCount)+"</td>\n" +
                "<td></td>\n" +
                "  </tr>\n" +
                "</table>";

        try {
            String finalReport = new String(Files.readAllBytes(Paths.get(htmlReportLocation)));
            finalReport = finalReport.replace("${content}",finalTableString);
            finalReport = finalReport.replace("${resultsummarytable}",summaryTable);

            // Writing to the File
            Files.write(Paths.get(htmlReportLocation), finalReport.getBytes());

            // Updating the Summary Graph
            String dashboardjs = new String(Files.readAllBytes(Paths.get(dashboardJSLocation)));
            //var data = {"OkPercent": 50.0, "KoPercent": 50.0};
            Double successPercentage = (double)successCount / (successCount+FailureCount);
            dashboardjs = dashboardjs.replace("${requestsummary}","\"OkPercent\": "+((double)successCount / (successCount+FailureCount)*100)+", \"KoPercent\": "+((double)FailureCount / (successCount+FailureCount)*100));
            Files.write(Paths.get(dashboardJSLocation), dashboardjs.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("\n\nTest Run Result Summary\n\nSuccessful Sampler Clount  : " +successCount + " \nSampler Failures : "+FailureCount);
    }
}