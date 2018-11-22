app.controller('loginController',['$scope','loginService',function ($scope,loginService) {

    $scope.userName="";
    $scope.password=""


     $scope.hideErrorMsg=function () {
        $scope.showErrorMsg=false;

     }

     $scope.hideSuccessMsg=function () {
         $scope.successMsg=false;
     }



    $scope.login=function(){

        if(angular.equals($scope.userName,"")){

            $scope.errorMsg="Enter User Name";
            $scope.showErrorMsg=true;
            return;
        }else if(angular.equals($scope.password,"")){
            $scope.errorMsg="Enter  Password Name";
            $scope.showErrorMsg=true;
            return;
        }
      else {
            var user = {
                userName: $scope.userName,
                password: $scope.password
            }

            loginService.login(user).then(function (success) {
                // navigate to the  dashboard screen
                console.log(success)
                if(success.data){
                    //set user object in  service
                    loginService.setUser(success.data)
                    // fire upward event to notify main controller
                    $scope.$emit("userLoggedIn")

                }else{
                    $scope.errorMsg=" Invalid  UserName or Password"
                    $scope.showErrorMsg=true;
                }

            }, function (error) {

                $scope.errorMsg = " Error Occurred !! "
            })

        }
    }
}]);