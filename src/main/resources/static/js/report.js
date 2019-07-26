var game = angular.module("ReportConfig", []);
game.controller("ReportConfigController", function ($scope, $http, $timeout) {


    $scope.winLines = [];
    $scope.reels = [];

    getWinLines();
    getReels();


    $scope.form = {
        start: 100,
        size: 1000,
        activeReels: [],
        activeWinLines: [],
        startingBalance: 5000,
        bet: 100
    };

    $scope.message = null;

    $scope.submit = function () {
        if (reelsSelected.length > 2){
            $http({
                method: "POST",
                url: 'report',
                data: angular.toJson($scope.form),
                headers: {
                    'Content-Type': 'application/json'
                }
            }).then(function successCallback(response) {
                $scope.message = response.data;
            })
        }else {
            alert("Please select at least 3 numbers of reels!");
        }
    };

    function getWinLines() {
        $http({
            method: 'GET',
            url: 'winLines'
        }).then(function successCallback(response) {
            $scope.winLines = response.data;
        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    }

    function getReels() {
        $http({
            method: 'GET',
            url: 'reels'
        }).then(function successCallback(response) {
            $scope.reels = response.data;
        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    }

    var winLinesSelected = [];
    $scope.selectWinLines = function () {
        winLinesSelected = [];
        var select = document.getElementById("winLines-select");
        for (var i = 0; i < select.length; i++) {
            if (select.options[i].selected) winLinesSelected.push(select.options[i].index);
        }
        console.log('Selected win lines: ' + winLinesSelected);
        $scope.form.activeWinLines = winLinesSelected;
    };

    var reelsSelected = [];
    $scope.selectReels = function () {
        reelsSelected = [];
        var select = document.getElementById("reels-select");
        for (var i = 0; i < select.length; i++) {
            if (select.options[i].selected) reelsSelected.push(select.options[i].index);
        }
        console.log('Selected reels: ' + reelsSelected);
        $scope.form.activeReels = reelsSelected;
    };

});