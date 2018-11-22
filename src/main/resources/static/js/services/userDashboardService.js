app.service('userDashboardService',['$http',function ($http) {

    return{

        getAllUserFiles:function (user) {
            var req={
                method:'POST',
                url:'/iprocessor/user/getUserFiles',
                data:user

            }
            return $http(req);
        },

        deleteFile:function (filePath) {
            var req={
                method:'DELETE',
                url:'/iprocessor/user/deleteFile',
                data:filePath

            }
            return $http(req);
        }

    }
}])