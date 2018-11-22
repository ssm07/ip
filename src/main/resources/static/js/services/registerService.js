
app.service('registerService',['$log','$http',function ($log,$http) {

    return {
        addUser:function(user)
        {
            var req = {
                method: 'POST',
                url: '/iprocessor/addUser',
                data:  user
            }
           return $http(req);
        },
        makePayment:function (payment) {
            var req={
                method:'POST',
                url:'/iprocessor/makePayment',
                data:payment
            }
            return $http(req)
        },
        addPremiumUser:function (premiumUser) {
            var req={
                method: 'POST',
                url:'/iprocessor/addPremiumUser',
                data:premiumUser
            }
            return $http(req);
        },
        isUserNameAvailable:function (userName) {
            var req={
                method:'POST',
                url:'/iprocessor/isUserNameAvailable',
                data:userName
            }
            return $http(req)
        },
        updateUser:function(user){
            var req={
                method:'POST',
                url:'/iprocessor/updateUser',
                data:user

            }
        },
        switchToPremiumAccount:function (user) {
            var req = {
                method: 'POST',
                url: '/iprocessor/switchToPremiumAccount',
                data:  user
            }
            return $http(req);
        },
        extendPremiumAccount:function (object) {
            var req = {
                method: 'POST',
                url: '/iprocessor/extendPremiumAccount',
                data:  object
            }
            return $http(req);
        }
    }

}]);