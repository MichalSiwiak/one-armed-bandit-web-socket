var functions = angular.module("ApplicationConfig", []);
functions.controller("ApplicationConfigController", function ($scope, $http, $timeout) {

    var stompClient = null;

    /*window.onbeforeunload = function () {
        console.log('cokolwiek');
        return "Do you really want to close?";
    };*/

    $("#connect").prop("disabled", false);
    $("#start").prop("disabled", true);
    $("#spin").prop("disabled", true);
    $("#end").prop("disabled", true);
    $("#bet").prop("disabled", true);
    $("#winLines-select").prop("disabled", true);
    $("#reels-select").prop("disabled", true);
    $("#conversation").show();

    /*function setConnected(connected) {

        $("#start").prop("disabled", !connected);
        $("#spin").prop("disabled", !connected);
        $("#connect").prop("disabled", connected);
        $("#end").prop("disabled", !connected);
        $("#bet").prop("disabled", !connected);
        $("#winLines-select").prop("disabled", !connected);
        $("#reels-select").prop("disabled", !connected);
    }*/

    var rno;
    var token;

    function connect() {
        getSessionId();
        var socket = new SockJS('/one-armed-bandit-websocket');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            $("#start").prop("disabled", false);
            $("#winLines-select").prop("disabled", false);
            $("#reels-select").prop("disabled", false);

            console.log('Connected: ' + frame);
            stompClient.subscribe('/game/start-game/' + gameId, function (message) {
                showMessage(JSON.parse(message.body));
                rno = JSON.parse(message.body).rno;
                token = JSON.parse(message.body).authorizationToken;
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

        $("#start").prop("disabled", true);
        $("#winLines-select").prop("disabled", true);
        $("#reels-select").prop("disabled", true);
        $("#connect").prop("disabled", false);
        $("#spin").prop("disabled", true);
        $("#end").prop("disabled", true);
        $("#bet").prop("disabled", true);

        console.log("Disconnected");
    }

    function startGame() {

        $("#start").prop("disabled", true);
        $("#winLines-select").prop("disabled", true);
        $("#reels-select").prop("disabled", true);
        $("#connect").prop("disabled", true);
        $("#spin").prop("disabled", false);
        $("#end").prop("disabled", false);
        $("#bet").prop("disabled", false);
        if (reelsSelected.length == 3) {
            var initParams = JSON.stringify(
                {'winLinesSelected': winLinesSelected, 'reelsSelected': reelsSelected});
            stompClient.send("/app/start/" + gameId, {}, initParams);
            console.log('ok');
        } else {
            alert("Please select exactly 3 numbers of reels!");

            $("#start").prop("disabled", false);
            $("#winLines-select").prop("disabled", false);
            $("#reels-select").prop("disabled", false);
            $("#connect").prop("disabled", false);
            $("#spin").prop("disabled", true);
            $("#end").prop("disabled", true);
            $("#bet").prop("disabled", true);
        }

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
        var status = JSON.stringify(message.status);
        if (status == '"END"') {
            $("#start").prop("disabled", true);
            $("#winLines-select").prop("disabled", true);
            $("#reels-select").prop("disabled", true);
            $("#connect").prop("disabled", false);
            $("#spin").prop("disabled", true);
            $("#end").prop("disabled", true);
            $("#bet").prop("disabled", true);
        }
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
            $timeout(function () {
                disconnect()
            }, 500);
            $("#end").prop("disabled", false);
        });
    });

    var winLinesSelected = [];

    $scope.selectWinLines = function () {
        winLinesSelected = [];
        var select = document.getElementById("winLines-select");
        for (var i = 0; i < select.length; i++) {
            if (select.options[i].selected) winLinesSelected.push(select.options[i].index);
        }
        console.log('Selected win lines: ' + winLinesSelected);
    };

    var reelsSelected = [];
    $scope.selectReels = function () {
        reelsSelected = [];
        var select = document.getElementById("reels-select");
        for (var i = 0; i < select.length; i++) {
            if (select.options[i].selected) reelsSelected.push(select.options[i].index);
        }
        console.log('Selected reels: ' + reelsSelected);
    };


    var gameId;

    $scope.gameId = '';

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