$(function () {

    echart_map();
    // 参数1 tableID,参数2 div高度，参数3 速度，参数4 tbody中tr几条以上滚动
    tableScroll('tableId', 200, 30, 10)
    var MyMarhq;

    function tableScroll(tableid, hei, speed, len) {
        clearTimeout(MyMarhq);
        $('#' + tableid).parent().find('.tableid_').remove()
        $('#' + tableid).parent().prepend(
            '<table class="tableid_"><thead>' + $('#' + tableid + ' thead').html() + '</thead></table>'
        ).css({
            'position': 'relative',
            'overflow': 'hidden',
            'height': hei + 'px'
        })
        $(".tableid_").find('th').each(function(i) {
            $(this).css('width', $('#' + tableid).find('th:eq(' + i + ')').width());
        });
        $(".tableid_").css({
            'position': 'absolute',
            'top': 0,
            'left': 0,
            'z-index': 9
        })
        $('#' + tableid).css({
            'position': 'absolute',
            'top': 0,
            'left': 0,
            'z-index': 1
        })

        if ($('#' + tableid).find('tbody tr').length > len) {
            $('#' + tableid).find('tbody').html($('#' + tableid).find('tbody').html() + $('#' + tableid).find('tbody').html());
            $(".tableid_").css('top', 0);
            $('#' + tableid).css('top', 0);
            var tblTop = 0;
            var outerHeight = $('#' + tableid).find('tbody').find("tr").outerHeight();

            function Marqueehq() {
                if (tblTop <= -outerHeight * $('#' + tableid).find('tbody').find("tr").length) {
                    tblTop = 0;
                } else {
                    tblTop -= 1;
                }
                $('#' + tableid).css('margin-top', tblTop + 'px');
                clearTimeout(MyMarhq);
                MyMarhq = setTimeout(function() {
                    Marqueehq()
                }, speed);
            }

            MyMarhq = setTimeout(Marqueehq, speed);
            $('#' + tableid).find('tbody').hover(function() {
                clearTimeout(MyMarhq);
            }, function() {
                clearTimeout(MyMarhq);
                if ($('#' + tableid).find('tbody tr').length > len) {
                    MyMarhq = setTimeout(Marqueehq, speed);
                }
            })
        }

    }

    // echart_1河南省景区流量统计
    function echart_1() {
        var url = "/henan/stream/";
        var list = null;
        // loading(getHAStreamData,"#chart_1")
        getHAStreamData()
        /**
         * 获取填充数据
         */
        function getHAStreamData() {
            var resultCity = [];
            var resultData = [
                [],
                [],
                []
            ];
            var yearsData = [];
            $.ajax({
                url: url,
                dataType: 'json',
                method: 'GET',
                success: function (data) {
                    list = data.completeList;
                    haCityStreams = list[0].haCityStreams;
                    for (var i = 0; i < haCityStreams.length; i++) {
                        resultCity[i] = haCityStreams[i].city
                    }
                    for (var i = 0; i < list.length; i++) {
                        yearsData[i] = list[i].year;
                        for (var j = 0; j < list[i].haCityStreams.length; j++) {
                            resultData[i].push(
                                {
                                    "value": (parseFloat(list[i].haCityStreams[j].count / 1000) + parseFloat(random(100, 1000))).toFixed(2),
                                    "name": list[i].haCityStreams[j].city
                                }
                            )
                        }

                    }
                    this.resultCity = resultCity
                    this.resultData = resultData
                    this.yearsData = yearsData
                    // $("#AjaxData").val(this.resultData)
                    console.log("yearsData" + yearsData)
                    console.log("1" + this.resultCity);
                    console.log("2" + this.resultData);
                    for (var i = 0; i < this.resultData.length; i++) {
                        console.log(this.resultData[i].name)
                    }
                    generateChart(this.resultCity, this.resultData, this.yearsData);
                    // 基于准备好的dom，初始化echarts实例

                }
            })

            function generateChart(resultCity, resultData, yearsData) {
                // 基于准备好的dom，初始化echarts实例
                var myChart = echarts.init(document.getElementById('chart_1'));
                var year = yearsData.reverse()

                console.log(resultData)
                optionXyMap01 = {
                    timeline: {
                        data: year,
                        axisType: 'category', //轴的类型
                        autoPlay: true, //表示是否自动播放
                        playInterval: 3000, // 表示播放的速度（跳动的间隔），单位毫秒（ms）
                        left: '10%',
                        right: '10%',
                        bottom: '3%',
                        width: '80%',
                        //  height: null,
                        label: {
                            normal: {
                                textStyle: {
                                    color: '#ddd'
                                }
                            },
                            emphasis: {
                                textStyle: {
                                    color: '#fff'
                                }
                            }
                        },
                        symbolSize: 10, //timeline标记的大小
                        lineStyle: {
                            color: '#555'
                        },
                        //『当前项』（checkpoint）的图形样式
                        checkpointStyle: {
                            borderColor: '#777',
                            borderWidth: 2
                        },
                        //『控制按钮』的样式。『控制按钮』包括：『播放按钮』、『前进按钮』、『后退按钮』
                        controlStyle: {
                            showNextBtn: true,
                            showPrevBtn: true,
                            normal: {
                                color: '#666',
                                borderColor: '#666'
                            },
                            emphasis: {
                                color: '#aaa',
                                borderColor: '#aaa'
                            }
                        },

                    },
                    baseOption: {
                        animation: true,
                        animationDuration: 1000,
                        animationEasing: 'cubicInOut',
                        animationDurationUpdate: 1000,
                        animationEasingUpdate: 'cubicInOut',
                        grid: {
                            right: '1%',
                            top: '15%',
                            bottom: '10%',
                            width: '20%'
                        },
                        //提示框组件
                        tooltip: {
                            // 触发类型
                            trigger: 'axis', // hover触发器
                            axisPointer: { // 坐标轴指示器，坐标轴触发有效
                                type: 'shadow', // 默认为直线，可选为：'line' | 'shadow'
                                shadowStyle: {
                                    color: 'rgba(150,150,150,0.1)' //hover颜色
                                }
                            }
                        },
                    },
                    options: []

                };
                for (var n = 0; n < year.length; n++) {
                    var a = 0
                    for (var i = 0; i < resultData[n].length; i++) {
                        a += parseInt(resultData[n][i].value)
                    }
                    resultData[n].push({
                        value: a, name: '_other',
                        tooltip: {
                            show: false
                        },
                        itemStyle: {
                            normal:
                                {color: 'transparent'}
                        }
                    });
                    optionXyMap01.options.push({

                        title: [{
                            /* text: '地图',
                         subtext: '内部数据请勿外传',
                         left: 'center',
                         textStyle: {
                             color: '#fff'
                         }*/
                        },
                            {
                                id: 'statistic',
                                text: year[n] + "年河南省景区流量统计",
                                left: '40%',
                                top: '8%',
                                textStyle: {
                                    color: '#fff',
                                    fontSize: 25
                                }
                            }
                        ],
                        tooltip: {
                            trigger: 'item',
                            formatter: "{a} <br/>{b} : {c}万人"
                        },
                        legend: {
                            x: 'center',
                            y: '15%',
                            data: resultCity,
                            icon: 'circle',
                            textStyle: {
                                color: '#fff',
                            }
                        },
                        calculable: true,
                        series: [{
                            name: '',
                            type: 'pie',
                            //起始角度，支持范围[0, 360]
                            startAngle: 0,
                            //饼图的半径，数组的第一项是内半径，第二项是外半径
                            radius: [41, 280.75],
                            //支持设置成百分比，设置成百分比时第一项是相对于容器宽度，第二项是相对于容器高度
                            center: ['50%', '40%'],
                            //是否展示成南丁格尔图，通过半径区分数据大小。可选择两种模式：
                            // 'radius' 面积展现数据的百分比，半径展现数据的大小。
                            //  'area' 所有扇区面积相同，仅通过半径展现数据大小

                            //是否启用防止标签重叠策略，默认开启，圆环图这个例子中需要强制所有标签放在中心位置，可以将该值设为 false。
                            avoidLabelOverlap: false,
                            label: {
                                normal: {
                                    show: true,
                                    formatter: '{c}万人'
                                },
                                emphasis: {
                                    show: true
                                }
                            },
                            labelLine: {
                                normal: {
                                    show: true,
                                    length2: 1,
                                },
                                emphasis: {
                                    show: true
                                }
                            },
                            itemStyle: {
                                normal: {
                                    color: function (params) {
                                        var colors = ["#1DE9B6", "#F46E36", "#04B9FF", "#5DBD32", "#FFC809", "#FB95D5", "#BDA29A", "#6E7074", "#546570", "#C4CCD3", "#37A2DA", "#67E0E3", "#32C5E9", "#9FE6B8", "#FFDB5C", "#FF9F7F", "#FB7293", "#E062AE", "#E690D1", "#E7BCF3", "#9D96F5", "#8378EA", "#8378EA", "#DD6B66", "#759AA0", "#E69D87", "#8DC1A9", "#EA7E53", "#EEDD78", "#73A373", "#73B9BC", "#7289AB", "#91CA8C", "#F49F42"];
                                        return colors[params.dataIndex]
                                    }

                                }
                            },
                            data: resultData[n]
                        }]
                    })
                }
                myChart.setOption(optionXyMap01);
                window.addEventListener("resize", function () {
                    myChart.resize();
                });
            };

        }


    }

    //echart_0河南省飞机场
    function echart_0() {
        // 基于准备好的dom，初始化echarts实例

        var myChart = echarts.init(document.getElementById('chart_0'));
        myChart.showLoading()
        function showProvince() {
            var geoCoordMap = {
                '郑州新郑国际机场': [113.8521068580, 34.5335850983],
                '洛阳北郊机场': [112.3994320339, 34.7415559858],
                '南阳姜营机场': [112.6254100714, 32.9878597272],
                '郑州上街机场': [113.2869457484, 34.8460888579],
                '商丘观堂机场': [115.4717728368, 34.4501923414],
            };
            var data = [{
                name: '郑州新郑国际机场',
                value: 100
            },
                {
                    name: '洛阳北郊机场',
                    value: 100
                },
                {
                    name: '南阳姜营机场',
                    value: 100
                },
                {
                    name: '郑州上街机场',
                    value: 100
                },
                {
                    name: '商丘观堂机场',
                    value: 100
                }
            ];
            var max = 480,
                min = 9; // todo
            var maxSize4Pin = 100,
                minSize4Pin = 20;
            var convertData = function (data) {
                var res = [];
                for (var i = 0; i < data.length; i++) {
                    var geoCoord = geoCoordMap[data[i].name];
                    if (geoCoord) {
                        res.push({
                            name: data[i].name,
                            value: geoCoord.concat(data[i].value)
                        });
                    }
                }
                return res;
            };

            myChart.setOption(option = {
                title: {
                    top: 20,
                    text: '',
                    subtext: '',
                    x: 'center',
                    textStyle: {
                        color: '#ccc'
                    }
                },
                legend: {
                    orient: 'vertical',
                    y: 'bottom',
                    x: 'right',
                    data: ['pm2.5'],
                    textStyle: {
                        color: '#fff'
                    }
                },
                visualMap: {
                    show: false,
                    min: 0,
                    max: 500,
                    left: 'left',
                    top: 'bottom',
                    text: ['高', '低'], // 文本，默认为数值文本
                    calculable: true,
                    seriesIndex: [1],
                    inRange: {}
                },
                geo: {
                    show: true,
                    map: 'henan',
                    mapType: 'henan',
                    label: {
                        normal: {show: true},
                        //鼠标移入后查看效果
                        emphasis: {
                            show: true,
                            textStyle: {
                                color: '#fff'
                            }
                        }
                    },
                    //鼠标缩放和平移
                    roam: true,
                    itemStyle: {
                        normal: {
                            //          	color: '#ddd',
                            borderColor: 'rgba(147, 235, 248, 1)',
                            borderWidth: 1,
                            areaColor: {
                                type: 'radial',
                                x: 0.5,
                                y: 0.5,
                                r: 0.8,
                                colorStops: [{
                                    offset: 0,
                                    color: 'rgba(175,238,238, 0)' // 0% 处的颜色
                                }, {
                                    offset: 1,
                                    color: 'rgba(	47,79,79, .2)' // 100% 处的颜色
                                }],
                                globalCoord: false // 缺省为 false
                            },
                            shadowColor: 'rgba(128, 217, 248, 1)',
                            shadowOffsetX: -2,
                            shadowOffsetY: 2,
                            shadowBlur: 10
                        },
                        emphasis: {
                            areaColor: '#389BB7',
                            borderWidth: 0
                        }
                    }
                },
                series: [{
                    name: 'light',
                    type: 'map',
                    coordinateSystem: 'geo',
                    data: convertData(data),
                    itemStyle: {
                        normal: {
                            color: '#F4E925'
                        }
                    }
                },
                    {
                        name: '点',
                        type: 'scatter',
                        coordinateSystem: 'geo',
                        symbol: 'pin',
                        symbolSize: function (val) {
                            var a = (maxSize4Pin - minSize4Pin) / (max - min);
                            var b = minSize4Pin - a * min;
                            b = maxSize4Pin - a * max;
                            return a * val[2] + b;
                        },
                        label: {
                            normal: {
                                // show: true,
                                // textStyle: {
                                //     color: '#fff',
                                //     fontSize: 9,
                                // }
                            }
                        },
                        itemStyle: {
                            normal: {
                                color: '#F62157', //标志颜色
                            }
                        },
                        zlevel: 6,
                        data: convertData(data),
                    },
                    {
                        name: 'light',
                        type: 'map',
                        mapType: 'henan',
                        geoIndex: 0,
                        aspectScale: 0.75, //长宽比
                        showLegendSymbol: false, // 存在legend时显示
                        label: {
                            normal: {
                                show: false
                            },
                            emphasis: {
                                show: false,
                                textStyle: {
                                    color: '#fff'
                                }
                            }
                        },
                        roam: true,
                        itemStyle: {
                            normal: {
                                areaColor: '#031525',
                                borderColor: '#FFFFFF',
                            },
                            emphasis: {
                                areaColor: '#2B91B7'
                            }
                        },
                        animation: false,
                        data: data
                    },
                    {
                        name: ' ',
                        type: 'effectScatter',
                        coordinateSystem: 'geo',
                        data: convertData(data.sort(function (a, b) {
                            return b.value - a.value;
                        }).slice(0, 5)),
                        symbolSize: function (val) {
                            return val[2] / 10;
                        },
                        showEffectOn: 'render',
                        rippleEffect: {
                            brushType: 'stroke'
                        },
                        hoverAnimation: true,
                        label: {
                            normal: {
                                formatter: '{b}',
                                position: 'right',
                                show: true
                            }
                        },
                        itemStyle: {
                            normal: {
                                color: '#05C3F9',
                                shadowBlur: 10,
                                shadowColor: '#05C3F9'
                            }
                        },
                        zlevel: 1
                    },

                ]
            });
        }

        showProvince();
        myChart.hideLoading()
        window.addEventListener("resize", function () {
            myChart.resize();
        });
    }

    //城市旅游企业收入
    function echart_2() {
        var url = "/economic/cityenterprise";
        var list = [];
        var uploadedDataURL = "/getchinaJson";
        loading(getProvinces,"#chart_2")
        // getProvinces()
        /**
         * 获取各省数据
         */
        function getProvinces() {
            var resultCoordMap = {};
            var resultData = [
                [], [], [], [], [], [], [], [], [], [], [], [],
                [], [], [], [], [], [], [], [], [], [], [], [],
                [], [], [], [], [], [], [], [], [], [], [], [],
                [],
                []
            ];
            var yearsData = [];
            $.ajax({
                url: url,
                dataType: 'json',
                method: 'GET',
                success: function (data) {

                    list = data.completeList;
                    for (var i = 0; i < list[0].cityBaseEntityVos.length; i++) {
                        resultCoordMap[list[0].cityBaseEntityVos[i].cityName] = [parseFloat(list[0].cityBaseEntityVos[i].lng), parseFloat(list[0].cityBaseEntityVos[i].lat)]
                    }
                    for (var i = 0; i < list.length; i++) {
                        yearsData.push(list[i].year)
                        for (var j = 0; j < list[i].cityBaseEntityVos.length; j++) {
                            resultData[i].push({
                                "time": list[i].cityBaseEntityVos[j].year,
                                "name": list[i].cityBaseEntityVos[j].cityName,
                                "value": parseFloat(list[i].cityBaseEntityVos[j].e_taking / 10000).toFixed(2)
                            })
                        }
                    }
                    this.resultCoordMap = resultCoordMap
                    this.resultData = resultData
                    this.yearsData = yearsData
                    generateMap(this.resultCoordMap, this.resultData, this.yearsData);
                    // 基于准备好的dom，初始化echarts实例
                }
            })
        }

        function generateMap(resultCoordMap, resultData, yearsData) {
            var myChart = echarts.init(document.getElementById('chart_2'));
            myChart.showLoading();
            //填充数据
            var geoCoordMap = resultCoordMap
            var GZData = resultData

            var colors = [
                ["#1DE9B6", "#F46E36", "#04B9FF", "#5DBD32", "#FB95D5", "#E062AE", "#67E0E3", "#E7BCF3", "#FFC809", "#73A373", "#9D96F5", "#73B9BC", "#8378EA"],
                ["#37A2DA", "#67E0E3", "#32C5E9", "#9FE6B8", "#FFDB5C", "#FF9F7F", "#FB7293"],
                ["#DD6B66", "#759AA0", "#E69D87", "#8DC1A9", "#EA7E53", "#EEDD78", "#7289AB", "#91CA8C", "#F49F42"],
            ];
            var colorIndex = 0;
            $(function () {

                var year = yearsData;
                /*柱子Y名称*/
                var categoryData = [];
                var barData = [];
                for (var key in geoCoordMap) {
                    categoryData.push(key);
                }

                for (var i = 0; i < GZData.length; i++) {
                    barData.push([]);
                    for (var j = 0; j < GZData[i].length; j++) {
                        barData[i].push(GZData[i][j].value)
                    }
                }
                $.getJSON(uploadedDataURL, function (geoJson) {

                    echarts.registerMap('china', geoJson);
                    myChart.hideLoading();
                    var convertData = function (data) {
                        var res = [];
                        for (var i = 0; i < data.length; i++) {
                            var geoCoord = geoCoordMap[data[i].name];
                            if (geoCoord) {
                                res.push({
                                    name: data[i].name,
                                    value: geoCoord.concat(data[i].value)
                                });
                            }
                        }
                        return res;
                    };
                    optionXyMap01 = {
                        timeline: {
                            data: year,
                            axisType: 'category', //轴的类型
                            autoPlay: true, //表示是否自动播放
                            playInterval: 3000, // 表示播放的速度（跳动的间隔），单位毫秒（ms）
                            left: '10%',
                            right: '10%',
                            bottom: '3%',
                            width: '80%',
                            //  height: null,
                            label: {
                                normal: {
                                    textStyle: {
                                        color: '#ddd'
                                    }
                                },
                                emphasis: {
                                    textStyle: {
                                        color: '#fff'
                                    }
                                }
                            },
                            symbolSize: 10, //timeline标记的大小
                            lineStyle: {
                                color: '#555'
                            },
                            //『当前项』（checkpoint）的图形样式
                            checkpointStyle: {
                                borderColor: '#777',
                                borderWidth: 2
                            },
                            //『控制按钮』的样式。『控制按钮』包括：『播放按钮』、『前进按钮』、『后退按钮』
                            controlStyle: {
                                showNextBtn: true,
                                showPrevBtn: true,
                                normal: {
                                    color: '#666',
                                    borderColor: '#666'
                                },
                                emphasis: {
                                    color: '#aaa',
                                    borderColor: '#aaa'
                                }
                            },

                        },
                        baseOption: {
                            animation: true,
                            animationDuration: 1000,
                            animationEasing: 'cubicInOut',
                            animationDurationUpdate: 1000,
                            animationEasingUpdate: 'cubicInOut',
                            grid: {
                                right: '1%',
                                top: '15%',
                                bottom: '10%',
                                width: '20%'
                            },
                            //提示框组件
                            tooltip: {
                                // 触发类型
                                trigger: 'axis', // hover触发器
                                axisPointer: { // 坐标轴指示器，坐标轴触发有效
                                    type: 'shadow', // 默认为直线，可选为：'line' | 'shadow'
                                    shadowStyle: {
                                        color: 'rgba(150,150,150,0.1)' //hover颜色
                                    }
                                }
                            },
                            geo: {
                                show: true, // 是否显示地理坐标系组件
                                map: 'china', // 地图类型
                                roam: true, //是否开启鼠标缩放和平移漫游。默认不开启。如果只想要开启缩放或者平移，可以设置成 'scale' 或者 'move'。设置成 true 为都开启
                                zoom: 1, // 当前视角的缩放比例
                                center: [113.83531246, 34.0267395887],
                                // 图形上的文本标签
                                label: {
                                    label: {
                                        normal: {show: false},
                                        //鼠标移入后查看效果
                                        emphasis: {
                                            textStyle: {
                                                color: '#fff'
                                            }
                                        }
                                    }

                                },
                                // 地图区域的多边形 图形样式
                                itemStyle: {
                                    normal: {
                                        borderColor: 'rgba(147, 235, 248, 1)',
                                        borderWidth: 1,
                                        areaColor: {
                                            type: 'radial',
                                            x: 0.5,
                                            y: 0.5,
                                            r: 0.8,
                                            colorStops: [{
                                                offset: 0,
                                                color: 'rgba(147, 235, 248, 0)' // 0% 处的颜色
                                            }, {
                                                offset: 1,
                                                color: 'rgba(147, 235, 248, .2)' // 100% 处的颜色
                                            }],
                                            globalCoord: false // 缺省为 false
                                        },
                                        shadowColor: 'rgba(128, 217, 248, 1)',
                                        // shadowColor: 'rgba(255, 255, 255, 1)',
                                        shadowOffsetX: -2,
                                        shadowOffsetY: 2,
                                        shadowBlur: 10 // 图形阴影的模糊大小
                                    },
                                    // 高亮状态下的多边形和标签样式。
                                    emphasis: {
                                        areaColor: '#389BB7',
                                        borderWidth: 0
                                    }
                                }
                            },
                        },
                        options: []

                    };
                    for (var n = 0; n < year.length; n++) {
                        optionXyMap01.options.push({

                            title: [{},
                                {
                                    id: 'statistic',
                                    text: year[n] + "年主要城市旅游企业收入统计",
                                    left: '74%',
                                    top: '8%',
                                    textStyle: {
                                        color: '#fff',
                                        fontSize: 20
                                    }
                                }
                            ],
                            //直角坐标系 grid 中的 x 轴，
                            // 一般情况下单个 grid 组件最多只能放上下两个 x 轴，
                            // 多于两个 x 轴需要通过配置 offset 属性防止同个位置多个 x 轴的重叠。
                            xAxis: {
                                type: 'value', // 坐标轴类型
                                scale: true, //只在数值轴中（type: 'value'）有效。是否是脱离 0 值比例。设置成 true 后坐标刻度不会强制包含零刻度。
                                position: 'top', //x轴位置
                                min: 0, //坐标刻度的最小值
                                boundaryGap: false, // 坐标轴两边留白策略，类目轴和非类目轴的设置和表现不一样。
                                //坐标轴在 grid 区域中的分隔线。
                                splitLine: {
                                    show: false
                                },
                                axisLine: {
                                    show: false
                                },
                                axisTick: {
                                    show: false
                                },
                                // 坐标轴刻度标签的相关设置
                                axisLabel: {
                                    margin: 2,
                                    textStyle: {
                                        color: '#aaa'
                                    }
                                },
                            },
                            yAxis: {
                                type: 'category',
                                //  name: 'TOP 20',
                                nameGap: 16, //
                                axisLine: {
                                    show: true,
                                    lineStyle: {
                                        color: '#ddd'
                                    }
                                },
                                axisTick: {
                                    show: false,
                                    lineStyle: {
                                        color: '#ddd'
                                    }
                                },
                                axisLabel: {
                                    interval: 0,
                                    textStyle: {
                                        color: '#ddd'
                                    }
                                },
                                data: categoryData
                            },
                            series: [
                                {
                                    //  name: 'Top 5',
                                    type: 'effectScatter',
                                    coordinateSystem: 'geo', // 该系列使用的坐标系
                                    data: convertData(GZData[n].sort(function (a, b) {
                                        return b.value - a.value;
                                    })),
                                    symbolSize: function (val) {
                                        return val[2] / 10 + 10;
                                    },
                                    showEffectOn: 'render', // 配置何时显示特效
                                    // 涟漪特效相关配置
                                    rippleEffect: {
                                        brushType: 'stroke'
                                    },
                                    hoverAnimation: true, //是否开启鼠标 hover 节点的提示动画效果
                                    label: {
                                        normal: {
                                            formatter: '{b}',
                                            position: 'right',
                                            show: true
                                        }
                                    },
                                    itemStyle: {
                                        normal: {
                                            color: colors[colorIndex][n],
                                            shadowBlur: 10,
                                            shadowColor: colors[colorIndex][n]
                                        }
                                    },
                                    //所有图形的 zlevel 值。
                                    //
                                    // zlevel用于 Canvas 分层，不同zlevel值的图形会放置在不同的 Canvas 中，Canvas 分层是一种常见的优化手段。
                                    // 我们可以把一些图形变化频繁（例如有动画）的组件设置成一个单独的zlevel。需要注意的是过多的 Canvas 会引起内存开销的增大，
                                    // 在手机端上需要谨慎使用以防崩溃。
                                    //
                                    // zlevel 大的 Canvas 会放在 zlevel 小的 Canvas 的上面。
                                    zlevel: 1
                                },
                                //柱状图
                                {
                                    tooltip: {
                                        trigger: 'item',
                                        formatter: function (params, ticket, callback) {
                                            //根据业务自己拓展要显示的内容
                                            var res = "";
                                            var name = params.name;
                                            var value = params.value;
                                            res = "<span style='color:#fff;'>" + name + "</span><br/>收入：" + value + "亿元";
                                            return res;
                                        }
                                    },
                                    zlevel: 1.5,
                                    type: 'bar',
                                    symbol: 'none',
                                    itemStyle: {
                                        normal: {
                                            color: colors[colorIndex][n]
                                        }
                                    },
                                    data: barData[n]
                                }
                            ]
                        })
                    }
                    myChart.setOption(optionXyMap01);
                    window.addEventListener("resize", function () {
                        myChart.resize();
                    });
                });
            });

        }
    }

    // echart_map地图热门常驻地展示
    function echart_map() {
        var url = "/henan/residence";
        var list = [];
        var haHotResidenceVos = null;
        console.log("ss")
        var mapName = 'china'
        var mapFeatures = echarts.getMap(mapName).geoJson;
        var uploadedDataURL = "/getchinaJson";
        // loading(getReisdanceData,"#chart_map")
        getReisdanceData()
        /**
         * 获取地图相关数据
         */
        function getReisdanceData() {
            var resultCoordMap = {};
            var resultData = [
                [],
                [],
                []
            ];
            var yearsData = [];
            $.ajax({
                url: url,
                dataType: 'json',
                method: 'GET',
                success: function (data) {
                    list = data.completeList;
                    console.log("haHotResidenceVos[i].residence" + data);
                    haHotResidenceVos = list[0].haHotResidenceVos;
                    console.log("data.yearCount" + haHotResidenceVos);
                    for (var i = 0; i < haHotResidenceVos.length; i++) {
                        resultCoordMap[haHotResidenceVos[i].residence] = [parseFloat(haHotResidenceVos[i].lng), parseFloat(haHotResidenceVos[i].lat)]
                        console.log("haHotResidenceVos[i].residence" + data)
                    }
                    for (var i = 0; i < list.length; i++) {
                        yearsData.push(list[i].year)
                        console.log("sdfsdfsdfsdfwetertret" + yearsData)
                        for (var j = 0; j < list[i].haHotResidenceVos.length; j++) {
                            resultData[i].push({
                                "time": list[i].haHotResidenceVos[j].time,
                                "name": list[i].haHotResidenceVos[j].residence,
                                "value": (parseFloat(list[i].haHotResidenceVos[j].count / 1000) + parseFloat(random(100, 1000))).toFixed(2)
                            })
                        }


                    }
                    this.resultCoordMap = resultCoordMap
                    this.resultData = resultData
                    this.yearsData = yearsData
                    $("#AjaxData").val(this.resultData)
                    console.log("1" + this.resultCoordMap);
                    console.log("2" + this.resultData);
                    generateMap(this.resultCoordMap, this.resultData,this.yearsData);
                    // 基于准备好的dom，初始化echarts实例

                }
            })
        }

        function generateMap(resultCoordMap, resultData, yearsData) {
            var myChart = echarts.init(document.getElementById('chart_map'));
            myChart.showLoading();
            //填充数据
            var geoCoordMap = resultCoordMap
            console.log(" geoCoordMap" + geoCoordMap)
            console.log(geoCoordMap.length)
            var GZData = resultData
            console.log(GZData + "GZData")
            console.log(GZData.length)

            var geoGpsMap = {
                '1': [113.65, 34.76],
                '2': [113.65, 34.76],
                '3': [113.65, 34.76],
                '4': [113.65, 34.76],
                '5': [113.65, 34.76]
            };

            var colors = [
                ["#1DE9B6", "#F46E36", "#04B9FF", "#5DBD32", "#FFC809", "#FB95D5", "#BDA29A", "#6E7074", "#546570", "#C4CCD3"],
                ["#37A2DA", "#67E0E3", "#32C5E9", "#9FE6B8", "#FFDB5C", "#FF9F7F", "#FB7293", "#E062AE", "#E690D1", "#E7BCF3", "#9D96F5", "#8378EA", "#8378EA"],
                ["#DD6B66", "#759AA0", "#E69D87", "#8DC1A9", "#EA7E53", "#EEDD78", "#73A373", "#73B9BC", "#7289AB", "#91CA8C", "#F49F42"],
            ];
            var colorIndex = 0;
            $(function () {

                var year = yearsData;
                /*柱子Y名称*/
                var categoryData = [];
                var barData = [];
                for (var key in geoCoordMap) {
                    categoryData.push(key);
                }
                console.log("GZData " + GZData)
                console.log("categoryData " + categoryData)

                for (var i = 0; i < GZData.length; i++) {
                    console.log("GZData[i]" + GZData[0])
                    barData.push([]);
                    for (var j = 0; j < GZData[i].length; j++) {
                        barData[i].push(GZData[i][j].value)
                    }
                }
                console.log("barData " + barData)
                $.getJSON(uploadedDataURL, function (geoJson) {

                    echarts.registerMap('china', geoJson);
                    myChart.hideLoading();

                    var convertData = function (data) {
                        console.log("sdfsdfsdfsdfsdfsdfsdsdfwewfwefwewe")
                        var res = [];
                        for (var i = 0; i < data.length; i++) {
                            var geoCoord = geoCoordMap[data[i].name];
                            console.log("geoCoord 1 1 " + geoCoord)
                            if (geoCoord) {
                                res.push({
                                    name: data[i].name,
                                    value: geoCoord.concat(data[i].value)
                                });
                            }
                        }
                        return res;
                    };
                    var convertToLineData = function (data, gps) {
                        console.log("sdfsdfsdfsdfsdfsdfsd")
                        var res = [];
                        for (var i = 0; i < data.length; i++) {
                            var dataItem = data[i];
                            var fromCoord = geoCoordMap[dataItem.name];
                            console.log(" fromCoord 5 5 5 " + fromCoord)
                            var toCoord = [113.65, 34.76]; //郑州
                            //  var toCoord = geoGps[Math.random()*3];
                            if (fromCoord && toCoord) {
                                res.push([{
                                    coord: fromCoord,
                                    value: dataItem.value
                                }, {
                                    coord: toCoord,
                                }]);
                            }
                        }
                        return res;
                    };

                    optionXyMap01 = {
                        timeline: {
                            data: year,
                            axisType: 'category', //轴的类型
                            autoPlay: true, //表示是否自动播放
                            playInterval: 3000, // 表示播放的速度（跳动的间隔），单位毫秒（ms）
                            left: '10%',
                            right: '10%',
                            bottom: '3%',
                            width: '80%',
                            //  height: null,
                            label: {
                                normal: {
                                    textStyle: {
                                        color: '#ddd'
                                    }
                                },
                                emphasis: {
                                    textStyle: {
                                        color: '#fff'
                                    }
                                }
                            },
                            symbolSize: 10, //timeline标记的大小
                            lineStyle: {
                                color: '#555'
                            },
                            //『当前项』（checkpoint）的图形样式
                            checkpointStyle: {
                                borderColor: '#777',
                                borderWidth: 2
                            },
                            //『控制按钮』的样式。『控制按钮』包括：『播放按钮』、『前进按钮』、『后退按钮』
                            controlStyle: {
                                showNextBtn: true,
                                showPrevBtn: true,
                                normal: {
                                    color: '#666',
                                    borderColor: '#666'
                                },
                                emphasis: {
                                    color: '#aaa',
                                    borderColor: '#aaa'
                                }
                            },

                        },
                        baseOption: {
                            animation: true,
                            animationDuration: 1000,
                            animationEasing: 'cubicInOut',
                            animationDurationUpdate: 1000,
                            animationEasingUpdate: 'cubicInOut',
                            grid: {
                                right: '1%',
                                top: '15%',
                                bottom: '10%',
                                width: '20%'
                            },
                            //提示框组件
                            tooltip: {
                                // 触发类型
                                trigger: 'axis', // hover触发器
                                axisPointer: { // 坐标轴指示器，坐标轴触发有效
                                    type: 'shadow', // 默认为直线，可选为：'line' | 'shadow'
                                    shadowStyle: {
                                        color: 'rgba(150,150,150,0.1)' //hover颜色
                                    }
                                }
                            },
                            geo: {
                                show: true, // 是否显示地理坐标系组件
                                map: 'china', // 地图类型
                                roam: true, //是否开启鼠标缩放和平移漫游。默认不开启。如果只想要开启缩放或者平移，可以设置成 'scale' 或者 'move'。设置成 true 为都开启
                                zoom: 1, // 当前视角的缩放比例
                                center: [113.83531246, 34.0267395887],
                                // 图形上的文本标签
                                label: {
                                    normal: {show: false},
                                    //鼠标移入后查看效果
                                    emphasis: {
                                        textStyle: {
                                            color: '#fff'
                                        }
                                    }
                                },


                                // 地图区域的多边形 图形样式
                                itemStyle: {
                                    normal: {
                                        borderColor: 'rgba(147, 235, 248, 1)',
                                        borderWidth: 1,
                                        areaColor: {
                                            type: 'radial',
                                            x: 0.5,
                                            y: 0.5,
                                            r: 0.8,
                                            colorStops: [{
                                                offset: 0,
                                                color: 'rgba(147, 235, 248, 0)' // 0% 处的颜色
                                            }, {
                                                offset: 1,
                                                color: 'rgba(147, 235, 248, .2)' // 100% 处的颜色
                                            }],
                                            globalCoord: false // 缺省为 false
                                        },
                                        shadowColor: 'rgba(128, 217, 248, 1)',
                                        // shadowColor: 'rgba(255, 255, 255, 1)',
                                        shadowOffsetX: -2,
                                        shadowOffsetY: 2,
                                        shadowBlur: 10 // 图形阴影的模糊大小
                                    },
                                    // 高亮状态下的多边形和标签样式。
                                    emphasis: {
                                        areaColor: '#389BB7',
                                        borderWidth: 0
                                    }
                                }
                            },
                        },
                        options: []

                    };
                    for (var n = 0; n < year.length; n++) {
                        optionXyMap01.options.push({

                            title: [{
                                /* text: '地图',
                                 subtext: '内部数据请勿外传',
                                 left: 'center',
                                 textStyle: {
                                     color: '#fff'
                                 }*/
                            },
                                {
                                    id: 'statistic',
                                    text: year[n] + "年热门常驻地统计",
                                    left: '77%',
                                    top: '8%',
                                    textStyle: {
                                        color: '#fff',
                                        fontSize: 20
                                    }
                                }
                            ],
                            //直角坐标系 grid 中的 x 轴，
                            // 一般情况下单个 grid 组件最多只能放上下两个 x 轴，
                            // 多于两个 x 轴需要通过配置 offset 属性防止同个位置多个 x 轴的重叠。
                            xAxis: {
                                type: 'value', // 坐标轴类型
                                scale: true, //只在数值轴中（type: 'value'）有效。是否是脱离 0 值比例。设置成 true 后坐标刻度不会强制包含零刻度。
                                position: 'top', //x轴位置
                                min: 0, //坐标刻度的最小值
                                boundaryGap: false, // 坐标轴两边留白策略，类目轴和非类目轴的设置和表现不一样。
                                //坐标轴在 grid 区域中的分隔线。
                                splitLine: {
                                    show: false
                                },
                                axisLine: {
                                    show: false
                                },
                                axisTick: {
                                    show: false
                                },
                                // 坐标轴刻度标签的相关设置
                                axisLabel: {
                                    margin: 2,
                                    textStyle: {
                                        color: '#aaa'
                                    }
                                },
                            },
                            yAxis: {
                                type: 'category',
                                //  name: 'TOP 20',
                                nameGap: 16, //
                                axisLine: {
                                    show: true,
                                    lineStyle: {
                                        color: '#ddd'
                                    }
                                },
                                axisTick: {
                                    show: false,
                                    lineStyle: {
                                        color: '#ddd'
                                    }
                                },
                                axisLabel: {
                                    interval: 0,
                                    textStyle: {
                                        color: '#ddd'
                                    }
                                },
                                data: categoryData
                            },
                            series: [
                                //未知作用
                                {
                                    //文字和标志
                                    name: 'light',
                                    type: 'effectScatter',
                                    coordinateSystem: 'geo',
                                    data: convertData(GZData[n]),
                                    symbolSize: function (val) {
                                        // console.log("val" + val[2])
                                        return val[2] / 30 - 6;
                                    },
                                    showEffectOn: 'render', // 配置何时显示特效
                                    // 涟漪特效相关配置
                                    rippleEffect: {
                                        brushType: 'stroke'
                                    },
                                    label: {
                                        normal: {
                                            formatter: '{b}', // 区域名称
                                            position: 'right',
                                            show: true
                                        },
                                        emphasis: {
                                            show: true
                                        }
                                    },
                                    itemStyle: {
                                        normal: {
                                            color: colors[colorIndex][n]
                                        }
                                    }
                                },
                                // //地图？
                                // {
                                //     type: 'map',
                                //     map: 'china',
                                //     geoIndex: 0,
                                //     aspectScale: 0.75, //长宽比
                                //     showLegendSymbol: false, // 存在legend时显示
                                //     label: {
                                //         normal: {
                                //             show: false
                                //         },
                                //         emphasis: {
                                //             show: false,
                                //             textStyle: {
                                //                 color: '#fff'
                                //             }
                                //         }
                                //     },
                                //     roam: true, // 是否开启鼠标缩放和平移漫游
                                //     itemStyle: {
                                //         normal: {
                                //             areaColor: '#031525',
                                //             borderColor: '#FFFFFF',
                                //         },
                                //         emphasis: {
                                //             areaColor: '#2B91B7'
                                //         }
                                //     },
                                //     animation: false,
                                //     data: GZData
                                // },
                                // 地图点的动画效果
                                {
                                    //  name: 'Top 5',
                                    type: 'effectScatter',
                                    coordinateSystem: 'geo', // 该系列使用的坐标系
                                    data: convertData(GZData[n].sort(function (a, b) {
                                        console.log("GZDatassss" + GZData[0].length)
                                        console.log("GZDatasssssdf" + GZData.length)
                                        return b.value - a.value;
                                    }).slice(0, 3)),
                                    symbolSize: function (val) {
                                        return val[2] / 30 - 5;
                                    },
                                    showEffectOn: 'render', // 配置何时显示特效
                                    // 涟漪特效相关配置
                                    rippleEffect: {
                                        brushType: 'stroke'
                                    },
                                    hoverAnimation: true, //是否开启鼠标 hover 节点的提示动画效果
                                    label: {
                                        normal: {
                                            formatter: '{b}',
                                            position: 'right',
                                            show: true
                                        }
                                    },
                                    itemStyle: {
                                        normal: {
                                            color: colors[colorIndex][n],
                                            shadowBlur: 10,
                                            shadowColor: colors[colorIndex][n]
                                        }
                                    },
                                    //所有图形的 zlevel 值。
                                    //
                                    // zlevel用于 Canvas 分层，不同zlevel值的图形会放置在不同的 Canvas 中，Canvas 分层是一种常见的优化手段。
                                    // 我们可以把一些图形变化频繁（例如有动画）的组件设置成一个单独的zlevel。需要注意的是过多的 Canvas 会引起内存开销的增大，
                                    // 在手机端上需要谨慎使用以防崩溃。
                                    //
                                    // zlevel 大的 Canvas 会放在 zlevel 小的 Canvas 的上面。
                                    zlevel: 1
                                },
                                // 目标点
                                {
                                    type: 'scatter',
                                    coordinateSystem: 'geo',
                                    zlevel: 2,
                                    rippleEffect: {
                                        period: 4,
                                        brushType: 'stroke',
                                        scale: 4
                                    },
                                    label: {
                                        normal: {
                                            show: true,
                                            position: 'right',
                                            //offset:[5, 0],
                                            color: '#E0FFFF',
                                            formatter: '{b}',
                                            textStyle: {
                                                color: "#E0FFFF"
                                            }
                                        },
                                        emphasis: {
                                            show: true,
                                            color: "#CDBA96"
                                        }
                                    },
                                    symbol: 'pin',
                                    symbolSize: 40,
                                    itemStyle: {
                                        normal: {
                                            show: false,
                                            color: "#EE3B3B"
                                        }
                                    },
                                    data: [{
                                        name: "河南省",
                                        value: [113.65, 34.76].concat([10]),
                                    }],
                                },
                                //地图线的动画效果
                                {
                                    type: 'lines',
                                    zlevel: 2,
                                    effect: {
                                        show: true,
                                        period: 4, //箭头指向速度，值越小速度越快
                                        trailLength: 0.02, //特效尾迹长度[0,1]值越大，尾迹越长重
                                        symbol: 'arrow', //箭头图标
                                        symbolSize: 3, //图标大小
                                    },
                                    lineStyle: {
                                        normal: {
                                            color: colors[colorIndex][n],
                                            width: 0.1, //尾迹线条宽度
                                            opacity: 0.5, //尾迹线条透明度
                                            curveness: .3 //尾迹线条曲直度
                                        }
                                    },
                                    data: convertToLineData(GZData[n], geoGpsMap[Math.ceil(Math.random() * 6)])
                                },
                                //柱状图
                                {
                                    tooltip: {
                                        trigger: 'item',
                                        formatter: function (params, ticket, callback) {
                                            //根据业务自己拓展要显示的内容
                                            var res = "";
                                            var name = params.name;
                                            var value = params.value;
                                            res = "<span style='color:#fff;'>" + name + "</span><br/>人数：" + value + "万人";
                                            return res;
                                        }
                                    },
                                    zlevel: 1.5,
                                    type: 'bar',
                                    symbol: 'none',
                                    itemStyle: {
                                        normal: {
                                            color: colors[colorIndex][n]
                                        }
                                    },
                                    data: barData[n]
                                }
                            ]
                        })
                    }
                    myChart.setOption(optionXyMap01);
                    window.addEventListener("resize", function () {
                        myChart.resize();
                    });
                });
            });

        }


    }

    //各省总旅游企业收入分布
    function echart_3() {
        var url = "/economic/provinceeconomictotal";
        // loading(getEconomicData,"#chart_3")
        getEconomicData()

        /**
         * 获取数据
         */
        function getEconomicData() {
            var typeData = [];
            var yearData = [];

            var provinceAgencyEconomicTotalIncome = [];
            var provinceHotelsEconomicTotalIncome = [];
            var provinceOtherEnterpriseEconomicTotalIncome = [];
            var provinceEnterpriseEconomicTotalIncome = [];

            var provincePredictAgencyEconomicTotalIncome = [];
            var provincePredictHotelsEconomicTotalIncome = [];
            var provincePredictOtherEnterpriseEconomicTotalIncome = [];
            var provincePredictEnterpriseEconomicTotalIncome = [];

            $.ajax({
                url: url,
                dataType: 'json',
                method: 'GET',
                success: function (data) {
                    this.yearsData = data.yearList;
                    this.provinceAgencyEconomicTotalIncome = data.provinceAgencyEconomicTotalIncome
                    this.provinceHotelsEconomicTotalIncome = data.provinceHotelsEconomicTotalIncome
                    this.provinceOtherEnterpriseEconomicTotalIncome = data.provinceOtherEnterpriseEconomicTotalIncome
                    this.provinceEnterpriseEconomicTotalIncome = data.provinceEnterpriseEconomicTotalIncome;

                    this.provincePredictAgencyEconomicTotalIncome = data.provincePredictAgencyEconomicTotalIncome
                    this.provincePredictHotelsEconomicTotalIncome = data.provincePredictHotelsEconomicTotalIncome
                    this.provincePredictOtherEnterpriseEconomicTotalIncome = data.provincePredictOtherEnterpriseEconomicTotalIncome
                    this.provincePredictEnterpriseEconomicTotalIncome = data.provincePredictEnterpriseEconomicTotalIncome;

                    this.typeData = data.provinceEconomicTotalType;
                    generateChart(this.yearsData,
                        this.provinceAgencyEconomicTotalIncome,
                        this.provinceHotelsEconomicTotalIncome,
                        this.provinceOtherEnterpriseEconomicTotalIncome,
                        this.provinceEnterpriseEconomicTotalIncome,
                        this.provincePredictAgencyEconomicTotalIncome,
                        this.provincePredictHotelsEconomicTotalIncome,
                        this.provincePredictOtherEnterpriseEconomicTotalIncome,
                        this.provincePredictEnterpriseEconomicTotalIncome,
                        this.typeData);
                    // 基于准备好的dom，初始化echarts实例

                }
            })
        }

        function generateChart(yearsData,
                               provinceAgencyEconomicTotalIncome, provinceHotelsEconomicTotalIncome,
                               provinceOtherEnterpriseEconomicTotalIncome, provinceEnterpriseEconomicTotalIncome,
                               provincePredictAgencyEconomicTotalIncome, provincePredictHotelsEconomicTotalIncome,
                               provincePredictOtherEnterpriseEconomicTotalIncome, provincePredictEnterpriseEconomicTotalIncome,
                               typeData) {

            var myChart = echarts.init(document.getElementById('chart_3'));
            console.log(typeData)
            option = {

                title: {
                    text: ''
                },
                tooltip: {
                    trigger: 'axis',
                    formatter: function (params) { // params 为一个数组，数组的每个元素 包含了 该折线图的点 所有的参数信息，比如 value(数值)、seriesName（系列名）、dataIndex（数据项的序号）
                        var tip = "";
                        tip += params[0].name + '<br />';
                        for (var i =0;i<params.length;i++){
                            error_rate = 0;
                            if (isContains(params[i].seriesName.toString(),'Predict')) {

                                try
                                {
                                    tip += params[i].marker + params[i].seriesName + ": " + params[i].value + ' 预测误差率为：' +computeErrorRate(params[i].value,params[i-params.length/2].value) + '<br />';

                                }
                                catch(TypeError)
                                {

                                }

                            }
                            else {
                                tip += params[i].marker + params[i].seriesName + ": " + params[i].value + '<br />';
                            }
                        }
                        return tip;
                    }
                },
                legend: {
                    data: typeData,
                    textStyle: {
                        color: '#fff'
                    },
                    top: '4%'
                },
                grid: {
                    left: '3%',
                    right: '4%',
                    bottom: '3%',
                    containLabel: true
                },
                toolbox: {
                    orient: 'vertical',
                    right: '1%',
                    top: '2%',
                    iconStyle: {
                        color: '#FFEA51',
                        borderColor: '#FFA74D',
                        borderWidth: 1,
                    },
                    feature: {
                        saveAsImage: {},
                        magicType: {
                            show: true,
                            type: ['line', 'bar', 'stack', 'tiled']
                        }
                    }
                },
                xAxis: {
                    type: 'category',
                    boundaryGap: false,
                    data: yearsData,
                    splitLine: {
                        show: false
                    },
                    axisLine: {
                        lineStyle: {
                            color: '#fff'
                        }
                    }
                },
                yAxis: {
                    name: '万元',
                    type: 'value',
                    splitLine: {
                        show: false
                    },
                    axisLine: {
                        lineStyle: {
                            color: '#fff'
                        }
                    }
                },
                color: ['#FF4949', '#FFA74D', '#FFEA51', '#4BF0FF', '#44AFF0', '#4E82FF', '#584BFF', '#BE4DFF', '#F845F1'],
                series: [
                    {
                        name: typeData[0],
                        type: 'line',
                        data: provinceAgencyEconomicTotalIncome
                    },
                    {
                        name: typeData[1],
                        type: 'line',
                        data: provinceHotelsEconomicTotalIncome
                    },
                    {
                        name: typeData[2],
                        type: 'line',
                        data: provinceOtherEnterpriseEconomicTotalIncome
                    },
                    {
                        name: typeData[3],
                        type: 'line',
                        data: provinceEnterpriseEconomicTotalIncome
                    },
                    {
                        name: typeData[4],
                        type: 'line',
                        data: provincePredictAgencyEconomicTotalIncome
                    },
                    {
                        name: typeData[5],
                        type: 'line',
                        data: provincePredictHotelsEconomicTotalIncome
                    },
                    {
                        name: typeData[6],
                        type: 'line',
                        data: provincePredictOtherEnterpriseEconomicTotalIncome
                    },
                    {
                        name: typeData[7],
                        type: 'line',
                        data: provincePredictEnterpriseEconomicTotalIncome
                    },

                ]
            };
            myChart.setOption(option);
        }

    }

    //各省旅游企业收入
    function echart_4() {
        var url = "/economic/provinceenterprise";
        var list = [];
        var uploadedDataURL = "/getchinaJson";
        loading(getProvinces,"#chart_4")
        // getProvinces()

        /**
         * 获取各省数据
         */
        function getProvinces() {
            var resultCoordMap = {};
            var resultData = [
                [], [], [], [], [], [], [], [], [], [], [], [],
                [], [], [], [], [], [], [], [], [], [], [], [],
                [], [], [], [], [], [], [], [], [], [], [], [],
                [],
                []
            ];
            var yearsData = [];
            $.ajax({
                url: url,
                dataType: 'json',
                method: 'GET',
                success: function (data) {

                    list = data.completeList;
                    for (var i = 0; i < list[0].provinceBaseEntityVos.length; i++) {
                        resultCoordMap[list[0].provinceBaseEntityVos[i].provinceName] = [parseFloat(list[0].provinceBaseEntityVos[i].lng), parseFloat(list[0].provinceBaseEntityVos[i].lat)]
                    }
                    for (var i = 0; i < list.length; i++) {
                        yearsData.push(list[i].year)
                        for (var j = 0; j < list[i].provinceBaseEntityVos.length; j++) {
                            resultData[i].push({
                                "time": list[i].provinceBaseEntityVos[j].year,
                                "name": list[i].provinceBaseEntityVos[j].provinceName,
                                "value": parseFloat(list[i].provinceBaseEntityVos[j].e_taking / 10000).toFixed(2)
                            })
                        }
                    }
                    this.resultCoordMap = resultCoordMap
                    this.resultData = resultData
                    this.yearsData = yearsData
                    console.log("1" + this.resultCoordMap);
                    console.log("2" + this.resultData);
                    generateMap(this.resultCoordMap, this.resultData, this.yearsData);
                    // 基于准备好的dom，初始化echarts实例
                }
            })
        }

        function generateMap(resultCoordMap, resultData, yearsData) {
            var myChart = echarts.init(document.getElementById('chart_4'));
            myChart.showLoading();
            //填充数据
            var geoCoordMap = resultCoordMap
            console.log(" geoCoordMap" + geoCoordMap)
            console.log(geoCoordMap.length)
            var GZData = resultData
            console.log(GZData + "GZData")
            console.log(GZData.length)


            var colors = [
                ["#1DE9B6", "#F46E36", "#04B9FF", "#5DBD32", "#FB95D5", "#E062AE", "#67E0E3", "#E7BCF3", "#FFC809", "#73A373", "#9D96F5", "#73B9BC", "#8378EA"],
                ["#37A2DA", "#67E0E3", "#32C5E9", "#9FE6B8", "#FFDB5C", "#FF9F7F", "#FB7293"],
                ["#DD6B66", "#759AA0", "#E69D87", "#8DC1A9", "#EA7E53", "#EEDD78", "#7289AB", "#91CA8C", "#F49F42"],
            ];
            var colorIndex = 0;
            $(function () {

                var year = yearsData;
                /*柱子Y名称*/
                var categoryData = [];
                var barData = [];
                for (var key in geoCoordMap) {
                    categoryData.push(key);
                }

                for (var i = 0; i < GZData.length; i++) {
                    barData.push([]);
                    for (var j = 0; j < GZData[i].length; j++) {
                        barData[i].push(GZData[i][j].value)
                    }
                }
                $.getJSON(uploadedDataURL, function (geoJson) {

                    echarts.registerMap('china', geoJson);
                    myChart.hideLoading();

                    var convertData = function (data) {
                        var res = [];
                        for (var i = 0; i < data.length; i++) {
                            var geoCoord = geoCoordMap[data[i].name];
                            if (geoCoord) {
                                res.push({
                                    name: data[i].name,
                                    value: geoCoord.concat(data[i].value)
                                });
                            }
                        }
                        return res;
                    };
                    optionXyMap01 = {
                        timeline: {
                            data: year,
                            axisType: 'category', //轴的类型
                            autoPlay: true, //表示是否自动播放
                            playInterval: 3000, // 表示播放的速度（跳动的间隔），单位毫秒（ms）
                            left: '10%',
                            right: '10%',
                            bottom: '3%',
                            width: '80%',
                            //  height: null,
                            label: {
                                normal: {
                                    textStyle: {
                                        color: '#ddd'
                                    }
                                },
                                emphasis: {
                                    textStyle: {
                                        color: '#fff'
                                    }
                                }
                            },
                            symbolSize: 10, //timeline标记的大小
                            lineStyle: {
                                color: '#555'
                            },
                            //『当前项』（checkpoint）的图形样式
                            checkpointStyle: {
                                borderColor: '#777',
                                borderWidth: 2
                            },
                            //『控制按钮』的样式。『控制按钮』包括：『播放按钮』、『前进按钮』、『后退按钮』
                            controlStyle: {
                                showNextBtn: true,
                                showPrevBtn: true,
                                normal: {
                                    color: '#666',
                                    borderColor: '#666'
                                },
                                emphasis: {
                                    color: '#aaa',
                                    borderColor: '#aaa'
                                }
                            },

                        },
                        baseOption: {
                            animation: true,
                            animationDuration: 1000,
                            animationEasing: 'cubicInOut',
                            animationDurationUpdate: 1000,
                            animationEasingUpdate: 'cubicInOut',
                            grid: {
                                right: '1%',
                                top: '15%',
                                bottom: '10%',
                                width: '20%'
                            },
                            //提示框组件
                            tooltip: {
                                // 触发类型
                                trigger: 'axis', // hover触发器
                                axisPointer: { // 坐标轴指示器，坐标轴触发有效
                                    type: 'shadow', // 默认为直线，可选为：'line' | 'shadow'
                                    shadowStyle: {
                                        color: 'rgba(150,150,150,0.1)' //hover颜色
                                    }
                                }
                            },
                            geo: {
                                show: true, // 是否显示地理坐标系组件
                                map: 'china', // 地图类型
                                roam: true, //是否开启鼠标缩放和平移漫游。默认不开启。如果只想要开启缩放或者平移，可以设置成 'scale' 或者 'move'。设置成 true 为都开启
                                zoom: 1, // 当前视角的缩放比例
                                center: [113.83531246, 34.0267395887],
                                // 图形上的文本标签
                                label: {
                                    label: {
                                        normal: {show: false},
                                        //鼠标移入后查看效果
                                        emphasis: {
                                            textStyle: {
                                                color: '#fff'
                                            }
                                        }
                                    }

                                },
                                // 地图区域的多边形 图形样式
                                itemStyle: {
                                    normal: {
                                        borderColor: 'rgba(147, 235, 248, 1)',
                                        borderWidth: 1,
                                        areaColor: {
                                            type: 'radial',
                                            x: 0.5,
                                            y: 0.5,
                                            r: 0.8,
                                            colorStops: [{
                                                offset: 0,
                                                color: 'rgba(147, 235, 248, 0)' // 0% 处的颜色
                                            }, {
                                                offset: 1,
                                                color: 'rgba(147, 235, 248, .2)' // 100% 处的颜色
                                            }],
                                            globalCoord: false // 缺省为 false
                                        },
                                        shadowColor: 'rgba(128, 217, 248, 1)',
                                        // shadowColor: 'rgba(255, 255, 255, 1)',
                                        shadowOffsetX: -2,
                                        shadowOffsetY: 2,
                                        shadowBlur: 10 // 图形阴影的模糊大小
                                    },
                                    // 高亮状态下的多边形和标签样式。
                                    emphasis: {
                                        areaColor: '#389BB7',
                                        borderWidth: 0
                                    }
                                }
                            },
                        },
                        options: []

                    };
                    for (var n = 0; n < year.length; n++) {
                        optionXyMap01.options.push({

                            title: [{},
                                {
                                    id: 'statistic',
                                    text: year[n] + "年省旅游企业收入统计",
                                    left: '75%',
                                    top: '8%',
                                    textStyle: {
                                        color: '#fff',
                                        fontSize: 20
                                    }
                                }
                            ],
                            //直角坐标系 grid 中的 x 轴，
                            // 一般情况下单个 grid 组件最多只能放上下两个 x 轴，
                            // 多于两个 x 轴需要通过配置 offset 属性防止同个位置多个 x 轴的重叠。
                            xAxis: {
                                type: 'value', // 坐标轴类型
                                scale: true, //只在数值轴中（type: 'value'）有效。是否是脱离 0 值比例。设置成 true 后坐标刻度不会强制包含零刻度。
                                position: 'top', //x轴位置
                                min: 0, //坐标刻度的最小值
                                boundaryGap: false, // 坐标轴两边留白策略，类目轴和非类目轴的设置和表现不一样。
                                //坐标轴在 grid 区域中的分隔线。
                                splitLine: {
                                    show: false
                                },
                                axisLine: {
                                    show: false
                                },
                                axisTick: {
                                    show: false
                                },
                                // 坐标轴刻度标签的相关设置
                                axisLabel: {
                                    margin: 2,
                                    textStyle: {
                                        color: '#aaa'
                                    }
                                },
                            },
                            yAxis: {
                                type: 'category',
                                //  name: 'TOP 20',
                                nameGap: 16, //
                                axisLine: {
                                    show: true,
                                    lineStyle: {
                                        color: '#ddd'
                                    }
                                },
                                axisTick: {
                                    show: false,
                                    lineStyle: {
                                        color: '#ddd'
                                    }
                                },
                                axisLabel: {
                                    interval: 0,
                                    textStyle: {
                                        color: '#ddd'
                                    }
                                },
                                data: categoryData
                            },
                            series: [
                                {
                                    //  name: 'Top 5',
                                    type: 'effectScatter',
                                    coordinateSystem: 'geo', // 该系列使用的坐标系
                                    data: convertData(GZData[n].sort(function (a, b) {
                                        return b.value - a.value;
                                    })),
                                    symbolSize: function (val) {
                                        return val[2] / 10 + 5;
                                    },
                                    showEffectOn: 'render', // 配置何时显示特效
                                    // 涟漪特效相关配置
                                    rippleEffect: {
                                        brushType: 'stroke'
                                    },
                                    hoverAnimation: true, //是否开启鼠标 hover 节点的提示动画效果
                                    label: {
                                        normal: {
                                            formatter: '{b}',
                                            position: 'right',
                                            show: true
                                        }
                                    },
                                    itemStyle: {
                                        normal: {
                                            color: colors[colorIndex][n],
                                            shadowBlur: 10,
                                            shadowColor: colors[colorIndex][n]
                                        }
                                    },
                                    //所有图形的 zlevel 值。
                                    //
                                    // zlevel用于 Canvas 分层，不同zlevel值的图形会放置在不同的 Canvas 中，Canvas 分层是一种常见的优化手段。
                                    // 我们可以把一些图形变化频繁（例如有动画）的组件设置成一个单独的zlevel。需要注意的是过多的 Canvas 会引起内存开销的增大，
                                    // 在手机端上需要谨慎使用以防崩溃。
                                    //
                                    // zlevel 大的 Canvas 会放在 zlevel 小的 Canvas 的上面。
                                    zlevel: 1
                                },
                                //柱状图
                                {
                                    tooltip: {
                                        trigger: 'item',
                                        formatter: function (params, ticket, callback) {
                                            //根据业务自己拓展要显示的内容
                                            var res = "";
                                            var name = params.name;
                                            var value = params.value;
                                            res = "<span style='color:#fff;'>" + name + "</span><br/>收入：" + value + "亿元";
                                            return res;
                                        }
                                    },
                                    zlevel: 1.5,
                                    type: 'bar',
                                    symbol: 'none',
                                    itemStyle: {
                                        normal: {
                                            color: colors[colorIndex][n]
                                        }
                                    },
                                    data: barData[n]
                                }
                            ]
                        })
                    }
                    myChart.setOption(optionXyMap01);
                    window.addEventListener("resize", function () {
                        myChart.resize();
                    });
                });
            });

        }
    }

    //河南热门城市
    function echart_6() {
        var url = "/henan/hotCities";
        var list = [];
        var uploadedDataURL = "/gethenanJson";
        // loading(Cities,"#chart_6")
        Cities()
        /**
         * 获取河南地图相关数据
         */
        function Cities() {
            var resultCoordMap = {};
            var resultData = [
                [],
                [],
                [],
                []
            ];
            var yearsData = [];
            $.ajax({
                url: url,
                dataType: 'json',
                method: 'GET',
                success: function (data) {
                    list = data.completeList;
                    for (var i = 0; i < list[0].haHotCitiesVos.length; i++) {
                        resultCoordMap[list[0].haHotCitiesVos[i].city] = [parseFloat(list[0].haHotCitiesVos[i].lng), parseFloat(list[0].haHotCitiesVos[i].lat)]
                    }
                    for (var i = 0; i < list.length; i++) {
                        yearsData.push(list[i].year)
                        for (var j = 0; j < list[i].haHotCitiesVos.length; j++) {
                            resultData[i].push({
                                "time": list[i].haHotCitiesVos[j].time,
                                "name": list[i].haHotCitiesVos[j].city,
                                "value": (parseFloat(list[i].haHotCitiesVos[j].count / 1000) + parseFloat(random(100, 1000))).toFixed(2)
                            })
                        }


                    }
                    this.resultCoordMap = resultCoordMap
                    this.resultData = resultData
                    this.yearsData = yearsData
                    console.log("1" + this.resultCoordMap);
                    console.log("2" + this.resultData);
                    generateMap(this.resultCoordMap, this.resultData, this.yearsData);
                    // 基于准备好的dom，初始化echarts实例

                }
            })
        }

        function generateMap(resultCoordMap, resultData, yearsData) {
            var myChart = echarts.init(document.getElementById('chart_6'));
            myChart.showLoading();
            //填充数据
            var geoCoordMap = resultCoordMap
            console.log(" geoCoordMap" + geoCoordMap)
            console.log(geoCoordMap.length)
            var GZData = resultData
            console.log(GZData + "GZData")
            console.log(GZData.length)

            var colors = [
                ["#1DE9B6", "#F46E36", "#04B9FF", "#5DBD32", "#FFC809", "#FB95D5", "#BDA29A", "#6E7074", "#546570", "#C4CCD3"],
                ["#37A2DA", "#67E0E3", "#32C5E9", "#9FE6B8", "#FFDB5C", "#FF9F7F", "#FB7293", "#E062AE", "#E690D1", "#E7BCF3", "#9D96F5", "#8378EA", "#8378EA"],
                ["#DD6B66", "#759AA0", "#E69D87", "#8DC1A9", "#EA7E53", "#EEDD78", "#73A373", "#73B9BC", "#7289AB", "#91CA8C", "#F49F42"],
            ];
            var colorIndex = 0;
            $(function () {

                var year = yearsData;
                /*柱子Y名称*/
                var categoryData = [];
                var barData = [];
                for (var key in geoCoordMap) {
                    categoryData.push(key);
                }

                for (var i = 0; i < GZData.length; i++) {
                    console.log("GZData[i]" + GZData[0])
                    barData.push([]);
                    for (var j = 0; j < GZData[i].length; j++) {
                        barData[i].push(GZData[i][j].value)
                    }
                }
                $.getJSON(uploadedDataURL, function (geoJson) {

                    echarts.registerMap('henan', geoJson);
                    myChart.hideLoading();

                    var convertData = function (data) {
                        var res = [];
                        for (var i = 0; i < data.length; i++) {
                            var geoCoord = geoCoordMap[data[i].name];
                            if (geoCoord) {
                                res.push({
                                    name: data[i].name,
                                    value: geoCoord.concat(data[i].value)
                                });
                            }
                        }
                        return res;
                    };
                    optionXyMap01 = {
                        timeline: {
                            data: year,
                            axisType: 'category', //轴的类型
                            autoPlay: true, //表示是否自动播放
                            playInterval: 3000, // 表示播放的速度（跳动的间隔），单位毫秒（ms）
                            left: '10%',
                            right: '10%',
                            bottom: '3%',
                            width: '80%',
                            //  height: null,
                            label: {
                                normal: {
                                    textStyle: {
                                        color: '#ddd'
                                    }
                                },
                                emphasis: {
                                    textStyle: {
                                        color: '#fff'
                                    }
                                }
                            },
                            symbolSize: 10, //timeline标记的大小
                            lineStyle: {
                                color: '#555'
                            },
                            //『当前项』（checkpoint）的图形样式
                            checkpointStyle: {
                                borderColor: '#777',
                                borderWidth: 2
                            },
                            //『控制按钮』的样式。『控制按钮』包括：『播放按钮』、『前进按钮』、『后退按钮』
                            controlStyle: {
                                showNextBtn: true,
                                showPrevBtn: true,
                                normal: {
                                    color: '#666',
                                    borderColor: '#666'
                                },
                                emphasis: {
                                    color: '#aaa',
                                    borderColor: '#aaa'
                                }
                            },

                        },
                        baseOption: {
                            animation: true,
                            animationDuration: 1000,
                            animationEasing: 'cubicInOut',
                            animationDurationUpdate: 1000,
                            animationEasingUpdate: 'cubicInOut',
                            grid: {
                                right: '1%',
                                top: '15%',
                                bottom: '10%',
                                width: '20%'
                            },
                            //提示框组件
                            tooltip: {
                                // 触发类型
                                trigger: 'axis', // hover触发器
                                axisPointer: { // 坐标轴指示器，坐标轴触发有效
                                    type: 'shadow', // 默认为直线，可选为：'line' | 'shadow'
                                    shadowStyle: {
                                        color: 'rgba(150,150,150,0.1)' //hover颜色
                                    }
                                }
                            },
                            geo: {
                                show: true, // 是否显示地理坐标系组件
                                map: 'henan', // 地图类型
                                roam: true, //是否开启鼠标缩放和平移漫游。默认不开启。如果只想要开启缩放或者平移，可以设置成 'scale' 或者 'move'。设置成 true 为都开启
                                zoom: 1, // 当前视角的缩放比例
                                center: [113.83531246, 34.0267395887],
                                // 图形上的文本标签
                                label: {
                                    label: {
                                        normal: {show: false},
                                        //鼠标移入后查看效果
                                        emphasis: {
                                            textStyle: {
                                                color: '#fff'
                                            }
                                        }
                                    }

                                },
                                // 地图区域的多边形 图形样式
                                itemStyle: {
                                    normal: {
                                        borderColor: 'rgba(147, 235, 248, 1)',
                                        borderWidth: 1,
                                        areaColor: {
                                            type: 'radial',
                                            x: 0.5,
                                            y: 0.5,
                                            r: 0.8,
                                            colorStops: [{
                                                offset: 0,
                                                color: 'rgba(147, 235, 248, 0)' // 0% 处的颜色
                                            }, {
                                                offset: 1,
                                                color: 'rgba(147, 235, 248, .2)' // 100% 处的颜色
                                            }],
                                            globalCoord: false // 缺省为 false
                                        },
                                        shadowColor: 'rgba(128, 217, 248, 1)',
                                        // shadowColor: 'rgba(255, 255, 255, 1)',
                                        shadowOffsetX: -2,
                                        shadowOffsetY: 2,
                                        shadowBlur: 10 // 图形阴影的模糊大小
                                    },
                                    // 高亮状态下的多边形和标签样式。
                                    emphasis: {
                                        areaColor: '#389BB7',
                                        borderWidth: 0
                                    }
                                }
                            },
                        },
                        options: []

                    };
                    for (var n = 0; n < year.length; n++) {
                        optionXyMap01.options.push({

                            title: [{
                                /* text: '地图',
                                 subtext: '内部数据请勿外传',
                                 left: 'center',
                                 textStyle: {
                                     color: '#fff'
                                 }*/
                            },
                                {
                                    id: 'statistic',
                                    text: year[n] + "年热门城市统计",
                                    left: '77%',
                                    top: '8%',
                                    textStyle: {
                                        color: '#fff',
                                        fontSize: 20
                                    }
                                }
                            ],
                            //直角坐标系 grid 中的 x 轴，
                            // 一般情况下单个 grid 组件最多只能放上下两个 x 轴，
                            // 多于两个 x 轴需要通过配置 offset 属性防止同个位置多个 x 轴的重叠。
                            xAxis: {
                                type: 'value', // 坐标轴类型
                                scale: true, //只在数值轴中（type: 'value'）有效。是否是脱离 0 值比例。设置成 true 后坐标刻度不会强制包含零刻度。
                                position: 'top', //x轴位置
                                min: 0, //坐标刻度的最小值
                                boundaryGap: false, // 坐标轴两边留白策略，类目轴和非类目轴的设置和表现不一样。
                                //坐标轴在 grid 区域中的分隔线。
                                splitLine: {
                                    show: false
                                },
                                axisLine: {
                                    show: false
                                },
                                axisTick: {
                                    show: false
                                },
                                // 坐标轴刻度标签的相关设置
                                axisLabel: {
                                    margin: 2,
                                    textStyle: {
                                        color: '#aaa'
                                    }
                                },
                            },
                            yAxis: {
                                type: 'category',
                                //  name: 'TOP 20',
                                nameGap: 16, //
                                axisLine: {
                                    show: true,
                                    lineStyle: {
                                        color: '#ddd'
                                    }
                                },
                                axisTick: {
                                    show: false,
                                    lineStyle: {
                                        color: '#ddd'
                                    }
                                },
                                axisLabel: {
                                    interval: 0,
                                    textStyle: {
                                        color: '#ddd'
                                    }
                                },
                                data: categoryData
                            },
                            series: [
                                //未知作用
                                {
                                    //文字和标志
                                    name: 'light',
                                    type: 'effectScatter',
                                    coordinateSystem: 'geo',
                                    data: convertData(GZData[n]),
                                    symbolSize: function (val) {
                                        // console.log("val" + val[2])
                                        return val[2] / 30 - 7;
                                    },
                                    showEffectOn: 'render', // 配置何时显示特效
                                    // 涟漪特效相关配置
                                    rippleEffect: {
                                        brushType: 'stroke'
                                    },
                                    label: {
                                        normal: {
                                            formatter: '{b}', // 区域名称
                                            position: 'right',
                                            show: true
                                        },
                                        emphasis: {
                                            show: true
                                        }
                                    },
                                    itemStyle: {
                                        normal: {
                                            color: colors[colorIndex][n]
                                        }
                                    }
                                },
                                {
                                    //  name: 'Top 5',
                                    type: 'effectScatter',
                                    coordinateSystem: 'geo', // 该系列使用的坐标系
                                    data: convertData(GZData[n].sort(function (a, b) {
                                        return b.value - a.value;
                                    }).slice(0, 3)),
                                    symbolSize: function (val) {
                                        return val[2] / 30 - 6;
                                    },
                                    showEffectOn: 'render', // 配置何时显示特效
                                    // 涟漪特效相关配置
                                    rippleEffect: {
                                        brushType: 'stroke'
                                    },
                                    hoverAnimation: true, //是否开启鼠标 hover 节点的提示动画效果
                                    label: {
                                        normal: {
                                            formatter: '{b}',
                                            position: 'right',
                                            show: true
                                        }
                                    },
                                    itemStyle: {
                                        normal: {
                                            color: colors[colorIndex][n],
                                            shadowBlur: 10,
                                            shadowColor: colors[colorIndex][n]
                                        }
                                    },
                                    //所有图形的 zlevel 值。
                                    //
                                    // zlevel用于 Canvas 分层，不同zlevel值的图形会放置在不同的 Canvas 中，Canvas 分层是一种常见的优化手段。
                                    // 我们可以把一些图形变化频繁（例如有动画）的组件设置成一个单独的zlevel。需要注意的是过多的 Canvas 会引起内存开销的增大，
                                    // 在手机端上需要谨慎使用以防崩溃。
                                    //
                                    // zlevel 大的 Canvas 会放在 zlevel 小的 Canvas 的上面。
                                    zlevel: 1
                                },
                                //柱状图
                                {
                                    tooltip: {
                                        trigger: 'item',
                                        formatter: function (params, ticket, callback) {
                                            //根据业务自己拓展要显示的内容
                                            var res = "";
                                            var name = params.name;
                                            var value = params.value;
                                            res = "<span style='color:#fff;'>" + name + "</span><br/>人数：" + value + "万人";
                                            return res;
                                        }
                                    },
                                    zlevel: 1.5,
                                    type: 'bar',
                                    symbol: 'none',
                                    itemStyle: {
                                        normal: {
                                            color: colors[colorIndex][n]
                                        }
                                    },
                                    data: barData[n]
                                }
                            ]
                        })
                    }
                    myChart.setOption(optionXyMap01);
                    myChart.on('click', function (params) {
                        var city = params.name;
                        $('.center_text').css('display', 'none');
                        $('.t_cos16').css('display', 'block');
                        echart_16(city);
                    });
                    window.addEventListener("resize", function () {
                        myChart.resize();
                    });
                });
            });

        }


    }

    //各市总旅游企业收入分布
    function echart_7() {
        var url = "/economic/cityeconomictotal";
        // loading(getEconomicData,"#chart_7")
        getEconomicData()
        /**
         * 获取数据
         */
        function getEconomicData() {
            var typeData = [];
            var yearData = [];

            var CityAgencyEconomicTotalIncome = [];
            var CityHotelsEconomicTotalIncome = [];
            var CityOtherEnterpriseEconomicTotalIncome = [];
            var CityEnterpriseEconomicTotalIncome = [];

            var CityPredictAgencyEconomicTotalIncome = [];
            var CityPredictHotelsEconomicTotalIncome = [];
            var CityPredictOtherEnterpriseEconomicTotalIncome = [];
            var CityPredictEnterpriseEconomicTotalIncome = [];

            $.ajax({
                url: url,
                dataType: 'json',
                method: 'GET',
                success: function (data) {
                    this.yearsData = data.yearList;
                    this.CityAgencyEconomicTotalIncome = data.CityAgencyEconomicTotalIncome
                    this.CityHotelsEconomicTotalIncome = data.CityHotelsEconomicTotalIncome
                    this.CityOtherEnterpriseEconomicTotalIncome = data.CityOtherEnterpriseEconomicTotalIncome
                    this.CityEnterpriseEconomicTotalIncome = data.CityEnterpriseEconomicTotalIncome;

                    this.CityPredictAgencyEconomicTotalIncome = data.CityPredictAgencyEconomicTotalIncome
                    this.CityPredictHotelsEconomicTotalIncome = data.CityPredictHotelsEconomicTotalIncome
                    this.CityPredictOtherEnterpriseEconomicTotalIncome = data.CityPredictOtherEnterpriseEconomicTotalIncome
                    this.CityPredictEnterpriseEconomicTotalIncome = data.CityPredictEnterpriseEconomicTotalIncome;

                    this.typeData = data.cityEconomicTotalType;
                    generateChart(this.yearsData,
                        this.CityAgencyEconomicTotalIncome,
                        this.CityHotelsEconomicTotalIncome,
                        this.CityOtherEnterpriseEconomicTotalIncome,
                        this.CityEnterpriseEconomicTotalIncome,
                        this.CityPredictAgencyEconomicTotalIncome,
                        this.CityPredictHotelsEconomicTotalIncome,
                        this.CityPredictOtherEnterpriseEconomicTotalIncome,
                        this.CityPredictEnterpriseEconomicTotalIncome,
                        this.typeData);
                    // 基于准备好的dom，初始化echarts实例

                }
            })
        }

        function generateChart(yearsData,
                               CityAgencyEconomicTotalIncome, CityHotelsEconomicTotalIncome,
                               CityOtherEnterpriseEconomicTotalIncome, CityEnterpriseEconomicTotalIncome,
                               CityPredictAgencyEconomicTotalIncome, CityPredictHotelsEconomicTotalIncome,
                               CityPredictOtherEnterpriseEconomicTotalIncome, CityPredictEnterpriseEconomicTotalIncome,
                               typeData) {

            var myChart = echarts.init(document.getElementById('chart_7'));
            console.log(typeData)
            option = {

                title: {
                    text: ''
                },
                tooltip: {
                    trigger: 'axis',
                    formatter: function (params) { // params 为一个数组，数组的每个元素 包含了 该折线图的点 所有的参数信息，比如 value(数值)、seriesName（系列名）、dataIndex（数据项的序号）
                        var tip = "";
                        tip += params[0].name + '<br />';
                        for (var i =0;i<params.length;i++){
                            error_rate = 0;
                            if (isContains(params[i].seriesName.toString(),'Predict')) {

                                try
                                {
                                    tip += params[i].marker + params[i].seriesName + ": " + params[i].value + ' 预测误差率为：' +computeErrorRate(params[i].value,params[i-params.length/2].value) + '<br />';

                                }
                                catch(TypeError)
                                {

                                }

                            }
                            else {
                                tip += params[i].marker + params[i].seriesName + ": " + params[i].value + '<br />';
                            }
                        }
                        return tip;
                    }
                },
                legend: {
                    data: typeData,
                    textStyle: {
                        color: '#fff'
                    },
                    top: '4%'
                },
                grid: {
                    left: '3%',
                    right: '4%',
                    bottom: '3%',
                    containLabel: true
                },
                toolbox: {
                    orient: 'vertical',
                    right: '1%',
                    top: '2%',
                    iconStyle: {
                        color: '#FFEA51',
                        borderColor: '#FFA74D',
                        borderWidth: 1,
                    },
                    feature: {
                        saveAsImage: {},
                        magicType: {
                            show: true,
                            type: ['line', 'bar', 'stack', 'tiled']
                        }
                    }
                },
                xAxis: {
                    type: 'category',
                    boundaryGap: false,
                    data: yearsData,
                    splitLine: {
                        show: false
                    },
                    axisLine: {
                        lineStyle: {
                            color: '#fff'
                        }
                    }
                },
                yAxis: {
                    name: '万元',
                    type: 'value',
                    splitLine: {
                        show: false
                    },
                    axisLine: {
                        lineStyle: {
                            color: '#fff'
                        }
                    }
                },
                color: ['#FF4949', '#FFA74D', '#FFEA51', '#4BF0FF', '#44AFF0', '#4E82FF', '#584BFF', '#BE4DFF', '#F845F1'],
                series: [
                    {
                        name: typeData[0],
                        type: 'line',
                        data: CityAgencyEconomicTotalIncome
                    },
                    {
                        name: typeData[1],
                        type: 'line',
                        data: CityHotelsEconomicTotalIncome
                    },
                    {
                        name: typeData[2],
                        type: 'line',
                        data: CityOtherEnterpriseEconomicTotalIncome
                    },
                    {
                        name: typeData[3],
                        type: 'line',
                        data: CityEnterpriseEconomicTotalIncome
                    },
                    {
                        name: typeData[4],
                        type: 'line',
                        data: CityPredictAgencyEconomicTotalIncome
                    },
                    {
                        name: typeData[5],
                        type: 'line',
                        data: CityPredictHotelsEconomicTotalIncome
                    },
                    {
                        name: typeData[6],
                        type: 'line',
                        data: CityPredictOtherEnterpriseEconomicTotalIncome
                    },
                    {
                        name: typeData[7],
                        type: 'line',
                        data: CityPredictEnterpriseEconomicTotalIncome
                    },

                ]
            };
            myChart.setOption(option);
        }

    }

    //星级酒店分布
    function echart_8() {
        var url = "/hotal/distribute";
        var list = [];
        var uploadedDataURL = "/getchinaJson";
        loading(getData,"#chart_8")
        // getData()

        /**
         * 获取各省星级酒店数据
         */
        function getData() {
            var resultCoordMap = {};
            var resultData = [
                [], [], [], [], [], [], [], [], [], [], [], [],
                [], [], [], [], [], [], [], [], [], [], [], [],
                [], [], [], [], [], [], [], [], [], [], [], [],
                [],
                []
            ];
            var yearsData = [];
            $.ajax({
                url: url,
                dataType: 'json',
                method: 'GET',
                success: function (data) {

                    list = data.completeList;
                    for (var i = 0; i < list[0].provinceHotelsVos.length; i++) {
                        resultCoordMap[list[0].provinceHotelsVos[i].e_regin_name] = [parseInt(list[0].provinceHotelsVos[i].lng), parseInt(list[0].provinceHotelsVos[i].lat)]
                    }
                    for (var i = 0; i < list.length; i++) {
                        yearsData.push(list[i].year)
                        for (var j = 0; j < list[i].provinceHotelsVos.length; j++) {
                            resultData[i].push({
                                "time": list[i].provinceHotelsVos[j].year,
                                "name": list[i].provinceHotelsVos[j].e_regin_name,
                                "value": parseInt(list[i].provinceHotelsVos[j].hotel_num)
                            })
                        }
                    }
                    this.resultCoordMap = resultCoordMap
                    this.resultData = resultData
                    this.yearsData = yearsData
                    console.log("1" + this.resultCoordMap);
                    console.log("2" + this.resultData);
                    generateMap(this.resultCoordMap, this.resultData, this.yearsData);
                    // 基于准备好的dom，初始化echarts实例
                }
            })
        }

        function generateMap(resultCoordMap, resultData, yearsData) {
            var myChart = echarts.init(document.getElementById('chart_8'));
            myChart.showLoading();
            //填充数据
            var geoCoordMap = resultCoordMap
            var GZData = resultData


            var colors = [
                ["#1DE9B6", "#F46E36", "#04B9FF", "#5DBD32", "#FB95D5", "#E062AE", "#67E0E3", "#73A373", "#9D96F5", "#73B9BC", "#8378EA", "#E7BCF3", "#FFC809", "#7289AB", "#91CA8C", "#F49F42",],
                ["#37A2DA", "#67E0E3", "#32C5E9", "#9FE6B8", "#FFDB5C", "#FF9F7F", "#FB7293"],
                ["#DD6B66", "#759AA0", "#E69D87", "#8DC1A9", "#EA7E53", "#EEDD78", "#7289AB", "#91CA8C", "#F49F42"],
            ];
            var colorIndex = 0;
            $(function () {

                var year = yearsData;
                /*柱子Y名称*/
                var categoryData = [];
                var barData = [];
                for (var key in geoCoordMap) {
                    categoryData.push(key);
                }

                for (var i = 0; i < GZData.length; i++) {
                    barData.push([]);
                    for (var j = 0; j < GZData[i].length; j++) {
                        barData[i].push(GZData[i][j].value)
                    }
                }
                $.getJSON(uploadedDataURL, function (geoJson) {

                    echarts.registerMap('china', geoJson);
                    myChart.hideLoading();

                    var convertData = function (data) {
                        var res = [];
                        for (var i = 0; i < data.length; i++) {
                            var geoCoord = geoCoordMap[data[i].name];
                            if (geoCoord) {
                                res.push({
                                    name: data[i].name,
                                    value: geoCoord.concat(data[i].value)
                                });
                            }
                        }
                        return res;
                    };
                    optionXyMap01 = {
                        timeline: {
                            data: year,
                            axisType: 'category', //轴的类型
                            autoPlay: true, //表示是否自动播放
                            playInterval: 3000, // 表示播放的速度（跳动的间隔），单位毫秒（ms）
                            left: '10%',
                            right: '10%',
                            bottom: '3%',
                            width: '80%',
                            //  height: null,
                            label: {
                                normal: {
                                    textStyle: {
                                        color: '#ddd'
                                    }
                                },
                                emphasis: {
                                    textStyle: {
                                        color: '#fff'
                                    }
                                }
                            },
                            symbolSize: 10, //timeline标记的大小
                            lineStyle: {
                                color: '#555'
                            },
                            //『当前项』（checkpoint）的图形样式
                            checkpointStyle: {
                                borderColor: '#777',
                                borderWidth: 2
                            },
                            //『控制按钮』的样式。『控制按钮』包括：『播放按钮』、『前进按钮』、『后退按钮』
                            controlStyle: {
                                showNextBtn: true,
                                showPrevBtn: true,
                                normal: {
                                    color: '#666',
                                    borderColor: '#666'
                                },
                                emphasis: {
                                    color: '#aaa',
                                    borderColor: '#aaa'
                                }
                            },

                        },
                        baseOption: {
                            animation: true,
                            animationDuration: 1000,
                            animationEasing: 'cubicInOut',
                            animationDurationUpdate: 1000,
                            animationEasingUpdate: 'cubicInOut',
                            grid: {
                                right: '1%',
                                top: '15%',
                                bottom: '10%',
                                width: '20%'
                            },
                            //提示框组件
                            tooltip: {
                                // 触发类型
                                trigger: 'axis', // hover触发器
                                axisPointer: { // 坐标轴指示器，坐标轴触发有效
                                    type: 'shadow', // 默认为直线，可选为：'line' | 'shadow'
                                    shadowStyle: {
                                        color: 'rgba(150,150,150,0.1)' //hover颜色
                                    }
                                }
                            },
                            geo: {
                                show: true, // 是否显示地理坐标系组件
                                map: 'china', // 地图类型
                                roam: true, //是否开启鼠标缩放和平移漫游。默认不开启。如果只想要开启缩放或者平移，可以设置成 'scale' 或者 'move'。设置成 true 为都开启
                                zoom: 1, // 当前视角的缩放比例
                                center: [113.83531246, 34.0267395887],
                                // 图形上的文本标签
                                label: {
                                    label: {
                                        normal: {show: false},
                                        //鼠标移入后查看效果
                                        emphasis: {
                                            textStyle: {
                                                color: '#fff'
                                            }
                                        }
                                    }

                                },
                                // 地图区域的多边形 图形样式
                                itemStyle: {
                                    normal: {
                                        borderColor: 'rgba(147, 235, 248, 1)',
                                        borderWidth: 1,
                                        areaColor: {
                                            type: 'radial',
                                            x: 0.5,
                                            y: 0.5,
                                            r: 0.8,
                                            colorStops: [{
                                                offset: 0,
                                                color: 'rgba(147, 235, 248, 0)' // 0% 处的颜色
                                            }, {
                                                offset: 1,
                                                color: 'rgba(147, 235, 248, .2)' // 100% 处的颜色
                                            }],
                                            globalCoord: false // 缺省为 false
                                        },
                                        shadowColor: 'rgba(128, 217, 248, 1)',
                                        // shadowColor: 'rgba(255, 255, 255, 1)',
                                        shadowOffsetX: -2,
                                        shadowOffsetY: 2,
                                        shadowBlur: 10 // 图形阴影的模糊大小
                                    },
                                    // 高亮状态下的多边形和标签样式。
                                    emphasis: {
                                        areaColor: '#389BB7',
                                        borderWidth: 0
                                    }
                                }
                            },
                        },
                        options: []

                    };
                    for (var n = 0; n < year.length; n++) {
                        optionXyMap01.options.push({

                            title: [{},
                                {
                                    id: 'statistic',
                                    text: year[n] + "年省星级酒店分布统计",
                                    left: '75%',
                                    top: '8%',
                                    textStyle: {
                                        color: '#fff',
                                        fontSize: 20
                                    }
                                }
                            ],
                            //直角坐标系 grid 中的 x 轴，
                            // 一般情况下单个 grid 组件最多只能放上下两个 x 轴，
                            // 多于两个 x 轴需要通过配置 offset 属性防止同个位置多个 x 轴的重叠。
                            xAxis: {
                                type: 'value', // 坐标轴类型
                                scale: true, //只在数值轴中（type: 'value'）有效。是否是脱离 0 值比例。设置成 true 后坐标刻度不会强制包含零刻度。
                                position: 'top', //x轴位置
                                min: 0, //坐标刻度的最小值
                                boundaryGap: false, // 坐标轴两边留白策略，类目轴和非类目轴的设置和表现不一样。
                                //坐标轴在 grid 区域中的分隔线。
                                splitLine: {
                                    show: false
                                },
                                axisLine: {
                                    show: false
                                },
                                axisTick: {
                                    show: false
                                },
                                // 坐标轴刻度标签的相关设置
                                axisLabel: {
                                    margin: 2,
                                    textStyle: {
                                        color: '#aaa'
                                    }
                                },
                            },
                            yAxis: {
                                type: 'category',
                                //  name: 'TOP 20',
                                nameGap: 16, //
                                axisLine: {
                                    show: true,
                                    lineStyle: {
                                        color: '#ddd'
                                    }
                                },
                                axisTick: {
                                    show: false,
                                    lineStyle: {
                                        color: '#ddd'
                                    }
                                },
                                axisLabel: {
                                    interval: 0,
                                    textStyle: {
                                        color: '#ddd'
                                    }
                                },
                                data: categoryData
                            },
                            series: [
                                {
                                    //  name: 'Top 5',
                                    type: 'effectScatter',
                                    coordinateSystem: 'geo', // 该系列使用的坐标系
                                    data: convertData(GZData[n].sort(function (a, b) {
                                        return b.value - a.value;
                                    })),
                                    symbolSize: function (val) {
                                        return val[2] / 15;
                                    },
                                    showEffectOn: 'render', // 配置何时显示特效
                                    // 涟漪特效相关配置
                                    rippleEffect: {
                                        brushType: 'stroke'
                                    },
                                    hoverAnimation: true, //是否开启鼠标 hover 节点的提示动画效果
                                    label: {
                                        normal: {
                                            formatter: '{b}',
                                            position: 'right',
                                            show: true
                                        }
                                    },
                                    itemStyle: {
                                        normal: {
                                            color: colors[colorIndex][n],
                                            shadowBlur: 10,
                                            shadowColor: colors[colorIndex][n]
                                        }
                                    },
                                    //所有图形的 zlevel 值。
                                    //
                                    // zlevel用于 Canvas 分层，不同zlevel值的图形会放置在不同的 Canvas 中，Canvas 分层是一种常见的优化手段。
                                    // 我们可以把一些图形变化频繁（例如有动画）的组件设置成一个单独的zlevel。需要注意的是过多的 Canvas 会引起内存开销的增大，
                                    // 在手机端上需要谨慎使用以防崩溃。
                                    //
                                    // zlevel 大的 Canvas 会放在 zlevel 小的 Canvas 的上面。
                                    zlevel: 1
                                },
                                //柱状图
                                {
                                    tooltip: {
                                        trigger: 'item',
                                        formatter: function (params, ticket, callback) {
                                            //根据业务自己拓展要显示的内容
                                            var res = "";
                                            var name = params.name;
                                            var value = params.value;
                                            res = "<span style='color:#fff;'>" + name + "</span><br/>酒店个数：" + value + "个";
                                            return res;
                                        }
                                    },
                                    zlevel: 1.5,
                                    type: 'bar',
                                    symbol: 'none',
                                    itemStyle: {
                                        normal: {
                                            color: colors[colorIndex][n]
                                        }
                                    },
                                    data: barData[n]
                                }
                            ]
                        })
                    }
                    myChart.setOption(optionXyMap01);
                    window.addEventListener("resize", function () {
                        myChart.resize();
                    });
                });
            });

        }
    }

    //世界地图
    function echart_9() {
        var url = "/foreign/nation";
        var list = [];
        var uploadedDataURL = "/getworldJson";
        // loading(getData,"#chart_9")
        getData()
        /**
         * 获取地图相关数据
         */
        function getData() {
            var resultCoordMap = {};
            var resultData = [
                [],[],[],[],[],[],[],[],[],[],[],[],
                [],[],[],[],[],[],[],[],[],[],[],[],
                [],
                []
            ];
            var yearsData = [];
            $.ajax({
                url: url,
                dataType: 'json',
                method: 'GET',
                success: function (data) {
                    list = data.completeList;
                    for (var i = 0; i < list[4].nationForeignTouristsVos.length; i++) {
                        resultCoordMap[list[4].nationForeignTouristsVos[i].nationality_name] = [parseFloat(list[4].nationForeignTouristsVos[i].lng), parseFloat(list[4].nationForeignTouristsVos[i].lat)]
                    }
                    for (var i = 0; i < list.length; i++) {
                        yearsData.push(list[i].year)
                        for (var j = 0; j < list[i].nationForeignTouristsVos.length; j++) {
                            resultData[i].push({
                                "time": list[i].nationForeignTouristsVos[j].year,
                                "name": list[i].nationForeignTouristsVos[j].nationality_name,
                                "value": (parseFloat(list[i].nationForeignTouristsVos[j].total)).toFixed(2)
                            })
                        }


                    }
                    this.resultCoordMap = resultCoordMap
                    this.resultData = resultData
                    this.yearsData = yearsData
                    generateMap(this.resultCoordMap, this.resultData,this.yearsData);
                    // 基于准备好的dom，初始化echarts实例

                }
            })
        }

        function generateMap(resultCoordMap, resultData, yearsData) {
            var myChart = echarts.init(document.getElementById('chart_9'));
            myChart.showLoading();
            //填充数据
            var geoCoordMap = resultCoordMap
            var GZData = resultData

            var geoGpsMap = {
                '1': [113.65, 34.76],
                '2': [113.65, 34.76],
                '3': [113.65, 34.76],
                '4': [113.65, 34.76],
                '5': [113.65, 34.76]
            };

            var colors = [
                ["#1DE9B6", "#F46E36", "#04B9FF", "#5DBD32", "#FFC809", "#FB95D5", "#BDA29A", "#67E0E3", "#32C5E9", "#C4CCD3","#DD6B66", "#759AA0", "#E69D87", "#8DC1A9", "#EA7E53", "#EEDD78", "#73A373"],
                ["#37A2DA", "#67E0E3", "#32C5E9", "#9FE6B8", "#FFDB5C", "#FF9F7F", "#FB7293", "#E062AE", "#E690D1", "#E7BCF3", "#9D96F5", "#8378EA", "#8378EA"],
                ["#DD6B66", "#759AA0", "#E69D87", "#8DC1A9", "#EA7E53", "#EEDD78", "#73A373", "#73B9BC", "#7289AB", "#91CA8C", "#F49F42"],
            ];
            var colorIndex = 0;
            $(function () {

                var year = yearsData;
                /*柱子Y名称*/
                var categoryData = [];
                var barData = [];
                for (var key in geoCoordMap) {
                    categoryData.push(key);
                }

                for (var i = 0; i < GZData.length; i++) {
                    barData.push([]);
                    for (var j = 0; j < GZData[i].length; j++) {
                        barData[i].push(GZData[i][j].value)
                    }
                }
                $.getJSON(uploadedDataURL, function (geoJson) {

                    echarts.registerMap('china', geoJson);
                    myChart.hideLoading();

                    var convertData = function (data) {
                        var res = [];
                        for (var i = 0; i < data.length; i++) {
                            var geoCoord = geoCoordMap[data[i].name];
                            if (geoCoord) {
                                res.push({
                                    name: data[i].name,
                                    value: geoCoord.concat(data[i].value)
                                });
                            }
                        }
                        return res;
                    };
                    var convertToLineData = function (data, gps) {
                        var res = [];
                        for (var i = 0; i < data.length; i++) {
                            var dataItem = data[i];
                            var fromCoord = geoCoordMap[dataItem.name];
                            var toCoord = [116.397128, 39.916527]; //北京
                            //  var toCoord = geoGps[Math.random()*3];
                            if (fromCoord && toCoord) {
                                res.push([{
                                    coord: fromCoord,
                                    value: dataItem.value
                                }, {
                                    coord: toCoord,
                                }]);
                            }
                        }
                        return res;
                    };

                    optionXyMap01 = {
                        timeline: {
                            data: year,
                            axisType: 'category', //轴的类型
                            autoPlay: true, //表示是否自动播放
                            playInterval: 3000, // 表示播放的速度（跳动的间隔），单位毫秒（ms）
                            left: '10%',
                            right: '10%',
                            bottom: '3%',
                            width: '80%',
                            //  height: null,
                            label: {
                                normal: {
                                    textStyle: {
                                        color: '#ddd'
                                    }
                                },
                                emphasis: {
                                    textStyle: {
                                        color: '#fff'
                                    }
                                }
                            },
                            symbolSize: 10, //timeline标记的大小
                            lineStyle: {
                                color: '#555'
                            },
                            //『当前项』（checkpoint）的图形样式
                            checkpointStyle: {
                                borderColor: '#777',
                                borderWidth: 2
                            },
                            //『控制按钮』的样式。『控制按钮』包括：『播放按钮』、『前进按钮』、『后退按钮』
                            controlStyle: {
                                showNextBtn: true,
                                showPrevBtn: true,
                                normal: {
                                    color: '#666',
                                    borderColor: '#666'
                                },
                                emphasis: {
                                    color: '#aaa',
                                    borderColor: '#aaa'
                                }
                            },

                        },
                        baseOption: {
                            animation: true,
                            animationDuration: 1000,
                            animationEasing: 'cubicInOut',
                            animationDurationUpdate: 1000,
                            animationEasingUpdate: 'cubicInOut',
                            grid: {
                                right: '1%',
                                top: '15%',
                                bottom: '10%',
                                width: '20%'
                            },
                            //提示框组件
                            tooltip: {
                                // 触发类型
                                trigger: 'axis', // hover触发器
                                axisPointer: { // 坐标轴指示器，坐标轴触发有效
                                    type: 'shadow', // 默认为直线，可选为：'line' | 'shadow'
                                    shadowStyle: {
                                        color: 'rgba(150,150,150,0.1)' //hover颜色
                                    }
                                }
                            },
                            geo: {
                                show: true, // 是否显示地理坐标系组件
                                map: 'china', // 地图类型
                                roam: true, //是否开启鼠标缩放和平移漫游。默认不开启。如果只想要开启缩放或者平移，可以设置成 'scale' 或者 'move'。设置成 true 为都开启
                                zoom: 1, // 当前视角的缩放比例
                                center: [113.83531246, 34.0267395887],
                                // 图形上的文本标签
                                label: {
                                    normal: {show: false},
                                    //鼠标移入后查看效果
                                    emphasis: {
                                        textStyle: {
                                            color: '#fff'
                                        }
                                    }
                                },


                                // 地图区域的多边形 图形样式
                                itemStyle: {
                                    normal: {
                                        borderColor: 'rgba(147, 235, 248, 1)',
                                        borderWidth: 1,
                                        areaColor: {
                                            type: 'radial',
                                            x: 0.5,
                                            y: 0.5,
                                            r: 0.8,
                                            colorStops: [{
                                                offset: 0,
                                                color: 'rgba(147, 235, 248, 0)' // 0% 处的颜色
                                            }, {
                                                offset: 1,
                                                color: 'rgba(147, 235, 248, .2)' // 100% 处的颜色
                                            }],
                                            globalCoord: false // 缺省为 false
                                        },
                                        shadowColor: 'rgba(128, 217, 248, 1)',
                                        // shadowColor: 'rgba(255, 255, 255, 1)',
                                        shadowOffsetX: -2,
                                        shadowOffsetY: 2,
                                        shadowBlur: 10 // 图形阴影的模糊大小
                                    },
                                    // 高亮状态下的多边形和标签样式。
                                    emphasis: {
                                        areaColor: '#389BB7',
                                        borderWidth: 0
                                    }
                                }
                            },
                        },
                        options: []

                    };
                    for (var n = 0; n < year.length; n++) {
                        optionXyMap01.options.push({

                            title: [{
                            },
                                {
                                    id: 'statistic',
                                    text: year[n] + "年客源国入境人数统计",
                                    left: '75%',
                                    top: '8%',
                                    textStyle: {
                                        color: '#fff',
                                        fontSize: 20
                                    }
                                }
                            ],
                            //直角坐标系 grid 中的 x 轴，
                            // 一般情况下单个 grid 组件最多只能放上下两个 x 轴，
                            // 多于两个 x 轴需要通过配置 offset 属性防止同个位置多个 x 轴的重叠。
                            xAxis: {
                                type: 'value', // 坐标轴类型
                                scale: true, //只在数值轴中（type: 'value'）有效。是否是脱离 0 值比例。设置成 true 后坐标刻度不会强制包含零刻度。
                                position: 'top', //x轴位置
                                min: 0, //坐标刻度的最小值
                                boundaryGap: false, // 坐标轴两边留白策略，类目轴和非类目轴的设置和表现不一样。
                                //坐标轴在 grid 区域中的分隔线。
                                splitLine: {
                                    show: false
                                },
                                axisLine: {
                                    show: false
                                },
                                axisTick: {
                                    show: false
                                },
                                // 坐标轴刻度标签的相关设置
                                axisLabel: {
                                    margin: 2,
                                    textStyle: {
                                        color: '#aaa'
                                    }
                                },
                            },
                            yAxis: {
                                type: 'category',
                                nameGap: 16, //
                                axisLine: {
                                    show: true,
                                    lineStyle: {
                                        color: '#ddd'
                                    }
                                },
                                axisTick: {
                                    show: false,
                                    lineStyle: {
                                        color: '#ddd'
                                    }
                                },
                                axisLabel: {
                                    interval: 0,
                                    textStyle: {
                                        color: '#ddd'
                                    }
                                },
                                data: categoryData
                            },
                            series: [
                                //未知作用
                                {
                                    //文字和标志
                                    name: 'light',
                                    type: 'effectScatter',
                                    coordinateSystem: 'geo',
                                    data: convertData(GZData[n]),
                                    symbolSize: function (val) {
                                        // console.log("val" + val[2])
                                        return val[2] / 10 +4  ;
                                    },
                                    showEffectOn: 'render', // 配置何时显示特效
                                    // 涟漪特效相关配置
                                    rippleEffect: {
                                        brushType: 'stroke'
                                    },
                                    label: {
                                        normal: {
                                            formatter: '{b}', // 区域名称
                                            position: 'right',
                                            show: true
                                        },
                                        emphasis: {
                                            show: true
                                        }
                                    },
                                    itemStyle: {
                                        normal: {
                                            color: colors[colorIndex][n]
                                        }
                                    }
                                },
                                {
                                    //  name: 'Top 5',
                                    type: 'effectScatter',
                                    coordinateSystem: 'geo', // 该系列使用的坐标系
                                    data: convertData(GZData[n].sort(function (a, b) {
                                        return b.value - a.value;
                                    }).slice(0, 10)),
                                    symbolSize: function (val) {
                                        return val[2] / 10 + 5;
                                    },
                                    showEffectOn: 'render', // 配置何时显示特效
                                    // 涟漪特效相关配置
                                    rippleEffect: {
                                        brushType: 'stroke'
                                    },
                                    hoverAnimation: true, //是否开启鼠标 hover 节点的提示动画效果
                                    label: {
                                        normal: {
                                            formatter: '{b}',
                                            position: 'right',
                                            show: true
                                        }
                                    },
                                    itemStyle: {
                                        normal: {
                                            color: colors[colorIndex][n],
                                            shadowBlur: 10,
                                            shadowColor: colors[colorIndex][n]
                                        }
                                    },
                                    //所有图形的 zlevel 值。
                                    //
                                    // zlevel用于 Canvas 分层，不同zlevel值的图形会放置在不同的 Canvas 中，Canvas 分层是一种常见的优化手段。
                                    // 我们可以把一些图形变化频繁（例如有动画）的组件设置成一个单独的zlevel。需要注意的是过多的 Canvas 会引起内存开销的增大，
                                    // 在手机端上需要谨慎使用以防崩溃。
                                    //
                                    // zlevel 大的 Canvas 会放在 zlevel 小的 Canvas 的上面。
                                    zlevel: 1
                                },
                                // 目标点
                                {
                                    type: 'scatter',
                                    coordinateSystem: 'geo',
                                    zlevel: 2,
                                    rippleEffect: {
                                        period: 4,
                                        brushType: 'stroke',
                                        scale: 4
                                    },
                                    label: {
                                        normal: {
                                            show: true,
                                            position: 'right',
                                            //offset:[5, 0],
                                            color: '#E0FFFF',
                                            formatter: '{b}',
                                            textStyle: {
                                                color: "#E0FFFF"
                                            }
                                        },
                                        emphasis: {
                                            show: true,
                                            color: "#CDBA96"
                                        }
                                    },
                                    symbol: 'pin',
                                    symbolSize: 40,
                                    itemStyle: {
                                        normal: {
                                            show: false,
                                            color: "#00BFFF"
                                        }
                                    },
                                    data: [{
                                        name: "北京",
                                        value: [116.397128, 39.916527].concat([10]),
                                    }],
                                },
                                //地图线的动画效果
                                {
                                    type: 'lines',
                                    zlevel: 2,
                                    effect: {
                                        show: true,
                                        period: 4, //箭头指向速度，值越小速度越快
                                        trailLength: 0.21, //特效尾迹长度[0,1]值越大，尾迹越长重
                                        symbol: 'arrow', //箭头图标
                                        symbolSize: 5, //图标大小
                                    },
                                    lineStyle: {
                                        normal: {
                                            color: colors[colorIndex][n],
                                            width: 0.1, //尾迹线条宽度
                                            opacity: 0.5, //尾迹线条透明度
                                            curveness: .3 //尾迹线条曲直度
                                        }
                                    },
                                    data: convertToLineData(GZData[n], geoGpsMap[Math.ceil(Math.random() * 6)])
                                },
                                //柱状图
                                {
                                    tooltip: {
                                        trigger: 'item',
                                        formatter: function (params, ticket, callback) {
                                            //根据业务自己拓展要显示的内容
                                            var res = "";
                                            var name = params.name;
                                            var value = params.value;
                                            res = "<span style='color:#fff;'>" + name + "</span><br/>人数：" + value + "万人";
                                            return res;
                                        }
                                    },
                                    zlevel: 1.5,
                                    type: 'bar',
                                    symbol: 'none',
                                    itemStyle: {
                                        normal: {
                                            color: colors[colorIndex][n]
                                        }
                                    },
                                    data: barData[n]
                                }
                            ]
                        })
                    }
                    myChart.setOption(optionXyMap01);
                    window.addEventListener("resize", function () {
                        myChart.resize();
                    });
                });
            });

        }


    }

    //入境方式
    function echart_10() {
        var url = "/foreign/transport";
        // loading(getData,"#chart_10")
        getData()
        /**
         * 获取数据
         */
        function getData() {
            var typeData = [];
            var yearData = [];
            var ships = [];
            var aeroplanes = [];
            var trains = [];
            var automobiles = [];
            var foots = [];
            var totals = [];
            $.ajax({
                url: url,
                dataType: 'json',
                method: 'GET',
                success: function (data) {
                    this.yearsData = data.yearList;
                    this.ships = data.ships
                    this.aeroplanes = data.aeroplanes
                    this.trains = data.trains
                    this.automobiles = data.automobiles
                    this.foots = data.foots
                    this.totals = data.totals
                    this.typeData = data.TotalType;
                    generateChart(this.yearsData,
                        this.ships,
                        this.aeroplanes,
                        this.trains,
                        this.automobiles,
                        this.foots,
                        this.totals,
                        this.typeData);
                    // 基于准备好的dom，初始化echarts实例

                }
            })
        }

        function generateChart(yearsData, ships, aeroplanes, trains, automobiles,foots, totals,typeData) {
            var myChart = echarts.init(document.getElementById('chart_10'));
            console.log(typeData)
            option = {

                title: {
                    text: ''
                },
                tooltip: {
                    trigger: 'axis'
                },
                legend: {
                    data: typeData,
                    textStyle: {
                        color: '#fff'
                    },
                    top: '4%'
                },
                grid: {
                    left: '3%',
                    right: '4%',
                    bottom: '3%',
                    containLabel: true
                },
                toolbox: {
                    orient: 'vertical',
                    right: '1%',
                    top: '2%',
                    iconStyle: {
                        color: '#FFEA51',
                        borderColor: '#FFA74D',
                        borderWidth: 1,
                    },
                    feature: {
                        saveAsImage: {},
                        magicType: {
                            show: true,
                            type: ['line', 'bar', 'stack', 'tiled']
                        }
                    }
                },
                xAxis: {
                    type: 'category',
                    boundaryGap: false,
                    data: yearsData,
                    splitLine: {
                        show: false
                    },
                    axisLine: {
                        lineStyle: {
                            color: '#fff'
                        }
                    }
                },
                yAxis: {
                    name: '万人',
                    type: 'value',
                    splitLine: {
                        show: false
                    },
                    axisLine: {
                        lineStyle: {
                            color: '#fff'
                        }
                    }
                },
                color: ['#FF4949', '#FFA74D', '#FFEA51', '#4BF0FF', '#44AFF0', '#4E82FF', '#584BFF', '#BE4DFF', '#F845F1'],
                series: [
                    {
                        name: typeData[0],
                        type: 'line',
                        data: ships
                    },
                    {
                        name: typeData[1],
                        type: 'line',
                        data: aeroplanes
                    },
                    {
                        name: typeData[2],
                        type: 'line',
                        data: trains
                    },
                    {
                        name: typeData[3],
                        type: 'line',
                        data: automobiles
                    },
                    {
                        name: typeData[4],
                        type: 'line',
                        data: foots
                    },
                    {
                        name: typeData[5],
                        type: 'line',
                        data: totals
                    },

                ]
            };
            myChart.setOption(option);
        }

    }

    //河南省收入
    function echart_11() {
        var url = "/economic/henaneconomic";
        // loading(getEconomicData,"#chart_3")
        getEconomicData()

        /**
         * 获取数据
         */
        function getEconomicData() {
            var typeData = [];
            var yearData = [];

            var provinceAgencyEconomicTotalIncome = [];
            var provinceHotelsEconomicTotalIncome = [];
            var provinceOtherEnterpriseEconomicTotalIncome = [];
            var provinceEnterpriseEconomicTotalIncome = [];

            var provincePredictAgencyEconomicTotalIncome = [];
            var provincePredictHotelsEconomicTotalIncome = [];
            var provincePredictOtherEnterpriseEconomicTotalIncome = [];
            var provincePredictEnterpriseEconomicTotalIncome = [];
            $.ajax({
                url: url,
                dataType: 'json',
                method: 'GET',
                success: function (data) {
                    this.yearsData = data.yearList;
                    this.provinceAgencyEconomicTotalIncome = data.provinceAgencyEconomicTotalIncome
                    this.provinceHotelsEconomicTotalIncome = data.provinceHotelsEconomicTotalIncome
                    this.provinceOtherEnterpriseEconomicTotalIncome = data.provinceOtherEnterpriseEconomicTotalIncome
                    this.provinceEnterpriseEconomicTotalIncome = data.provinceEnterpriseEconomicTotalIncome;

                    this.provincePredictAgencyEconomicTotalIncome = data.provincePredictAgencyEconomicTotalIncome
                    this.provincePredictHotelsEconomicTotalIncome = data.provincePredictHotelsEconomicTotalIncome
                    this.provincePredictOtherEnterpriseEconomicTotalIncome = data.provincePredictOtherEnterpriseEconomicTotalIncome
                    this.provincePredictEnterpriseEconomicTotalIncome = data.provincePredictEnterpriseEconomicTotalIncome;

                    this.typeData = data.provinceEconomicTotalType;
                    generateChart(this.yearsData,
                        this.provinceAgencyEconomicTotalIncome,
                        this.provinceHotelsEconomicTotalIncome,
                        this.provinceOtherEnterpriseEconomicTotalIncome,
                        this.provinceEnterpriseEconomicTotalIncome,
                        this.provincePredictAgencyEconomicTotalIncome,
                        this.provincePredictHotelsEconomicTotalIncome,
                        this.provincePredictOtherEnterpriseEconomicTotalIncome,
                        this.provincePredictEnterpriseEconomicTotalIncome,
                        this.typeData);
                    // 基于准备好的dom，初始化echarts实例

                }
            })
        }

        function generateChart(yearsData,
                               provinceAgencyEconomicTotalIncome, provinceHotelsEconomicTotalIncome,
                               provinceOtherEnterpriseEconomicTotalIncome, provinceEnterpriseEconomicTotalIncome,
                               provincePredictAgencyEconomicTotalIncome, provincePredictHotelsEconomicTotalIncome,
                               provincePredictOtherEnterpriseEconomicTotalIncome, provincePredictEnterpriseEconomicTotalIncome,
                               typeData) {

            var myChart = echarts.init(document.getElementById('chart_11'));
            console.log(typeData)
            option = {

                title: {
                    text: ''
                },
                tooltip: {
                    trigger: 'axis',
                    formatter: function (params) { // params 为一个数组，数组的每个元素 包含了 该折线图的点 所有的参数信息，比如 value(数值)、seriesName（系列名）、dataIndex（数据项的序号）
                        var tip = "";
                        tip += params[0].name + '<br />';
                        for (var i =0;i<params.length;i++){
                            error_rate = 0;
                            if (isContains(params[i].seriesName.toString(),'Predict')) {

                                try
                                {
                                    tip += params[i].marker + params[i].seriesName + ": " + params[i].value + ' 预测误差率为：' +computeErrorRate(params[i].value,params[i-params.length/2].value) + '<br />';

                                }
                                catch(TypeError)
                                {

                                }

                            }
                            else {
                                tip += params[i].marker + params[i].seriesName + ": " + params[i].value + '<br />';
                            }
                        }
                        return tip;
                    }
                },
                legend: {
                    data: typeData,
                    textStyle: {
                        color: '#fff'
                    },
                    top: '4%'
                },
                grid: {
                    left: '3%',
                    right: '4%',
                    bottom: '3%',
                    containLabel: true
                },
                toolbox: {
                    orient: 'vertical',
                    right: '1%',
                    top: '2%',
                    iconStyle: {
                        color: '#FFEA51',
                        borderColor: '#FFA74D',
                        borderWidth: 1,
                    },
                    feature: {
                        saveAsImage: {},
                        magicType: {
                            show: true,
                            type: ['line', 'bar', 'stack', 'tiled']
                        }
                    }
                },
                xAxis: {
                    type: 'category',
                    boundaryGap: false,
                    data: yearsData,
                    splitLine: {
                        show: false
                    },
                    axisLine: {
                        lineStyle: {
                            color: '#fff'
                        }
                    }
                },
                yAxis: {
                    name: '万元',
                    type: 'value',
                    splitLine: {
                        show: false
                    },
                    axisLine: {
                        lineStyle: {
                            color: '#fff'
                        }
                    }
                },
                color: ['#FF4949', '#FFA74D', '#FFEA51', '#4BF0FF', '#44AFF0', '#4E82FF', '#584BFF', '#BE4DFF', '#F845F1'],
                series: [
                    {
                        name: typeData[0],
                        type: 'line',
                        data: provinceAgencyEconomicTotalIncome
                    },
                    {
                        name: typeData[1],
                        type: 'line',
                        data: provinceHotelsEconomicTotalIncome
                    },
                    {
                        name: typeData[2],
                        type: 'line',
                        data: provinceOtherEnterpriseEconomicTotalIncome
                    },
                    {
                        name: typeData[3],
                        type: 'line',
                        data: provinceEnterpriseEconomicTotalIncome
                    },
                    {
                        name: typeData[4],
                        type: 'line',
                        data: provincePredictAgencyEconomicTotalIncome
                    },
                    {
                        name: typeData[5],
                        type: 'line',
                        data: provincePredictHotelsEconomicTotalIncome
                    },
                    {
                        name: typeData[6],
                        type: 'line',
                        data: provincePredictOtherEnterpriseEconomicTotalIncome
                    },
                    {
                        name: typeData[7],
                        type: 'line',
                        data: provincePredictEnterpriseEconomicTotalIncome
                    },

                ]
            };
            myChart.setOption(option);
        }

    }

    //评论词云
    function echart_12() {
        var url = "/comment/listwordcloud";
        
        getWordCloudData();

        /**
         * 获取数据
         */
        function getWordCloudData() {
            var keywords = {};
            $.ajax({
                url: url,
                dataType: 'json',
                method: 'GET',
                success: function (data) {
                    list = data.wordClouds;
                    console.log(list)
                    for (var i=0;i<list.length;i++){
                        keywords[list[i].word] = list[i].count
                    }
                    generateChart(keywords);
                    // 基于准备好的dom，初始化echarts实例

                }
            })
        }
        function generateChart(keywords) {
            console.log(keywords)
            var myChart = echarts.init(document.getElementById('chart_12'));

            myChart.clear();
            var data = [];
            for (var name in keywords) {
                data.push({
                    name: name,
                    value: Math.sqrt(keywords[name])
                })
            }

            var maskImage = new Image();
            maskImage.src = '/img/wordcloud.png';
            var option = {
                backgroundColor: "#fff",
                title: {
                    x: 'center',
                    textStyle: {
                        fontSize: 23
                    }

                },
                series: [{
                    type: 'wordCloud',
                    //size: ['9%', '99%'],
                    sizeRange: [30, 100],
                    //textRotation: [0, 45, 90, -45],
                    rotationRange: [-45, 90],
                    //shape: 'circle',
                    maskImage: maskImage,
                    gridSize: 10,
                    textPadding: 0,
                    autoSize: {
                        enable: true,
                        minSize: 6
                    },
                    textStyle: {
                        normal: {
                            color: function() {
                                return 'rgb(' + [
                                    Math.round(Math.random() * 160),
                                    Math.round(Math.random() * 160),
                                    Math.round(Math.random() * 160)
                                ].join(',') + ')';
                            }
                        },
                        emphasis: {
                            shadowBlur: 10,
                            shadowColor: '#333'
                        }
                    },
                    data: data
                }]
            };
            maskImage.onload = function() {
                myChart.setOption(option);
            };
            window.onresize = function() {
                myChart.resize();
            }
        }

    }

    //龙门石窟评论词云
    function echart_17() {
        var url = "/comment/listlmwordcloud";

        getWordCloudData();

        /**
         * 获取数据
         */
        function getWordCloudData() {
            var keywords = {};
            $.ajax({
                url: url,
                dataType: 'json',
                method: 'GET',
                success: function (data) {
                    list = data.wordClouds;
                    console.log(list)
                    for (var i=0;i<list.length;i++){
                        keywords[list[i].word] = list[i].count
                    }
                    generateChart(keywords);
                    // 基于准备好的dom，初始化echarts实例

                }
            })
        }
        function generateChart(keywords) {
            console.log(keywords)
            var myChart = echarts.init(document.getElementById('chart_17'));

            myChart.clear();
            var data = [];
            for (var name in keywords) {
                data.push({
                    name: name,
                    value: Math.sqrt(keywords[name])
                })
            }

            var maskImage = new Image();
            maskImage.src = '/img/wordcloud.png';
            var option = {
                backgroundColor: "#fff",
                title: {
                    x: 'center',
                    textStyle: {
                        fontSize: 23
                    }

                },
                series: [{
                    type: 'wordCloud',
                    //size: ['9%', '99%'],
                    sizeRange: [30, 100],
                    //textRotation: [0, 45, 90, -45],
                    rotationRange: [-45, 90],
                    //shape: 'circle',
                    maskImage: maskImage,
                    gridSize: 10,
                    textPadding: 0,
                    autoSize: {
                        enable: true,
                        minSize: 6
                    },
                    textStyle: {
                        normal: {
                            color: function() {
                                return 'rgb(' + [
                                    Math.round(Math.random() * 160),
                                    Math.round(Math.random() * 160),
                                    Math.round(Math.random() * 160)
                                ].join(',') + ')';
                            }
                        },
                        emphasis: {
                            shadowBlur: 10,
                            shadowColor: '#333'
                        }
                    },
                    data: data
                }]
            };
            maskImage.onload = function() {
                myChart.setOption(option);
            };
            window.onresize = function() {
                myChart.resize();
            }
        }

    }

    //白马寺评论词云
    function echart_18() {
        var url = "/comment/listbmwordcloud";

        getWordCloudData();

        /**
         * 获取数据
         */
        function getWordCloudData() {
            var keywords = {};
            $.ajax({
                url: url,
                dataType: 'json',
                method: 'GET',
                success: function (data) {
                    list = data.wordClouds;
                    console.log(list)
                    for (var i=0;i<list.length;i++){
                        keywords[list[i].word] = list[i].count
                    }
                    generateChart(keywords);
                    // 基于准备好的dom，初始化echarts实例

                }
            })
        }
        function generateChart(keywords) {
            console.log(keywords)
            var myChart = echarts.init(document.getElementById('chart_18'));

            myChart.clear();
            var data = [];
            for (var name in keywords) {
                data.push({
                    name: name,
                    value: Math.sqrt(keywords[name])
                })
            }

            var maskImage = new Image();
            maskImage.src = '/img/wordcloud.png';
            var option = {
                backgroundColor: "#fff",
                title: {
                    x: 'center',
                    textStyle: {
                        fontSize: 23
                    }

                },
                series: [{
                    type: 'wordCloud',
                    //size: ['9%', '99%'],
                    sizeRange: [30, 100],
                    //textRotation: [0, 45, 90, -45],
                    rotationRange: [-45, 90],
                    //shape: 'circle',
                    maskImage: maskImage,
                    gridSize: 10,
                    textPadding: 0,
                    autoSize: {
                        enable: true,
                        minSize: 6
                    },
                    textStyle: {
                        normal: {
                            color: function() {
                                return 'rgb(' + [
                                    Math.round(Math.random() * 160),
                                    Math.round(Math.random() * 160),
                                    Math.round(Math.random() * 160)
                                ].join(',') + ')';
                            }
                        },
                        emphasis: {
                            shadowBlur: 10,
                            shadowColor: '#333'
                        }
                    },
                    data: data
                }]
            };
            maskImage.onload = function() {
                myChart.setOption(option);
            };
            window.onresize = function() {
                myChart.resize();
            }
        }

    }

    //老君山评论词云
    function echart_19() {
        var url = "/comment/listljwordcloud";

        getWordCloudData();

        /**
         * 获取数据
         */
        function getWordCloudData() {
            var keywords = {};
            $.ajax({
                url: url,
                dataType: 'json',
                method: 'GET',
                success: function (data) {
                    list = data.wordClouds;
                    console.log(list)
                    for (var i=0;i<list.length;i++){
                        keywords[list[i].word] = list[i].count
                    }
                    generateChart(keywords);
                    // 基于准备好的dom，初始化echarts实例

                }
            })
        }
        function generateChart(keywords) {
            console.log(keywords)
            var myChart = echarts.init(document.getElementById('chart_19'));

            myChart.clear();
            var data = [];
            for (var name in keywords) {
                data.push({
                    name: name,
                    value: Math.sqrt(keywords[name])
                })
            }

            var maskImage = new Image();
            maskImage.src = '/img/wordcloud.png';
            var option = {
                backgroundColor: "#fff",
                title: {
                    x: 'center',
                    textStyle: {
                        fontSize: 23
                    }

                },
                series: [{
                    type: 'wordCloud',
                    //size: ['9%', '99%'],
                    sizeRange: [30, 100],
                    //textRotation: [0, 45, 90, -45],
                    rotationRange: [-45, 90],
                    //shape: 'circle',
                    maskImage: maskImage,
                    gridSize: 10,
                    textPadding: 0,
                    autoSize: {
                        enable: true,
                        minSize: 6
                    },
                    textStyle: {
                        normal: {
                            color: function() {
                                return 'rgb(' + [
                                    Math.round(Math.random() * 160),
                                    Math.round(Math.random() * 160),
                                    Math.round(Math.random() * 160)
                                ].join(',') + ')';
                            }
                        },
                        emphasis: {
                            shadowBlur: 10,
                            shadowColor: '#333'
                        }
                    },
                    data: data
                }]
            };
            maskImage.onload = function() {
                myChart.setOption(option);
            };
            window.onresize = function() {
                myChart.resize();
            }
        }

    }

    //龙潭大峡谷评论词云
    function echart_20() {
        var url = "/comment/listltwordcloud";

        getWordCloudData();

        /**
         * 获取数据
         */
        function getWordCloudData() {
            var keywords = {};
            $.ajax({
                url: url,
                dataType: 'json',
                method: 'GET',
                success: function (data) {
                    list = data.wordClouds;
                    console.log(list)
                    for (var i=0;i<list.length;i++){
                        keywords[list[i].word] = list[i].count
                    }
                    generateChart(keywords);
                    // 基于准备好的dom，初始化echarts实例

                }
            })
        }
        function generateChart(keywords) {
            console.log(keywords)
            var myChart = echarts.init(document.getElementById('chart_20'));

            myChart.clear();
            var data = [];
            for (var name in keywords) {
                data.push({
                    name: name,
                    value: Math.sqrt(keywords[name])
                })
            }

            var maskImage = new Image();
            maskImage.src = '/img/wordcloud.png';
            var option = {
                backgroundColor: "#fff",
                title: {
                    x: 'center',
                    textStyle: {
                        fontSize: 23
                    }

                },
                series: [{
                    type: 'wordCloud',
                    //size: ['9%', '99%'],
                    sizeRange: [30, 100],
                    //textRotation: [0, 45, 90, -45],
                    rotationRange: [-45, 90],
                    //shape: 'circle',
                    maskImage: maskImage,
                    gridSize: 10,
                    textPadding: 0,
                    autoSize: {
                        enable: true,
                        minSize: 6
                    },
                    textStyle: {
                        normal: {
                            color: function() {
                                return 'rgb(' + [
                                    Math.round(Math.random() * 160),
                                    Math.round(Math.random() * 160),
                                    Math.round(Math.random() * 160)
                                ].join(',') + ')';
                            }
                        },
                        emphasis: {
                            shadowBlur: 10,
                            shadowColor: '#333'
                        }
                    },
                    data: data
                }]
            };
            maskImage.onload = function() {
                myChart.setOption(option);
            };
            window.onresize = function() {
                myChart.resize();
            }
        }

    }

    //echart_2河南省地图
    function echart_13() {
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('chart_13'));
        function showProvince() {
            myChart.setOption(option = {
                // backgroundColor: '#ffffff',
                visualMap: {
                    show: false,
                    min: 0,
                    max: 100,
                    left: 'left',
                    top: 'bottom',
                    bottom: '8%',
                    text: ['高', '低'], // 文本，默认为数值文本
                    calculable: true,
                    inRange: {
                        color: ['yellow', 'lightskyblue', 'orangered']
                    }
                },
                series: [{
                    type: 'map',
                    mapType: 'henan',
                    roam: true,
                    label: {
                        normal: {
                            show: true
                        },
                        emphasis: {
                            textStyle: {
                                color: '#fff'
                            }
                        }
                    },
                    label: {
                        normal: {
                            textStyle: {
                                fontSize: 10,
                                fontWeight: 'bold',

                            }
                        }
                    },
                    itemStyle: {
                        normal: {
                            label: {show: true},
                            borderColor: '#389BB7',
                            areaColor: '#fff',
                        },
                        emphasis: {
                            label: {show: true},
                            areaColor: '#389BB7',
                            borderWidth: 0
                        }
                    },
                    animation: false,
                    data: [{
                        name: '洛阳市',
                        value: 100
                    }, {
                        name: '安阳市',
                        value: 96
                    }, {
                        name: '鹤壁市',
                        value: 98
                    }, {
                        name: '濮阳市',
                        value: 80
                    }, {
                        name: '商丘市',
                        value: 88
                    }, {
                        name: '新乡市',
                        value: 79
                    }, {
                        name: '开封市',
                        value: 77,
                    }, {
                        name: '周口市',
                        value: 33
                    }, {
                        name: '郑州市',
                        value: 69,
                    }, {
                        name: '焦作市',
                        value: 66
                    }, {
                        name: '许昌市',
                        value: 22
                    }, {
                        name: '漯河市',
                        value: 51
                    }, {
                        name: '驻马店市',
                        value: 44
                    }, {
                        name: '信阳市',
                        value: 9
                    }, {
                        name: '平顶山市',
                        value: 19
                    }, {
                        name: '济源市',
                        value: 9
                    }, {
                        name: '三门峡市',
                        value: 33
                    }, {
                        name: '南阳市',
                        value: 60
                    }]
                }]
            });
        }
        showProvince();
        // 使用刚指定的配置项和数据显示图表。
        window.addEventListener("resize", function () {
            myChart.resize();
        });
    }

    //GPS
    function echart_14() {
        var myChart = echarts.init(document.getElementById('chart_14'));

        var data = [
            {
                name: '海门', value: 9,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '鄂尔多斯', value: 12,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '招远', value: 12,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '舟山', value: 12,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '齐齐哈尔', value: 14,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '盐城', value: 15,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '赤峰', value: 16,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '青岛', value: 18,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '乳山', value: 18,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '金昌', value: 19,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '泉州', value: 21,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '莱西', value: 21,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '日照', value: 21,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '胶南', value: 22,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '南通', value: 23,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '拉萨', value: 24,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '云浮', value: 24,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '梅州', value: 25,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '文登', value: 25,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '上海', value: 25,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '攀枝花', value: 25,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '威海', value: 25,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '承德', value: 25,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '厦门', value: 26,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '汕尾', value: 26,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '潮州', value: 26,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '丹东', value: 27,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '太仓', value: 27,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '曲靖', value: 27,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '烟台', value: 28,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '福州', value: 29,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '瓦房店', value: 30,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '即墨', value: 30,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '抚顺', value: 31,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '玉溪', value: 31,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '张家口', value: 31,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '阳泉', value: 31,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '莱州', value: 32,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '湖州', value: 32,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '汕头', value: 32,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '昆山', value: 33,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '宁波', value: 33,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '湛江', value: 33,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '揭阳', value: 34,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '荣成', value: 34,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '连云港', value: 35,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '葫芦岛', value: 35,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '常熟', value: 36,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '东莞', value: 36,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '河源', value: 36,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '淮安', value: 36,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '泰州', value: 36,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '南宁', value: 37,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '营口', value: 37,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '惠州', value: 37,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '江阴', value: 37,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '蓬莱', value: 37,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '韶关', value: 38,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '嘉峪关', value: 38,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '广州', value: 38,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '延安', value: 38,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '太原', value: 39,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '清远', value: 39,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '中山', value: 39,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '昆明', value: 39,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '寿光', value: 40,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '盘锦', value: 40,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '长治', value: 41,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '深圳', value: 41,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '珠海', value: 42,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '宿迁', value: 43,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '咸阳', value: 43,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '铜川', value: 44,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '平度', value: 44,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '佛山', value: 44,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '海口', value: 44,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '江门', value: 45,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '章丘', value: 45,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '肇庆', value: 46,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '大连', value: 47,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '临汾', value: 47,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '吴江', value: 47,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '石嘴山', value: 49,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '沈阳', value: 50,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '苏州', value: 50,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '茂名', value: 50,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '嘉兴', value: 51,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '长春', value: 51,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '胶州', value: 52,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '银川', value: 52,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '张家港', value: 52,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '三门峡', value: 53,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '锦州', value: 54,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '南昌', value: 54,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '柳州', value: 54,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '三亚', value: 54,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '自贡', value: 56,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '吉林', value: 56,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '阳江', value: 57,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '泸州', value: 57,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '西宁', value: 57,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '宜宾', value: 58,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '呼和浩特', value: 58,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '成都', value: 58,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '大同', value: 58,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '镇江', value: 59,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '桂林', value: 59,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '张家界', value: 59,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '宜兴', value: 59,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '北海', value: 60,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '西安', value: 61,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '金坛', value: 62,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '东营', value: 62,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '牡丹江', value: 63,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '遵义', value: 63,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '绍兴', value: 63,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '扬州', value: 64,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '常州', value: 64,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '潍坊', value: 65,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '重庆', value: 66,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '台州', value: 67,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '南京', value: 67,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '滨州', value: 70,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '贵阳', value: 71,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '无锡', value: 71,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '本溪', value: 71,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '克拉玛依', value: 72,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '渭南', value: 72,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '马鞍山', value: 72,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '宝鸡', value: 72,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '焦作', value: 75,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '句容', value: 75,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '北京', value: 79,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '徐州', value: 79,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '衡水', value: 80,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '包头', value: 80,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '绵阳', value: 80,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '乌鲁木齐', value: 84,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '枣庄', value: 84,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '杭州', value: 84,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '淄博', value: 85,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '鞍山', value: 86,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '溧阳', value: 86,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '库尔勒', value: 86,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '安阳', value: 90,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '开封', value: 90,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '济南', value: 92,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '德阳', value: 93,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '温州', value: 95,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '九江', value: 96,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '邯郸', value: 98,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '临安', value: 99,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '兰州', value: 99,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '沧州', value: 100,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '临沂', value: 103,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '南充', value: 104,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '天津', value: 105,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '富阳', value: 106,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '泰安', value: 112,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '诸暨', value: 112,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '郑州', value: 113,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '哈尔滨', value: 114,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '聊城', value: 116,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '芜湖', value: 117,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '唐山', value: 119,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '平顶山', value: 119,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '邢台', value: 119,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '德州', value: 120,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '济宁', value: 120,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '荆州', value: 127,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '宜昌', value: 130,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '义乌', value: 132,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '丽水', value: 133,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '洛阳', value: 134,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '秦皇岛', value: 136,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '株洲', value: 143,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '石家庄', value: 147,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '莱芜', value: 148,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '常德', value: 152,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '保定', value: 153,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '湘潭', value: 154,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '金华', value: 157,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '岳阳', value: 169,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '长沙', value: 175,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '衢州', value: 177,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '廊坊', value: 170,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '菏泽', value: 175,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '合肥', value: 180,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '武汉',
                value: 190,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            },
            {
                name: '大庆',
                value: 150,
                address: '二道区东环域路2038号',
                typeName: '联运',
                area: '0.18',
                service: '东北地区'
            }
        ];
        var geoCoordMap = {
            '海门': [121.15, 31.89],
            '鄂尔多斯': [109.781327, 39.608266],
            '招远': [120.38, 37.35],
            '舟山': [122.207216, 29.985295],
            '齐齐哈尔': [123.97, 47.33],
            '盐城': [120.13, 33.38],
            '赤峰': [118.87, 42.28],
            '青岛': [120.33, 36.07],
            '乳山': [121.52, 36.89],
            '金昌': [102.188043, 38.520089],
            '泉州': [118.58, 24.93],
            '莱西': [120.53, 36.86],
            '日照': [119.46, 35.42],
            '胶南': [119.97, 35.88],
            '南通': [121.05, 32.08],
            '拉萨': [91.11, 29.97],
            '云浮': [112.02, 22.93],
            '梅州': [116.1, 24.55],
            '文登': [122.05, 37.2],
            '上海': [121.48, 31.22],
            '攀枝花': [101.718637, 26.582347],
            '威海': [122.1, 37.5],
            '承德': [117.93, 40.97],
            '厦门': [118.1, 24.46],
            '汕尾': [115.375279, 22.786211],
            '潮州': [116.63, 23.68],
            '丹东': [124.37, 40.13],
            '太仓': [121.1, 31.45],
            '曲靖': [103.79, 25.51],
            '烟台': [121.39, 37.52],
            '福州': [119.3, 26.08],
            '瓦房店': [121.979603, 39.627114],
            '即墨': [120.45, 36.38],
            '抚顺': [123.97, 41.97],
            '玉溪': [102.52, 24.35],
            '张家口': [114.87, 40.82],
            '阳泉': [113.57, 37.85],
            '莱州': [119.942327, 37.177017],
            '湖州': [120.1, 30.86],
            '汕头': [116.69, 23.39],
            '昆山': [120.95, 31.39],
            '宁波': [121.56, 29.86],
            '湛江': [110.359377, 21.270708],
            '揭阳': [116.35, 23.55],
            '荣成': [122.41, 37.16],
            '连云港': [119.16, 34.59],
            '葫芦岛': [120.836932, 40.711052],
            '常熟': [120.74, 31.64],
            '东莞': [113.75, 23.04],
            '河源': [114.68, 23.73],
            '淮安': [119.15, 33.5],
            '泰州': [119.9, 32.49],
            '南宁': [108.33, 22.84],
            '营口': [122.18, 40.65],
            '惠州': [114.4, 23.09],
            '江阴': [120.26, 31.91],
            '蓬莱': [120.75, 37.8],
            '韶关': [113.62, 24.84],
            '嘉峪关': [98.289152, 39.77313],
            '广州': [113.23, 23.16],
            '延安': [109.47, 36.6],
            '太原': [112.53, 37.87],
            '清远': [113.01, 23.7],
            '中山': [113.38, 22.52],
            '昆明': [102.73, 25.04],
            '寿光': [118.73, 36.86],
            '盘锦': [122.070714, 41.119997],
            '长治': [113.08, 36.18],
            '深圳': [114.07, 22.62],
            '珠海': [113.52, 22.3],
            '宿迁': [118.3, 33.96],
            '咸阳': [108.72, 34.36],
            '铜川': [109.11, 35.09],
            '平度': [119.97, 36.77],
            '佛山': [113.11, 23.05],
            '海口': [110.35, 20.02],
            '江门': [113.06, 22.61],
            '章丘': [117.53, 36.72],
            '肇庆': [112.44, 23.05],
            '大连': [121.62, 38.92],
            '临汾': [111.5, 36.08],
            '吴江': [120.63, 31.16],
            '石嘴山': [106.39, 39.04],
            '沈阳': [123.38, 41.8],
            '苏州': [120.62, 31.32],
            '茂名': [110.88, 21.68],
            '嘉兴': [120.76, 30.77],
            '长春': [125.35, 43.88],
            '胶州': [120.03336, 36.264622],
            '银川': [106.27, 38.47],
            '张家港': [120.555821, 31.875428],
            '三门峡': [111.19, 34.76],
            '锦州': [121.15, 41.13],
            '南昌': [115.89, 28.68],
            '柳州': [109.4, 24.33],
            '三亚': [109.511909, 18.252847],
            '自贡': [104.778442, 29.33903],
            '吉林': [126.57, 43.87],
            '阳江': [111.95, 21.85],
            '泸州': [105.39, 28.91],
            '西宁': [101.74, 36.56],
            '宜宾': [104.56, 29.77],
            '呼和浩特': [111.65, 40.82],
            '成都': [104.06, 30.67],
            '大同': [113.3, 40.12],
            '镇江': [119.44, 32.2],
            '桂林': [110.28, 25.29],
            '张家界': [110.479191, 29.117096],
            '宜兴': [119.82, 31.36],
            '北海': [109.12, 21.49],
            '西安': [108.95, 34.27],
            '金坛': [119.56, 31.74],
            '东营': [118.49, 37.46],
            '牡丹江': [129.58, 44.6],
            '遵义': [106.9, 27.7],
            '绍兴': [120.58, 30.01],
            '扬州': [119.42, 32.39],
            '常州': [119.95, 31.79],
            '潍坊': [119.1, 36.62],
            '重庆': [106.54, 29.59],
            '台州': [121.420757, 28.656386],
            '南京': [118.78, 32.04],
            '滨州': [118.03, 37.36],
            '贵阳': [106.71, 26.57],
            '无锡': [120.29, 31.59],
            '本溪': [123.73, 41.3],
            '克拉玛依': [84.77, 45.59],
            '渭南': [109.5, 34.52],
            '马鞍山': [118.48, 31.56],
            '宝鸡': [107.15, 34.38],
            '焦作': [113.21, 35.24],
            '句容': [119.16, 31.95],
            '北京': [116.46, 39.92],
            '徐州': [117.2, 34.26],
            '衡水': [115.72, 37.72],
            '包头': [110, 40.58],
            '绵阳': [104.73, 31.48],
            '乌鲁木齐': [87.68, 43.77],
            '枣庄': [117.57, 34.86],
            '杭州': [120.19, 30.26],
            '淄博': [118.05, 36.78],
            '鞍山': [122.85, 41.12],
            '溧阳': [119.48, 31.43],
            '库尔勒': [86.06, 41.68],
            '安阳': [114.35, 36.1],
            '开封': [114.35, 34.79],
            '济南': [117, 36.65],
            '德阳': [104.37, 31.13],
            '温州': [120.65, 28.01],
            '九江': [115.97, 29.71],
            '邯郸': [114.47, 36.6],
            '临安': [119.72, 30.23],
            '兰州': [103.73, 36.03],
            '沧州': [116.83, 38.33],
            '临沂': [118.35, 35.05],
            '南充': [106.110698, 30.837793],
            '天津': [117.2, 39.13],
            '富阳': [119.95, 30.07],
            '泰安': [117.13, 36.18],
            '诸暨': [120.23, 29.71],
            '郑州': [113.65, 34.76],
            '哈尔滨': [126.63, 45.75],
            '聊城': [115.97, 36.45],
            '芜湖': [118.38, 31.33],
            '唐山': [118.02, 39.63],
            '平顶山': [113.29, 33.75],
            '邢台': [114.48, 37.05],
            '德州': [116.29, 37.45],
            '济宁': [116.59, 35.38],
            '荆州': [112.239741, 30.335165],
            '宜昌': [111.3, 30.7],
            '义乌': [120.06, 29.32],
            '丽水': [119.92, 28.45],
            '洛阳': [112.44, 34.7],
            '秦皇岛': [119.57, 39.95],
            '株洲': [113.16, 27.83],
            '石家庄': [114.48, 38.03],
            '莱芜': [117.67, 36.19],
            '常德': [111.69, 29.05],
            '保定': [115.48, 38.85],
            '湘潭': [112.91, 27.87],
            '金华': [119.64, 29.12],
            '岳阳': [113.09, 29.37],
            '长沙': [113, 28.21],
            '衢州': [118.88, 28.97],
            '廊坊': [116.7, 39.53],
            '菏泽': [115.480656, 35.23375],
            '合肥': [117.27, 31.86],
            '武汉': [114.31, 30.52],
            '大庆': [125.03, 46.58]
        };

        var convertData = function (data) {
            var res = [];
            for (var i = 0; i < data.length; i++) {
                var geoCoord = geoCoordMap[data[i].name];
                if (geoCoord) {
                    res.push({
                        name: data[i].name,
                        value: geoCoord.concat(data[i].value)
                    });
                }

            }
            return res;
        };

        var option = {
            title: {
                text: '',
            },
            tooltip: {
                show: false,
                trigger: 'item',
                formatter: '{b}<br>{c}',
            },
            bmap: {
                center: [104.114129, 37.550339],
                zoom: 5,
                roam: false,  //鼠标缩放
                mapStyle: {
                    styleJson: [{
                        'featureType': 'land',   //土地颜色；
                        'elementType': 'all',
                        'stylers': {
                            'color': '#f5f3ef'
                        }
                    }, {
                        'featureType': 'water',  //水颜色
                        'elementType': 'all',
                        'stylers': {
                            'color': '#a2c1de'
                        }
                    }, {
                        'featureType': 'railway',  //调整铁路颜色
                        'elementType': 'all',
                        'stylers': {
                            'visibility': 'off'
                        }
                    }, {
                        'featureType': 'highway', //调整高速道路颜色
                        'elementType': 'all',
                        'stylers': {
                            'color': '#fdfdfd'
                        }
                    }, {
                        'featureType': 'highway', //调整建筑物标签是否可视
                        'elementType': 'labels',
                        'stylers': {
                            'visibility': 'off'
                        }
                    }, {
                        'featureType': 'arterial',    //调整一些干道颜色
                        'elementType': 'geometry',
                        'stylers': {
                            'color': '#fefefe'
                        }
                    }, {
                        'featureType': 'arterial',
                        'elementType': 'geometry.fill',
                        'stylers': {
                            'color': '#fefefe'
                        }
                    }, {
                        'featureType': 'poi',
                        'elementType': 'all',
                        'stylers': {
                            'visibility': 'off'
                        }
                    }, {
                        'featureType': 'green',
                        'elementType': 'all',
                        'stylers': {
                            'visibility': 'off'
                        }
                    }, {
                        'featureType': 'subway',
                        'elementType': 'all',
                        'stylers': {
                            'visibility': 'off'
                        }
                    }, {
                        'featureType': 'manmade',
                        'elementType': 'all',
                        'stylers': {
                            'color': '#d1d1d1'
                        }
                    }, {
                        'featureType': 'local',
                        'elementType': 'all',
                        'stylers': {
                            'color': '#d1d1d1'
                        }
                    }, {
                        'featureType': 'arterial',
                        'elementType': 'labels',
                        'stylers': {
                            'visibility': 'off'
                        }
                    }, {
                        'featureType': 'boundary',  //边界颜色
                        'elementType': 'all',
                        'stylers': {
                            'color': '#bcab78'
                        }
                    }, {
                        'featureType': 'building',  //建筑颜色
                        'elementType': 'all',
                        'stylers': {
                            'color': '#d1d1d1'
                        }
                    }, {
                        'featureType': 'label',           //地名颜色；
                        'elementType': 'labels.text.fill',
                        'stylers': {
                            'color': '#898989'
                        }
                    }]
                }
            },
            series: [
                {
                    name: 'pm2.5',
                    type: 'scatter',
                    coordinateSystem: 'bmap',
                    data: convertData(data),
                    hoverAnimation: false,         //hover动画;
                    symbolSize: function (val) {
                        return val[2] / 10;
                    },
                    label: {
                        normal: {
                            formatter: '{b}',
                            position: 'right',
                            show: false
                        },
                        emphasis: {
                            show: false
                        }
                    },
                    itemStyle: {
                        normal: {
                            color: '#de1300'
                        }
                    }
                }
            ]
        };

        myChart.setOption(option);

        // 获取百度地图实例，使用百度地图自带的控件
        var bmap = myChart.getModel().getComponent('bmap').getBMap();
        bmap.addControl(new BMap.NavigationControl());  //左侧缩放；
        bmap.enableDragging();   //开启拖拽

        var opts = {
            offset: {height: -5, width: 5},
            width: 250,     // 信息窗口宽度
            height: 150,     // 信息窗口高度
            title: "", // 信息窗口标题
            enableMessage: true//设置允许信息窗发送短息
        };
        for (var i = 0; i < data.length; i++) {
            var icon = new BMap.Icon('../images/ico.png', new BMap.Size(10, 10), {
                anchor: new BMap.Size(5, 5)
            });
            var marker = new BMap.Marker(new BMap.Point(geoCoordMap[data[i].name][0], geoCoordMap[data[i].name][1]), {icon: icon});  // 创建标注
            var content = "<b>" + data[i].name + "</b><br><br>" +
                "园区地址：" + data[i].address + "<br>" +
                "园区类型：" + data[i].typeName + "<br>" +
                "园区面积：" + data[i].area + "<br>" +
                "入驻企业：" + data[i].value + "家<br>" +
                "服务范围：" + data[i].service;

            bmap.addOverlay(marker);               // 将标注添加到地图中
            addClickHandler(content, marker);
        }

        function addClickHandler(content, marker) {
            marker.addEventListener("mouseover", function (e) {
                openInfo(content, e);
            });
            marker.addEventListener("mouseout", function (e) {
                bmap.closeInfoWindow(); //关闭信息窗口
            });
        }

        function openInfo(content, e) {
            var p = e.target;
            var point = new BMap.Point(p.getPosition().lng, p.getPosition().lat);
            var infoWindow = new BMap.InfoWindow(content, opts);  // 创建信息窗口对象
            bmap.openInfoWindow(infoWindow, point); //开启信息窗口
        }
    }

    // echart_15河南省景区收入统计
    function echart_15() {
        var url = "/henan/income/";
        var list = null;
        // loading(getHAIncomeData,"#chart_15")
        getHAIncomeData()
        /**
         * 获取填充数据
         */
        function getHAIncomeData() {
            var resultCity = [];
            var resultData = [
                [],
                [],
                []
            ];
            var yearsData = [];
            $.ajax({
                url: url,
                dataType: 'json',
                method: 'GET',
                success: function (data) {
                    list = data.completeList;
                    haCityIncomes = list[0].haCityIncomes;
                    for (var i = 0; i < haCityIncomes.length; i++) {
                        resultCity[i] = haCityIncomes[i].city
                    }
                    for (var i = 0; i < list.length; i++) {
                        yearsData[i] = list[i].year;
                        for (var j = 0; j < list[i].haCityIncomes.length; j++) {
                            resultData[i].push(
                                {
                                    "value": (parseFloat(list[i].haCityIncomes[j].taking / 10000) + parseFloat(random(10, 1000))).toFixed(2),
                                    "name": list[i].haCityIncomes[j].city
                                }
                            )
                        }

                    }
                    this.resultCity = resultCity
                    this.resultData = resultData
                    this.yearsData = yearsData
                    // $("#AjaxData").val(this.resultData)
                    console.log("yearsData" + yearsData)
                    console.log("1" + this.resultCity);
                    console.log("2" + this.resultData);
                    for (var i = 0; i < this.resultData.length; i++) {
                        console.log(this.resultData[i].name)
                    }
                    generateChart(this.resultCity, this.resultData, this.yearsData);
                    // 基于准备好的dom，初始化echarts实例

                }
            })

            function generateChart(resultCity, resultData, yearsData) {
                // 基于准备好的dom，初始化echarts实例
                var myChart = echarts.init(document.getElementById('chart_15'));
                var year = yearsData

                console.log(resultData)
                optionXyMap01 = {
                    timeline: {
                        data: year,
                        axisType: 'category', //轴的类型
                        autoPlay: true, //表示是否自动播放
                        playInterval: 3000, // 表示播放的速度（跳动的间隔），单位毫秒（ms）
                        left: '10%',
                        right: '10%',
                        bottom: '3%',
                        width: '80%',
                        //  height: null,
                        label: {
                            normal: {
                                textStyle: {
                                    color: '#ddd'
                                }
                            },
                            emphasis: {
                                textStyle: {
                                    color: '#fff'
                                }
                            }
                        },
                        symbolSize: 10, //timeline标记的大小
                        lineStyle: {
                            color: '#555'
                        },
                        //『当前项』（checkpoint）的图形样式
                        checkpointStyle: {
                            borderColor: '#777',
                            borderWidth: 2
                        },
                        //『控制按钮』的样式。『控制按钮』包括：『播放按钮』、『前进按钮』、『后退按钮』
                        controlStyle: {
                            showNextBtn: true,
                            showPrevBtn: true,
                            normal: {
                                color: '#666',
                                borderColor: '#666'
                            },
                            emphasis: {
                                color: '#aaa',
                                borderColor: '#aaa'
                            }
                        },

                    },
                    baseOption: {
                        animation: true,
                        animationDuration: 1000,
                        animationEasing: 'cubicInOut',
                        animationDurationUpdate: 1000,
                        animationEasingUpdate: 'cubicInOut',
                        grid: {
                            right: '1%',
                            top: '15%',
                            bottom: '10%',
                            width: '20%'
                        },
                        //提示框组件
                        tooltip: {
                            // 触发类型
                            trigger: 'axis', // hover触发器
                            axisPointer: { // 坐标轴指示器，坐标轴触发有效
                                type: 'shadow', // 默认为直线，可选为：'line' | 'shadow'
                                shadowStyle: {
                                    color: 'rgba(150,150,150,0.1)' //hover颜色
                                }
                            }
                        },
                    },
                    options: []

                };
                for (var n = 0; n < year.length; n++) {
                    var a = 0
                    for (var i = 0; i < resultData[n].length; i++) {
                        a += parseInt(resultData[n][i].value)
                    }
                    resultData[n].push({
                        value: a, name: '_other',
                        tooltip: {
                            show: false
                        },
                        itemStyle: {
                            normal:
                                {color: 'transparent'}
                        }
                    });
                    optionXyMap01.options.push({

                        title: [{
                            /* text: '地图',
                         subtext: '内部数据请勿外传',
                         left: 'center',
                         textStyle: {
                             color: '#fff'
                         }*/
                        },
                            {
                                id: 'statistic',
                                text: year[n] + "年河南省景区收入统计",
                                left: '40%',
                                top: '8%',
                                textStyle: {
                                    color: '#fff',
                                    fontSize: 25
                                }
                            }
                        ],
                        tooltip: {
                            trigger: 'item',
                            formatter: "{a} <br/>{b} : {c}万元"
                        },
                        legend: {
                            x: 'center',
                            y: '15%',
                            data: resultCity,
                            icon: 'circle',
                            textStyle: {
                                color: '#fff',
                            }
                        },
                        calculable: true,
                        series: [{
                            name: '',
                            type: 'pie',
                            //起始角度，支持范围[0, 360]
                            startAngle: 0,
                            //饼图的半径，数组的第一项是内半径，第二项是外半径
                            radius: [41, 280.75],
                            //支持设置成百分比，设置成百分比时第一项是相对于容器宽度，第二项是相对于容器高度
                            center: ['50%', '40%'],
                            //是否展示成南丁格尔图，通过半径区分数据大小。可选择两种模式：
                            // 'radius' 面积展现数据的百分比，半径展现数据的大小。
                            //  'area' 所有扇区面积相同，仅通过半径展现数据大小

                            //是否启用防止标签重叠策略，默认开启，圆环图这个例子中需要强制所有标签放在中心位置，可以将该值设为 false。
                            avoidLabelOverlap: false,
                            label: {
                                normal: {
                                    show: true,
                                    formatter: '{c}万元'
                                },
                                emphasis: {
                                    show: true
                                }
                            },
                            labelLine: {
                                normal: {
                                    show: true,
                                    length2: 1,
                                },
                                emphasis: {
                                    show: true
                                }
                            },
                            itemStyle: {
                                normal: {
                                    color: function (params) {
                                        var colors = ["#37A2DA", "#67E0E3", "#32C5E9", "#9FE6B8", "#FFDB5C", "#FF9F7F", "#FB7293", "#E062AE", "#E690D1", "#E7BCF3", "#9D96F5", "#8378EA", "#8378EA", "#DD6B66", "#759AA0", "#E69D87", "#8DC1A9", "#EA7E53", "#EEDD78", "#73A373", "#73B9BC", "#7289AB", "#91CA8C", "#F49F42"];
                                        return colors[params.dataIndex]
                                    }

                                }
                            },
                            data: resultData[n]
                        }]
                    })
                }
                myChart.setOption(optionXyMap01);
                window.addEventListener("resize", function () {
                    myChart.resize();
                });
            };

        }


    }

    // echart_16 河南省市热门景区
    function echart_16(city) {
        var url = null;
        var list = null;
        if (city == null || city.length != 3) {
            url = "/henan/hotscenic";

            getHAHotScenicData(url);
            city = "全国"
        } else {
            url = "/henan/hotscenic/" + city
            getHAHotScenicInCitiesData(url);
        }

        /**
         * 获取全国热门景区填充数据
         * @param url
         */
        function getHAHotScenicData(url) {
            var resultScenic = [
                [],
                [],
                []
            ];
            var resultData = [
                [],
                [],
                []
            ];
            var yearsData = [];
            $.ajax({
                url: url,
                dataType: 'json',
                method: 'GET',
                success: function (data) {
                    list = data.completeList;
                    for (var i = 0; i < list.length; i++) {
                        yearsData[i] = list[i].year;
                        for (var j = 0; j < list[i].haHotScenics.length; j++) {
                            resultScenic[i].push(list[i].haHotScenics[j].name)
                            resultData[i].push(
                                {
                                    "value": (parseFloat(list[i].haHotScenics[j].count / 1000) + parseFloat(random(10, 1000))).toFixed(2),
                                    "name": list[i].haHotScenics[j].name
                                }
                            )
                        }

                    }
                    this.resultScenic = resultScenic
                    this.resultData = resultData
                    this.yearsData = yearsData
                    // $("#AjaxData").val(this.resultData)
                    console.log("yearsData" + yearsData)
                    console.log("1" + this.resultScenic);
                    console.log("2" + this.resultData);
                    for (var i = 0; i < this.resultData.length; i++) {
                        console.log(this.resultData[i].name)
                    }
                    generateChart(this.resultScenic, this.resultData, this.yearsData);
                    // 基于准备好的dom，初始化echarts实例

                }
            })
        }

        /**
         * 获取城市热门景区填充数据
         */
        function getHAHotScenicInCitiesData(url) {
            var resultScenic = [
                [],
                [],
                []
            ];
            var resultData = [
                [],
                [],
                []
            ];
            var yearsData = [];
            $.ajax({
                url: url,
                dataType: 'json',
                method: 'GET',
                success: function (data) {
                    list = data.completeList;
                    for (var i = 0; i < list.length; i++) {
                        yearsData[i] = list[i].year;
                        for (var j = 0; j < list[i].hotScenicInCities.length; j++) {
                            resultScenic[i].push(list[i].hotScenicInCities[j].name)
                            resultData[i].push(
                                {
                                    "value": (parseFloat(list[i].hotScenicInCities[j].count / 1000) + parseFloat(random(10, 1000))).toFixed(2),
                                    "name": list[i].hotScenicInCities[j].name
                                }
                            )
                        }

                    }
                    this.resultScenic = resultScenic
                    this.resultData = resultData
                    this.yearsData = yearsData
                    // $("#AjaxData").val(this.resultData)
                    console.log("yearsData" + yearsData)
                    console.log("1" + this.resultCity);
                    console.log("2" + this.resultData);
                    for (var i = 0; i < this.resultData.length; i++) {
                        console.log(this.resultData[i].name)
                    }
                    generateChart(this.resultScenic, this.resultData, this.yearsData);
                    // 基于准备好的dom，初始化echarts实例

                }
            })
        }

        function generateChart(resultScenic, resultData, yearsData) {

            // 基于准备好的dom，初始化echarts实例
            var myChart = echarts.init(document.getElementById('chart_16'));
            var year = yearsData

            console.log("resultScenic[0]" + resultScenic[0])
            optionXyMap01 = {
                timeline: {
                    data: year,
                    axisType: 'category', //轴的类型
                    autoPlay: true, //表示是否自动播放
                    playInterval: 3000, // 表示播放的速度（跳动的间隔），单位毫秒（ms）
                    left: '10%',
                    right: '10%',
                    bottom: '3%',
                    width: '80%',
                    //  height: null,
                    label: {
                        normal: {
                            textStyle: {
                                color: '#ddd'
                            }
                        },
                        emphasis: {
                            textStyle: {
                                color: '#fff'
                            }
                        }
                    },
                    symbolSize: 10, //timeline标记的大小
                    lineStyle: {
                        color: '#555'
                    },
                    //『当前项』（checkpoint）的图形样式
                    checkpointStyle: {
                        borderColor: '#777',
                        borderWidth: 2
                    },
                    //『控制按钮』的样式。『控制按钮』包括：『播放按钮』、『前进按钮』、『后退按钮』
                    controlStyle: {
                        showNextBtn: true,
                        showPrevBtn: true,
                        normal: {
                            color: '#666',
                            borderColor: '#666'
                        },
                        emphasis: {
                            color: '#aaa',
                            borderColor: '#aaa'
                        }
                    },

                },
                baseOption: {
                    animation: true,
                    animationDuration: 1000,
                    animationEasing: 'cubicInOut',
                    animationDurationUpdate: 1000,
                    animationEasingUpdate: 'cubicInOut',
                    grid: {
                        right: '1%',
                        top: '15%',
                        bottom: '10%',
                        width: '20%'
                    },
                    //提示框组件
                    tooltip: {
                        // 触发类型
                        trigger: 'axis', // hover触发器
                        axisPointer: { // 坐标轴指示器，坐标轴触发有效
                            type: 'shadow', // 默认为直线，可选为：'line' | 'shadow'
                            shadowStyle: {
                                color: 'rgba(150,150,150,0.1)' //hover颜色
                            }
                        }
                    },
                },
                options: []

            };
            for (var n = 0; n < year.length; n++) {
                optionXyMap01.options.push({

                    title: [{
                        /* text: '地图',
                     subtext: '内部数据请勿外传',
                     left: 'center',
                     textStyle: {
                         color: '#fff'
                     }*/
                    },
                        {
                            id: 'statistic',
                            text: year[n] + "年" + city + "热门景区",
                            left: '40%',
                            top: '8%',
                            textStyle: {
                                color: '#fff',
                                fontSize: 25
                            }
                        }
                    ],
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                            type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                        }
                    },
                    grid: {
                        top: '6%',
                        left: '3%',
                        right: '4%',
                        bottom: '10%',
                        containLabel: true
                    },
                    xAxis: [
                        {
                            type: 'category',
                            data: resultScenic[n],
                            axisTick: {
                                alignWithLabel: true
                            },
                            axisLine: {
                                lineStyle: {
                                    color: '#fff'
                                }
                            }
                        }
                    ],
                    yAxis: [
                        {
                            name: '万人',
                            type: 'value',
                            axisLine: {
                                lineStyle: {
                                    color: '#fff'
                                }
                            }
                        }
                    ],
                    series: [
                        {
                            name: '人数',
                            type: 'bar',
                            barWidth: '20%',
                            itemStyle: {
                                normal: {
                                    color: function (params) {
                                        var colors = ["#37A2DA", "#67E0E3", "#32C5E9", "#9FE6B8", "#FFDB5C", "#FF9F7F", "#FB7293", "#E062AE", "#E690D1", "#E7BCF3", "#9D96F5", "#8378EA", "#8378EA", "#DD6B66", "#759AA0", "#E69D87", "#8DC1A9", "#EA7E53", "#EEDD78", "#73A373", "#73B9BC", "#7289AB", "#91CA8C", "#F49F42"];
                                        return colors[params.dataIndex]
                                    }

                                }
                            },
                            data: resultData[n],
                            label: {
                                normal: {
                                    show: true,
                                    formatter: '{c}万人'
                                },
                                emphasis: {
                                    show: true
                                }
                            },
                        },

                    ]
                })
            }
            myChart.setOption(optionXyMap01);
            window.addEventListener("resize", function () {
                myChart.resize();
            });
        };

    }


    /**
     * 产生随机整数，包含下限值，包括上限值
     * @param {Number} lower 下限
     * @param {Number} upper 上限
     * @return {Number} 返回在下限到上限之间的一个随机整数
     */
    function random(lower, upper) {
        return Math.floor(Math.random() * (upper - lower + 1)) + lower;
    }

    /***
     * 判断一个字符串中是否包含另一个字符串
     * @param str
     * @param substr
     * @returns {boolean}
     */
    function isContains(str, substr) {
        return new RegExp(substr).test(str);
    }

    /**
     * 计算误差值
     * @param predict_value
     * @param real_value
     * @returns {string}
     */
    function computeErrorRate(predict_value,real_value) {
        predict_value = parseFloat(predict_value)
        real_value = parseFloat(real_value)
        return (Math.abs(predict_value-real_value)/real_value).toFixed(3)
    }
    function loading(func,chartId){
        var begin=new Date()
        func();
        var end=new Date();
        var time=end-begin;
        console.log("time" + time)
        var load = new Loading();
        load.init({
            target: chartId
        });
        load.start();
        setTimeout(function() {
            load.stop();
        }, time*1000+1000)
    }
    //操作按钮
    // 热门常驻地
    $('.t_btn0').click(function () {
        window.location.href = './index';
        $('.center_text').css('display', 'none');
        $('.t_cos0').css('display', 'block');
        echart_map();
    });
    //城市旅游企业收入
    $('.t_btn1').click(function () {
        $('.center_text').css('display', 'none');
        $('.t_cos1').css('display', 'block');
        echart_2();

    });
    $('.t_btn2').click(function () {
        $('.center_text').css('display', 'none');
        $('.t_cos2').css('display', 'block');
        echart_0();
    });
    //各省旅游企业收入
    $('.t_btn3').click(function () {
        $('.center_text').css('display', 'none');
        $('.t_cos3').css('display', 'block');
        echart_4();
    });
    // 河南城市
    $('.t_btn4').click(function () {
        $('.center_text').css('display', 'none');
        $('.t_cos6').css('display', 'block');
        echart_6();
    });
    // 河南省各市流量
    $('.t_btn5').click(function () {
        $('.center_text').css('display', 'none');
        $('.t_cos4').css('display', 'block');
        echart_1();
    });
    // 河南省各市收入
    $('.t_btn15').click(function () {
        $('.center_text').css('display', 'none');
        $('.t_cos15').css('display', 'block');
        echart_15();
    });

    // 热门景区
    $('.t_btn16').click(function () {
        $('.center_text').css('display', 'none');
        $('.t_cos16').css('display', 'block');
        echart_16();
    });
    // 省旅游收入分布
    $('.t_btn6').click(function () {
        $('.center_text').css('display', 'none');
        $('.t_cos5').css('display', 'block');
        echart_3();
    });
    // 市旅游收入分布
    $('.t_btn7').click(function () {
        $('.center_text').css('display', 'none');
        $('.t_cos7').css('display', 'block');
        echart_7();
    });
    // 星级酒店分布
    $('.t_btn8').click(function () {
        $('.center_text').css('display', 'none');
        $('.t_cos8').css('display', 'block');
        echart_8();
    });
    // 世界地图  客源国
    $('.t_btn9').click(function () {
        $('.center_text').css('display', 'none');
        $('.t_cos9').css('display', 'block');
        echart_9();
    });
    // 入境方式统计
    $('.t_btn10').click(function () {
        $('.center_text').css('display', 'none');
        $('.t_cos10').css('display', 'block');
        echart_10();
    });
    // 河南省收入
    $('.t_btn11').click(function () {
        $('.center_text').css('display', 'none');
        $('.t_cos11').css('display', 'block');
        echart_11();
    });
    // 评论词云
    $('.t_btn12').click(function () {
        $('.center_text').css('display', 'none');
        $('.t_cos12').css('display', 'block');
        echart_12();
    });
    $('.t_btn13').click(function () {
        $('.center_text').css('display', 'none');
        $('.t_cos13').css('display', 'block');
        echart_13();
    });
    $('.t_btn14').click(function () {
        $('.center_text').css('display', 'none');
        $('.t_cos14').css('display', 'block');
        echart_14();
    });
    // 龙门石窟词云
    $('.t_btn17').click(function () {
        $('.center_text').css('display', 'none');
        $('.t_cos17').css('display', 'block');
        echart_17();
    });
    // 白马寺词云
    $('.t_btn18').click(function () {
        $('.center_text').css('display', 'none');
        $('.t_cos18').css('display', 'block');
        echart_18();
    });
    // 老君山词云
    $('.t_btn19').click(function () {
        $('.center_text').css('display', 'none');
        $('.t_cos19').css('display', 'block');
        echart_19();
    });
    // 龙潭大峡谷词云
    $('.t_btn20').click(function () {
        $('.center_text').css('display', 'none');
        $('.t_cos20').css('display', 'block');
        echart_20();
    });
    // 龙潭大峡谷词云
    $('.t_btn21').click(function () {
        // window.location.href="http://localhost:8099/main";
        window.open("http://localhost:8099/main","_blank");
    });

