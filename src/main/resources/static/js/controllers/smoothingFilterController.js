// create the controller and inject Angular's $scope
app.controller('smoothingFilterController', ['$scope', '$window', 'smoothingFilterService', 'loginService','$location',function ($scope, $window, smoothingFilterService,loginService,$location) {
    // create a message to display in our view


    $scope.file = '';
    $scope.showGaussianControls = false;
    $scope.imageObj;
    $scope.resultDiv=false;
    $scope.userObj=null;
     $scope.medianFilterControls=false;
     $scope.bilateralFilterControls=false;
     $scope.averagingFilterControls=false;
     $scope.gaussianKernelSize="-1";
     $scope.customGaussianFilter="";

     //variables for  bilateral filter
    $scope.diameter="";
    $scope.sigmaColor="";
    $scope.sigmaSpace="";

    //variables for averaging filter
    $scope.pointX="";
    $scope.pointY=""
    $scope.kernelWidth="";
    $scope.kernelHeight="";


    $scope.$watch('filterType', function (newVal, oldVal) {

        if (!angular.equals(newVal.value, oldVal.value)) {

            if (angular.equals(newVal.value, 'gaussian')) {
                $scope.hideAllControls();
                //display  drop down  for kernel size
                $scope.showGaussianControls = true;
                
            }else if(angular.equals(newVal.value, 'averaging')){
                $scope.hideAllControls();
                $scope.averagingFilterControls=true;
            }else if(angular.equals(newVal.value, 'bilateral')){
                $scope.hideAllControls();
                $scope.bilateralFilterControls=true;

            }else if(angular.equals(newVal.value, 'median')){
                $scope.hideAllControls();
                $scope.medianFilterControls=true;
            }

        }

    })
    
    $scope.hideAllControls=function () {
        $scope.averagingFilterControls=false;
        $scope.bilateralFilterControls=false;
        $scope.medianFilterControls=false;
        $scope.showGaussianControls = false;
    }


    $scope.email = function () {
        smoothingFilterService.sendEmail($scope.imageObj.destinationPath).then(
            function (success) {
                $scope.showErrorMsg=false;
                $scope.showSuccessMsg=true;
                $scope.successMsg=" Image successfully sent to the "+$scope.userObj.emailId;

            }, function (error) {
                $scope.showErrorMsg=false;
                $scope.showSuccessMsg=true;
                $scope.successMsg=" Image successfully sent to the "+$scope.userObj.emailId;

            }
        )


    }

    $scope.kernelSizeChange=function () {
        if(angular.equals($scope.gaussianKernelSize,'select')){

        }

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

     $scope.doValidation=function () {
         if (angular.equals($scope.filterType.value, 'gaussian')) {

             var number=parseInt($scope.gaussianKernelSize)
             if(number>0){
                 return true;

             }
             else{
                 $scope.showErrorMsg=true;
                 $scope.errorMsg=" Select kernel size ";
                 return false;

             }
         }
         else if(angular.equals($scope.filterType.value, 'bilateral')){
             if(!angular.isNumber(parseInt($scope.diameter))){
                 $scope.displayErrorMsg(" Enter Diameter");
                 return false;
             }
             if(!angular.isNumber(parseInt($scope.sigmaColor))){
                 $scope.displayErrorMsg(" Enter  Sigma Color");
                 return false;
             }
             if(!angular.isNumber(parseInt($scope.sigmaSpace))){
                 $scope.displayErrorMsg(" Enter Sigma Space");
                 return false;
             }

             return true;
         }else if(angular.equals($scope.filterType.value, 'averaging')){

             if(!angular.isNumber(parseInt($scope.kernelWidth))){
                 $scope.displayErrorMsg(" Kernel width should be numeric");
                 return false;
             }
             if(!angular.isNumber(parseInt($scope.kernelHeight))){
                 $scope.displayErrorMsg(" Kernel width should be numeric");
                 return false;
             } if(!angular.isNumber(parseInt($scope.pointX))){
                 $scope.displayErrorMsg(" Kernel width should be numeric");
                 return false;
             } if(!angular.isNumber(parseInt($scope.pointY))){
                 $scope.displayErrorMsg(" Kernel width should be numeric");
                 return false;
             }
             return true

         }else if(angular.equals($scope.filterType.value, 'median')){
            return true
         }else{
             $scope.showErrorMsg=true;
             $scope.errorMsg=" Invalid filter type selected ";
             return false;
         }

     }

     $scope.displayErrorMsg=function (msg) {
         $scope.showSuccessMsg=false;
         $scope.showErrorMsg="true";
         $scope.errorMsg=msg
     }
    $scope.constructImageObject = function () {

        if (angular.equals($scope.filterType.value, "averaging")){
            var obj={filterType: $scope.filterType.value,
            kernelWidth:  $scope.kernelWidth,
            kernelHeight: $scope.kernelHeight,
            pointX: $scope.pointX,
            pointY:$scope.pointY}
            return obj;
        }else if (angular.equals($scope.filterType.value, "bilateral")){

            var obj={
                filterType: $scope.filterType.value,
                    diameter:$scope.diameter,
                sigmaColor:$scope.sigmaColor,
                sigmaSpace:$scope.sigmaSpace

            }
            return obj;
        }
        else if (angular.equals($scope.filterType.value, "gaussian")){

            var obj={
                filterType: $scope.filterType.value,
                kernelWidth:$scope.gaussianKernelSize,
                kernelHeight:$scope.gaussianKernelSize,
                sigma:$scope.sigmaSpace
            }
            return obj;
        }
        else if (angular.equals($scope.filterType.value, "median")){

            var obj={
                filterType: $scope.filterType.value,
                ksize:$scope.gaussianKernelSize

            }
            return obj;
        }
    }

    $scope.generate = function () {
        //do validation
        var formData = new FormData();
        $scope.imageObj = $scope.constructImageObject();
        formData.append("file", $scope.file._file);
        formData.append("imageObj", $scope.imageObj)
        if ($scope.doValidation()) {



            //append kernel size
            smoothingFilterService.filter($scope.file._file, $scope.imageObj).then(function (success) {
                $scope.imageObj = success.data;
                $scope.outputImage = $scope.imageObj.imageAsBase64
                $scope.resultDiv=true;
                $scope.showErrorMsg=false;
            }, function (error) {
                $scope.resultDiv=false;
                $scope.showErrorMsg=true;
                $scope.errorMsg=" Oops Something went wrong !  "
            })

        }


    }
    $scope.filtersForStandardUser = function () {
        return [{name: ' Select ', value: 'select'},
            {name: ' Averaging Filter ', value: 'averaging'},
            {name: ' Median Filter ', value: 'median'}];
    }

    $scope.filtersForPremiumUser = function () {
        var filters = $scope.filtersForStandardUser();
        var filter = {name: " Bilateral Filter ", value: "bilateral"};
        var filter1={name:'Gaussian Filter',value:'gaussian'}
        filters.push(filter);
        filters.push(filter1)
        return filters;
    }

    //filter option
    // for standard user

    $scope.initFilter = function () {
        console.debug(JSON.stringify($scope.user))
        if (!$scope.userObj.premiumUser && !$scope.userObj.admin) {
            $scope.filters = $scope.filtersForStandardUser();

        } else {
            $scope.filters = $scope.filtersForPremiumUser()
        }
        $scope.filterType = $scope.filters[0];
    }

    $scope.init=function () {
     $scope.userObj=  loginService.getUser();
     if($scope.userObj===null){
         $location.path("/iprocessor/login")
     }else{
         $scope.initFilter();
     }
    }

    $scope.init();


}]);
