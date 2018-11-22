app.controller("userDashBoardController",['$scope','userDashboardService','loginService','registerService','utilService',
    function ($scope,userDashboardService,loginService,registerService,utilService){
    console.debug(" in userDash Controller ")




        $scope.showErrorMsg = false;
        $scope.showSuccessMsg = false
        $scope.errorMsg = null;
        $scope.successMsg = null;
        $scope.premiumUser=false;
        $scope.cardNumber = "";
        $scope.cvv = "";
        $scope.expirationDateAsString = "";
        $scope.subMonths="";
        $scope.payment;
        $scope.cardHolderName="";
        $scope.modalStandardUser=null;
        
        $scope.modalTitle=null;
        

    //information tab

    $scope.editMode=false
    $scope.editFirstName="";
    $scope.editLastName="";
    $scope.editEmailId=""

    $scope.edit=function()
    {
        $scope.editMode=true;

    }

    $scope.clear=function () {
        $scope.editFirstName="";
        $scope.editLastName="";
        $scope.editEmailId=""
    }

   $scope.cancel=function () {
       $scope.editMode=false;
   }

    $scope.save=function () {
         $scope.user.firstName= $scope.editFirstName;
         $scope.user.lastName=$scope.editLastName;
         $scope.user.emailId=$scope.editEmailId;
        registerService.updateUser(user).then(function (success) {
            if(success){
                $scope.displaySuccessMsg(" Updated Successfully.");
            }
        },function(error){
            $scope.displayErrorMsg("Unable to update, please try again ! ")
        });

    }


    /**
     *
     * <p> This function validates premium user </p>
     *
     * */
        $scope.validatePremiumUser = function () {


                if ($scope.cardNumber.length == 0) {
                    $scope.modalDisplayErrorMsg(" Card Number name cannot be empty.");
                    return false;
                }
                if ($scope.cvv.length == 0) {
                    $scope.modalDisplayErrorMsg(" Security Number(CVV) cannot be empty.");
                    return false;
                }
                if ($scope.expirationDateAsString.length == 0) {
                    $scope.modalDisplayErrorMsg(" Expiration date cannot be empty.");
                    return false;
                }
                if(!angular.isNumber($scope.cardNumber)){
                    $scope.modalDisplayErrorMsg(" Card number should be numeric. ");
                    return false;
                }
                if(!angular.isNumber($scope.cvv)){
                   $scope.modalDisplayErrorMsg(" CVV should be numeric. ");
                   return false;
                }
                return true;
        }

        $scope.constructPaymentDetail = function () {
            console.debug(" in construct payment detail ")
            var payment = {
                userName: $scope.user.userName,
                cardNumber: $scope.cardNumber,
                cvv: $scope.cvv,
                expirationDateAsString: $scope.expirationDateAsString,
                paymentAmount: $scope.amount
            }
            $scope.payment = payment;
            console.debug(" payment object \n " + JSON.stringify($scope.payment));
        }


        /**
         *  <p> This function  upgrades standard user to premium user.</p>
         * */
        $scope.switchToPremiumAccount=function(){

        if($scope.validatePremiumUser()){
            $scope.premiumUser=$scope.user;
            $scope.premiumUser["subMonths"] = $scope.subMonths;
            console.debug(" Premium user object \n " + JSON.stringify($scope.user));
            $scope.constructPaymentDetail()
            var object = {
                premiumUser: $scope.premiumUser,
                payment: $scope.payment
            }
            registerService.switchToPremiumAccount(object).then(function (success) {

                $scope.modalDisplaySuccessMsg(" User successfully upgraded to premium account.");

            },function (data) {
                $scope.modalDisplaySuccessMsg(" User successfully upgraded to premium account.");

            })

        }

    }


    $scope.extendPremiumAccount=function () {
        if($scope.validatePremiumUser()){
            $scope.premiumUserObj=$scope.user;
            $scope.premiumUserObj["newSubMonths"] = $scope.subMonths;
            console.debug(" Premium user object \n " + JSON.stringify($scope.premiumUserObj));
            $scope.constructPaymentDetail()
            var object = {
                premiumUser: $scope.premiumUser,
                payment: $scope.payment
            }
            registerService.extendPremiumAccount(object).then(function (success) {

            },function (data) {

            })
        }
    }

    //ends here

    //userFiles code 
    
    $scope.files=[];
    $scope.user=null;
    
    $scope.delete=function (index) {
        console.debug("in delete function , index"+index);
        userDashboardService.deleteFile($scope.files[index].filePath).then(function (succes) {
            $scope.hideAllMsg()
            if(succes.data) {
                $scope.files.splice(index, 1);
                $scope.displaySuccessMsg (" File deleted successfully");
            }else{

                $scope.displayErrorMsg(" Unable to delete file. ");
            }
        },function (error) {
            $scope.displayErrorMsg(" Unable to delete file. ");
        })
        
        
    }
    
    
    $scope.download=function (index) {
        console.debug(" in download function , index "+index)
        utilService.download($scope.files[index].filePath).then(function (data, status, headers, config) {
            var blob = new Blob([data.data], {
                type: $scope.files[index].mimeType
            });
            saveAs(blob, $scope.files[index].fileName);
        },function () {
            $scope.hideAllMsg()
            $scope.displayErrorMsg(" Unable to download file. ");
        })
        
    }
    
    $scope.email=function (index) {

        utilService.email($scope.files[index].filePath).then(function (success) {
            $scope.hideAllMsg();
            $scope.displaySuccessMsg(" File emailed to the "+$scope.user.emailId)

        },function (error) {
            $scope.hideAllMsg();
            $scope.displayErrorMsg(" Unable to email file");
        })
    }
    
    

    

    $scope.getUserFiles=function(){

        userDashboardService.getAllUserFiles($scope.user).then(function (success) {
            $scope.files=success.data
            for(var i=0;i<$scope.files.length;i++){
                $scope.files[i]["formattedSize"]=niceBytes($scope.files[i].size)
            }
        },function (error) {
           console.error(" error while getting user files !!")
        })
    }

    //ends here




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



        // success and error message on modal

        $scope.modalDisplayErrorMsg = function (msg) {
            $scope.modalshowSuccessMsg = false;
            $scope.modalerrorMsg = msg;
            $scope.modalshowErrorMsg = true;
            $scope.modalsuccessMsg = null;
        }

        $scope.modalDisplaySuccessMsg = function (msg) {
            $scope.modalshowSuccessMsg = true;
            $scope.modalerrorMsg = null;
            $scope.modalshowErrorMsg = false;
            $scope.modalsuccessMsg = msg;

        }

        $scope.modalHideAllMsg = function () {
            $scope.modalshowErrorMsg = false;
            $scope.modalshowSuccessMsg = false
            $scope.modalerrorMsg = null;
            $scope.modalsuccessMsg = null;
        }


        //

        const units = ['bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];

        function niceBytes(x){
            var l = 0, n = parseInt(x, 10) || 0;
            while(n >= 1024 && ++l)
                n = n/1024;
            return(n.toFixed(n >= 10 || l < 1 ? 0 : 1) + ' ' + units[l]);
        }
    
    
        $scope.setModal=function () {
            if($scope.user.premiumUser) {
                $scope.modalTitle = "Extend Premium Account";
                $scope.modalStandardUser=false;
                $scope.expirationDate=$scope.user.premiumUserExpirationDate;
            }else{
                $scope.modalTitle ="Switch to Premium Account";
                $scope.modalStandardUser=true;
            }
        }
    //initialization code 
    $scope.init= function () {
        $scope.user=loginService.getUser();
        if($scope.user!==null){
            loginService.isLoggedIn().then(function (success) {
                loginService.setUser(success.data);
                $scope.user=loginService.getUser();
                $scope.user.availableSpace=niceBytes($scope.user.resourceUsage);
                $scope.getUserFiles();
                $scope.setModal()
            },function (error) {
                console.error("error  while getting logged in user !")
            })
        }else{
            $scope.user.availableSpace=niceBytes($scope.user.resourceUsage);
            $scope.getUserFiles();
            $scope.setModal()
        }
    }
    
    $scope.init();
    
}]);


app.controller('userInformationController',['$scope','$uibModal',function ($scope,$uibModal) {
    
    
    
    $scope.openModal=function () {

        var pc=this;

      pc.data=$scope.user;
        var modalInstance = $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/views/paymentModal.html',
            controller: 'ModalInstanceCtrl',
            controllerAs: 'pc',
            size: 'lg',
            resolve: {
                data: function () {
                    return pc.data;
                }
            }
        });

        modalInstance.result.then(function () {
            alert("now I'll close the modal");
        });
    }
    
}]);


app.controller('ModalInstanceCtrl', function ($uibModalInstance, data) {
    var pc = this;
    pc.data = data;

    console.log(JSON.stringify(pc.data))

    pc.ok = function () {
        //{...}
        alert("You clicked the ok button.");
        $uibModalInstance.close();
    };

    pc.cancel = function () {
        //{...}
        alert("You clicked the cancel button.");
        $uibModalInstance.dismiss('cancel');
    };
});