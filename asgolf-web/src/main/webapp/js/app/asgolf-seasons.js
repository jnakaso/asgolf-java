(function() {

	angular.module('ASGolfSeasons', [ 'ngResource', 'ui.bootstrap' ]) //
	.service('seasonsService', seasonsService) // 
	.filter('scoringPolicy', scoringPolicy) //
	.filter('handicapPolicy', handicapPolicy) //
	.controller('seasonsCtrl', seasonsCtrl);

	seasonsService.$inject = [ '$resource', 'alertService' ];
	function seasonsService($resource, alertService) {
		var _s = this;

		var _r = $resource('', {}, {
			query : {
				url : 'seasons',
				method : 'GET',
				params : {},
				isArray : true
			},
			get : {
				url : 'season/:id',
				method : 'GET',
				isArray : false
			},
			updateTotals : {
				url : 'season-update-totals/:id',
				method : 'GET',
				isArray : false
			},
			getSummary : {
				url : 'season-summary/:id',
				method : 'GET',
				isArray : true
			},
			getTournaments : {
				url : 'season-tournaments/:id',
				method : 'GET',
				isArray : true
			},
			save : {
				url : 'season-update',
				method : 'POST',
				isArray : false
			},
			create : {
				url : 'season-create',
				method : 'POST',
				isArray : false
			}
		});

		_s.get = function(sId) {
			return _r.get({
				id : sId
			});
		}

		_s.save = function(season) {
			return _r.save(season, function(data) {
				alertService.addAlert({
					type : 'success',
					msg : 'Season saved!'
				});
				return data;
			});
		};

		_s.updateTotals = function(season) {
			console.log("updateTotals " + season);
			return _r.updateTotals({
				id :season.id
			});
		};

		_s.refresh = function(season) {
			console.log("refresh " + season.id);
			if (season.id == 0) {
				return _s.newSeason();
			} else {
				return _s.get(season.id);
			}
		};

		_s.query = function() {
			return _r.query();
		};

		_s.newSeason = function() {
			return _r.create();
		};

		_s.getTournaments = function(sId) {
			return _r.getTournaments({
				id : sId
			});
		}

		_s.getSummary = function(sId) {
			return _r.getSummary({
				id : sId
			});
		}

	}

	function scoringPolicy() {
		return function(input) {
			if (input == "DEFAULT_10")
				return '$15/$10/$5';
			if (input == "DEFAULT_20")
				return '$20/$15/$10';
			if (input == "DEFAULT_2015")
				return '$40/$25/$15';
			return '';
		}
	}

	function handicapPolicy() {
		return function(input) {
			if (input == "FIVE_OF_TEN")
				return 'Five of Ten';
			if (input == "TEN_OF_TWENTY")
				return 'Ten of Twenty';
			return '';
		}
	}

	seasonsCtrl.$inject = [ 'seasonsService', 'alertService', '$routeParams', '$location' ];
	function seasonsCtrl(seasonsService, alertService, $routeParams, $location) {
		var vm = this;

		vm.mode = $routeParams.id == null ? null : 'read';
		vm.seasons = seasonsService.query();
		vm.season = $routeParams.id == null ? null : seasonsService.get($routeParams.id);
		vm.tournaments = $routeParams.id == null ? null : seasonsService.getTournaments($routeParams.id);
		vm.stats = $routeParams.id == null ? null : seasonsService.getSummary($routeParams.id);

		vm.newSeason = function() {
			vm.season = seasonsService.newSeason();
			vm.mode = 'edit';
		};
		
		vm.newTournament = function() {
			$location.path('tournament-edit/0/' + vm.season.id);
		};
		
		vm.edit = function() {
			vm.mode = 'edit';
		};

		vm.cancel = function() {
			vm.season = seasonsService.refresh(vm.season);
			vm.mode = 'read';
		};

		vm.save = function() {
			vm.season = seasonsService.save(vm.season);
			vm.mode = 'read';
		};

		vm.updateTotals = function() {
			vm.season = seasonsService.updateTotals(vm.season);
			vm.season.$promise.then(function(data) {
		    	vm.stats = seasonsService.getSummary(vm.season.id);
		    	alertService.addAlert({
					type : 'success',
					msg : 'Season Totals Updated!'
				});
		    })
			vm.mode = 'read';
		};

	}

})();