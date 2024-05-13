package in.co.codeplanet.rewi.schedular;

import in.co.codeplanet.rewi.controller.ReWiController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Component
public class TimeOut {
    private static final Logger Logger = LoggerFactory.getLogger(TimeOut.class);
    @Scheduled(cron = " 0 * * * * *")
    public void timeSpent()throws Exception{
        ReWiController.timeOver();
        Logger.info("one minute  completed and entry deleted. ");
    }
}
