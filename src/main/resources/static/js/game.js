var game = angular.module("ApplicationConfig", []);
game.controller("ApplicationConfigController", function ($scope, $http, $timeout) {

    var stompClient = null;

    setConnectedFalse();
    setIkons();

    var rno;
    var token;
    var symbols;

    function connect() {
        getSessionId();
        var socket = new SockJS('/one-armed-bandit-websocket');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {

            $("#start").prop("disabled", false);
            $("#winLines-select").prop("disabled", false);
            $("#reels-select").prop("disabled", false);
            $("#connect").prop("disabled", true);

            console.log('Connected: ' + frame);
            stompClient.subscribe('/game/start-game/' + gameId, function (message) {
                rno = JSON.parse(message.body).rno;
                token = JSON.parse(message.body).authorizationToken;
                showMessage(JSON.parse(message.body));
            });
            stompClient.subscribe('/game/spin-game/' + gameId, function (message) {
                showMessage(JSON.parse(message.body));
                symbols = JSON.parse(message.body).symbols;
                updateIkons(symbols);
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
        console.log("Disconnected");
    }

    function startGame() {
        if (reelsSelected.length == 3) {
            var initParams = JSON.stringify(
                {'winLinesSelected': winLinesSelected, 'reelsSelected': reelsSelected});
            stompClient.send("/app/start/" + gameId, {}, initParams);
            setConnectedTrue();
        } else {
            alert("Please select exactly 3 numbers of reels!");
            $("#start").prop("disabled", false);
            $("#winLines-select").prop("disabled", false);
            $("#reels-select").prop("disabled", false);
            $("#connect").prop("disabled", true);
        }

        $timeout(function () {
            updateGame()
        }, 1000);
    }

    function spinGame() {
        var bet = document.getElementById("bet").value;
        rno = rno + 1;
        var data = JSON.stringify({'rno': rno, 'bet': bet, 'authorizationToken': token, 'gameId': gameId});
        stompClient.send("/app/spin/" + gameId, {}, data);
        $timeout(function () {
            updateGame()
        }, 1000);

    }

    function endGame() {
        stompClient.send("/app/end/" + gameId, {}, JSON.stringify({'authorizationToken': token, 'gameId': gameId}));
        $timeout(function () {
            updateGame()
        }, 1000);
    }

    function updateGame() {
        stompClient.send("/app/results", {}, JSON.stringify({'gameId': gameId}));
    }


    function showMessage(message) {

        $("#gameId").text(message.gameId);
        $("#token").text(token);
        $("#status").text(message.status);
        $("#message").text(message.message);
        $("#winLines").text(JSON.stringify(message.winLineData));
        $("#symbols").text(JSON.stringify(message.symbols));
        $("#win").text(message.win);
        $("#winValue").text(message.winValue);
        $("#rno").text(message.rno);
        $("#balance").text(message.balance);

        $("#connect").val();
        if (message.status == 'TERMINATED') {
            setIkons();
            setConnectedFalse();
        }
        if (message.status == 'CONFIGURATION_NOT_ACCEPTED') {
            setIkons();
            setConnectedFalse();
        }
        if (message.status == 'UNAUTHORIZED') {
            setIkons();
            setConnectedFalse();
        }

    }

    function updateIkons(symbols) {
        $("#icon1").attr("src", '/img/' + symbols[0][0] + '.png');
        $("#icon2").attr("src", '/img/' + symbols[1][0] + '.png');
        $("#icon3").attr("src", '/img/' + symbols[2][0] + '.png');

        $("#icon4").attr("src", '/img/' + symbols[0][1] + '.png');
        $("#icon5").attr("src", '/img/' + symbols[1][1] + '.png');
        $("#icon6").attr("src", '/img/' + symbols[2][1] + '.png');

        $("#icon7").attr("src", '/img/' + symbols[0][2] + '.png');
        $("#icon8").attr("src", '/img/' + symbols[1][2] + '.png');
        $("#icon9").attr("src", '/img/' + symbols[2][2] + '.png');
    }

    function setIkons() {
        $("#icon1").attr("src", '/img/0.png');
        $("#icon2").attr("src", '/img/0.png');
        $("#icon3").attr("src", '/img/0.png');
        $("#icon4").attr("src", '/img/2.png');
        $("#icon5").attr("src", '/img/2.png');
        $("#icon6").attr("src", '/img/2.png');
        $("#icon7").attr("src", '/img/7.png');
        $("#icon8").attr("src", '/img/7.png');
        $("#icon9").attr("src", '/img/7.png');

    }

    function setConnectedTrue() {
        $("#start").prop("disabled", true);
        $("#winLines-select").prop("disabled", true);
        $("#reels-select").prop("disabled", true);
        $("#connect").prop("disabled", true);
        $("#spin").prop("disabled", false);
        $("#end").prop("disabled", false);
        $("#bet").prop("disabled", false);
    }

    function setConnectedFalse() {
        $("#connect").prop("disabled", false);
        $("#start").prop("disabled", true);
        $("#spin").prop("disabled", true);
        $("#end").prop("disabled", true);
        $("#bet").prop("disabled", true);
        $("#winLines-select").prop("disabled", true);
        $("#reels-select").prop("disabled", true);
    }

    $(function () {
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
            }, 2000);
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

    function getSessionId() {
        $http({
            method: 'GET',
            url: 'gameId'
        }).then(function successCallback(response) {
            gameId = response.data;
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