app.controller("adminDashboardController", ['$scope', 'loginService', '$location', 'adminDashboardService', 'utilService', function ($scope, loginService, $location, adminDashboardService, utilService) {

    $scope.user;
    $scope.startDate = null;
    $scope.endDate = null;
    $scope.showReportParameter = false;


    $scope.init = function () {
        $scope.user = loginService.getUser();
        if ($scope.user === null) {
            $location.path("/iprocessor/login")
        } else {
            adminDashboardService.getReportFile().then(function (success) {
                $scope.availableFiles = success.data;
            }, function () {

            })
        }
    };


    $scope.availableCommands = [{
        id: 1,
        name: "Pre Expiration Command",
        checked: false,
        value: "pre"
    }, {
        id: 2,
        name: " Post Expiration Command",
        checked: false,
        value: "post"
    }, {
        id: 3,
        name: "  Generate Report ",
        checked: false,
        value: "report"
    }];


    $scope.check = function () {
        if ($scope.availableCommands[2].checked) {
            // report is checked display
            console.debug(" Generate Report checked ");
            $scope.showReportParameter = true;
        } else {
            console.debug(" Generate Report unchecked ");
            $scope.showReportParameter = false;
        }
    }

    $scope.availableFiles = [];

    $scope.runProductionCycle = function () {
        console.debug("in runProductionCycle methods");
        var selectedCommands = []
        for (var i = 0; i < $scope.availableCommands.length; i++) {
            if ($scope.availableCommands[i].checked) {
                selectedCommands.push($scope.availableCommands[i].value)
            }

        }
        console.debug(JSON.stringify(selectedCommands));
        if ($scope.validate(selectedCommands)) {
            var command = {
                commandList: selectedCommands,
                startDate: $scope.dt,
                endDate: $scope.dt2,
                reportName: $scope.reportName
            }
            adminDashboardService.runProductionCycle(command).then(function (success) {
                $scope.hideAllMsg();
                adminDashboardService.getReportFile().then(function (success) {
                    $scope.availableFiles = success.data;
                    $scope.hideAllMsg();
                    $scope.displaySuccessMsg("Production cycle ran successfully  ")
                }, function (error) {
                    $scope.hideAllMsg();
                    $scope.displayErrorMsg(" Unable to fetch report files.")
                })
            }, function (error) {
                $scope.hideAllMsg();
                $scope.displayErrorMsg(" Production Cycle failed.")
            })

        }
    }


    $scope.validate = function (selectedCommands) {
        var result = true;
        if (selectedCommands.length == 0) {
            $scope.displayErrorMsg(" Select at least one command.")
            result = false;
        } else {
            for (var i = 0; i < selectedCommands.length; i++) {
                if (selectedCommands[i] == "report") {
                    if ($scope.reportName.length == 0) {
                        $scope.displayErrorMsg(" Enter Report Name");
                        result = false;
                    }
                }
            }

        }
        return result
    }


    $scope.displayErrorMsg = function (msg) {
        $scope.showSuccessMsg = false;
        $scope.errorMsg = msg;
        $scope.showErrorMsg = true;
        $scope.successMsg = null;
    }

    $scope.displaySuccessMsg = function (msg) {
        $scope.showSuccessMsg = true;
        $scope.errorMsg = null;
        $scope.showErrorMsg = false;
        $scope.successMsg = msg;
    }

    $scope.hideAllMsg = function () {
        $scope.showErrorMsg = false;
        $scope.showSuccessMsg = false
        $scope.errorMsg = null;
        $scope.successMsg = null;
    }
    $scope.init()

}]);


