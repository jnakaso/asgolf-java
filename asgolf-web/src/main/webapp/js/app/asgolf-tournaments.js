(function() {

	angular.module('ASGolfTournaments', [ 'ngResource', 'ui.bootstrap', 'ASCore', 'ASGolfCourses']) //
	.filter('roundsByFlight', roundsByFlight) // 
	.controller('tournamentsCtrl', tournamentsCtrl) ;

	function roundsByFlight() {
		return function(items, flt) {
			return items == null ? [] : items.filter(function(input) {
				return input.round.flight == flt;
			});
		};
	}

	function tournamentsCtrl(tournamentsService, $routeParams, $location) {
		var vm = this;

		vm.tournament = $routeParams.id == null ? null : tournamentsService.get($routeParams.id);

		vm.edit = function() {
			$location.path('tournament-edit/' +  vm.tournament.id + '/' + vm.tournament.seasonID);
		};
	}

})();