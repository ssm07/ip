
app.service('smoothingFilterService',['$log','$http','$upload',function ($log,$http,$upload) {

    return {
        filter:function(file,imageObj)
        {


           return  $upload.upload({
                url : '/iprocessor/filter',
                file : file,
                data : imageObj,
                method : 'POST'
            });
        },
        downloadImage:function(filePath){
            var req={
                method:'GET',
                url:'/iprocessor/downloadImage',
                params: {filePath: filePath},
                responseType: 'arraybuffer'
            }
            return $http(req)

        },
        sendEmail:function (filePath) {
            var req={
                method:'GET',
                url:'/iprocessor/sendEmail',
                params:{filePath:filePath}
            }
            return $http(req)
        }


    }

}]);