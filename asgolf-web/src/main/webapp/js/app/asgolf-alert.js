(function() {
	'use strict';

	angular.module('ASCore') //
	.service('alertService', alertService) //
	.controller('alertCtrl', alertCtrl);

	function alertService() {
		var _s = this;

		var alerts = [];

		_s.alerts = function() {
			return alerts;
		};

		_s.addAlert = function(msg) {
			alerts.push(msg);
		};

		_s.closeAlert = function(index) {
			alerts.splice(index, 1);
		};

		_s.clearAlerts = function() {
			alerts.splice(0, alerts.length);
		};
	}

	function alertCtrl(alertService) {
		var vm = this;

		vm.alerts = alertService.alerts();

		vm.closeAlert = function(index) {
			alertService.closeAlert(index);
		};
	}

})();