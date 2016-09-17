;(function() {
    'use strict';

    angular.module('picaloc')
        .factory('thumbnailsFactory', ['$http', '$q', thumbnailsFac]);

    function thumbnailsFac($http, $q) {
        var location = {
            latitude: 0,
            longitude: 50
        };

        var self = {
            getThumbList: getThumbList
        };

        function getThumbList() {
            var deferred = $q.defer();

            $http({
                method: 'POST',
                url: 'https://picaloc.herokuapp.com/posts/get',
                params: {
                    user_id: 1,
                    location: location
                }
            }).then(function success(response) {
                console.log(response);
                deferred.resolve(response.data);
            });

            return deferred.promise;
        }

        return self;


    }
})();