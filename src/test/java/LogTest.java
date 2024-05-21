import com.anos.logger.LogEnginer;
import com.anos.logger.types.EntityEnums;

public class LogTest {

    private static void addLog(LogEnginer logEngine, String message){
        logEngine.info(message);
    }

    public static void main(String[] args) {
        EntityEnums.LoggerLibrary loggerLibrary = EntityEnums.LoggerLibrary.JDK;
        LogEnginer logEnginer = LogEnginer.initialize(LogTest.class, loggerLibrary, null);
        logEnginer.addToThreadContext("userName", "<userName>");
        logEnginer.addToThreadContext("requestId", "<requestId>");
        logEnginer.addToThreadContext("wsSessionToken", "<wsSessionToken>");
        if (args.length > 0) {
            for (String arg : args) {
                logEnginer.info(arg);
            }
        } else {
            for (int i = 0; i < 15000; i++) {
                addLog(logEnginer, "Hello World "+i);
            }
        }
    }
}
