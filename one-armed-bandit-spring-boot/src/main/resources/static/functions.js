var stompClient = null;

/*window.onbeforeunload = function () {
    console.log('cokolwiek');
    return "Do you really want to close?";
};*/

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

var rno;
var token;
function connect() {
    var socket = new SockJS('/one-armed-bandit-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/game/start-game/' + gameId, function (message) {
            showMessage(JSON.parse(message.body));
            rno=JSON.parse(message.body).rno;
            token=JSON.parse(message.body).authorizationToken;
        });
        stompClient.subscribe('/game/spin-game/' + gameId, function (message) {
            showMessage(JSON.parse(message.body));
        });
        stompClient.subscribe('/game/end-game/' + gameId, function (message) {
            showMessage(JSON.parse(message.body));
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function startGame() {
    var initParams = JSON.stringify(
        {'winLinesSelected': winLinesSelected, 'reelsSelected': reelsSelected});
    stompClient.send("/app/start/" + gameId, {}, initParams);
}

function spinGame() {
    var bet = document.getElementById("bet").value;
    rno = rno + 1;
    var data = JSON.stringify({'rno': rno, 'bet': bet, 'authorizationToken': token});
    stompClient.send("/app/spin/" + gameId, {}, data);
}

function endGame() {
    stompClient.send("/app/end/" + gameId, {}, JSON.stringify({'authorizationToken': token}));
}

function showMessage(message) {
    $("#greetings").append("<tr><td>" + JSON.stringify(message) + "</td></tr>");
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
    $("#start").click(function () {
        startGame();
    });
    $("#spin").click(function () {
        spinGame();
    });
    $("#end").click(function () {
        endGame();
    });
});

var winLinesSelected;

function selectWinLines() {
    winLinesSelected = [];
    var select = document.getElementById("winLines-select");
    for (var i = 0; i < select.length; i++) {
        if (select.options[i].selected) winLinesSelected.push(select.options[i].index);
    }
    console.log('Selected win lines: ' + winLinesSelected);
}

var reelsSelected;

function selectReels() {
    reelsSelected = [];
    var select = document.getElementById("reels-select");
    for (var i = 0; i < select.length; i++) {
        if (select.options[i].selected) reelsSelected.push(select.options[i].index);
    }
    console.log('Selected reels: ' + reelsSelected);
}


var gameId;
var functions = angular.module("ApplicationConfig", []);
functions.controller("ApplicationConfigController", function ($scope, $http) {

    $scope.gameId = '';

    getSessionId();

    function getSessionId() {
        $http({
            method: 'GET',
            url: 'gameId'
        }).then(function successCallback(response) {
            $scope.gameId = response.data;
            gameId = $scope.gameId;
        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    }

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
});