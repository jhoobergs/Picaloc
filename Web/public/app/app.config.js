;(function() {
    'use strict';
    
    angular.module('picaloc')
        .config(['$locationProvider', '$stateProvider', 'uiGmapGoogleMapApiProvider', config]);
    
    function config($locationProvider, $stateProvider, uiGmapGoogleMapApiProvider) {
        $locationProvider.html5Mode(true);

        $stateProvider
            .state('home', {
                url: '/',
                templateUrl: 'app/overview/overview.html'
            });

        uiGmapGoogleMapApiProvider.configure({
            key: 'AIzaSyAc0dxPV8-Sw-MrP62UXvtYLNshTx5djYk	',
            v: '3.20', //defaults to latest 3.X anyhow
            libraries: 'weather,geometry,visualization,places'
        })
    }
})();