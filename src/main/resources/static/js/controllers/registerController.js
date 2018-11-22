// create the controller and inject Angular's $scope
app.controller('registerController', ['$scope', 'registerService', function ($scope, registerService) {


    $scope.showPaymentModule = false;
    $scope.showSuccessMsg = false;
    $scope.typeofuser = 'standardUser';
    $scope.user;
    $scope.amount;
    $scope.confirmPassword;

    $scope.userName = "";
    $scope.password = "";
    $scope.firstName = "";
    $scope.lastName = "";
    $scope.emailId = "";
    $scope.confirmPassword = "";
    $scope.cardNumber = "";
    $scope.cvv = "";
    $scope.expirationDateAsString = "";
    $scope.userNameAvailable = false;

    $scope.initialize = function () {
        $scope.userName = "";
        $scope.password = "";
        $scope.firstName = "";
        $scope.lastName = "";
        $scope.emailId = "";
        $scope.confirmPassword = "";
        $scope.cardNumber = "";
        $scope.cvv = "";
        $scope.expirationDateAsString = "";
        $scope.userNameAvailable = false;
    }

    $scope.initialize();
    $scope.$watch('typeofuser', function (newVal) {
        if (angular.equals(newVal, 'premiumUser')) {
            console.debug("Premium user is selected >>")
            $scope.showPaymentModule = true;
            $scope.premiumUser = 1;
            $scope.admin = 0;
        } else if (angular.equals(newVal, 'standardUser')) {
            $scope.showPaymentModule = false;
            $scope.premiumUser = 0;
            $scope.admin = 0;
        }

    });

    $scope.$watch('subMonths', function (newVal, oldVal) {
        console.debug("in subMonths watch listener \t new val " + newVal + "\t old val" + oldVal)
        if (newVal !== oldVal) {
            if (newVal == 1) {
                $scope.amount = 3;
            } else if (newVal == 3) {
                $scope.amount = 5
            } else if (newVal == 12) {
                $scope.amount = 10;
            } else {
                console.debug(" Not Supported value of subMonths" + newVal)
            }

        }
    })

    $scope.constructStandardUser = function () {
        console.debug("in construct standard user ")
        var userObj = {
            userName: $scope.userName,
            password: $scope.password,
            firstName: $scope.firstName,
            lastName: $scope.lastName,
            emailId: $scope.emailId,
            premiumUser: $scope.premiumUser,
            admin: $scope.admin

        };
        $scope.user = userObj;
        console.debug(" standard user object \n " + JSON.stringify($scope.user));
    }

    $scope.constructPremiumUser = function () {
        console.debug("in construct premium user function");
        $scope.constructStandardUser();
        $scope.premiumUser=$scope.user;
        $scope.premiumUser["subMonths"] = $scope.subMonths;
        $scope.premiumUser["premiumUser"]=1;
        console.debug(" Premium user object \n " + JSON.stringify($scope.user));
    }

    $scope.constructPaymentDetail = function () {
        console.debug(" in construct payment detail ")
        var payment = {
            userName: $scope.userName,
            cardNumber: $scope.cardNumber,
            cvv: $scope.cvv,
            expirationDateAsString: $scope.expirationDateAsString,
            paymentAmount: $scope.amount
        }
        $scope.payment = payment;
        console.debug(" payment object \n " + JSON.stringify($scope.payment));
    }

    $scope.register = function () {

        console.debug("in register function")
        if (angular.equals($scope.typeofuser, 'standardUser')) {
            $scope.addStandardUser();
        } else if (angular.equals($scope.typeofuser, 'premiumUser')) {
            $scope.addPremiumUser();
        } else {
            $scope.displayErrorMsg(" Error occurred. Invalid type of  user ");
            console.error(" Error occurred : invalid type of user")
        }
    }


    $scope.addStandardUser = function () {
        console.debug("in add Standard user function");
        if ($scope.validateStandardUser()) {
            $scope.constructStandardUser();
            registerService.addUser($scope.user).then(function (sucess) {

                $scope.displaySuccessMsg(" Registration  Successful  ")

            }, function (error) {
                $scope.displayErrorMsg(" Unable to register , please try again")
            })

        }

    }


    $scope.addPremiumUser = function () {
        console.debug("in  add premium user function");
        if ($scope.validatePremiumUser()) {
            $scope.constructPremiumUser();
            $scope.constructPaymentDetail();
            var object = {
                premiumUser: $scope.premiumUser,
                payment: $scope.payment
            }
            registerService.addPremiumUser(object).then(function (sucess) {
                $scope.displaySuccessMsg(" Registration  Successful  ")
            }, function (error) {
                $scope.displayErrorMsg(" Unable to register , please try again")
            })
        }
    }


    $scope.validatePremiumUser = function () {
        if ($scope.validateStandardUser()) {

            if ($scope.cardNumber.length == 0) {
                $scope.displayErrorMsg(" Card Number name cannot be empty.");
                return false;
            }
            if ($scope.cvv.length == 0) {
                $scope.displayErrorMsg(" Security Number(CVV) cannot be empty.");
                return false;
            }
            if ($scope.expirationDateAsString.length == 0) {
                $scope.displayErrorMsg(" Expiration date cannot be empty.");
                return false;
            }

            return true;

        }
        else {
            return false
        }
    }

    $scope.validateStandardUser = function () {
        console.debug(" in validate standard user");
        if ($scope.firstName.length === 0) {
            $scope.displayErrorMsg(" First name cannot be empty.");
            return false;
        }
        if ($scope.lastName.length === 0) {
            $scope.displayErrorMsg("  Last name cannot be empty.");
            return false;
        }
        if ($scope.userName.length < 3) {
            $scope.displayErrorMsg("  User name should be at least 3 character .");
            return false;
        }
        if ($scope.emailId.length == 0) {
            $scope.displayErrorMsg("  EmailId  cannot be empty.");
            return false;
        }
        if ($scope.password.length == 0) {
            $scope.displayErrorMsg(" Password  cannot be empty.")
            return false;
        }

        if ($scope.confirmPassword.length == 0) {
            $scope.displayErrorMsg(" Confirm password  cannot be empty.")
            return false;
        }


        if (!angular.equals($scope.password, $scope.confirmPassword)) {
            $scope.displayErrorMsg(" Password and Confirm password does not match");
            return false;
        }
        if (!$scope.validateEmailId($scope.emailId)) {
            $scope.displayErrorMsg("  Enter valid Email Id");
            return false;
        }
        if (!$scope.userNameAvailable) {
            $scope.displayErrorMsg("User Name is already taken");
            return false;
        }

        return true;

    }

    $scope.validateEmailId = function (email) {
        var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        return re.test(email);
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

    $scope.isUserNameAvailable = function () {
        console.debug("in checkUserName");
        if ($scope.userName.length >= 3) {
            registerService.isUserNameAvailable($scope.userName).then(function (success) {
                if (success.data == false) {
                    $scope.displayErrorMsg("User Name is already taken");
                    $scope.userNameAvailable = false;
                } else {
                    $scope.userNameAvailable = true;
                    $scope.hideAllMsg()
                }
            }, function (error) {

            });
        }
    }

}]);
