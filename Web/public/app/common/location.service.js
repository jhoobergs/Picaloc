;(function() {
    'use strict';

    angular.module('picaloc')
        .factory('locationFactory', ['$rootScope', locationFac]);

    function locationFac($rootScope) {
        var location = {
            latitude: 50.8,
            longitude: 4.4
        };

        var self = {
            setLocation: setLocation,
            getLocation: getLocation
        };

        function setLocation(latitude, longitude, type) {
            location = {
                latitude: latitude,
                longitude: longitude
            };
            if (type === 'ext') {
                $rootScope.$broadcast('locationChange', location);
            }
        }

        function getLocation() {
            return location;
        }
        
        return self;
    }
})();