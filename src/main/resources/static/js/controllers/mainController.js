// create the controller and inject Angular's $scope
app.controller('mainController', ['$scope', 'loginService', '$location', '$rootScope','$http','$templateCache',
    function ($scope, loginService, $location, $rootScope,$http,$templateCache) {
    // create a message to display in our view
    $scope.message = 'Main Controller!';
    $scope.showDashBoardMenu = false;
    $scope.showLoginMenu = true;
    $scope.userObj;
    $scope.showSideBarMenu = false;
    $scope.footerWidth = 'maxWidth';

    $scope.$on("userLoggedIn", function (event, data) {
        console.log("in Main Controller :: userLoggedIn");
        $scope.userObj = loginService.getUser()
        console.log(" first name " + $scope.userObj.firstName)
        $scope.showLoggedInControls();
        var route = $location.path();
        console.debug("requested  route " + route);
        $http.get('/views/payment.html', {cache:$templateCache});
        if ($scope.userObj.admin) {
            $scope.dashboard = '/iprocessor/adminDashboard';
            $location.path("/iprocessor/adminDashboard");
        } else {
            $scope.dashboard = "/iprocessor/userDashboard";
            $location.path("/iprocessor/userDashboard");
        }

    });


    $scope.showLoggedInControls = function () {
        // if logged in  display  dashboard menu,
        $scope.showDashBoardMenu = true;
        $scope.showLoginMenu = false;
        $scope.showSideBarMenu = true;
        $scope.footerWidth = '';
    }

    $scope.hideLoggedInControls = function () {
        $scope.showDashBoardMenu = false;
        $scope.showLoginMenu = true;
        $scope.showSideBarMenu = false;
    }


    loginService.isLoggedIn().then(function (success) {
        $scope.userObj = success.data;
        console.debug(JSON.stringify(success.data))
        if (angular.isDefined($scope.userObj.userName)) {
            console.debug(" first name " + $scope.userObj.firstName);
            loginService.setUser($scope.userObj)
            $rootScope.$broadcast("userLoggedIn")

        } else {
            $location.path("/iprocessor/login")
        }

    }, function (error) {
         console.error(" main controller:: unable to get logged in request");
    })


    $scope.logout = function () {
        console.debug("in side log out function")
        loginService.logout().then(function (success) {
            loginService.setUser(null);
            $scope.hideLoggedInControls();
            $location.path("/")
        }, function (error) {

        })

    }


}]);
