var functions = angular.module("SessionConfig", []);
functions.controller("SessionConfigController", function ($scope, $http, $timeout) {

    $scope.games = [];
    var socket = null;
    var stompClient = null;

    getAllGames();

    function getAllGames() {
        $http({
            method: 'GET',
            url: 'sessions'
        }).then(function successCallback(response) {
            $scope.games = response.data;

            socket = new SockJS('/one-armed-bandit-websocket');
            stompClient = Stomp.over(socket);
            stompClient.connect({}, function (frame) {
                console.log('Connected: ' + frame);
                stompClient.subscribe('/game/results-game', function () {
                    updateGame();
                });
            });

        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    }

    function updateGame() {
        if (stompClient !== null) {
            stompClient.disconnect();
        }
        getAllGames();
    }

    $scope.closeGame = function (id, token) {
        stompClient.send("/app/end/" + id, {}, JSON.stringify({'authorizationToken': token}));
        $timeout(function () {
            stompClient.send("/app/results", {}, JSON.stringify({'gameId': id}));
        }, 500);
    };

});