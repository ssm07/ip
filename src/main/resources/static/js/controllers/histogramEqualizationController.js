app.controller('histogramEqualizationController',['$scope','smoothingFilterService','loginService',function ($scope,smoothingFilterService,loginService) {

    $scope.file = '';
    $scope.resultDiv=false;
    $scope.userObj=loginService.getUser();

    $scope.displayErrorMsg=function (msg) {
        $scope.showSuccessMsg=false;
        $scope.showErrorMsg="true";
        $scope.errorMsg=msg
    }

    $scope.generate = function () {

        var formData = new FormData();
        $scope.imageObj = {
            filterType:"equalization",

        }
        formData.append("file", $scope.file._file);
        formData.append("imageObj", $scope.imageObj);
        smoothingFilterService.filter($scope.file._file, $scope.imageObj).then(function (success) {
            $scope.imageObj = success.data;
            $scope.outputImage = $scope.imageObj.imageAsBase64
            $scope.resultDiv=true;
            $scope.showErrorMsg=false;

        },function (error) {
            $scope.resultDiv=false;
            $scope.showErrorMsg=true;
            $scope.errorMsg=" Oops Something went wrong !  "
        });

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
                $scope.showErrorMsg=false;
                $scope.showSuccessMsg=true;
                $scope.successMsg=" Image successfully sent to the "+$scope.userObj.emailId;

            }, function (error) {
                $scope.showErrorMsg=true;
                $scope.showSuccessMsg=false;
                $scope.errorMsg=" Unable to sent email. Please try again"

            }
        )


    }


    }])
