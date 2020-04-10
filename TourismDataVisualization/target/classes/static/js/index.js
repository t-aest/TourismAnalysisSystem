$(function () {

    var load = new Loading();
    load.init();
    load.start();

    var tim=null;

    setTimeout(function() {
        var begin=new Date()
        echart_1();
        echart_2();
        echart_3();
        echart_4();
        echart_map();
        echart_5();
        var end=new Date();
        tim = end-begin
        load.stop();
        }, tim)
    //生成了随机秒数60-150之间
    var num = Math.round(Math.random()*90+20);
    //循环执行，每隔60-150秒钟执行一次showMsgIcon()
    window.setInterval(echart_4, 1000*num);
    console.log("numnumnumnum" + num)

    var time = 2016;
    //echart_1河南省人流量
    function echart_1() {
        var url = "/henan/stream/" + time;
        var list = null;
        getHAStreamData()

        /**
         * 获取填充数据
         */
        function getHAStreamData() {
            var resultCity = [];
            var resultData = [];
            $.ajax({
                url: url,
                dataType: 'json',
                method: 'GET',
                success: function (data) {
                    list = data.haCityStreams;
                    for (var i = 0; i < list.length; i++) {
                        resultCity[i] = list[i].city
                    }
                    for (var i = 0; i < list.length; i++) {
                        resultData.push({"value": list[i].count / 1000, "name": list[i].city})
                        console.log(list[i].count / 10000)
                    }
                    this.resultCity = resultCity
                    this.resultData = resultData
                    // $("#AjaxData").val(this.resultData)
                    console.log("1" + this.resultCity);
                    console.log("2" + this.resultData);
                    for (var i = 0; i < this.resultData.length; i++) {
                        console.log(this.resultData[i].name)
                    }
                    generateChart(this.resultCity, this.resultData);
                    // 基于准备好的dom，初始化echarts实例

                }
            })

            function generateChart(resultCity, resultData) {
                // 基于准备好的dom，初始化echarts实例
                var myChart = echarts.init(document.getElementById('chart_1'));
                var a = 0
                for (var i = 0; i < resultData.length; i++) {
                    a += parseInt(resultData[i].value)
                }
                resultData.push({
                    value: a, name: '_other',
                    tooltip: {
                        show: false
                    },
                    itemStyle: {
                        normal:
                            {color: 'transparent'}
                    }
                });
                console.log(resultData)
                option = {
                    tooltip: {
                        trigger: 'item',
                        formatter: "{a} <br/>{b} : {c}万人"
                    },
                    legend: {
                        x: 'center',
                        y: '10%',
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
                        radius: [41, 100.75],
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
                        data: resultData
                    }]
                };
                // 使用刚指定的配置项和数据显示图表。
                myChart.setOption(option);
                window.addEventListener("resize", function () {
                    myChart.resize();
                });
            }
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

    }

    //echart_2河南省地图
    function echart_2() {
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('chart_2'));

        function showProvince() {
            myChart.setOption(option = {
                // backgroundColor: '#ffffff',
                visualMap: {
                    show: false,
                    min: 0,
                    max: 100,
                    left: 'left',
                    top: 'bottom',
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
                            label: { show: true },
                            borderColor: '#389BB7',
                            areaColor: '#fff',
                        },
                        emphasis: {
                            label: { show: true },
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

        var currentIdx = 0;
        showProvince();
        // 使用刚指定的配置项和数据显示图表。
        window.addEventListener("resize", function () {
            myChart.resize();
        });
    }


    // echart_map中国地图
    function echart_map() {
        var url = "/henan/residence/" + time;
        var list = null;
        getReisdanceData()

        /**
         * 获取地图填充相关数据
         */
        function getReisdanceData() {
            var resultCoordMap = {};
            var resultData = [];
            $.ajax({
                url: url,
                dataType: 'json',
                method: 'GET',
                success: function (data) {
                    list = data.haHotResidenceVos;
                    for (var i = 0; i < list.length; i++) {
                        resultCoordMap[list[i].residence] = [parseFloat(list[i].lng), parseFloat(list[i].lat)]
                    }
                    for (var i = 0; i < list.length; i++) {
                        resultData[i] = [{"name": list[i].residence, "value": parseInt(list[i].count)}]
                    }
                    this.resultCoordMap = resultCoordMap
                    this.resultData = resultData
                    generateMap(this.resultCoordMap, this.resultData);
                    // 基于准备好的dom，初始化echarts实例

                }
            })
        }

        function generateMap(resultCoordMap, resultData) {
            var myChart = echarts.init(document.getElementById('chart_map'));

            myChart.showLoading();
            //填充数据
            var geoCoordMap = resultCoordMap
            var GZData = resultData
            var convertData = function (data) {
                var res = [];
                for (var i = 0; i < data.length; i++) {
                    var dataItem = data[i];
                    var fromCoord = geoCoordMap[dataItem[0].name];
                    var toCoord = [113.65, 34.76];
                    if (fromCoord && toCoord) {
                        res.push([{
                            coord: fromCoord,
                            value: dataItem[0].value
                        }, {
                            coord: toCoord,
                        }]);
                    }
                }
                return res;
            };
            var color = ['#9ACD32'];
            var series = [];
            [['河南省', GZData]].forEach(function (item, i) {
                series.push({
                        cursor: "pointer",
                        type: 'lines',
                        zlevel: 2,
                        effect: {
                            show: true,
                            period: 4, //箭头指向速度，值越小速度越快
                            trailLength: 0.02, //特效尾迹长度[0,1]值越大，尾迹越长重
                            symbol: 'arrow', //箭头图标
                            symbolSize: 4, //图标大小
                        },
                        lineStyle: {
                            normal: {
                                width: 1, //尾迹线条宽度
                                opacity: 0.6, //尾迹线条透明度
                                curveness: .3, //尾迹线条曲直度

                                color: color[i]
                            }
                        },
                        data: convertData(item[1])
                    }, {
                        type: 'effectScatter',
                        coordinateSystem: 'geo',
                        zlevel: 2,
                        rippleEffect: { //涟漪特效
                            period: 4, //动画时间，值越小速度越快
                            brushType: 'stroke', //波纹绘制方式 stroke, fill
                            scale: 3 //波纹圆环最大限制，值越大波纹越大
                        },
                        label: {
                            normal: {
                                show: true,
                                position: 'right', //显示位置
                                offset: [5, 0], //偏移设置
                                formatter: function (params) {//圆环显示文字
                                    return params.data.name;
                                },
                                fontSize: 13
                            },
                            emphasis: {
                                show: true
                            }
                        },
                        symbol: 'circle',
                        symbolSize: function (val) {
                            console.log(val)
                            return val[2] / 25000; //圆环大小
                        },
                        itemStyle: {
                            normal: {
                                show: false,
                                color: color[i]
                            }
                        },
                        data: item[1].map(function (dataItem) {
                            return {
                                name: dataItem[0].name,
                                // value: geoCoordMap[dataItem[0].name].concat([dataItem[0].value])
                                value: geoCoordMap[dataItem[0].name].concat([dataItem[0].value])
                            };
                        }),
                    },
                    //被攻击点
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
                                color: "#f60"
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
                            name: item[0],
                            value: [113.65, 34.76].concat([10]),
                        }],
                    }
                );
            });
            option = {
                tooltip: {
                    trigger: 'item',
                    formatter: function (params, ticket, callback) {
                        //根据业务自己拓展要显示的内容
                        var res = "";
                        var name = params.name;
                        var value = params.value[params.seriesIndex + 1];
                        res = "<span style='color:#fff;'>" + name + "</span><br/>人数：" + value + "人";
                        return res;
                    }
                },
                geo: {
                    map: 'china',
                    label: {
                        emphasis: {
                            show: false
                        }
                    },
                    roam: true,
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
                                    color: 'rgba(175,238,238, 0)' // 0% 处的颜色
                                }, {
                                    offset: 1,
                                    color: 'rgba(47,79,79, .1)' // 100% 处的颜色
                                }],
                                globalCoord: false // 缺省为 false
                            },
                            shadowColor: 'rgba(128, 217, 248, 1)',
                            // shadowColor: 'rgba(255, 255, 255, 1)',
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
                series: series
            };
            myChart.hideLoading();
            // 使用刚指定的配置项和数据显示图表。
            myChart.setOption(option);
            window.addEventListener("resize", function () {
                myChart.resize();
            });

        }


    }

    //echart_3 入境方式统计
    function echart_3() {
        var url = "/foreign/transport";

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
            var myChart = echarts.init(document.getElementById('chart_3'));
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

    //内存占比
    function echart_4() {

        var url = "/getsystem";

        getData();

        /**
         * 获取数据
         */
        function getData() {
            var memory = 0;

            $.ajax({
                url: url,
                dataType: 'json',
                method: 'GET',
                success: function (data) {
                    this.memory = data.memory;

                    generateChart(this.memory);
                    // 基于准备好的dom，初始化echarts实例

                }
            })
        }

        function generateChart(memory){
            // 基于准备好的dom，初始化echarts实例
            var myChart = echarts.init(document.getElementById('chart_4'));
            var value = parseFloat(parseInt(memory)/100);
            var data = [value, value, value, value, value, ];
            var option = {
                series: [{
                    type: 'liquidFill',
                    radius: '60%',
                    data: data,
                    backgroundStyle: {
                        borderWidth: 5,
                        borderColor: 'rgb(255,0,255,0.9)',
                        color: 'rgb(255,0,255,0.01)'
                    },
                    label: {
                        normal: {
                            formatter: (value * 100).toFixed(2) + '%',
                            textStyle: {
                                fontSize: 30
                            }
                        }
                    }
                }]
            }

            myChart.setOption(option)
        }

    }

    //河南省飞机场
    function echart_5() {
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('chart_5'));

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
                        normal: {},
                        //鼠标移入后查看效果
                        emphasis: {
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
                        mapType: 'hunan',
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

        // 使用刚指定的配置项和数据显示图表。
        // myChart.setOption(option);
        window.addEventListener("resize", function () {
            myChart.resize();
        });
    }

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

    //点击跳转
    $('#chart_map').click(function () {
        window.location.href = './page/index';
    });
    $('.t_btn2').click(function () {
        window.location.href = "./page/index?id=2";
    });
    $('.t_btn3').click(function () {
        window.location.href = "./page/index?id=3";
    });
    $('.t_btn4').click(function () {

    });
    $('.t_btn5').click(function () {
        window.location.href = "./page/index?id=5";
    });
    $('.t_btn6').click(function () {
        window.location.href = "./page/index?id=6";
    });
    $('.t_btn7').click(function () {
        window.location.href = "./page/index?id=7";
    });
    $('.t_btn8').click(function () {
        window.location.href = "./page/index?id=8";
    });
    $('.t_btn9').click(function () {
        window.location.href = "./page/index?id=9";
    });
});
