(function() {

	angular.module('ASGolfPlayers', [ 'ngResource', 'ui.bootstrap', 'ASCore' ]) //
	.service('playersService', playersService) // 
	.controller('playersCtrl', playersCtrl);

	playersService.$inject = [ '$resource', 'alertService' ];
	function playersService($resource, alertService) {
		var _s = this;

		var _r = $resource('', {}, {
			query : {
				url : 'players',
				method : 'GET',
				params : {},
				isArray : true
			},
			get : {
				url : 'player/:id',
				method : 'GET',
				isArray : false
			},
			save : {
				url : 'player-update',
				method : 'POST',
				isArray : false
			},
			create : {
				url : 'player-create',
				method : 'POST',
				isArray : false
			},
			history : {
				url : 'player-history',
				method : 'GET',
				isArray : true
			},
			seasonSummaries : {
				url : 'player-summaries',
				method : 'GET',
				isArray : true
			}
		});

		_s.get = function(pId) {
			return _r.get({
				id : pId
			});
		}

		_s.save = function(player) {
			return _r.save(player, function(data) {
				alertService.addAlert({
					type : 'success',
					msg : 'Player saved!'
				});
				return data;
			});
		};

		_s.refresh = function(player) {
			console.log("refresh " + player.id);
			if (player.id == 0) {
				return _s.newPlayer();
			} else {
				return _s.get(player.id);
			}
		};

		_s.query = function() {
			return _r.query();
		};

		_s.history = function(id) {
			return _r.history({
				id : id
			});
		};

		_s.seasonSummaries = function(id) {
			return _r.seasonSummaries({
				id : id
			});
		};

		_s.newPlayer = function() {
			return _r.create();
		};
	}

	playersCtrl.$inject = [ 'playersService', '$routeParams' ];
	function playersCtrl(playersService, $routeParams) {
		var vm = this;

		vm.mode = $routeParams.id == null ? null : 'read';
		vm.players = playersService.query();
		vm.player = $routeParams.id == null ? null : playersService.get($routeParams.id);
		vm.hints = $routeParams.hints == null ? {
			name : null,
			active : 'true'
		} : angular.fromJson($routeParams.hints);

		vm.gridSummaries = {
			showGridFooter : true,
			columnDefs : [ {
				name : 'Season',
				field : 'seasonID',
				type : 'number',
				cellClass : 'text-right'
			}, {
				name : 'Attendance',
				field : 'attendance',
				type : 'number',
				cellClass : 'text-right'
			}, {
				name : 'KPs',
				field : 'kps',
				type : 'number',
				cellClass : 'text-right'
			}, {
				name : 'Points',
				field : 'points',
				type : 'number',
				cellClass : 'text-right'
			}, {
				name : 'Earnings',
				field : 'earnings',
				type : 'number',
				cellFilter : 'currency',
				cellClass : 'text-right'
			} ],
			data : $routeParams.id == null ? [] : playersService.seasonSummaries($routeParams.id)
		};

		vm.gridHistories = {
			showGridFooter : true,
			enableFiltering : true,
			columnDefs : [ {
				name : '',
				field : 'accepted',
				cellClass : 'text-center'
			}, {
				name : 'Course',
				field : 'tournament.course.name'
			}, {
				name : 'Date',
				field : 'tournament.date',
				type : 'date',
				cellFilter : 'date',
				cellClass : 'text-right'
			}, {
				name : 'Total',
				field : 'total',
				type : 'number',
				cellClass : 'text-right'
			}, {
				name : 'Adjusted',
				field : 'adjusted',
				type : 'number',
				cellClass : 'text-right'
			} ],
			data : $routeParams.id == null ? [] : playersService.history($routeParams.id)
		};

		vm.filterPlayers = function(player) {
			var sText = vm.hints.name == null ? '' : vm.hints.name.toLowerCase();
			var pFirst = player.firstName.toLowerCase();
			var pLast = player.lastName.toLowerCase();
			var nCheck = sText == '' ? true : (pFirst.indexOf(sText) >= 0 || pLast.indexOf(sText) >= 0);
			var aCheck = vm.hints.active == '' ? true : vm.hints.active == ('' + player.active);
			return nCheck && aCheck;
		};

		vm.newPlayer = function() {
			vm.player = playersService.newPlayer();
			vm.mode = 'edit';
		};

		vm.edit = function() {
			vm.mode = 'edit';
		};

		vm.cancel = function() {
			vm.player = playersService.refresh(vm.player);
			vm.mode = 'read';
		};

		vm.save = function() {
			vm.player = playersService.save(vm.player);
			vm.mode = 'read';
		};

	}

})();