package com.iprocessor.controller;
import com.iprocessor.DTO.CommandDTO;
import com.iprocessor.DTO.FileDTO;
import com.iprocessor.DTO.PremiumUser;
import com.iprocessor.DTO.User;
import com.iprocessor.service.AdminDashBoardService;
import com.iprocessor.service.ProductionCycle;
import com.iprocessor.service.UtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 *  <p> This controller provides an  API for admin functionality</p>
 *
 * @author  Saurabh Moghe, Abhijeet Sathe
 * */

@RestController
public class AdminController {

    @Autowired
    ProductionCycle productionCycle;
    @Autowired
    UtilService utilService;
    @Autowired
    AdminDashBoardService adminDashBoardService;

    @RequestMapping(value = "/iprocessor/admin/runProductionCycle", method = RequestMethod.POST)
    public void runProductionCycle(@RequestBody CommandDTO commandDTO) {
        productionCycle.takeOrders(commandDTO.getCommandList(), commandDTO.getStartDate(), commandDTO.getEndDate(), commandDTO.getReportName());
        productionCycle.execute();
    }

    @RequestMapping(value = "/iprocessor/admin/getReportFile", method = RequestMethod.GET)
    public @ResponseBody
    List<FileDTO> getAllReportFile() {
        return utilService.getAllReportFile();
    }

    @RequestMapping(value = "/iprocessor/admin/deleteUser", method = RequestMethod.POST)
    public void deleteUser(@RequestBody User user) {
        adminDashBoardService.deleteUser(user);
    }

    @RequestMapping(value = "/iprocessor/admin/downgradeUser", method = RequestMethod.POST)
    public void downgradeUser(@RequestBody PremiumUser premiumUser) {
        adminDashBoardService.downgradeUser(premiumUser);
    }

    @RequestMapping(value = "/iprocessor/admin/searchUsers", method = RequestMethod.GET)
    public @ResponseBody
    List<User> searchUsers(@RequestParam String searchString) {
        return adminDashBoardService.searchUsers(searchString);

    }
}
