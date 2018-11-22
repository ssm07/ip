/**
 *  <p>  This  service provides an API  for utility function like
 *     download, email etc.
 *  </p>
 *
 * */
app.service('utilService',['$http',function ($http) {

    return{

        download: function (filePath) {
            var req={
                method:'GET',
                url:'/iprocessor/downloadImage',
                params: {filePath: filePath},
                responseType: 'arraybuffer'
            }
            return $http(req)
        },
        email:function (filePath) {
            var req={
                method:'GET',
                url:'/iprocessor/sendEmail',
                params:{filePath:filePath}
            }
            return $http(req)
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

}]);