package utility;

public class DataVariables {

    public static int threadNumber = 100;
    public static int iterationNumber = 200;
    public static String ipAddress = "//localhost";
    public static String port = "8080";
    public static String ip = ipAddress.concat(":").concat(port);
    public static String fileName = "/Users/danni/Desktop/CS6650/Assignment#2/BSDSAssignment2Day2.csv";
    public static String milliseconds = " milliseconds";
    public static String splitStr = ",";

    public static int skierCounts = 40000;
    public static int recordCounts = 800000;
    public static String dayOfRecord = "1";
    public static int tableIndex = 1;
    public static final String xAxis = "Time";
    public static final String yAxis = "Latency";
    public static final int imageHeight = 800;
    public static final int imageWidth = 1200;


    public static final String jedisHost = "localhost";
    public static final int jedisPort = 6379;
    public static int maxBatchSize = 40000;


    public static String GETURL = "SimpleJersey_war/rest/myresource/myvert";
    public static String POSTURL = "SimpleJersey_war/rest/myresource/load";
}
