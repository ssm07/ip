 var app= angular.module('app',['ngRoute','angularFileUpload','ngFileUpload','ui.bootstrap','ui.select','ngAnimate']);

 // configure our routes
 app.config(function($routeProvider, $locationProvider) {
     $routeProvider

     // route for the home page
         .when('/index', {
             templateUrl : 'index.html',
             controller  : 'mainController'
         })
         .when('/iprocessor/signUp',{
             templateUrl:'register.html'
         })
         .when('/iprocessor/login',{
             templateUrl:'login.html',
             controller:'loginController'
         })

         .when('/iprocessor/smoothingFilter',{
             templateUrl:'views/smoothingFilters.html',
             controller:'smoothingFilterController'
         })

         .when('/welcome',{
             templateUrl:'views/welcome.html',
             controller:'welcomeController'
         })
         .when('/iprocessor/adminDashboard',{
             templateUrl:'views/adminDashboard.html',
             controller:'adminDashboardController'
         })
         .when("/iprocessor/userDashboard",{
             templateUrl:'views/userDashboard.html',
             controller:'userDashBoardController'
         })
         .when("/iprocessor/histogramEqualization",{
             templateUrl:'views/histogramEqualization.html',
             controller:'histogramEqualizationController'
         })
         .when("/iprocessor/sharpening",{
             templateUrl:'views/sharpeningFilter.html',
             controller:'sharpeningController'
         })

         .otherwise({
             redirect: '/index'
         })

     ;
     // use the HTML5 History API
     $locationProvider.html5Mode(true);
 });





