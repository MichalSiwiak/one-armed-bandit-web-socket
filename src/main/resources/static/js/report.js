var game = angular.module("ReportConfig", ['zingchart-angularjs'])

    .filter('percentage', ['$filter', function ($filter) {
        return function (input, decimals) {
            return $filter('number')(input * 100, decimals) + '%';
        };
    }]);


game.controller("ReportConfigController", function ($scope, $http, $timeout) {


    $scope.winLines = [];
    $scope.reels = [];
    $scope.showSpinner = true;

    getWinLines();
    getReels();
    _draw_chart();


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
        if (reelsSelected.length > 2) {
            $scope.showSpinner = false;
            $http({
                method: "POST",
                url: 'report',
                data: angular.toJson($scope.form),
                headers: {
                    'Content-Type': 'application/json'
                }
            }).then(function successCallback(response) {
                $scope.showSpinner = true;
                $scope.message = response.data;
                $scope.myJson.series[0].values = response.data.balanceChart;
                $scope.myJson.scaleX.values = response.data.rnoScaleList;
            })
        } else {
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

    //Drawing chart
    function _draw_chart() {

        $scope.myJson = {
            gui: {
                contextMenu: {
                    button: {
                        visible: 0
                    }
                }
            },
           /* title: {
                "text":"changes of account balance through a lot of games"
            },*/
            backgroundColor: "#434343",
            globals: {
                shadow: false,
                fontFamily: "Helvetica"
            },
            type: "area",
         /*   legend: {
                layout: "x4",
                backgroundColor: "transparent",
                borderColor: "transparent",
                marker: {
                    borderRadius: "50px",
                    borderColor: "transparent"
                },
                item: {
                    fontColor: "white"
                }

            },*/
            scaleX: {
                transform: {
                    type: 'number'
                },
                zooming: true,
                values: [],
                lineColor: "white",
                lineWidth: "1px",
                tick: {
                    lineColor: "white",
                    lineWidth: "1px"
                },
                item: {
                    fontColor: "white"
                },
                guide: {
                    visible: false
                },
                label: {
                    text: 'RNO',
                    paddingTop: '15px',
                    fontColor: 'white',
                    fontSize: '12px'
                }
            },
            scaleY: {
                lineColor: "white",
                lineWidth: "1px",
                short:true,
                "short-unit":"M",
                "thousands-separator":",",
                tick: {
                    lineColor: "white",
                    lineWidth: "1px"
                },
                guide: {
                    lineStyle: "solid",
                    lineColor: "#626262"
                },
                item: {
                    fontColor: "white",
                    fontSize: '8px'
                },
                /*label: {
                    text: 'Balance (millions)',
                    paddingTop: '15px',
                    fontColor: 'white',
                    fontSize: '8px'
                }*/
            },
            tooltip: {
                visible: false
            },
            crosshairX: {
                scaleLabel: {
                    backgroundColor: "#fff",
                    fontColor: "black"
                },
                plotLabel: {
                    backgroundColor: "#434343",
                    fontColor: "#FFF",
                    "thousands-separator": ",",
                    _text: "Number of hits : %v"
                }
            },
            plot: {
                lineWidth: "2px",
                marker: {
                    visible: false
                }
            },
            series: [{
                text: "Balance",
                values: [],
                backgroundColor1: "#4AD8CC",
                backgroundColor2: "#272822",
                lineColor: "#4AD8CC"
            }]
        };
    }

});