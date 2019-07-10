var ws;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    } else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    ws = new WebSocket('ws://localhost:8080/game');
    ws.onopen = function () {
        var data = JSON.stringify(
            {'winLinesSelected': winLinesSelected, 'reelsSelected': reelsSelected});
        ws.send(data);
    };
    ws.onmessage = function (data) {
        showGreeting(data.data);
    };
    setConnected(true);
}

function disconnect() {
    if (ws != null) {
        ws.close();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    var data = JSON.stringify({'name': $("#name").val()});
    ws.send(data);
}

function showGreeting(message) {
    $("#greetings").append("<tr><td> " + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $("#connect").click(function () {
        connect();
    });
    $("#disconnect").click(function () {
        disconnect();
    });
    $("#send").click(function () {
        sendName();
    });
});

var winLinesSelected;

function selectWinLines() {
    winLinesSelected = [];
    var select = document.getElementById("winLines-select");
    for (var i = 0; i < select.length; i++) {
        if (select.options[i].selected) winLinesSelected.push(select.options[i].index);
    }
    console.log(winLinesSelected);
}

var reelsSelected;

function selectReels() {
    reelsSelected = [];
    var select = document.getElementById("reels-select");
    for (var i = 0; i < select.length; i++) {
        if (select.options[i].selected) reelsSelected.push(select.options[i].index);
    }
    console.log(reelsSelected);
}

var functions = angular.module("AvailableInitConfig", []);
functions.controller("AvailableInitConfigController", function ($scope, $http) {

    $scope.winLines = [];
    $scope.reels = [];

    getWinLines();
    getReels();

    function getWinLines() {
        $http({
            method: 'GET',
            url: 'winLines'
        }).then(function successCallback(response) {
            $scope.winLines = response.data;
            console.log($scope.winLines);
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
            console.log($scope.reels);
        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    }
});

