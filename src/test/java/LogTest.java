import com.anos.logger.LogEngineer;
import com.anos.logger.types.EntityEnums;

public class LogTest {

    private static void addLog(LogEngineer logEngine, String message){
        logEngine.info(message);
    }

    public static void main(String[] args) {
        EntityEnums.LoggerLibrary loggerLibrary = EntityEnums.LoggerLibrary.JDK;
        LogEngineer logEngineer = LogEngineer.initializeJdkLogger(LogTest.class,null);
        logEngineer.addToThreadContext("userName", "<userName>");
        logEngineer.addToThreadContext("requestId", "<requestId>");
        logEngineer.addToThreadContext("wsSessionToken", "<wsSessionToken>");
        if (args.length > 0) {
            for (String arg : args) {
                logEngineer.info(arg);
            }
        } else {
            for (int i = 0; i < 15000; i++) {
                addLog(logEngineer, "Hello World "+i);
            }
        }
    }
}