app.controller('productionCycleController', ['$scope', 'adminDashboardService', 'utilService', function ($scope, adminDashboardService, utilService) {
    console.debug("in productionCycleController")
    $scope.reportName = "";
    $scope.dt;
    $scope.dt2;

    $scope.today = function () {
        $scope.dt = new Date();
        $scope.dt2 = new Date();
    };
    $scope.today();

    $scope.clear = function () {
        $scope.dt = null;
    };

    $scope.inlineOptions = {
        customClass: getDayClass,
        minDate: new Date(),
        showWeeks: true
    };
    $scope.dateOptions = {
        formatYear: 'yy',
        maxDate: new Date(2020, 5, 22),
        minDate: new Date(),
        startingDay: 1
    };
    $scope.toggleMin = function () {
        $scope.inlineOptions.minDate = $scope.inlineOptions.minDate ? null : new Date();
        $scope.dateOptions.minDate = $scope.inlineOptions.minDate;
    };
    $scope.toggleMin();
    $scope.open1 = function () {
        $scope.popup1.opened = true;
    };
    $scope.open2 = function () {
        $scope.popup2.opened = true;
    };

    $scope.setDate = function (year, month, day) {
        $scope.dt = new Date(year, month, day);
    };

    $scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
    $scope.format = $scope.formats[0];
    $scope.altInputFormats = ['M!/d!/yyyy'];

    $scope.popup1 = {
        opened: false
    };

    $scope.popup2 = {
        opened: false
    };

    var tomorrow = new Date();
    tomorrow.setDate(tomorrow.getDate() + 1);
    var afterTomorrow = new Date();
    afterTomorrow.setDate(tomorrow.getDate() + 1);

    $scope.events = [
        {
            date: tomorrow,
            status: 'full'
        },
        {
            date: afterTomorrow,
            status: 'partially'
        }
    ];

    function getDayClass(data) {
        var date = data.date,
            mode = data.mode;
        if (mode === 'day') {
            var dayToCheck = new Date(date).setHours(0, 0, 0, 0);

            for (var i = 0; i < $scope.events.length; i++) {
                var currentDay = new Date($scope.events[i].date).setHours(0, 0, 0, 0);

                if (dayToCheck === currentDay) {
                    return $scope.events[i].status;
                }
            }
        }

        return '';
    }


    $scope.runProductionCycle = function () {
        console.debug("in runProductionCycle methods");
        var selectedCommands = []
        for (var i = 0; i < $scope.availableCommands.length; i++) {
            if ($scope.availableCommands[i].checked) {
                selectedCommands.push($scope.availableCommands[i].value)
            }
        }
        console.debug(JSON.stringify(selectedCommands));
        if ($scope.validate(selectedCommands)) {
            var command = {
                commandList: selectedCommands,
                startDate: $scope.dt,
                endDate: $scope.dt2,
                reportName: $scope.reportName
            }
            adminDashboardService.runProductionCycle(command).then(function (success) {
                $scope.hideAllMsg();
                adminDashboardService.getReportFile().then(function (success) {
                    $scope.availableFiles = success.data;
                    $scope.hideAllMsg();
                    $scope.displaySuccessMsg("Production cycle ran successfully  ")
                }, function (error) {
                    $scope.hideAllMsg();
                    $scope.displayErrorMsg(" Unable to fetch report files.")
                })
            }, function (error) {
                $scope.hideAllMsg();
                $scope.displayErrorMsg(" Production Cycle failed.")
            })
        }

    }


    $scope.download = function (index) {
        var filePath = $scope.availableFiles[index].filePath;
        utilService.download(filePath).then(function (data, status, headers, config) {
                var blob = new Blob([data.data], {});
                saveAs(blob, $scope.availableFiles[index].fileName);
            }, function (error) {
                $scope.hideAllMsg()
                $scope.displayErrorMsg(" Unable to download file. ");
            }
        );

    }

    $scope.email = function (index) {
        var filePath = $scope.availableFiles[index].filePath;
        utilService.email(filePath).then(function (success) {
            $scope.hideAllMsg();
            $scope.displaySuccessMsg(" File is sent to registered email id ");

        }, function (error) {
            $scope.hideAllMsg();
            $scope.displaySuccessMsg(" File is sent to registered email id ");


        })
    };

    $scope.delete = function (index) {
        var filePath = $scope.availableFiles[index].filePath;
        utilService.deleteFile(filePath).then(function (success) {
            $scope.availableFiles.splice(index, 1);
            $scope.hideAllMsg();
            $scope.displaySuccessMsg(" File is  deleted ");

        }, function (error) {
            $scope.hideAllMsg();
            $scope.displayErrorMsg(" Unable to  delete file.")
        })

    }

    $scope.validate = function (selectedCommands) {
        var result = true;
        if (selectedCommands.length == 0) {
            $scope.displayErrorMsg(" Select at least one command.")
            result = false;
        } else {
            for (var i = 0; i < selectedCommands.length; i++) {
                if (selectedCommands[i] == "report") {
                    if ($scope.reportName.length == 0) {
                        $scope.displayErrorMsg(" Enter Report Name");
                        result = false;
                    }
                }
            }
        }
        return result
    }
}]);


//controlled for users tab in admin dashboard screen
app.controller('usersController', ['$scope', 'adminDashboardService', 'registerService', function ($scope, adminDashboardService, registerService) {


    $scope.searchString = "";
    $scope.users = [];

    $scope.searchUsers = function () {
        if ($scope.searchString.length < 3) {
            $scope.$parent.displayErrorMsg(" Enter at least 3 character to search");
        } else {
            adminDashboardService.searchUsers($scope.searchString).then(function (success) {
                $scope.users = success.data;
                if ($scope.users.length == 0) {
                    $scope.$parent.displayErrorMsg(" No result found with " + $scope.searchString);
                }
            }, function (error) {
                $scope.$parent.displayErrorMsg(" Error  occurred, please try again. ");
            })

        }
    }

    $scope.deleteUser = function (index) {

        adminDashboardService.deleteUser($scope.users[index]).then(function (success) {
            $scope.$parent.displaySuccessMsg(" User deleted ");
            $scope.users.splice(index, 1)
        }, function (error) {
            $scope.$parent.displayErrorMsg(" Error  occurred, please try again. ");
        })

    }

    $scope.constructDummyPaymentDetail = function (index) {
        console.debug(" in construct payment detail ")
        var payment = {
                userName: $scope.users[index].userName,
                cardNumber: 0000000000000,
                cvv: 000,
                expirationDateAsString: 0000,
                paymentAmount: 0
            }
        ;
        console.debug("dummy payment object \n " + JSON.stringify($scope.payment));
        return payment;

    }

    //this method upgrade user from standard to premium.
    //user will be upgraded for one month period
    $scope.upgradeUser = function (index) {
        var premiumUser = $scope.users[index];
        premiumUser["subMonths"] = 1;
        console.debug(" Premium user object \n " + JSON.stringify($scope.user));
        var payment = $scope.constructDummyPaymentDetail(index)
        var object = {
            premiumUser: premiumUser,
            payment: payment
        }
        registerService.switchToPremiumAccount(object).then(function (success) {
            $scope.$parent.displaySuccessMsg(" User successfully upgraded to premium account.");
            $scope.users[index].premiumUser = 1
        }, function (data) {
            $scope.$parent.displayErrorMsg(" Error  occurred, please try again. ");
        })
    }


    //this method downgrade users from premium account to standard account
    $scope.downgradeUser = function (index) {
        var premiumUser = $scope.users[index];
        adminDashboardService.downgradeUser(premiumUser).then(function (success) {
            $scope.users[index].premiumUser = 0;
            $scope.$parent.displaySuccessMsg(" User successfully downgraded to standard account.");
        }, function (error) {
            $scope.$parent.displayErrorMsg(" Error  occurred, please try again. ");
        })

    }


}]);

