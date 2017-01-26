var ASGolfTourEditor = angular.module('ASGolfTourEditor', ['ngResource', 'ui.bootstrap']);

ASGolfTourEditor.factory('PlayersService', ['$resource',
  function($resource){
    return $resource('data/players.js', {}, {
      query: {method:'GET', params:{}, isArray:true}
    });
}]);

ASGolfTourEditor.factory('CourseService', ['$resource',
  function($resource){
    return $resource('data/courses.js', {}, {
      query: {method:'GET', params:{}, isArray:true}
    });
}]);

ASGolfTourEditor.filter('compact', function() {
  return function(orig) {
		var tempT = new Tournament();
		tempT.tournament = angular.copy(orig.tournament);
		orig.rounds.forEach(function(oRound) {
			var nRound = angular.copy(oRound);
			nRound.player = oRound.player.name;
			nRound.playerID = oRound.player.id;
			tempT.rounds.push(nRound);
		});
		orig.kps.forEach(function(oKp) {
			var nKp = angular.copy(oKp);
			nKp.player = oKp.player.id;
			tempT.kps.push(nKp);
		});
		
		return angular.toJson(tempT);
	};
})

ASGolfTourEditor.service('TourService', ['$resource', '$window', '$filter', 
		function($resource, $window, $filter) {

	var myService = this;
	var myTournament = angular.fromJson(sessionStorage.tournament);
	var myHoneyPot = new JSHoneyPot(0, [],[],[]);
	var myWinners = [];
	
	this.getTournament = function() {
		if (myTournament == null) {
			myTournament = new Tournament();
		} 
		return myTournament;
	}

	this.getHoneyPot = function() {
		return myHoneyPot;
	}
	
	this.getWinners = function() {
		return myWinners;
	}

	this.newTournament = function(){
		myTournament = new Tournament(); 
		this.updateTotals();
		return myTournament;
	}
	
	this.updateInfo = function (info) {
		myTournament.tournament = info;
		myService.saveTournament();
	}

	this.saveTournament = function() {
		sessionStorage.tournament = angular.toJson(myTournament);
	}
	
	this.sendTournament = function () {
		var orig = myService.getTournament();
		var info = escape('ASGolf : ' + orig.tournament.courseName + ' : ' + $filter('date')(orig.tournament.date, "yyyy-MM-dd"));
		var tour = $filter('compact')(orig);
		$window.location = 'mailto:jnakaso@yahoo.com?Subject=' + info +'&Body=' + tour;
	}
	
	this.addKp = function(kp) {
		myTournament.kps.push(kp);
		myService.saveTournament();
	}

	this.removeKp = function(kp) {
		var index = myTournament.kps.indexOf(kp);
		myTournament.kps.splice(index, 1);
		myService.saveTournament();
	}
	
	this.updateRound = function(oldRound, newRound) {
		angular.copy(newRound, oldRound);
		this.updateTotals();
	}
	
	this.addRound = function(round) {
		myTournament.rounds.push(round);
		this.updateTotals();
	}
	
	this.removeRound = function(round) {
		var index = myTournament.rounds.indexOf(round);
		myTournament.rounds.splice(index, 1);
		this.updateTotals();
	}
	
	this.updateTotals = function () {
		myHoneyPot = this.createHoneyPot(myTournament.rounds);
		myWinners = this.createWinners(myTournament.rounds);
		myService.saveTournament();
	}
	
	this.createPurse = function () {
		var purse =  new Array();
		purse[0]= new JSPurse(9, 40);
		purse[1]= new JSPurse(6, 25);
		purse[2]= new JSPurse(3, 15);
		return purse;
	}
	
	this.createWinners = function(rounds) {
		var winners = new Array();
		
		['A', 'B'].forEach( function(flt) {
			var purse = myService.createPurse();
			var working = rounds.filter(function (el) {
				return el.flight == flt;
			});
			working.sort(CompareByTotal);
	
			var place = 1;
			while (purse.length > 0 && working.length > 0) {
				var left = 0;
				var money = 0;
				var points = 0;
				var winnerRounds = ASGOLF_FindLowest(working);

				left = Math.min(purse.length, winnerRounds.length);
				for (var i=0; i<left; i++) {
					money += purse[i].earnings;
					points += purse[i].points;
				}
				money = money/winnerRounds.length;
				points = points/winnerRounds.length;
				
				winnerRounds.forEach(function(round) {
					var winner = new JSWinner (round, place, points, money);
					winner.flight=flt;
					winners.push(winner);
				});
				place += left;
				purse =	purse.slice(left);
				working = working.slice(left);
			}		
		});
		return winners;
	}
	
	this.selectByFlight = function(round, flt) {
		return round.flight == flt;
	};

	this.createHoneyPot = function(rounds) {
		var purse = 5 * rounds.length;
		var working = angular.copy(rounds);
		var overall = ASGOLF_FindLowest(rounds);
	
		working = removeAll(working, overall);
		working.sort(CompareByFront);
	
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
		
		working = removeAll(working, front);
		working.sort(CompareByBack);
	
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
		return new JSHoneyPot (purse, front, back, overall);
	}

}]);

