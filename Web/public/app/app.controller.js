;(function() {
    'use strict';

    angular.module('picaloc')
        .controller('appController', ['$scope', '$state', 'locationFactory', appCtrl]);

    function appCtrl($scope, $state, locationFactory) {

        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(function (position) {
                console.log(position);
                locationFactory.setLocation(position.coords.latitude, position.coords.longitude, 'ext');
            });
        }

    }
})();