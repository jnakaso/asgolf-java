(function() {
	'use strict';

	angular.module('ASGolfCourses', [ 'ngResource', 'ui.bootstrap', 'ui.grid', 'ASCore' ]) //
	.controller('coursesCtrl', coursesCtrl) //
	.controller('courseTeeModalCtrl', courseTeeModalCtrl);

function coursesCtrl($uibModal, coursesService, $routeParams) {
		var vm = this;
		vm.mode = $routeParams.id == null ? null : 'read';
		vm.hint = $routeParams.hint;
		vm.course = $routeParams.id == null ? null : coursesService.get($routeParams.id);
		vm.courses = coursesService.query();
		vm.where = coursesService.where();

		vm.newCourse = function() {
			vm.course = coursesService.newCourse();
			vm.mode = 'edit';
		};

		vm.edit = function() {
			vm.mode = 'edit';
		};

		vm.cancel = function() {
			vm.course = coursesService.refresh(vm.course);
			vm.mode = 'read';
		};

		vm.save = function() {
			vm.course = coursesService.save(vm.course);
			vm.mode = 'read';
		};

		vm.filterCourses = function(course) {
			var sText = vm.hint == null ? '' : vm.hint.toLowerCase();
			var cName = course.name.toLowerCase();
			return sText == '' ? true : (cName.indexOf(sText) >= 0);
		};

		vm.newTee = function(course) {
			var modalInstance = $uibModal.open({
				templateUrl : 'html/courseTeeModal.html',
				controller : 'courseTeeModalCtrl',
				controllerAs : 'ctrl',
				resolve : {
					course : function() {
						return course;
					},
					tee : function() {
						return coursesService.newTee();
					},
					msg : function() {
						return 'Add tee to ' + course.name;
					}
				}
			});

			modalInstance.result.then(function(map) {
				coursesService.addTee(map.course, map.tee);
			}, function() {
				console.log('Modal dismissed at: ' + new Date());
			});
		};

		vm.editTee = function(course, tee) {
			var modalInstance = $uibModal.open({
				templateUrl : 'html/courseTeeModal.html',
				controller : 'courseTeeModalCtrl',
				controllerAs : 'ctrl',
				resolve : {
					course : function() {
						return course;
					},
					tee : function() {
						return angular.copy(tee);
					},
					msg : function() {
						return 'Update  \'' + tee.name + '\' tee from ' + course.name;
					}
				}
			});

			modalInstance.result.then(function(map) {
				angular.copy(map.tee, tee);
			}, function() {
				console.log('Modal dismissed at: ' + new Date());
			});
		};

		vm.removeTee = function(course, tee) {
			var teeModalInstance = $uibModal.open({
				templateUrl : 'html/confirmModal.html',
				controller : 'confirmCtrl',
				controllerAs : 'ctrl',
				resolve : {
					title : function() {
						return "Confirm Deletion";
					},
					message : function() {
						return 'Delete \'' + tee.name + '\' tee from ' + course.name + '?';
					}
				}
			});

			teeModalInstance.result.then(function(map) {
				coursesService.removeTee(course, tee);
			}, function() {
				console.log('Modal dismissed at: ' + new Date());
			});
		};
	}

	function courseTeeModalCtrl($modalInstance, course, tee, msg) {
		var vm = this;

		vm.msg = msg;
		vm.tee = tee;

		vm.ok = function() {
			$modalInstance.close({
				course : course,
				tee : tee
			});
		};

		vm.cancel = function() {
			$modalInstance.dismiss('cancel');
		};
	}

})();