function removeAll(start, remove) {
    remove.forEach(function (round) {
    	for (i=0; i<start.length;i++){
    		if (round.player.id == start[i].player.id) {
		    	start.splice(i, 1);
		    	break;
		    }
		}
    });
    return start;
}

ASGolfTourEditor.filter('getById', function() {
  return function(input, id) {
    var i=0, len=input.length;
    for (; i<len; i++) {
      if (+input[i].id == +id) {
        return input[i];
      }
    }
    return null;
  }
});
     
function CompareByTotal(aRound, bRound) {
	return (aRound.totalNet <= bRound.totalNet) ? -1 : 1;
}

function CompareByFront(aRound, bRound) {
	return (aRound.frontNet <= bRound.frontNet) ? -1 : 1;
}
function CompareByBack(aRound, bRound) {
	return (aRound.backNet <= bRound.backNet) ? -1 : 1;
}

function ASGOLF_FindLowest(rounds) {
	var working = rounds.slice();
	working.sort(CompareByTotal);
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
}

//CONTROLLERS

ASGolfTourEditor.controller('TourEditorCtrl', ['$scope', '$modal', 'TourService', 'CourseService', 'PlayersService', function ($scope, $modal, TourService, CourseService, PlayersService) {
	
	$scope.selectType='none';
	$scope.deleteMsg = 'Delete Me?';
	$scope.tournament = TourService.getTournament();	
	$scope.honeyPot = TourService.getHoneyPot();	

	$scope.saveTournament = function() {
		TourService.get().$promise.then(function (data) {
			$scope.tournament = data;
		});
	}
	
	$scope.sendTournament = function() {
		TourService.sendTournament();
	}
	
	$scope.selectRound = function(newRound) {
		$scope.selectType = 'round';
		$scope.selectedObject = newRound;
		$scope.deleteMsg = 'Delete ' +  newRound.player.firstName +'\'s round?';
	}
	
	$scope.selectKp = function(newKp) {
		$scope.selectType = 'kp';
		$scope.selectedObject = newKp;
		$scope.deleteMsg = 'Delete ' +  newKp.player +'\'s hard earned KP?';
	}

	$scope.deleteObject = function(){
		if ($scope.selectType == 'kp') {
			TourService.removeKp($scope.selectedObject);
		} else if ($scope.selectType == 'round') {
			TourService.removeRound($scope.selectedObject);
		}
	}
	
	$scope.openTournament = function() {
		var tourModalInstance = $modal.open({
			templateUrl: 'tourInfoModal.html',
			controller: 'TourInfoEditorCtrl',
			resolve: {
				tourInfo: function () {
				  return $scope.tournament.tournament;
				},
				courses: function () {
				  return CourseService.query();
				},
				mode: function() {
					return 'Edit';
				}
			}
		});
	
		tourModalInstance.result.then(function (info) {
			TourService.updateInfo(info);
		}, function () {
			console.log('Modal dismissed at: ' + new Date());
		});
    };
    
    $scope.newTournament = function() {
		var tourModalInstance = $modal.open({
		  templateUrl: 'tourInfoModal.html',
		  controller: 'TourInfoEditorCtrl',
		  resolve: {
			tourInfo: function () {
			  return new TournamentInfo();
			},
			courses: function () {
			  return CourseService.query();
			},
			mode: function() {
				return 'New';
			}
		  }
		});
	
		tourModalInstance.result.then(function (info) {
			TourService.newTournament();
			TourService.updateInfo(info);
		}, function () {
			console.log('Modal dismissed at: ' + new Date());
		});
    };
    
    $scope.editRound = function(selectedRound) {
	 	$scope.players = PlayersService.query();
   		var roundModalInstance = $modal.open({
		  templateUrl: 'roundModal.html',
		  controller: 'RoundEditorCtrl',
		  size: 'lg',
		  resolve: {
		    tournament: function() {
		    	return $scope.tournament;
		    },
			round: function () {
				return selectedRound;
			},
			players: function () {
			  return $scope.players;
			},
			mode: function() {
				return 'Edit';
			}
		  }
		});
	
		roundModalInstance.result.then(function (dict) {
			TourService.updateRound(dict.oldRound, dict.newRound);
			$scope.honeyPot = TourService.getHoneyPot();	
			$scope.tournament.winners = TourService.getWinners();	
		}, function () {
			console.log('Modal dismissed at: ' + new Date());
		});
    };
    
    $scope.newRound = function() {
    	$scope.players = PlayersService.query();
    
		var roundModalInstance = $modal.open({
		  templateUrl: 'roundModal.html',
		  controller: 'RoundEditorCtrl',
		  size: 'lg',
		  resolve: {
		    tournament: function() {
		    	return $scope.tournament;
		    },
			round: function () {
			  return null;
			},
			players: function () {
			  return $scope.players;
			},
			mode: function() {
				return 'New';
			}
		  }
		});
	
		roundModalInstance.result.then(function (dict) {
			TourService.addRound(dict.newRound);
			$scope.honeyPot = TourService.getHoneyPot();	
			$scope.tournament.winners = TourService.getWinners();	
		}, function () {
			console.log('Modal dismissed at: ' + new Date());
		});
    };
	
	$scope.openKp = function() {
		var kpModalInstance = $modal.open({
		  templateUrl: 'kpModal.html',
		  controller: 'KpEditorCtrl',
		  resolve: {
			players: function () {
			  return PlayersService.query();
			}
		  }
		});
	
		kpModalInstance.result.then(function (kp) {
			TourService.addKp(kp);
		}, function () {
		  console.log('Modal dismissed at: ' + new Date());
		});
    };	    
    
}]);

