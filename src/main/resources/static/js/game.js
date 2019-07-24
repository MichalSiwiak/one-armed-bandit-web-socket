var game = angular.module("ApplicationConfig", []);
game.controller("ApplicationConfigController", function ($scope, $http, $timeout) {

    var stompClient = null;

    setConnectedFalse();
    set3Ikons();
    set4Ikons();
    set5Ikons();

    var rno;
    var token;
    var symbols;
    var lackOfFunds = false;

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
                var balance = JSON.parse(message.body).balance;
                if (symbols.length == 3) {
                    updateIconsForThreeReels(symbols);
                } else if (symbols.length == 4) {
                    updateIconsForFourReels(symbols);
                } else {
                    updateIconsForFiveReels(symbols);
                }

                if (balance <= 0) {
                    endGame();
                    lackOfFunds = true;
                }

            });
            stompClient.subscribe('/game/end-game/' + gameId, function (message) {
                showMessage(JSON.parse(message.body));
                if (lackOfFunds) {
                    $("#message").text("Game is closed due to lack of funds");
                }
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
        if (reelsSelected.length > 2) {
            var initParams = JSON.stringify(
                {'winLinesSelected': winLinesSelected, 'reelsSelected': reelsSelected});
            stompClient.send("/app/start/" + gameId, {}, initParams);
            if (reelsSelected.length == 3) {
                $("#reels3").show();
                $("#reels4").hide();
                $("#reels5").hide();
            } else if (reelsSelected.length == 4) {
                $("#reels3").hide();
                $("#reels4").show();
                $("#reels5").hide();
            } else {
                $("#reels3").hide();
                $("#reels4").hide();
                $("#reels5").show();
            }
            setConnectedTrue();
        } else {
            alert("Please select at least 3 numbers of reels!");
            $("#reels3").hide();
            $("#reels4").hide();
            $("#reels5").hide();
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
        $("#winValue").text(message.winValue);
        $("#win").text(message.win);
        $("#rno").text(message.rno);
        $("#balance").text(message.balance);

        $("#connect").val();
        if (message.status == 'TERMINATED') {
            $("#reels3").hide();
            $("#reels4").hide();
            $("#reels5").hide();
            setConnectedFalse();
        }
        if (message.status == 'CONFIGURATION_NOT_ACCEPTED') {
            $("#reels3").hide();
            $("#reels4").hide();
            $("#reels5").hide();
            setConnectedFalse();
        }
    }

    function updateIconsForThreeReels(symbols) {
        $("#3icon1").attr("src", '/img/' + symbols[0][0] + '.png');
        $("#3icon2").attr("src", '/img/' + symbols[1][0] + '.png');
        $("#3icon3").attr("src", '/img/' + symbols[2][0] + '.png');

        $("#3icon4").attr("src", '/img/' + symbols[0][1] + '.png');
        $("#3icon5").attr("src", '/img/' + symbols[1][1] + '.png');
        $("#3icon6").attr("src", '/img/' + symbols[2][1] + '.png');

        $("#3icon7").attr("src", '/img/' + symbols[0][2] + '.png');
        $("#3icon8").attr("src", '/img/' + symbols[1][2] + '.png');
        $("#3icon9").attr("src", '/img/' + symbols[2][2] + '.png');
    }

    function updateIconsForFourReels(symbols) {
        $("#4icon1").attr("src", '/img/' + symbols[0][0] + '.png');
        $("#4icon2").attr("src", '/img/' + symbols[1][0] + '.png');
        $("#4icon3").attr("src", '/img/' + symbols[2][0] + '.png');
        $("#4icon4").attr("src", '/img/' + symbols[3][0] + '.png');

        $("#4icon5").attr("src", '/img/' + symbols[0][1] + '.png');
        $("#4icon6").attr("src", '/img/' + symbols[1][1] + '.png');
        $("#4icon7").attr("src", '/img/' + symbols[2][1] + '.png');
        $("#4icon8").attr("src", '/img/' + symbols[3][1] + '.png');

        $("#4icon9").attr("src", '/img/' + symbols[0][2] + '.png');
        $("#4icon10").attr("src", '/img/' + symbols[1][2] + '.png');
        $("#4icon11").attr("src", '/img/' + symbols[2][2] + '.png');
        $("#4icon12").attr("src", '/img/' + symbols[3][2] + '.png');
    }

    function updateIconsForFiveReels(symbols) {
        $("#5icon1").attr("src", '/img/' + symbols[0][0] + '.png');
        $("#5icon2").attr("src", '/img/' + symbols[1][0] + '.png');
        $("#5icon3").attr("src", '/img/' + symbols[2][0] + '.png');
        $("#5icon4").attr("src", '/img/' + symbols[3][0] + '.png');
        $("#5icon5").attr("src", '/img/' + symbols[4][0] + '.png');

        $("#5icon6").attr("src", '/img/' + symbols[0][1] + '.png');
        $("#5icon7").attr("src", '/img/' + symbols[1][1] + '.png');
        $("#5icon8").attr("src", '/img/' + symbols[2][1] + '.png');
        $("#5icon9").attr("src", '/img/' + symbols[3][1] + '.png');
        $("#5icon10").attr("src", '/img/' + symbols[4][1] + '.png');

        $("#5icon11").attr("src", '/img/' + symbols[0][2] + '.png');
        $("#5icon12").attr("src", '/img/' + symbols[1][2] + '.png');
        $("#5icon13").attr("src", '/img/' + symbols[2][2] + '.png');
        $("#5icon14").attr("src", '/img/' + symbols[3][2] + '.png');
        $("#5icon15").attr("src", '/img/' + symbols[4][2] + '.png');
    }

    function set3Ikons() {
        $("#3icon1").attr("src", '/img/0.png');
        $("#3icon2").attr("src", '/img/0.png');
        $("#3icon3").attr("src", '/img/0.png');
        $("#3icon4").attr("src", '/img/2.png');
        $("#3icon5").attr("src", '/img/2.png');
        $("#3icon6").attr("src", '/img/2.png');
        $("#3icon7").attr("src", '/img/7.png');
        $("#3icon8").attr("src", '/img/7.png');
        $("#3icon9").attr("src", '/img/7.png');
    }

    function set4Ikons() {
        $("#4icon1").attr("src", '/img/0.png');
        $("#4icon2").attr("src", '/img/0.png');
        $("#4icon3").attr("src", '/img/0.png');
        $("#4icon4").attr("src", '/img/0.png');
        $("#4icon5").attr("src", '/img/2.png');
        $("#4icon6").attr("src", '/img/2.png');
        $("#4icon7").attr("src", '/img/2.png');
        $("#4icon8").attr("src", '/img/2.png');
        $("#4icon9").attr("src", '/img/7.png');
        $("#4icon10").attr("src", '/img/7.png');
        $("#4icon11").attr("src", '/img/7.png');
        $("#4icon12").attr("src", '/img/7.png');
    }

    function set5Ikons() {
        $("#5icon1").attr("src", '/img/0.png');
        $("#5icon2").attr("src", '/img/0.png');
        $("#5icon3").attr("src", '/img/0.png');
        $("#5icon4").attr("src", '/img/0.png');
        $("#5icon5").attr("src", '/img/0.png');
        $("#5icon6").attr("src", '/img/2.png');
        $("#5icon7").attr("src", '/img/2.png');
        $("#5icon8").attr("src", '/img/2.png');
        $("#5icon9").attr("src", '/img/2.png');
        $("#5icon10").attr("src", '/img/2.png');
        $("#5icon11").attr("src", '/img/7.png');
        $("#5icon12").attr("src", '/img/7.png');
        $("#5icon13").attr("src", '/img/7.png');
        $("#5icon14").attr("src", '/img/7.png');
        $("#5icon15").attr("src", '/img/7.png');
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
        $("#reels3").hide();
        $("#reels4").hide();
        $("#reels5").hide();
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