/**
 * Copyright (C) 2015 Bonitasoft S.A.
 * Bonitasoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
(function() {

  'use strict';

  angular
    .module('bonitasoft.designer.home')
    .directive('uidImportArtifact', uidImportArtifact);

  function uidImportArtifact() {
    return {
      scope: true,
      templateUrl: 'js/home/import/home-import.html',
      controller: ImportArtifactCtrl,
      controllerAs: 'import'
    };
  }

  function ImportArtifactCtrl($modal, $scope) {
    var vm = this;

    vm.createElement = () =>
      $modal.open({
        templateUrl: 'js/home/create-popup.html',
        controller: 'CreatePopupController',
        controllerAs: 'createCtrl',
        size: 'sm'
      });

    vm.importElement = function() {
      var modalInstance = $modal.open({
        templateUrl: 'js/home/import/import-popup.html',
        controller: 'ImportPopupController',
        controllerAs: 'import',
        resolve: {
          type: () => 'artifact',
          title: () => 'Import a UI Designer artifact'
        }
      });

      modalInstance.result.then((importReport) => (!!importReport) && manageImportReport(importReport)).then($scope.refreshAll);
    };

    function manageImportReport(importReport) {
      return $modal.open({
        templateUrl: 'js/home/import/import-report-popup.html',
        controller: 'ImportReportPopupController',
        controllerAs: 'importReport',
        resolve: {
          importReport: () => importReport
        }
      }).result;
    }
  }

})();