ASGolfTourEditor.controller('RoundEditorCtrl', ['$scope', '$modalInstance', '$filter', 'tournament', 'round', 'players', 'mode'
			, function ($scope, $modalInstance, $filter, tournament, round, players, mode) {
	$scope.mode = mode;
	$scope.flights = ['A', 'B'];
	$scope.players = players;
	if (round == null) {
		$scope.round = new Round();
	} else {
		$scope.round = angular.copy(round);
		players.$promise.then(function() {
			var player = $filter('getById')(players, round.player.id)
			$scope.round.player = player;
		});
	}

	$scope.save = function () {
		$modalInstance.close({oldRound:round, newRound:$scope.round});
	};

	$scope.cancel = function () {
		$modalInstance.dismiss('cancel');
	};	
	
	$scope.$watch("round.player", function(newValue, oldValue) {
		if (newValue != oldValue) {
		    $scope.round.hdcp = Math.round(newValue.handicap * tournament.tournament.slope / 113);
		}
	});
	
	$scope.$watch("round.hdcp", function(newValue, oldValue) {
		if (newValue != oldValue) {
		    $scope.updateTotals($scope.round.holes);
		}
	});
	
	$scope.$watchCollection("round.holes", function(newValue, oldValue) {
		if (newValue != oldValue) {
			$scope.updateTotals(newValue);
		}
	});
	
	$scope.updateTotals = function(newValue) {
		var fr = 0;
		for (i=0;i<9; i++) {
			fr+=newValue[i];
		}
		var bk = 0;
		for (i=9;i<18; i++){
			bk+=newValue[i];
		}
		$scope.round.front = fr;
		$scope.round.back = bk;
		$scope.round.total = fr+bk;
		$scope.round.frontNet = fr-($scope.round.hdcp/2);
		$scope.round.backNet = bk-($scope.round.hdcp/2);
		$scope.round.totalNet = $scope.round.total - $scope.round.hdcp;
	}

}]);

