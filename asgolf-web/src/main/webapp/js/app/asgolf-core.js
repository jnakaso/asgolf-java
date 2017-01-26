(function() {
	'use strict';

	angular.module('ASCore', []) //
	.service('coursesService', coursesService) // 
	.service('tournamentsService', tournamentsService) // 
	.controller('confirmCtrl', confirmCtrl) // 
	.controller('pageLegendController', pageLegendController) // 
	.filter('stringArray', stringArray) //
	.component('asgolfPageLegend', {
		templateUrl : 'html/components/pageLegend.html',
		controller : 'pageLegendController',
		bindings : {
			title : '@',
			altTitle : '@',
			action : '&'
		}
	});

	function stringArray() {
		return function(input, start, end) {
			return input.slice(start, end).join(', ');
		}
	}

	function pageLegendController() {
		var vm = this;
		vm.title;
		vm.altTitle;
	
		vm.onClick = function() {
			vm.action();
		};
	}

	function confirmCtrl($modalInstance, title, message) {
		var vm = this;
		vm.title = title;
		vm.message = message;
		vm.ok = function() {
			$modalInstance.close('success');
		};
		vm.cancel = function() {
			$modalInstance.dismiss('cancel');
		};
	}

	function coursesService($resource, alertService) {
		var _s = this;

		var _r = $resource('', {}, {
			query : {
				url : 'courses',
				method : 'GET',
				isArray : true
			},
			get : {
				url : 'course/:id',
				method : 'GET',
				isArray : false
			},
			save : {
				url : 'course-update',
				method : 'POST',
				isArray : false
			},
			create : {
				url : 'course-create',
				method : 'POST',
				isArray : false
			},
			where : {
				url : 'courses-where',
				method : 'GET',
				params : {},
				isArray : true
			}
		});

		_s.get = function(cId) {
			return _r.get({
				id : cId
			});
		};

		_s.save = function(course) {
			return _r.save(course, function(data) {
				alertService.addAlert({
					type : 'success',
					msg : 'Course saved!'
				});
				return data;
			});
		};

		_s.refresh = function(course) {
			console.log("refresh " + course.id);
			if (course.id == 0) {
				return _s.newCourse();
			} else {
				return _s.get(course.id);
			}
		};

		_s.query = function() {
			return _r.query();
		};

		_s.newCourse = function() {
			return _r.create();
		};

		_s.newTee = function() {
			return {
				id : 0,
				name : 'New Tee',
				direction : '',
				phone : '',
				pars : [],
				handicaps : []
			};
		};

		_s.addTee = function(course, tee) {
			console.log("adding tee " + course.id + ' ' + tee);
			course.tees.push(tee);
		};

		_s.removeTee = function(course, tee) {
			var index = course.tees.indexOf(tee);
			course.tees.splice(index, 1);
		};

		_s.where = function() {
			return _r.where();
		}

	}

	function tournamentsService($resource, alertService) {
		var _s = this;

		var _r = $resource('', {}, {
			query : {
				url : 'tournaments',
				method : 'GET',
				params : {},
				isArray : true
			},
			get : {
				url : 'tournament/:id',
				method : 'GET',
				isArray : false
			},
			save : {
				url : 'tournament-update',
				method : 'POST',
				isArray : false
			},
			import : {
				url : 'tournament-import',
				method : 'POST',
				params : {
					seasonId : '@seasonId',
					tournament : '@tournament'
				},
				isArray : false
			},
			create : {
				url : 'tournament-create',
				method : 'POST',
				isArray : false
			}
		});

		_s.create = function(sId) {
			return _r.create({
				seasonId : sId
			});
		}

		_s.get = function(pId) {
			return _r.get({
				id : pId
			});
		}

		_s.import = function(season, tournament) {
			return _r.import({
				"seasonId" : season.id,
				"tournament" : tournament
			}, function(data) {
				alertService.clearAlerts();
				alertService.addAlert({
					type : 'success',
					msg : 'Tournament imported!'
				});
				return data;
			});
		};

		_s.save = function(tournament) {
			return _r.save(tournament, function(data) {
				alertService.addAlert({
					type : 'success',
					msg : 'Tournament saved!'
				});
				return data;
			});
		};

		_s.query = function() {
			return _r.query();
		};

		_s.newPlayer = function() {
			return _rPlayer.create();
		};

		_s.updateTotals = function(tournament) {
			// tournament.honeyPot = _s.createHoneyPot(tournament.rounds);
			// tournament.winners = _s.createWinners(tournament.rounds);
		};

		_s.createHoneyPot = function(rounds) {
			var purse = 5 * rounds.length;
			var working = angular.copy(rounds);
			var overall = _s.lowest(rounds);

			working = _s.removeAll(working, overall);
			working.sort(_s.frontCmp);

			var front = new Array();
			var low = null;
			for (var i = 0; i < working.length; i++) {
				var round = working[i];
				var total = round.frontNet;
				if (low == null) {
					low = total;
				} else if (low < total) {
					break;
				}
				front.push(round);
			}

			working = _s.removeAll(working, front);
			working.sort(_s.backCmp);

			var back = new Array();
			var low = null;
			for (var i = 0; i < working.length; i++) {
				var round = working[i];
				var total = round.backNet;
				if (low == null) {
					low = total;
				} else if (low < total) {
					break;
				}
				back.push(round);
			}
			return new JSHoneyPot(purse, front, back, overall);
		}

		_s.removeAll = function(start, remove) {
			remove.forEach(function(round) {
				for (i = 0; i < start.length; i++) {
					if (round.player.id == start[i].player.id) {
						start.splice(i, 1);
						break;
					}
				}
			});
			return start;
		};

		_s.lowest = function(rounds) {
			var working = rounds.slice();
			working.sort(_s.totalCmp);
			var lowest = new Array();
			var low = null;
			for (var i = 0; i < working.length; i++) {
				var round = working[i];
				var total = round.totalNet;
				if (low == null) {
					low = total;
				} else if (low < total) {
					break;
				}
				lowest.push(round);
			}
			return lowest;
		};

		_s.totalCmp = function(aRound, bRound) {
			return (aRound.totalNet <= bRound.totalNet) ? -1 : 1;
		};

		_s.frontCmp = function(aRound, bRound) {
			return (aRound.frontNet <= bRound.frontNet) ? -1 : 1;
		};

		_s.backCmp = function(aRound, bRound) {
			return (aRound.backNet <= bRound.backNet) ? -1 : 1;
		};

		var flights = [ '', 'A', 'B' ];
		_s.getFlights = function() {
			return flights;
		}

		_s.adjustedHandicap = function(tournament, player) {
			return Math.round(player.handicap * tournament.slope / 113.0);
		}

		_s.updateRoundTotals = function(round) {
			round.front = round.scores.slice(0, 9).reduce(function(a, b) {
				return a + b
			}, 0);
			round.back = round.scores.slice(9, 18).reduce(function(a, b) {
				return a + b
			}, 0);
			round.total = round.front + round.back;
			round.frontNet = round.front - (round.handicap / 2);
			round.backNet = round.back - (round.handicap / 2);
			round.totalNet = round.total - round.handicap;
		};
	}

})();