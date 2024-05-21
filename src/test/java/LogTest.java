import com.anos.logger.LogEnginer;
import com.anos.logger.types.EntityEnums;

public class LogTest {

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
            logEnginer.info("Test1");
            logEnginer.info("Test2 dasdasd sad");
            logEnginer.info("asd Test3");
            logEnginer.info("Teasds asdst4");
            logEnginer.error("hata var");
        }
    }
}