ASGolfTourEditor.controller('KpEditorCtrl', ['$scope', '$modalInstance', 'players', function ($scope, $modalInstance, players) {
	$scope.flights = ['A', 'B'];
	$scope.players = players;
	$scope.k_player;

	$scope.save = function () {
		var kp = new KP($scope.k_player.id, $scope.k_player.firstName + ' ' + $scope.k_player.lastName, $scope.k_hole, $scope.k_flight);
		$modalInstance.close(kp);
	};

	$scope.cancel = function () {
		$modalInstance.dismiss('cancel');
	};	
}]);

ASGolfTourEditor.controller('TourInfoEditorCtrl', ['$scope', '$modalInstance', 'tourInfo', 'courses', 'mode', function ($scope, $modalInstance, tourInfo, courses, mode){
	$scope.t_type = tourInfo.type;
	$scope.t_date = tourInfo.date;
	$scope.t_rating = tourInfo.rating;
	$scope.t_slope = tourInfo.slope;
	
	$scope.courses = courses;
	courses.$promise.then(function(data) {
		for (index = 0; index < data.length; ++index) {
			if (data[index].name == tourInfo.courseName) {
				$scope.course = data[index];
				break;
			}
			$scope.course = null;
		}
	});
	
	$scope.$watch("course", function(newValue, oldValue) {
		if (newValue != oldValue) {
			var tee;
			for (index = 0; index < newValue.tees.length; ++index) {
				tee = newValue.tees[index];
				if (tee.name == 'white') {
					break;
				}
			}
		    $scope.t_rating = tee.rating;
			$scope.t_slope = tee.slope;
		}
	});

	$scope.save = function () {
		var info = new TournamentInfo();
		info.type = $scope.t_type;
		info.courseName = $scope.course.name;
		info.date = $scope.t_date;
		info.rating = $scope.t_rating;
		info.slope = $scope.t_slope;
		$modalInstance.close(info);
	};

	$scope.cancel = function () {
		$modalInstance.dismiss('cancel');
	};	
	
	
}]);

function KP (playerID, playerName, hole, flight) {
	this.playerID = playerID;
	this.player = playerName;
	this.hole = hole;
	this.flight = flight;
}

function Tournament() {
	this.tournament = new TournamentInfo();
	this.rounds = new Array();
	this.kps = new Array();
	this.winners = new Array();
}

function Round() {
	this.player;
	this.flight;
	this.holes = Array.apply(null, new Array(18)).map(Number.prototype.valueOf, 0);
	this.hdcp = 0;
	this.front = 0;
	this.back = 0;
	this.total = 0;
	this.frontNet = 0;
	this.backNet = 0;
	this.totalNet = 0;
}

function TournamentInfo() {
	this.courseName = 'New Tournament';
	this.date = new Date();
	this.type = 'NORMAL';
	this.rating = 70.0;
	this.slope = 113;
}

function JSHoneyPot (purse, front, back, overall) {
	this.purse = purse;
	this.front = front;
	this.back = back;
	this.overall = overall;
}

function JSPurse(points, earnings) {
	this.points = points;
	this.earnings = earnings;
}

function JSWinner (round, place, points, earnings){
	this.round = round;
	this.place = place;
	this.points = points;
	this.earnings = earnings;
}

