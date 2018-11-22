app.service('adminDashboardService',['$http',function ($http) {

    return{

        runProductionCycle:function (command) {
            var req={
                method:'POST',
                url:'/iprocessor/admin/runProductionCycle',
                data:command
            }
            return $http(req);
        },

        getReportFile:function(){
            var req={
                method:'GET',
                url:'/iprocessor/admin/getReportFile'
            }
            return $http(req)
        },
        deleteUser:function (user) {
            var req={
                method:'POST',
                url:'/iprocessor/admin/deleteUser',
                data:user

            }
            return $http(req);
        },
        downgradeUser:function (user) {
            var req={
                method:'POST',
                url:'/iprocessor/admin/downgradeUser',
                data:user
            }
            return $http(req);
        },
        searchUsers:function (searchString) {
            var req={
                method:'GET',
                url:'/iprocessor/admin/searchUsers',
                params:{searchString:searchString}
            }
            return $http(req);

        }

    }
}])