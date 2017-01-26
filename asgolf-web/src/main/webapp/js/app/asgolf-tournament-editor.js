(function() {

	angular.module('ASGolfTournaments') //
	.controller('tournamentEditorCtrl', tournamentEditorCtrl)//
	.controller('roundEditorCtrl', roundEditorCtrl) //
	.controller('kpEditorCtrl', kpEditorCtrl);

	function tournamentEditorCtrl(tournamentsService, coursesService, $routeParams, $filter, $location, $uibModal) {
		var vm = this;

		if ($routeParams.id == 0) {
			vm.tournament = tournamentService.create($routeParams.seasonId);
		} else {
			vm.tournament = tournamentsService.get($routeParams.id);
		}

		vm.tournament.$promise.then(function(data) {
			vm.courseId = data.course.id;
		});
		vm.courses = coursesService.query();
		// for datepicker
		vm.status = {
			opened : false
		};

		vm.save = function() {
			tournamentsService.save(vm.tournament);
			$location.path('tournament/' + $routeParams.id);
		};
		vm.cancel = function() {
			$location.path('tournament/' + $routeParams.id);
		};
		vm.openDate = function($event) {
			vm.status.opened = true;
		};

		vm.editRound = function(round) {
			var roundModalInstance = $uibModal.open({
				templateUrl : 'html/roundModal.html',
				controller : 'roundEditorCtrl',
				controllerAs : 'ctrl',
				size : 'lg',
				resolve : {
					tournament : function() {
						return vm.tournament;
					},
					round : function() {
						return angular.copy(round);
					},
					title : function() {
						return 'Edit Round';
					}
				}
			});

			roundModalInstance.result.then(function(uRound) {
				angular.copy(uRound, round);
				tournamentsService.updateTotals(vm.tournament);
			}, function() {
				console.log('Modal dismissed at: ' + new Date());
			});
		};

		vm.newRound = function(round) {
			var roundModalInstance = $uibModal.open({
				templateUrl : 'html/roundModal.html',
				controller : 'roundEditorCtrl',
				controllerAs : 'ctrl',
				size : 'lg',
				resolve : {
					tournament : function() {
						return vm.tournament;
					},
					round : function() {
						return {};
					},
					title : function() {
						return 'New Round';
					}
				}
			});

			roundModalInstance.result.then(function(round) {
				vm.tournament.rounds.push(round);
				tournamentsService.updateTotals(vm.tournament);
			}, function() {
				console.log('Modal dismissed at: ' + new Date());
			});
		};

		vm.deleteRound = function(round) {
			var modalInstance = $uibModal.open({
				templateUrl : 'html/confirmModal.html',
				controller : 'confirmCtrl',
				controllerAs : 'ctrl',
				resolve : {
					title : function() {
						return "Confirm Deletion";
					},
					message : function() {
						return 'Delete ' + round.player.firstName + '\'s round?';
					}
				}
			});
			modalInstance.result.then(function(result) {
				var index = vm.tournament.rounds.indexOf(round);
				vm.tournament.rounds.splice(index, 1);
				tournamentsService.updateTotals(vm.tournament);
			}, function() {
				console.log('Modal dismissed at: ' + new Date());
			});
		}

		vm.editKp = function(kp) {
			var roundModalInstance = $uibModal.open({
				templateUrl : 'html/kpModal.html',
				controller : 'kpEditorCtrl',
				controllerAs : 'ctrl',
				resolve : {
					kp : function() {
						return angular.copy(kp);
					},
					title : function() {
						return 'Edit KP';
					}
				}
			});

			roundModalInstance.result.then(function(updated) {
				angular.copy(updated, kp);
			}, function() {
				console.log('Modal dismissed at: ' + new Date());
			});
		};

		vm.newKp = function(kp) {
			var roundModalInstance = $uibModal.open({
				templateUrl : 'html/kpModal.html',
				controller : 'kpEditorCtrl',
				controllerAs : 'ctrl',
				resolve : {
					kp : function() {
						return {};
					},
					title : function() {
						return 'New KP';
					}
				}
			});

			roundModalInstance.result.then(function(kp) {
				vm.tournament.kps.push(kp);
			}, function() {
				console.log('Modal dismissed at: ' + new Date());
			});
		};

		vm.deleteKp = function(kp) {
			var kpModalInstance = $uibModal.open({
				templateUrl : 'html/confirmModal.html',
				controller : 'confirmCtrl',
				controllerAs : 'ctrl',
				resolve : {
					title : function() {
						return "Confirm Deletion";
					},
					message : function() {
						return 'Delete ' + kp.player.firstName + '\'s hard earned KP?';
					}
				}
			});
			kpModalInstance.result.then(function(result) {
				var index = vm.tournament.kps.indexOf(kp);
				vm.tournament.kps.splice(index, 1);
			}, function() {
				console.log('Modal dismissed at: ' + new Date());
			});
		}
	}

	function roundEditorCtrl($modalInstance, $filter, tournamentsService, playersService, title, tournament, round) {
		var vm = this;

		vm.title = title;
		vm.flights = tournamentsService.getFlights();
		vm.players = playersService.query();
		vm.round = round;

		vm.save = function() {
			$modalInstance.close(vm.round);
		};

		vm.cancel = function() {
			$modalInstance.dismiss('cancel');
		};

		vm.changePlayer = function() {
			vm.round.handicap = tournamentsService.adjustedHandicap(tournament, vm.round.player);
		}

		vm.changeHandicap = function() {
			tournamentsService.updateRoundTotals(vm.round);
		}

		vm.changeScore = function() {
			tournamentsService.updateRoundTotals(vm.round);
		}

	}

	function kpEditorCtrl($modalInstance, tournamentsService, playersService, kp, title) {
		var vm = this;
		vm.title = title;
		vm.flights = tournamentsService.getFlights();
		vm.players = playersService.query();
		vm.holes = [ '1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15', '16', '17', '18' ];
		vm.kp = kp;

		vm.save = function() {
			$modalInstance.close(vm.kp);
		};

		vm.cancel = function() {
			$modalInstance.dismiss('cancel');
		};
	}

})();