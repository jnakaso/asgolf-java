(function() {
	'use strict';

	angular.module('ASGolf',
			[ 'ngRoute', 'ASGolfHome', 'ASGolfSeasons', 'ASGolfCourses', 'ASGolfPlayers', 'ASGolfTournaments' ]) //
	.config(routing) // 
	.service('clubService', clubService).controller('mainCtrl', mainCtrl);

	routing.$inject = [ '$locationProvider', '$routeProvider' ];
	function routing($locationProvider, $routeProvider) {
		$routeProvider.when('/home', {
			templateUrl : 'html/home.html',
		}).when('/seasons', {
			templateUrl : 'html/seasons.html'
		}).when('/season/:id', {
			templateUrl : 'html/seasons.html'
		}).when('/players', {
			templateUrl : 'html/players.html'
		}).when('/player/:id', {
			templateUrl : 'html/players.html'
		}).when('/courses', {
			templateUrl : 'html/courses.html'
		}).when('/course/:id', {
			templateUrl : 'html/courses.html'
		}).when('/tournament/:id', {
			templateUrl : 'html/tournament.html'
		}).when('/tournament-edit/:id/:seasonId', {
			templateUrl : 'html/tournament-editor.html'
		}).otherwise({
			redirectTo : '/home'
		});
	}

	function clubService($http) {
		this.save = function(callback) {
			$http({
				method : "POST",
				url : "/club/save"
			}).success(callback);
		}
	}

	function mainCtrl(clubService) {
		var vm = this;

		vm.save = function() {
			clubService.save(function(response) {
				console.log(response);
			});
		}
	}

})();