//获取地址栏参数
    $(function () {
        function getUrlParms(name) {
            var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
            var r = window.location.search.substr(1).match(reg);
            if (r != null)
                return unescape(r[2]);
            return null;
        }

        var id = getUrlParms("id");
        if (id == 2) {
            $('.center_text').css('display', 'none');
            $('.t_cos7').css('display', 'block');
            echart_7();
        }
        if (id == 3) {
            $('.center_text').css('display', 'none');
            $('.t_cos8').css('display', 'block');
            echart_8();
        }

        if (id == 5) {
            $('.center_text').css('display', 'none');
            $('.t_cos2').css('display', 'block');
            echart_0();
        }
        if (id == 6) {
            $('.center_text').css('display', 'none');
            $('.t_cos4').css('display', 'block');
            echart_1();
        }
        if (id == 7) {
            $('.center_text').css('display', 'none');
            $('.t_cos10').css('display', 'block');
            echart_10();
        }
        // 各省旅游企业收入
        if (id == 8) {
            $('.center_text').css('display', 'none');
            $('.t_cos5').css('display', 'block');
            echart_3();
        }
        if (id == 9) {
            $('.center_text').css('display', 'none');
            $('.t_cos13').css('display', 'block');
            echart_13();
        }
    });

//参数n为休眠时间，单位为毫秒:
    function sleep(n) {
        var start = new Date().getTime();
        //  console.log('休眠前：' + start);
        while (true) {
            if (new Date().getTime() - start > n) {
                break;
            }
        }
        // console.log('休眠后：' + new Date().getTime());
    }
})
;
