/**
 * Created by ymy on 2018/10/12.
 */
/**
 * @author Dimitry Kudrayvtsev
 * @version 2.0
 */

d3.gantt = function() {
    var FIT_TIME_DOMAIN_MODE = "fit";
    var FIXED_TIME_DOMAIN_MODE = "fixed";

    var margin = {
        top : 20,
        right : 20,
        bottom : 20,
        left : 80
    };
    var timeDomainStart = d3.time.format("%H:%M").parse('06:00');
    var timeDomainEnd = d3.time.format("%H:%M").parse('22:00');
    var timeDomainMode = FIXED_TIME_DOMAIN_MODE;// fixed or fit
    var taskTypes = [];
    var taskStatus = [];
    var height = document.body.clientHeight/2-margin.bottom;
    var width = document.body.clientWidth /2-margin.left;

    var tickFormat = "%H:%M";

    var keyFunction = function(d) {
        return d.startDate + d.taskName + d.endDate;
    };

    var rectTransform = function(d) {
        return "translate(" + x(d.startDate) + "," + y(d.taskName) + ")";
    };

    var x = d3.time.scale().domain([ timeDomainStart, timeDomainEnd ]).range([ 0, width ]).clamp(true);

    var y = d3.scale.ordinal().domain(taskTypes).rangeRoundBands([ 0, height ], .1);

    var xAxis = d3.svg.axis().scale(x).orient("bottom").tickFormat(d3.time.format(tickFormat)).tickSubdivide(true)
        .tickSize(8).tickPadding(8);

    var yAxis = d3.svg.axis().scale(y).orient("left").tickSize(0);

    var initTimeDomain = function() {
        if (timeDomainMode === FIT_TIME_DOMAIN_MODE) {
            if (tasks === undefined || tasks.length < 1) {
                timeDomainStart = d3.time.day.offset(new Date(), -3);
                timeDomainEnd = d3.time.hour.offset(new Date(), +3);
                return;
            }
            tasks.sort(function(a, b) {
                return a.endDate - b.endDate;
            });
            timeDomainEnd = tasks[tasks.length - 1].endDate;
            tasks.sort(function(a, b) {
                return a.startDate - b.startDate;
            });
            timeDomainStart = tasks[0].startDate;
        }
    };

    var initAxis = function() {
        x = d3.time.scale().domain([ timeDomainStart, timeDomainEnd ]).range([ 0, width]).clamp(true);
        y = d3.scale.ordinal().domain(taskTypes).rangeRoundBands([ 0, height]);
        xAxis = d3.svg.axis().scale(x).orient("bottom").tickFormat(d3.time.format(tickFormat)).tickSubdivide(true)
            .tickSize(8).tickPadding(8);

        yAxis = d3.svg.axis().scale(y).orient("left").tickSize(8);
    };

    function gantt(tasks) {

        initTimeDomain();
        initAxis();

        var svg = d3.select("#Gantt")
            .append("svg")
            .attr("class", "chart")
            .attr("width", width + margin.left + margin.right)
            .attr("height", height + margin.top + margin.bottom)
            .append("g")
            .attr("class", "gantt-chart")
            .attr("width", width )
            .attr("height", height)
            .attr("transform", "translate(" + margin.left + ", " + 0+ ")");

        svg.selectAll(".chart")
            .data(tasks, keyFunction)
            .enter()
            .append("rect")
            .attr("rx", 5)
            .attr("ry", 5)
            .attr("class", function(d){
                if(taskStatus[d.status] == null){ return "a";}
                return taskStatus[d.status];
            })
            .attr("y", 0)
            .attr("transform", rectTransform)
            .attr("height", function(d) { return y.rangeBand(); })
            .attr("width", function(d) {
                return (x(d.endDate) - x(d.startDate));
            });


        svg.append("g")
            .attr("class", "x axis")
            .attr("transform", "translate(0, " + (height) + ")")
            .transition()
            .call(xAxis);

        svg.append("g").attr("class", "y axis").transition().call(yAxis);

        return gantt;

    };

    var drawMarks = function (tasks) {
        var rects = [];
        var circles = [];
        var d1 = "8px";
        for(var i=0;i<tasks.length;i++){
            if(tasks[i].mark == 1){
                rects.push(tasks[i]);
            }else{
                circles.push(tasks[i]);
            }
        }
        var circle = d3.select("svg").select(".gantt-chart").selectAll("circle").data(circles,keyFunction);
        var rect = d3.select("svg").select(".gantt-chart").selectAll(".littlerect").data(rects,keyFunction);
        circle.enter()
            .insert("circle",":first-child")
            .attr("r", function(d){
                // var s = x(d.endDate)-x(d.startDate);
                // if( s > d1 ){
                //     return d1/2;
                // }else{
                //     return s/4;
                // }
                return 4;
            })
            .attr("cx",4)
            .transition()
            .attr("cy",function(d){
                // var s = x(d.endDate)-x(d.startDate);
                // if( s > d1 ){
                //     return d1/2;
                // }else{
                //     return s/4;
                // }
                return 12;
            } )
            .attr("transform", rectTransform)
            .style({
                'fill': 'purple'
            });
        rect.enter()
            .insert("rect",":first-child")
            .attr("class","littlerect")
            .attr("rx", 1)
            .attr("ry", 1)
            .transition()
            .attr("y", 0)
            .attr("transform", rectTransform)
            .attr("height", function(d) {
                // return y.rangeBand()/2;
                return d1;
            })
            .attr("width", function(d) {
                // var s = x(d.endDate)-x(d.startDate);
                // if( s > d1){
                //     return d1;
                // }else {
                //     return s/2;
                // }
                return d1;
            }).style({
            'fill': 'blue'
            });
        // circle.transition()
        //     .attr("transform", rectTransform)
        //     .attr("height", function(d) { return y.rangeBand(); })
        //     .attr("width", function(d) {
        //         return (x(d.endDate) - x(d.startDate));
        //     });

    };
    gantt.redraw = function(tasks,marks) {

        initTimeDomain();
        initAxis();
        var svg = d3.select("svg");
        d3.selectAll("rect").remove();
        d3.selectAll("circle").remove();
        drawMarks(marks);

        var ganttChartGroup = svg.select(".gantt-chart");
        var rect = ganttChartGroup.selectAll(".bigrect").data(tasks, keyFunction);

        rect.enter()
            .insert("rect",":first-child")
            .attr("rx", 5)
            .attr("ry", 5)
            .attr("class", function(d){
                if(taskStatus[d.status] == null){ return "bar";}
                return taskStatus[d.status]+" bigrect";
            })
            .transition()
            .attr("y", 0)
            .attr("transform", rectTransform)
            .attr("height", function(d) { return y.rangeBand(); })
            .attr("width", function(d) {
                return (x(d.endDate) - x(d.startDate));
            });

        // rect.transition()
        //     .attr("transform", rectTransform)
        //     .attr("height", function(d) { return y.rangeBand(); })
        //     .attr("width", function(d) {
        //         return (x(d.endDate) - x(d.startDate));
        //     });
        rect.exit().remove();

        svg.select(".x").transition().call(xAxis);
        svg.select(".y").transition().call(yAxis);

        return gantt;
    };

    gantt.margin = function(value) {
        if (!arguments.length)
            return margin;
        margin = value;
        return gantt;
    };

    gantt.timeDomain = function(value) {
        if (!arguments.length)
            return [ timeDomainStart, timeDomainEnd ];
        timeDomainStart = +value[0], timeDomainEnd = +value[1];
        return gantt;
    };

    /**
     * @param {string}
     *                vale The value can be "fit" - the domain fits the data or
     *                "fixed" - fixed domain.
     */
    gantt.timeDomainMode = function(value) {
        if (!arguments.length)
            return timeDomainMode;
        timeDomainMode = value;
        return gantt;

    };


    gantt.taskTypes = function(value) {
        if (!arguments.length)
            return taskTypes;
        taskTypes = value;
        return gantt;
    };

    gantt.taskStatus = function(value) {
        if (!arguments.length)
            return taskStatus;
        taskStatus = value;
        return gantt;
    };

    gantt.width = function(value) {
        if (!arguments.length)
            return width;
        width = +value;
        return gantt;
    };

    gantt.height = function(value) {
        if (!arguments.length)
            return height;
        height = +value;
        return gantt;
    };

    gantt.tickFormat = function(value) {
        if (!arguments.length)
            return tickFormat;
        tickFormat = value;
        return gantt;
    };
    return gantt;
};