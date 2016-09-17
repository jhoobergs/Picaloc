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
            })

            .state('login', {
                url: '/login',
                templateUrl: 'app/login/login.html'
            });

        uiGmapGoogleMapApiProvider.configure({
            //    key: 'your api key',
            v: '3.20', //defaults to latest 3.X anyhow
            libraries: 'weather,geometry,visualization,places'
        })
    }
})();