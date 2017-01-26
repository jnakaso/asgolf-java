(function() {

	angular.module('ASGolfHome', [ 'ngResource', 'ui.bootstrap', 'ASCore' ]) //
	.service('scheduleService', scheduleService) //
	.service('siteService', siteService) //
	.controller('homeCtrl', homeCtrl);

	function scheduleService($resource) {
		return $resource('/events', {}, {
			query : {
				method : 'GET',
				isArray : false,
				transformResponse : function(data, headersGetter) {
					var x2js = new X2JS();
					var jObjs = x2js.xml_str2json(data);
					return jObjs.Events;
				}
			}
		});
	}

	siteService.$inject = [ '$resource', 'alertService' ];
	function siteService($resource, alertService) {
		var _s = this;

		var _r = $resource('', {}, {
			create : {
				url : 'create-site-defaults',
				method : 'GET',
				isArray : false
			},
			updateSiteDefaults : {
				url : 'update-site-defaults',
				method : 'POST',
				isArray : false
			},
			updateSiteInfo : {
				url : 'update-site-info',
				method : 'POST',
				params : {season:'@season'},
				isArray : false
			}
		});

		_s.create = function(cId) {
			return _r.create();
		};

		_s.update = function(siteDefaults) {
			return _r.updateSiteDefaults(siteDefaults, function(data) {
				alertService.clearAlerts();
				alertService.addAlert({
					type : 'success',
					msg : 'Defaults file created!'
				});
			})
		};

		_s.updateSiteInfo = function(season) {
			return _r.updateSiteInfo({
				'season' : season
			}, function(data) {
				alertService.clearAlerts();
				alertService.addAlert({
					type : 'success',
					msg : season == null ? 'All seasons updated!' : 'Season ' + season + ' updated!'
				});
			})
		};
	}

	homeCtrl.$inject = [ 'seasonsService', 'tournamentsService', 'coursesService', 'siteService', 'scheduleService' ];
	function homeCtrl(seasonsService, tournamentsService, coursesService, siteService, scheduleService) {
		var vm = this;
		vm.siteSeason;

		vm.season;
		vm.seasons = seasonsService.query();
		vm.uploadMethod = 'FILE';
		vm.tournament;
		vm.uploadFile;

		vm.courses = coursesService.query();
		siteService.create().$promise.then(function(data) {
			vm.siteDefaults = data;
		});

		scheduleService.query().$promise.then(function(data) {
			vm.events = data.Event;
		});

		vm.upload = function() {
			if (vm.uploadMethod == 'TEXT') {
				console.log('season=' + vm.season + ' tournament = ' + vm.tournament);
				tournamentsService.import(vm.season, angular.fromJson(vm.tournament));
			}
			if (vm.uploadMethod == 'FILE') {
				var f = document.getElementById('uploadFile').files[0];
				var r = new FileReader();
				r.onloadend = function(e) {
					console.log('season=' + vm.season + ' file = ' + f);
					var data = e.target.result;
					tournamentsService.import(vm.season, angular.fromJson(data));
				}
				r.readAsText(f);
			}
		};

		vm.updateSiteDefaults = function() {
			console.log('update site defaults ' + vm.siteDefaults);
			siteService.update(vm.siteDefaults);
		};

		vm.updateSiteInfo = function() {
			console.log('update site info ' + vm.siteSeason);
			siteService.updateSiteInfo(vm.siteSeason);
		};
	}

})();