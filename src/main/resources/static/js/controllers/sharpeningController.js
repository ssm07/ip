app.controller('sharpeningController', ['$scope', 'loginService', 'smoothingFilterService', function ($scope, loginService, smoothingFilterService) {

    $scope.user;
    $scope.filters = [];
    $scope.filterType;

    $scope.file = '';
    $scope.resultDiv = false;

    //parameter for canny
    $scope.threshold1;
    $scope.threshold2 ;
    $scope.kernelWidth ;
    $scope.kernelHeight ;


    // parameter for sobel
    $scope.ddepth;
    $scope.dx ;
    $scope.dy ;

    //for laplace
    $scope.ksize ;
    $scope.scale ;
    $scope.delta ;


    $scope.showCannyControls = false;
    $scope.showLaplaceControls = false;
    $scope.showSobelControls = false;


    $scope.initUser = function () {
        if (loginService.getUser() == null) {
            // make rest call
            loginService.isLoggedIn().then(function (success) {
                if (success.data == null) {
                    //forward to the login page
                }
                else {
                    //initialize
                    loginService.setUser(success.data);
                    $scope.user = loginService.getUser()

                }
            })

        } else {
            $scope.user = loginService.getUser();
        }
    }


    $scope.filtersForStandardUser = function () {
        return [{name: ' Select ', value: 'select'},
            {name: ' Laplace Filter ', value: 'laplace'},
            {name: ' Sobel Filter ', value: 'sobel'}];
    }

    $scope.filtersForPremiumUser = function () {
        var filters = $scope.filtersForStandardUser();
        var filter = {name: " Canny Edge Detection ", value: "canny"};
        filters.push(filter);
        return filters;
    }

    //filter option
    // for standard user

    $scope.initFilter = function () {

        console.debug(JSON.stringify($scope.user))
        if (!$scope.user.premiumUser && !$scope.user.admin) {
            $scope.filters = $scope.filtersForStandardUser();

        } else {
            $scope.filters = $scope.filtersForPremiumUser()
        }
    }


    $scope.$watch('filterType', function (newVal, oldVal) {

        console.debug(" old val" + oldVal + " new val" + newVal)
        if (!angular.equals(oldVal.value, newVal.value)) {
            if (angular.equals(newVal.value, "canny")) {
                $scope.hideAllControls();
                $scope.showCannyControls = true;

            } else if (angular.equals(newVal.value, "laplace")) {
                $scope.hideAllControls();
                $scope.showLaplaceControls = true;

            } else if (angular.equals(newVal.value, "sobel")) {
                $scope.hideAllControls();
                $scope.showSobelControls = true;

            } else {
                console.debug(" not supported  filter type" + newVal.value);
            }
        }


    })


    $scope.doValidation = function () {

        if (angular.equals($scope.filterType.value, "canny")) {

            if ($scope.threshold1 === "" || $scope.threshold2 === "") {
                $scope.displayErrorMsg("Enter  threshold values")
                return false
            }
          return true
        } else if (angular.equals($scope.filterType.value, "laplace")) {
            return true;
        } else if (angular.equals($scope.filterType.value, "sobel")) {
            if ( $scope.dx === "" || $scope.dy === "") {
                $scope.displayErrorMsg(" Enter  values for dx and dy.")
                return false;
            }
            return true;

        } else {
            //un supported filter type
            return false;
        }

        return true
    }


    $scope.generate = function () {


        if ($scope.doValidation())

            var formData = new FormData();
        $scope.imageObj = $scope.constructImageObject();
        formData.append("file", $scope.file._file);
        formData.append("imageObj", $scope.imageObj);
        smoothingFilterService.filter($scope.file._file, $scope.imageObj).then(function (success) {
            $scope.imageObj = success.data;
            $scope.outputImage = $scope.imageObj.imageAsBase64;
            $scope.resultDiv = true;
            $scope.showErrorMsg = false;

        }, function (error) {
            $scope.resultDiv = false;
            $scope.showErrorMsg = true;
            $scope.errorMsg = " Oops Something went wrong !  "
        });

    }


    $scope.constructImageObject = function () {

        var obj=null;

        if (angular.equals($scope.filterType.value, "sobel")){
             obj = {
                filterType: $scope.filterType.value,
                dx: $scope.dx,
                dy: $scope.dy,

            }

        }else if(angular.equals($scope.filterType.value, "canny")){

            obj = {
                filterType: $scope.filterType.value,
                threshold1: $scope.threshold1,
                threshold2: $scope.threshold2,
                kernelHeight:$scope.kernelHeight,
                kernelWidth:$scope.kernelWidth

            }
        }else if(angular.equals($scope.filterType.value, "laplace")){
            obj = {
                filterType: $scope.filterType.value,
                scale: $scope.scale,
                delta: $scope.delta,
                ksize:$scope.ksize

            }
        }

        return obj;

    }

    $scope.download = function () {
        smoothingFilterService.downloadImage($scope.imageObj.destinationPath).then(function (data, status, headers, config) {

            var blob = new Blob([data.data], {
                type: $scope.imageObj.mimeType
            });
            saveAs(blob, $scope.imageObj.fileName);
        }, function (error) {
        })
    }

    $scope.email = function () {
        smoothingFilterService.sendEmail($scope.imageObj.destinationPath).then(
            function (success) {
                $scope.showErrorMsg = false;
                $scope.showSuccessMsg = true;
                $scope.successMsg = " Image successfully sent to the " + $scope.userObj.emailId;

            }, function (error) {
                $scope.showErrorMsg = true;
                $scope.showSuccessMsg = false;
                $scope.errorMsg = " Unable to sent email. Please try again"

            }
        )


    }

    $scope.displayErrorMsg = function (msg) {
        $scope.showSuccessMsg = false;
        $scope.showErrorMsg = "true";
        $scope.errorMsg = msg
    }


    $scope.hideAllControls = function () {
        $scope.showCannyControls = false;
        $scope.showLaplaceControls = false;
        $scope.showSobelControls = false;
    }

    $scope.init = function () {

        $scope.initUser();
        $scope.initFilter();
        $scope.filterType = $scope.filters[0];
    }

    $scope.init();

}])