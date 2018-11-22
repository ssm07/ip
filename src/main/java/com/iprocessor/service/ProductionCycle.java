package com.iprocessor.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * <p> This class provides an API to  run various commands such as pre expiration , post expiration,
 *   report generation. It uses command design pattern to run command.
 *    User can add any number of commands in production cycle.
 * </p>
 * @author  Saurabh Moghe , Abhijeet Sathe
 *
 * */
@Service
public class ProductionCycle {


    public static final String PRE = "pre";
    public static final String POST = "post";
    public static final String REPORT = "report";

    @Autowired
    PreExpirationCommand preExpirationCommand;
    @Autowired
    PostExpirationCommand postExpirationCommand;
    @Autowired
    ReportCommand reportCommand;


    private List<Command> commandList = new ArrayList<>();

    public void takeOrders(List<String> commands, Date startDate, Date endDate, String reportName) {

        for (String command : commands) {

            if (command.toLowerCase().equalsIgnoreCase(PRE)) {
                commandList.add(preExpirationCommand);
            } else if (command.toLowerCase().equalsIgnoreCase(POST)) {
                commandList.add(postExpirationCommand);
            } else if (command.toLowerCase().equalsIgnoreCase(REPORT)) {
                reportCommand.setEndDate(endDate);
                reportCommand.setStartDate(startDate);
                reportCommand.setReportName(reportName);
                commandList.add(reportCommand);
            }
        }
    }


    public void takeOrders(List<String> commands, Date startDate) {
        takeOrders(commands, startDate, null, null);
    }

    public void takeOrders(List<String> commands) {
        takeOrders(commands, null, null, null);
    }


    public void execute() {

        for (Command command : commandList) {
            command.execute();
        }

    }

}
