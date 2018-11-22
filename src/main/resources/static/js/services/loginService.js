
app.service('loginService',['$log','$http',function ($log,$http) {

    var userObj;

    return {
         isLoggedIn:function()
      {
        var req={
            method:'GET',
            url:'/iprocessor/isLoggedIn'
        }
         return $http(req)
    },


        login:function (user) {
             var req={
                 method:'POST',
                 url:'/iprocessor/login',
                 data:user
             }

             return $http(req)
        },

       logout:function(){

             var req={
                 method:'GET',
                 url:'/iprocessor/logout'
             }
             return $http(req)
       }  ,
        setUser:function (user) {
            userObj=user;
        },
        getUser:function () {
            return userObj
        }
}

}]);